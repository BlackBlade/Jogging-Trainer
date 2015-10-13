package com.example.luca.firstprojectapp.Utilities;

import android.graphics.Color;
import android.location.Location;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

/**
 * Created by stage1 on 14/09/2015.
 */
public class PolylineManager {

    private PolylineOptions polyline = new PolylineOptions();
    private long distance;
    private Location previousLocation;

    private PolylineManager(){
        polyline.width(5);
        polyline.color(Color.BLUE);
        distance = 0;
        previousLocation = null;
    }

    private static PolylineManager instance = new PolylineManager();

    public static PolylineManager getInstance(){
        return instance;
    }

    public void addPolylinePoint(Location loc){
        if(previousLocation != null){
            distance += (long)loc.distanceTo(previousLocation);
        }
        previousLocation = loc;

        LatLng lastPosition = new LatLng(loc.getLatitude(),loc.getLongitude());
        polyline.add(lastPosition);
    }

    /**
     *
     * @return a copy of the local polyline options instance
     */
    public PolylineOptions getPolyline(){
        PolylineOptions newOptions = new PolylineOptions();
        newOptions.addAll(polyline.getPoints());
        return newOptions;
    }

    public long getDistance(){
        return distance;
    }

    public void clear(){
        polyline = new PolylineOptions();
    }
}