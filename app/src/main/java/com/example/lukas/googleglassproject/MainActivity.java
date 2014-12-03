package com.example.lukas.googleglassproject;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class MainActivity extends Activity {

    Location currentLocation;
    LocationManager locationManager;

    private Locator locator;

    private ServiceConnection locatorConnection;

    private Handler mHandler;

    private Compass mCompass;

    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // create a new thread handler
        this.mHandler = new Handler();

        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        locatorConnection = new ServiceConnection() {
            public void onServiceConnected(ComponentName className, IBinder service) {
                // get the locator instance from the binder
                locator = ((Locator.LocalBinder) service).getLocator();
            }

            public void onServiceDisconnected(ComponentName className) {
                // we don't need the locator anymore so make sure it isn't used
                locator = null;
            }
        };

        this.mCompass = new Compass(sensorManager);

        //bind the activity to the Locator service
        bindService(new Intent(this, Locator.class), locatorConnection, Context.BIND_AUTO_CREATE);

        mCompass.start();
        new Thread(new Runnable() {
            @Override
            public void run() {

                // infinite loop
                while (true) {
                    try {
                        // wait for 2 seconds
                        Thread.sleep(3000);
                        mCompass.flag = true;
                        mHandler.post(new Runnable() {

                            @Override
                            public void run() {

//                                Log.i("Loc_Lat", Double.toString(locator.l.getLatitude()));
//                                Log.i("Loc_Lon", Double.toString(locator.l.getLongitude()));
//                                Log.i("Degrees", Double.toString(mCompass.getDegrees()));

                                //String b_name = DistanceCalculator.scanForBuilding(locator.l.getLatitude(), locator.l.getLongitude(), mCompass.getDegrees());


                                try {
                                    //((TextView) findViewById(R.id.location_name)).setText(b_name);
                                    //Log.i("Building name", b_name);
                                } catch (Exception e) {
                                    Log.i("ERROR", "Building not found");
                                }
                            }
                        });
                    } catch (NullPointerException e) {
                        /*
                         * It takes some time for the location to generate the first update. 3 seconds should be
                         * more than enough, but let's have this error handler just in case
                         */
                        Log.i("Error", "Location not yet updated");
                    } catch (InterruptedException e) {

                    }
                }
            }
        }).start();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return super.onOptionsItemSelected(item);
    }
}
