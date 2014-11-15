package com.example.lukas.googleglassproject;

import android.location.Location;

/**
 * Created by lukas on 11/15/14.
 */
public class DistanceCalculator {
    private final static int RADIUS = 6371000; // earth's radius in meters
    private final static int INCREMENT = 5; // 5 meter increment
    private final static double ANGULAR_DISTANCE = (double) INCREMENT / (double) RADIUS;

    public DistanceCalculator() {
    }

    /**
     * Given on the params, it calculates a new location and returns it as an Android object
     *
     * @param lat     current latitude
     * @param longt   current longitude
     * @param bearing initial bearing
     * @return Android.Location with the new location
     */
    public static Location getLocation(double lat, double longt, double bearing) {
        double rad_lat = Math.toRadians(lat);
        double rad_long = Math.toRadians(longt);
        double rad_bearing = Math.toRadians(bearing);

        // formula to calculate new latitude
        double new_lat = Math.asin(Math.sin(rad_lat) * Math.cos(ANGULAR_DISTANCE) +
                Math.cos(rad_lat) * Math.sin(ANGULAR_DISTANCE) * Math.cos(rad_bearing));

        // formula to calculate new longtitude
        double new_long = rad_long + Math.atan2(Math.sin(rad_bearing) * Math.sin(ANGULAR_DISTANCE) * Math.cos(rad_lat),
                Math.cos(ANGULAR_DISTANCE) - Math.sin(rad_lat) * Math.sin(new_lat));


        // convert them back to degrees and return as a location
        Location loc = new Location("New Location");
        loc.setLongitude(Math.toDegrees(new_long));
        loc.setLatitude(Math.toDegrees(new_lat));
        return loc;
    }


}
