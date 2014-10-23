package com.example.lukas.googleglassproject;

/**
 * Created by lukas on 10/17/14.
 */

import android.content.Context;
import android.hardware.GeomagneticField;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

public class Compass {


    /**
     * The sensors used by the compass are mounted in the movable arm on Glass. Depending on how
     * this arm is rotated, it may produce a displacement ranging anywhere from 0 to about 12
     * degrees. Since there is no way to know exactly how far the arm is rotated, we just split the
     * difference.
     */
    private static final int ARM_DISPLACEMENT_DEGREES = 6;

    private final SensorManager mSensorManager; // to get the sensor manager
    private final float[] mRotationMatrix;  // to compute the rotation matrix
    private final float[] mOrientation; // to compute the orientation

    private float mHeading;
    private float mPitch;
    private GeomagneticField mGeomagneticField;

    private SensorEventListener mSensorListener = new SensorEventListener() {

        // default function that has to be implemented
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {}

        @Override
        public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
                // Get the current heading from the sensor, then notify the listeners of the
                // change.
                SensorManager.getRotationMatrixFromVector(mRotationMatrix, event.values);
                SensorManager.remapCoordinateSystem(mRotationMatrix, SensorManager.AXIS_X,
                        SensorManager.AXIS_Z, mRotationMatrix);
                SensorManager.getOrientation(mRotationMatrix, mOrientation);

                // Store the pitch (used to display a message indicating that the user's head
                // angle is too steep to produce reliable results.
                mPitch = (float) Math.toDegrees(mOrientation[1]);

                // Convert the heading (which is relative to magnetic north) to one that is
                // relative to true north, using the user's current location to compute this.
                float magneticHeading = (float) Math.toDegrees(mOrientation[0]);
                mHeading = (computeTrueNorth(magneticHeading) % 360.0f) - ARM_DISPLACEMENT_DEGREES;
                Log.i("LUKAS", Float.toString(mHeading));
            }
        }

    };

    public Compass(SensorManager sensorManager) {
        this.mRotationMatrix = new float[16];
        this.mOrientation = new float[9];
        this.mSensorManager = sensorManager;
    }


    /**
     * In start function we merely register the event listener
     */
    public void start() {
        mSensorManager.registerListener(mSensorListener,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR),
                SensorManager.SENSOR_DELAY_UI);
    }

    /**
     * Unregisters the event listener
     */
    public void stop() {
        mSensorManager.unregisterListener(mSensorListener);
    }

    /**
     * Gets the user's current heading, in degrees. The result is guaranteed to be between 0 and
     * 360.
     *
     * @return the user's current heading, in degrees
     */
    public float getHeading() {
        return mHeading;
    }

    /**
     * Gets the user's current pitch (head tilt angle), in degrees. The result is guaranteed to be
     * between -90 and 90.
     *
     * @return the user's current pitch angle, in degrees
     */
    public float getPitch() {
        return mPitch;
    }

    /**
     * Use the magnetic field to compute true (geographic) north from the specified heading
     * relative to magnetic north.
     *
     * @param heading the heading (in degrees) relative to magnetic north
     * @return the heading (in degrees) relative to true north
     */
    private float computeTrueNorth(float heading) {
        if (mGeomagneticField != null) {
            return heading + mGeomagneticField.getDeclination();
        } else {
            return heading;
        }
    }
}
