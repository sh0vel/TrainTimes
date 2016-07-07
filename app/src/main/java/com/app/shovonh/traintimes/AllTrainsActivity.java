package com.app.shovonh.traintimes;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.app.shovonh.traintimes.Data.DBHelper;

import java.util.ArrayList;
import java.util.List;

public class AllTrainsActivity extends AppCompatActivity implements AllTrainsFragment.OnFragmentInteractionListener {
    public static final String LOG_TAG = AllTrainsActivity.class.getSimpleName();

    public static final int PERMISSION_GPS = 5;
    FloatingActionButton fab;
    boolean selectionMade = false;
    DBHelper dbHelper;
    LocationManager locationManager;
    LocationListener locationListener;
    //Activity activity;

    //TODO: add snackbar on first use saying "Select all frequently used stations"
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_trains);
       // activity = this;

        dbHelper = new DBHelper(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Select Stations");

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);


        // Acquire a reference to the system Location Manager
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                onLocationReceived(location, findViewById(R.id.activity_all_trains));

            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
                Log.v(LOG_TAG, "Provider Disabled");
            }
        };

        // Register the listener with the Location Manager to receive location updates


        fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                requestLocationPermission(locationManager, locationListener, view.getContext(), (Activity) view.getContext());
            }
//               final Intent topTrainsIntent = new Intent(view.getContext(), TopTrainsActivity.class);
//                //if (fab.getDrawable().getConstantState().equals(getResources()
//                  //      .getDrawable(R.drawable.ic_done_white_24dp).getConstantState())) {
//                FetchTrainTimes fetchTrainTimes = new FetchTrainTimes(new FetchTrainTimes.FetchComplete() {
//                    @Override
//                    public void onFetchCompete(ArrayList<TrainStop> trainStops) {
//                        topTrainsIntent.putExtra(TopTrainsActivity.EXTRA_ARRAYLIST, Parcels.wrap(trainStops));
//                        topTrainsIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        //TODO: pass true if new station selected, toptrainsactivity viewpager will autoscroll to new selection.
//                        startActivity(topTrainsIntent);
//                        finish();
//                    }
//                });
//                fetchTrainTimes.execute();
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
        dbHelper.insertData(station);
        Intent topTrains = new Intent(this, TopTrainsActivity.class);
        topTrains.putExtra(TopTrainsActivity.EXTRA_SCROLL_TO_LAST, true);
        topTrains.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //TODO: pass true if new station selected, toptrainsactivity viewpager will autoscroll to new selection.
        startActivity(topTrains);
        finish();
//        hideFAB();
//        selectionMade = true;
//        Snackbar sb = Snackbar.make(view, station + " added to main screen", Snackbar.LENGTH_LONG);
//        View.OnClickListener snackButtonUndo = new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        };
//        sb.setAction("UNDO", snackButtonUndo);
//        sb.show();
//        sb.setCallback(new Snackbar.Callback() {
//            @Override
//            public void onDismissed(Snackbar snackbar, int event) {
//                super.onDismissed(snackbar, event);
//                fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_done_white_24dp));
//                showFAB();
//            }
//        });

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

    public void onLocationReceived(Location location,View view) {
        Snackbar sb = Snackbar.make(view, "LAT = " + location.getLatitude() + " LONG = " + location.getLongitude(), Snackbar.LENGTH_LONG);
        sb.show();
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED)
            locationManager.removeUpdates(locationListener);


    }

    public static void requestLocationPermission(LocationManager locationManager, LocationListener locationListener, Context context, Activity activity) {

        if (isGPSActive(locationManager)) {

            int permissionCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);

            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                requestLocation(locationManager, locationListener, context);
            } else {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_GPS);
            }
        } else {
            showAlert(context);
        }
    }

    public static void requestLocation(LocationManager locationManager, LocationListener locationListener, Context context) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED)
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_GPS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //permission granted;
                    //add correct station to top stations
                    requestLocation(locationManager, locationListener, getApplicationContext());

                } else {
                    //permission denied;
                }
                return;
        }
    }

    private static void showAlert(Context c) {
        final Context context = c;
        final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle("Enable Location")
                .setMessage("Your Locations Settings is set to 'Off'.\nPlease Enable Location to " +
                        "continue")
                .setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        context.startActivity(myIntent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    }
                });
        dialog.show();
    }

    public static boolean isGPSActive(LocationManager locationManager) {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }


    @Override
    protected void onResume() {
        super.onResume();
        //check if user is coming back from settings screen
    }

}
