package com.app.shovonh.traintimes;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.shovonh.traintimes.Obj.TrainStop;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class TopTrainsFragment extends Fragment {

    //TODO: if no upcoming trains, display a message

    private static final String ARG_STATION_NAME = "param1";

    public static final String LOG_TAG = TopTrainsFragment.class.getSimpleName();
    private static String STATION_NAME;
    private static TrainStop[] upcomingTrains;

    ArrayList<View> trainTimesDisplay = new ArrayList<>();

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        if (getArguments() != null) {
            STATION_NAME = getArguments().getString(ARG_STATION_NAME);
            upcomingTrains = Utilities.getRelevantStations(STATION_NAME);

        }
        View view = inflater.inflate(R.layout.fragment_top_trains, container, false);
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.train_container);
        LinearLayout.LayoutParams lp =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0,0,0,8);
        for (int i = 0; i < upcomingTrains.length; i++) {
            View trainTimeCard = inflater.inflate(R.layout.item_train_times, null);
            //trainTimeCard.setLayoutParams(lp);
            TrainStop t = upcomingTrains[i];
            LinearLayout bgColor = (LinearLayout) trainTimeCard.findViewById(R.id.train_color_bg);
            TextView time = (TextView) trainTimeCard.findViewById(R.id.train_time_remaining);
            TextView direction = (TextView) trainTimeCard.findViewById(R.id.train_direction);
            TextView destination = (TextView) trainTimeCard.findViewById(R.id.train_final_stop);
            TextView lineColor = (TextView) trainTimeCard.findViewById(R.id.train_line_color);
            View coloredDivider = trainTimeCard.findViewById(R.id.train_colored_divider);


            bgColor.setBackgroundColor(Utilities.getColorRes(getContext(), t.getLine()));
            time.setText(t.getWaitingTime());
            direction.setText(Utilities.getDirectionString(t.getDirection()));
            destination.setText(t.getDestination());
            lineColor.setText(Utilities.getLine(t.getLine()));
            coloredDivider.setBackgroundColor(Utilities.getColorRes(getContext(), t.getLine()));

            if (i == upcomingTrains.length - 1)
                coloredDivider.setVisibility(View.GONE);

            trainTimesDisplay.add(trainTimeCard);
            linearLayout.addView(trainTimeCard);
        }
        return view;
    }

//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//
//        outState.putString(ARG_STATION_NAME, STATION_NAME);
//
//        super.onSaveInstanceState(outState);
//    }
}
