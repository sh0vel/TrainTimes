package com.app.shovonh.traintimes;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import com.app.shovonh.traintimes.Obj.TrainStop;

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

    public static TrainStop[] getRelevantStations(String stationName, ArrayList<TrainStop> allStations) {
        ArrayList<TrainStop> relevantStations = new ArrayList<>();
        for (TrainStop t : allStations) {
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
}
