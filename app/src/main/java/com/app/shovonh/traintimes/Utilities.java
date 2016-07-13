package com.app.shovonh.traintimes;

import android.content.Context;
import android.location.Location;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.app.shovonh.traintimes.Obj.TrainStop;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;

/**
 * Created by Shovon on 6/30/16.
 */
public class Utilities {

    public static String getDirectionString(String d) {
        switch (d) {
            case "N":
                return "Northbound";
            case "S":
                return "Southbound";
            case "E":
                return "Eastbound";
            case "W":
                return "Westbound";
            default:
                return "";
        }
    }

    public static int getColorRes(Context context, String color) {
        switch (color) {
            case "BLUE":
                return ContextCompat.getColor(context, R.color.blueLine);
            case "RED":
                return ContextCompat.getColor(context, R.color.redLine);
            case "GOLD":
                return ContextCompat.getColor(context, R.color.goldLine);
            case "GREEN":
                return ContextCompat.getColor(context, R.color.greenLine);
            default:
                return ContextCompat.getColor(context, R.color.mtrl_white);
        }
    }

    public static String getLine(String color) {
        switch (color) {
            case "BLUE":
                return "Blue line";
            case "RED":
                return "Red line";
            case "GOLD":
                return "Gold line";
            case "GREEN":
                return "Green line";
            default:
                return "Unknown line";
        }
    }

    public static TrainStop[] getRelevantStations(String stationName) {
        ArrayList<TrainStop> relevantStations = new ArrayList<>();
        for (TrainStop t : FetchTrainTimes.trainStops) {
            String name = t.getStation();
            if ((stationName + " station").equalsIgnoreCase(name)) {
                relevantStations.add(t);
            } else if (stationName.contains("/")) {
                if ((stationName.substring(0, stationName.indexOf("/")) + " station").equalsIgnoreCase(name))
                    relevantStations.add(t);
            }
        }
        TrainStop[] stops = new TrainStop[relevantStations.size()];
        for (int i = 0; i < relevantStations.size(); i++) {
            stops[i] = relevantStations.get(i);
        }
        return stops;
    }

    public static JSONArray getAllStationsFromJSON(Context context) throws UnsupportedEncodingException, JSONException {

        InputStream is = context.getResources().openRawResource(R.raw.all_stations_json);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } catch (IOException e) {
            Log.e("IOException", e.toString());
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                Log.e("IOException", e.toString());
            }
        }

        String jsonString = writer.toString();
        return new JSONArray(jsonString);
    }

    public static String getNearestStation(Context c, double latitude, double longitude) {
        String JSON_NAME = "name";
        String JSON_LATITUDE = "latitude";
        String JSON_LONGITUDE = "longitude";

        double lowestDistance = 100;
        String lowestName = "";
        try {
            JSONArray jsonArray = getAllStationsFromJSON(c);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                double distance =
                        distanceFormula(latitude, longitude, jsonObject.getDouble(JSON_LATITUDE), jsonObject.getDouble(JSON_LONGITUDE));
                if (distance < lowestDistance) {
                    lowestDistance = distance;
                    lowestName = jsonObject.getString(JSON_NAME);
                }
            }
        } catch (UnsupportedEncodingException e) {

        } catch (JSONException e) {

        }
        return lowestName;
    }

    public static Location getCoordinates(Context context, String name) {
        Location location = new Location("");
        String JSON_NAME = "name";
        String JSON_LATITUDE = "latitude";
        String JSON_LONGITUDE = "longitude";
        try {
            JSONArray jsonArray = getAllStationsFromJSON(context);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (name.equalsIgnoreCase(jsonObject.getString(JSON_NAME))) {
                    location.setLatitude(jsonObject.getDouble(JSON_LATITUDE));
                    location.setLongitude(jsonObject.getDouble(JSON_LONGITUDE));
                    break;
                }
            }
        } catch (JSONException e) {

        } catch (UnsupportedEncodingException e) {

        } finally {
            return location;
        }

    }

    public static double distanceFormula(double x1, double y1, double x2, double y2) {
        double x2minusx1 = x2 - x1;
        double y2minusy1 = y2 - y1;
        double x2minusx1sqrd = x2minusx1 * x2minusx1;
        double y2minusy1sqrd = y2minusy1 * y2minusy1;
        double sqrtOfSum = Math.sqrt(x2minusx1sqrd + y2minusy1sqrd);

        return sqrtOfSum;
    }


}
