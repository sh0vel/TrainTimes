package com.app.shovonh.traintimes;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.app.shovonh.traintimes.Data.DBHelper;

import java.util.ArrayList;
import java.util.List;

public class TopTrainsActivity extends AppCompatActivity {
    public static final String LOG_TAG = TopTrainsActivity.class.getSimpleName();
    public static final String EXTRA_ARRAYLIST = "list";

    public static boolean dontExit = false;

    ArrayList<String> savedStationNames;
    ViewPager viewPager;
    TabLayout tabs;
    Adapter adapter;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_trains);
        dbHelper = new DBHelper(this);

        if (savedStationNames == null) {
            savedStationNames = dbHelper.getAllStations();
            if (savedStationNames.isEmpty()){
                Intent intent = new Intent(this, AllTrainsActivity.class);
                dontExit = true;
                startActivity(intent);
            }
        }


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Upcoming Trains");

        viewPager = (ViewPager) findViewById(R.id.pager_top);
        setupViewPager(viewPager);

        tabs = (TabLayout) findViewById(R.id.tabs_scrolling);
        if (tabs != null)
            tabs.setupWithViewPager(viewPager);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), AllTrainsActivity.class);
                    dontExit = true;
                    startActivity(intent);
                }
            });
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new Adapter(getSupportFragmentManager());
        for (String name : savedStationNames) {
            adapter.addFragment(TopTrainsFragment.newInstance(name, getIntent().getParcelableExtra(EXTRA_ARRAYLIST)), name);
        }
        viewPager.setAdapter(adapter);
    }


    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        private long baseId = 0;

        public Adapter(FragmentManager manager) {
            super(manager);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);

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
                break;
            case R.id.action_nav:
                break;
            case R.id.action_delete:
                String d = adapter.getPageTitle(tabs.getSelectedTabPosition()).toString();
                dbHelper.deleteEntry(d);
                dontExit = true;
                Intent intent = getIntent();
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onStop() {
        if (!dontExit) {
            finish();
            System.exit(0);
        }
        super.onStop();

    }
}
