package com.app.shovonh.traintimes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //if db is empty
        Intent intent = new Intent(this, AllTrainsActivity.class);
        startActivity(intent);
        finish();
        //else run asynctask and open traintimes


        //TODO: Check if database is empty
        //TODO: if empty, open all train activity and ask to select most used trains
        //TODO: if not, asynctask to receive arrival times and open activity with most used trains



    }
}
