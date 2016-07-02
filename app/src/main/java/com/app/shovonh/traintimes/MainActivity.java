package com.app.shovonh.traintimes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.app.shovonh.traintimes.Data.DBHelper;
import com.app.shovonh.traintimes.Obj.TrainStop;

import org.parceler.Parcels;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static final String LOG_TAG = MainActivity.class.getSimpleName();
    public Intent topTrainsIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DBHelper dbHelper = new DBHelper(this);
        ArrayList<String> mainStations = dbHelper.getAllStations();

        if (mainStations.size() > 0) {
            topTrainsIntent = new Intent(this, TopTrainsActivity.class);
            FetchTrainTimes fetchTrainTimes = new FetchTrainTimes(new FetchTrainTimes.FetchComplete() {
                @Override
                public void onFetchCompete(ArrayList<TrainStop> trainStops) {
                    topTrainsIntent.putExtra(TopTrainsActivity.EXTRA_ARRAYLIST, Parcels.wrap(trainStops));
                    startActivity(topTrainsIntent);
                    finish();

                }
            });
            fetchTrainTimes.execute();
        } else {
            Intent intent = new Intent(this, AllTrainsActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
            finish();
        }


    }

}
