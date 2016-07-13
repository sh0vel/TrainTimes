package com.app.shovonh.traintimes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.app.shovonh.traintimes.Data.DBHelper;

public class MainActivity extends AppCompatActivity {
    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final DBHelper dbHelper = new DBHelper(this);

        //if internet connected
        FetchTrainTimes fetchTrainTimes = new FetchTrainTimes(new FetchTrainTimes.FetchComplete() {
            @Override
            public void onFetchCompete() {
                if (dbHelper.getCount() > 0) {
                    Intent topTrains = new Intent(getApplicationContext(), TopTrainsActivity.class);
                    startActivity(topTrains);
                    finish();
                } else {
                    Intent allTrains = new Intent(getApplicationContext(), AllTrainsActivity.class);
                    allTrains.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(allTrains);
                    finish();
                }
            }
        });
        fetchTrainTimes.execute();
    }
}
