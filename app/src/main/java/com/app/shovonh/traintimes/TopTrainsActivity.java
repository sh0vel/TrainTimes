package com.app.shovonh.traintimes;

import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.app.shovonh.traintimes.Data.DBHelper;
import com.pnikosis.materialishprogress.ProgressWheel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TopTrainsActivity extends AppCompatActivity {
    public static final String LOG_TAG = TopTrainsActivity.class.getSimpleName();
    public static final String EXTRA_SCROLL_TO_LAST = "scroll";
    public static final String EXTRA_DELETED = "delete";
    public static final String EXTRA_DELETED_TAB = "tabnum";
    boolean scrollToLast = false;
    boolean safeToRefreshOnResume;
    boolean stationDeleted;

    ArrayList<String> savedStationNames;
    ViewPager viewPager;
    TabLayout tabs;
    Adapter adapter;
    DBHelper dbHelper;
    FloatingActionButton fab;
    ProgressWheel wheel;
    TabLayout.Tab tab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_trains);
        Log.v(LOG_TAG, "onCreate");
        dbHelper = new DBHelper(this);
        safeToRefreshOnResume = false;
        stationDeleted = getIntent().getBooleanExtra(EXTRA_DELETED, false);
        //if internet is connected{

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Upcoming Trains");

        fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), AllTrainsActivity.class);
                    startActivity(intent);
                }
            });
        }
        wheel = (ProgressWheel) findViewById(R.id.progress_wheel);
        wheel.setCircleRadius(75);
        wheel.setVisibility(View.INVISIBLE);

        if (!stationDeleted) {
            FetchTrainTimes fetchTrainTimes = new FetchTrainTimes(new FetchTrainTimes.fetchListener() {
                @Override
                public void onFetchStarted() {
                    fab.hide();
                    wheel.setVisibility(View.VISIBLE);
                }

                @Override
                public void onFetchCompete() {
                    wheel.setVisibility(View.INVISIBLE);
                    fab.show();
                    setup();
                }
            });
            fetchTrainTimes.execute();
            //} else no internet activity
        }else{
            setup();
        }

    }

    private void setup(){
        viewPager = (ViewPager) findViewById(R.id.pager_top);
        setupViewPager(viewPager);

        tabs = (TabLayout) findViewById(R.id.tabs_scrolling);
        if (tabs != null)
            tabs.setupWithViewPager(viewPager);

        scrollToLast = getIntent().getBooleanExtra(EXTRA_SCROLL_TO_LAST, false);
        if (scrollToLast) {
            tab = tabs.getTabAt(tabs.getTabCount() - 1);
            tab.select();
            getIntent().putExtra(EXTRA_SCROLL_TO_LAST, false);
        }
        if (stationDeleted){
           int deletedTab = getIntent().getIntExtra(EXTRA_DELETED_TAB, -1);
            if (!(deletedTab < 0) && deletedTab != 0){
                tab = tabs.getTabAt(deletedTab - 1);
                        tab.select();
            }
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        safeToRefreshOnResume = false;
        savedStationNames = dbHelper.getAllStations();
        adapter = new Adapter(getSupportFragmentManager());
        for (String name : savedStationNames) {
            Log.v(LOG_TAG, "setUpViewPager: " + name);
            adapter.addFragment(TopTrainsFragment.newInstance(name), name);
        }
        viewPager.setAdapter(adapter);
    }


     class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public Adapter(FragmentManager manager) {
            super(manager);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);

        }

        public void removeFragments(int i){
            mFragmentList.remove(i);
            mFragmentTitleList.remove(i);
            savedStationNames = dbHelper.getAllStations();
            notifyDataSetChanged();
            Log.v(LOG_TAG, "RemoveFragments " + mFragmentTitleList);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_top_trains, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_map:
                safeToRefreshOnResume = true;
                Intent mapImageIntent = new Intent(this, MartaMapActivity.class);
                startActivity(mapImageIntent);
                break;
            case R.id.action_nav:
                Location location = Utilities.getCoordinates(getApplicationContext(),
                        adapter.getPageTitle(tabs.getSelectedTabPosition()).toString());
                String uri = String.format(Locale.ENGLISH, "google.navigation:q=%f,%f", location.getLatitude(), location.getLongitude());
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(mapIntent);
                break;
            case R.id.action_delete:
                int currentTab = tabs.getSelectedTabPosition();
                String d = adapter.getPageTitle(currentTab).toString();
                dbHelper.deleteEntry(d);
                adapter.removeFragments(currentTab);
                if (adapter.getCount() == 0){
                    Intent intent = new Intent(this, AllTrainsActivity.class);
                    startActivity(intent);
                    finish();
                }else {
//                   viewPager.getAdapter().notifyDataSetChanged();
                    Intent intent = getIntent();
                    intent.putExtra(EXTRA_DELETED, true);
                    intent.putExtra(EXTRA_DELETED_TAB, currentTab);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    finish();

//                    if (currentTab != 0 ) {
//                        tab = tabs.getTabAt(currentTab - 1);
//                        tab.select();
//                    }

                }
                break;
            case R.id.action_refresh:
                refreshTimes();
                break;
        }
        return super.onOptionsItemSelected(item);
    }




    private void refreshTimes() {
        FetchTrainTimes fetchTrainTimes = new FetchTrainTimes(new FetchTrainTimes.fetchListener() {
            @Override
            public void onFetchStarted() {
                fab.hide();
                wheel.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFetchCompete() {
                fab.show();
                wheel.setVisibility(View.INVISIBLE);
//                List<Fragment> fragments = adapter.mFragmentList;
//                List<String> names = adapter.mFragmentTitleList;
//                Log.v(LOG_TAG, "refresh: " + names);
//                int currentTab = tabs.getSelectedTabPosition();
//                //if first or last
//                //update 2 of them
//                if (fragments.size() > 1) {
//                    if (currentTab == 0) {
//                        fragments.set(0, TopTrainsFragment.newInstance(names.get(0)));
//                        fragments.set(1, TopTrainsFragment.newInstance(names.get(1)));
//                    } else if (currentTab == fragments.size() - 1) {
//                        fragments.set(fragments.size() - 1, TopTrainsFragment.newInstance(names.get(fragments.size() - 1)));
//                        fragments.set(fragments.size() - 2, TopTrainsFragment.newInstance(names.get(fragments.size() - 2)));
//                    } else {
//                        //else update 3
//                        fragments.set(tabs.getSelectedTabPosition(), TopTrainsFragment.newInstance(names.get(tabs.getSelectedTabPosition())));
//                        fragments.set(tabs.getSelectedTabPosition() - 1, TopTrainsFragment.newInstance(names.get(tabs.getSelectedTabPosition() - 1)));
//                        fragments.set(tabs.getSelectedTabPosition() + 1, TopTrainsFragment.newInstance(names.get(tabs.getSelectedTabPosition() + 1)));
//                    }
//                } else {
//
//                    fragments.set(0, TopTrainsFragment.newInstance(names.get(0)));
//                    //fragments.set(1, TopTrainsFragment.newInstance(names.get(1)));
//                }
                viewPager.getAdapter().notifyDataSetChanged();
            }

        });
        fetchTrainTimes.execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //show sync icon
        if (safeToRefreshOnResume) {
            refreshTimes();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        safeToRefreshOnResume = true;
    }


    @Override
    protected void onStop() {
        super.onStop();
    }
}
