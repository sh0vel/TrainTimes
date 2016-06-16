package com.app.shovonh.traintimes;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A placeholder fragment containing a simple view.
 */
public class TopTrainsFragment extends Fragment {
    public static final String LOG_TAG = TopTrainsFragment.class.getSimpleName();
    private static String STATION_NAME;

    private static final String ARG_STATION_NAME = "param1";

    public TopTrainsFragment() {
    }

    public static TopTrainsFragment newInstance(String stationName) {
        TopTrainsFragment fragment = new TopTrainsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_STATION_NAME, stationName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null){
            STATION_NAME = getArguments().getString(ARG_STATION_NAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.v(LOG_TAG, "onCreateView");
        View view = inflater.inflate(R.layout.fragment_top_trains, container, false);

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putString(ARG_STATION_NAME, STATION_NAME);

        super.onSaveInstanceState(outState);
    }


    @Override
    public void onResume() {
        super.onResume();
        if (getArguments() != null){
            STATION_NAME = getArguments().getString(ARG_STATION_NAME);
        }
        TextView name = (TextView) getView().findViewById(R.id.tv_station_name);
        name.setText(STATION_NAME);
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
