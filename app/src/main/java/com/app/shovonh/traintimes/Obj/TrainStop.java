package com.app.shovonh.traintimes.Obj;

/**
 * Created by Shovon on 6/10/16.
 */

// JSON object
//        "DESTINATION": "Doraville",
//        "DIRECTION": "N",
//        "EVENT_TIME": "6/10/2016 3:50:59 PM",
//        "LINE": "GOLD",
//        "NEXT_ARR": "04:18:45 PM",
//        "STATION": "CHAMBLEE STATION",
//        "TRAIN_ID": "303326",
//        "WAITING_SECONDS": "1643",
//        "WAITING_TIME": "26 min"
public class TrainStop {
    String station, line, direction, destination, waiting_seconds, waiting_time;

    public TrainStop(){

    }

    public TrainStop(String station, String line, String direction,
                    String destination, String waiting_seconds, String waiting_time){
        this.station = station;
        this.line = line;
        this.direction = direction;
        this.destination = destination;
        this.waiting_seconds = waiting_seconds;
        this.waiting_time = waiting_time;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getWaiting_seconds() {
        return waiting_seconds;
    }

    public void setWaiting_seconds(String waiting_seconds) {
        this.waiting_seconds = waiting_seconds;
    }

    public String getWaiting_time() {
        return waiting_time;
    }

    public void setWaiting_time(String waiting_time) {
        this.waiting_time = waiting_time;
    }
}
