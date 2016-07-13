package com.app.shovonh.traintimes.Obj;

import org.parceler.Parcel;

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
@Parcel
public class TrainStop{
    public String station, line, direction, destination, waitingSeconds, waitingTime;
    public long latitude, longitude;


    public TrainStop(){

    }

    public TrainStop(String station, String line, String direction,
                     String destination, String waitingSeconds, String waitingTime){
        this.station = station;
        this.line = line;
        this.direction = direction;
        this.destination = destination;
        this.waitingSeconds = waitingSeconds;
        this.waitingTime = waitingTime;
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

    public String getWaitingSeconds() {
        return waitingSeconds;
    }

    public void setWaitingSeconds(String waitingSeconds) {
        this.waitingSeconds = waitingSeconds;
    }

    public String getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(String waitingTime) {
        this.waitingTime = waitingTime;
    }


    public long getLatitude() {
        return latitude;
    }

    public void setLatitude(long latitude) {
        this.latitude = latitude;
    }

    public long getLongitude() {
        return longitude;
    }

    public void setLongitude(long longitude) {
        this.longitude = longitude;
    }
}
