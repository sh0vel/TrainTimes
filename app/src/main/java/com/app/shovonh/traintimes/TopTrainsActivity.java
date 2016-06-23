package com.app.shovonh.traintimes;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import com.app.shovonh.traintimes.Obj.TrainStop;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class TopTrainsActivity extends AppCompatActivity implements FetchTrainTimes.FetchComplete{
    public static final String LOG_TAG = TopTrainsActivity.class.getSimpleName();
    public static final String EXTRA_ARRAYLIST = "list";

    ArrayList<String> savedStationNames;
    ArrayList<TrainStop> allTrainStations;
    String currentStationFrag; //set when during fragments onResume function via listner

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_trains);
        final DBHelper dbHelper = new DBHelper(this);

        if (savedStationNames == null){
            savedStationNames = dbHelper.getAllStations();
        }

        allTrainStations = Parcels.unwrap(getIntent().getParcelableExtra(EXTRA_ARRAYLIST));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Upcoming Trains");

        ViewPager viewPager = (ViewPager) findViewById(R.id.pager_top);
        setupViewPager(viewPager);

        TabLayout tabs = (TabLayout) findViewById(R.id.tabs_scrolling);
        tabs.setupWithViewPager(viewPager);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbHelper.deleteAll();
                Snackbar.make(view, "Database Clearedd", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void setupViewPager(ViewPager viewPager){
        //TODO:Check if names is empty, redirect to allstations activity if so
        Adapter adapter = new Adapter(getSupportFragmentManager());
        for (String name : savedStationNames){
            adapter.addFragment(TopTrainsFragment.newInstance(name), name);
        }
        viewPager.setAdapter(adapter);
    }

    static class Adapter extends FragmentPagerAdapter{
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public Adapter(FragmentManager manager){
            super(manager);
        }

        public void addFragment(Fragment fragment, String title){
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
    public void onFetchCompete(ArrayList<TrainStop> trainStops) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_top_trains, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.action_map:break;
            case R.id.action_nav:break;
            case R.id.action_settings:break;
        }
        return super.onOptionsItemSelected(item);
    }
}
