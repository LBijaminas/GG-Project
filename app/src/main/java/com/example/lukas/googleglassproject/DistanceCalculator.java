package com.example.lukas.googleglassproject;

import android.location.Location;
import android.util.Log;

/**
 * Created by lukas on 11/15/14.
 */
public class DistanceCalculator {
    private final static int RADIUS = 6371000; // earth's radius in meters
    private final static int INCREMENT = 5; // 5 meters increment
    private final static int MAX_DISTANCE = 400; // 400 meters is the maximum distance to search for

    private static double lat, longt, bearing, ang_distance;

    public DistanceCalculator() {
    }

    /**
     * It calculates a new location and returns it as an Android object
     * @return Android.Location with the new location
     */
    private static Location getLocation() {
        double rad_lat = Math.toRadians(lat);
        double rad_long = Math.toRadians(longt);
        double rad_bearing = Math.toRadians(bearing);

        // formula to calculate new latitude
        double new_lat = Math.asin(Math.sin(rad_lat) * Math.cos(ang_distance) +
                Math.cos(rad_lat) * Math.sin(ang_distance) * Math.cos(rad_bearing));

        // formula to calculate new longtitude
        double new_long = rad_long + Math.atan2(Math.sin(rad_bearing) * Math.sin(ang_distance) * Math.cos(rad_lat),
                Math.cos(ang_distance) - Math.sin(rad_lat) * Math.sin(new_lat));


        // convert them back to degrees and return as a location
        Location loc = new Location("New Location");
        loc.setLongitude(Math.toDegrees(new_long));
        loc.setLatitude(Math.toDegrees(new_lat));
        return loc;
    }

    /**
     * Given on the params, it finds whether a building is in front of the viewport or not
     *
     * @param lat     current latitude
     * @param longt   current longitude
     * @param bearing initial bearing
     * @return String building name, if found, null if not found
     */
    public static String scanForBuilding(double lat, double longt, double bearing) {
        DistanceCalculator.lat = lat;
        DistanceCalculator.longt = longt;
        DistanceCalculator.bearing = bearing;
        int inc = 0;

        Location temp_loc;

        String b_name; // string to hold building name
        // create a Buildings instance for checking
        Buildings build;

        for (; inc <= MAX_DISTANCE; inc += INCREMENT) {
            ang_distance = (double) inc / (double) RADIUS;
            temp_loc = getLocation();

            if ((b_name = (new Buildings()).GetBuildingName(temp_loc.getLatitude(), temp_loc.getLongitude())) != null) {
                return b_name;
            }
        }

        return null;

    }

}
