package com.app.shovonh.traintimes;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class TopTrainsFragment extends Fragment {
    //arraylist with all train times
    //number of traintimes added to layout = arraylist.size
    //onResume fills out traintimes layouts from arraylist

    public static final String LOG_TAG = TopTrainsFragment.class.getSimpleName();
    private static String STATION_NAME;

    private static final String ARG_STATION_NAME = "param1";
    ArrayList<View> trainTimesList = new ArrayList<>();

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
        if (getArguments() != null) {
            STATION_NAME = getArguments().getString(ARG_STATION_NAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.v(LOG_TAG, "onCreateView");
        View view = inflater.inflate(R.layout.fragment_top_trains, container, false);
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.train_container);
        LinearLayout.LayoutParams lp =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0,0,0,8);
        for (int i = 0; i < 3; i++) {
            View trainTimeCard = inflater.inflate(R.layout.item_train_times, null);
            //trainTimeCard.setLayoutParams(lp);
            trainTimesList.add(trainTimeCard);
            linearLayout.addView(trainTimeCard);
        }


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
        if (getArguments() != null) {
            STATION_NAME = getArguments().getString(ARG_STATION_NAME);
        }
        int n = 1;
        for (View v : trainTimesList) {

            TextView time = (TextView) v.findViewById(R.id.train_time_remaining);
            time.setText(n + " mins");
            n++;
        }
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
