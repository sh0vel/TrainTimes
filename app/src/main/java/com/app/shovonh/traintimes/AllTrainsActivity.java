package com.app.shovonh.traintimes;

import android.content.Intent;
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
import android.view.View;

import com.app.shovonh.traintimes.Data.DBHelper;
import com.app.shovonh.traintimes.Obj.TrainStop;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class AllTrainsActivity extends AppCompatActivity implements AllTrainsFragment.OnFragmentInteractionListener {


    FloatingActionButton fab;
    boolean selectionMade = false;
    DBHelper dbHelper;
    //TODO: add snackbar on first use saying "Select all frequently used stations"
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_trains);

        dbHelper = new DBHelper(this);

        Location;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Select Stations");

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               final Intent topTrainsIntent = new Intent(view.getContext(), TopTrainsActivity.class);
                //if (fab.getDrawable().getConstantState().equals(getResources()
                  //      .getDrawable(R.drawable.ic_done_white_24dp).getConstantState())) {
                FetchTrainTimes fetchTrainTimes = new FetchTrainTimes(new FetchTrainTimes.FetchComplete() {
                    @Override
                    public void onFetchCompete(ArrayList<TrainStop> trainStops) {
                        topTrainsIntent.putExtra(TopTrainsActivity.EXTRA_ARRAYLIST, Parcels.wrap(trainStops));
                        topTrainsIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        //TODO: pass true if new station selected, toptrainsactivity viewpager will autoscroll to new selection.
                        startActivity(topTrainsIntent);
                        finish();
                    }
                });
                fetchTrainTimes.execute();
            }
        });


    }

    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(AllTrainsFragment.newInstance(AllTrainsFragment.PAGE_NORTHSOUTH), "North/South");
        adapter.addFragment(AllTrainsFragment.newInstance(AllTrainsFragment.PAGE_EASTWEST), "East/West");
        viewPager.setAdapter(adapter);
    }


    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public Adapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


    @Override
    public void listItemSelected(View view, String station) {
        //TODO: When selected, add to db, switch to toptrains activity, display time of newly selected station
        dbHelper.insertData(station);
        hideFAB();
        selectionMade = true;
        Snackbar sb = Snackbar.make(view, station + " added to main screen", Snackbar.LENGTH_LONG);
        View.OnClickListener snackButtonUndo = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        };
        sb.setAction("UNDO", snackButtonUndo);
        sb.show();
        sb.setCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar snackbar, int event) {
                super.onDismissed(snackbar, event);
                fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_done_white_24dp));
                showFAB();
            }
        });
    }

    @Override
    public void hideFAB() {
        if (fab.isShown() && !selectionMade)
            fab.hide();
    }

    @Override
    public void showFAB() {
        if (!fab.isShown())
            fab.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
