package com.app.shovonh.traintimes;

import android.os.AsyncTask;

import com.app.shovonh.traintimes.Obj.TrainStop;

import java.util.ArrayList;

/**
 * Created by Shovon on 5/25/16.
 */
public class FetchTrainTimes extends AsyncTask<String, Void, TrainStop[]> {
    //TODO: get train times from marta
    //TODO: pass information and completion to main activity via listner
    //TODO: pass info to ui after initial request
    ArrayList<TrainStop> trainStops;

    public interface FetchComplete{
        void onFetchCompete(ArrayList<TrainStop> trainStops);
    }

    @Override
    protected TrainStop[] doInBackground(String... strings) {
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        trainStops = new ArrayList<>();
    }

    @Override
    protected void onPostExecute(TrainStop[] trainStops) {
        super.onPostExecute(trainStops);
    }
}
