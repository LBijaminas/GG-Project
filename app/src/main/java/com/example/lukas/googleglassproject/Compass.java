package com.example.lukas.googleglassproject;

/**
 * Created by lukas on 10/17/14.
 */

import android.hardware.GeomagneticField;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

/*
 * TODO: Change that the degrees would be from 0 to 360 instead of -180 to 180. But do we really need that?
 */

public class Compass {


    /**
     * The sensors used by the compass are mounted in the movable arm on Glass. Depending on how
     * this arm is rotated, it may produce a displacement ranging anywhere from 0 to about 12
     * degrees. Since there is no way to know exactly how far the arm is rotated, we just split the
     * difference.
     */
    private static final int ARM_DISPLACEMENT_DEGREES = 6;

    private final SensorManager sensorManager; // to get the sensor manager
    private final float[] rotationMatrix;  // to compute the rotation matrix
    private final float[] orientation; // to compute the orientation

    private float degrees;
    private GeomagneticField mGeomagneticField;

    public boolean flag = false; // this flag will be used to handling the angle calculation

    // create the event handler
    private SensorEventListener sensorListener = new SensorEventListener() {

        // default function that has to be implemented, but we don't need it at this point
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {}

        /**
         * The function handles the sensor measurement change
         *
         * @param event the Sensor event that has happened
         */
        @Override
        public void onSensorChanged(SensorEvent event) {

            // we only care about the rotation about our axis, so let's
            // see if the rotation happened

            // handle the flag
            if (flag && event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {

                // get the rotation matrix and put it into our variable
                SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values);

                // remap the matrix, so that we could calculate the rotaton about the Z axis
                SensorManager.remapCoordinateSystem(rotationMatrix, SensorManager.AXIS_X,
                        SensorManager.AXIS_Z, rotationMatrix);

                // compute the device orientation based on the rotation matrix
                SensorManager.getOrientation(rotationMatrix, orientation);


                //calculate the degrees
                degrees = (float) (Math.toDegrees(orientation[0]) % 360.0f) - ARM_DISPLACEMENT_DEGREES;

                Log.d("Degrees", Double.toString(degrees));
            }
        }

    };

    public Compass(SensorManager sensorManager) {
        this.rotationMatrix = new float[16];
        this.orientation = new float[9];
        this.sensorManager = sensorManager;
    }


    /**
     * In start function we merely register the event listener
     */
    public void start() {
        this.sensorManager.registerListener(this.sensorListener,
                this.sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR),
                SensorManager.SENSOR_DELAY_UI);
    }

    /**
     * Unregisters the event listener
     */
    public void stop() {
        this.sensorManager.unregisterListener(this.sensorListener);
    }

    /**
     * Gets the user's current heading, in degrees. The result is guaranteed to be between 0 and
     * 360.
     *
     * @return the user's current heading, in degrees
     */
    public float getDegrees() {
        return (this.degrees > 0) ? this.degrees : (this.degrees + 360);
    }

}
