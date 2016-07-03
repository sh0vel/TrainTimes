package com.app.shovonh.traintimes;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.app.shovonh.traintimes.Obj.TrainStop;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Shovon on 5/25/16.
 */
public class FetchTrainTimes extends AsyncTask<Void, Void, ArrayList<TrainStop>> {
    public static final String LOG_TAG = FetchTrainTimes.class.getSimpleName();
    public static ArrayList<TrainStop> trainStops;
    FetchComplete fetchCompleteListener;


    public interface FetchComplete {
        void onFetchCompete(ArrayList<TrainStop> trainStops);
    }

    public FetchTrainTimes(FetchComplete fetchCompleteListener){
        this.fetchCompleteListener = fetchCompleteListener;
    }

    //    String station, line, direction, destination, waiting_seconds, waiting_time;
    private ArrayList<TrainStop> getTrainStopsFromJson(String jsonStr)
            throws JSONException {
        final String API_STATION = "STATION";
        final String API_LINE = "LINE";
        final String API_DIRECTION = "DIRECTION";
        final String API_DESTINATION = "DESTINATION";
        final String API_WAIT_SECONDS = "WAITING_SECONDS";
        final String API_WATING_TIME = "WAITING_TIME";


        JSONArray jsonArray = new JSONArray(jsonStr);
        ArrayList<TrainStop> trainStops = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++){
            String station, line, direction, destination, waiting_seconds, waiting_time;

            JSONObject trainStop = jsonArray.getJSONObject(i);
            station = trainStop.getString(API_STATION);
            line = trainStop.getString(API_LINE);
            direction = trainStop.getString(API_DIRECTION);
            destination = trainStop.getString(API_DESTINATION);
            waiting_seconds = trainStop.getString(API_WAIT_SECONDS);
            waiting_time = trainStop.getString(API_WATING_TIME);

            TrainStop stop = new TrainStop(station, line, direction, destination, waiting_seconds, waiting_time);
            trainStops.add(stop);

        }
        return trainStops;
    }

    @Override
    protected ArrayList<TrainStop> doInBackground(Void... voids) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String resultsJsonStr = null;
        String format = "json";


        try {
            final String MOVIE_BASE_URL = "http://developer.itsmarta.com/RealtimeTrain/RestServiceNextTrain/GetRealtimeArrivals?";
            final String API_KEY = "apikey";

            Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                    .appendQueryParameter(API_KEY, BuildConfig.MARTA_API_KEY)
                    .build();

            URL url = new URL(builtUri.toString());

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                resultsJsonStr = null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append((line + "\n"));
            }

            if (buffer.length() == 0) {
                resultsJsonStr = null;
            }

            resultsJsonStr = buffer.toString();

        } catch (IOException e) {
            Log.e("MovieFragment", "Error ", e);
            resultsJsonStr = null;
        } finally {
            if (urlConnection != null)
                urlConnection.disconnect();
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("MoviesFragment", "Error closingstream", e);
                }
            }
        }


        try {
            return getTrainStopsFromJson(resultsJsonStr);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected void onPostExecute(ArrayList<TrainStop> trainStops) {
        super.onPostExecute(trainStops);
        this.trainStops = trainStops;
        fetchCompleteListener.onFetchCompete(trainStops);
    }
}
