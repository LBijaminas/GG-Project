package com.example.lukas.googleglassproject;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

/**
 * A transparent {@link Activity} displaying a "Stop" options menu to remove the {Live Card}.
 */
public class LiveCardMenuActivity extends Activity {
    Location currentLocation;
    LocationManager locationManager;

    private Locator locator;

    private ServiceConnection locatorConnection;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*
         * Monitors state of service.
         */
        locatorConnection = new ServiceConnection() {
            public void onServiceConnected(ComponentName className, IBinder service) {
                // get the locator instance from the binder
                locator = ((Locator.LocalBinder)service).getLocator();
            }

            public void onServiceDisconnected(ComponentName className) {
                // we don't need the locator anymore so make sure it isn't used
                locator = null;
            }
        };

        // bind the activity to the Locator service
        bindService(new Intent(this, Locator.class), locatorConnection, Context.BIND_AUTO_CREATE);

        /*
         * TODO: Initialize the GUI here.
         */
    }

    @Override
    public boolean onKeyDown(int keycode, KeyEvent event) {
        if (keycode == KeyEvent.KEYCODE_BACK) {
            // get the sensor manager from the device
            SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

            Log.i("Long", Double.toString(currentLocation.getLongitude()));
            Log.i("Lat", Double.toString(currentLocation.getLatitude()));

            String b_name = DistanceCalculator.scanForBuilding(25.879738, -80.197840, 0);


            try {
                Log.i("B_NAME", b_name);
            } catch (Exception e) {
                Log.i("ERROR", "Building not found");
            }
            return true;
        }
        return super.onKeyDown(keycode, event);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        // Open the options menu right away.
        openOptionsMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.live_card, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_stop:
                // Stop the service which will unpublish the live card.
                stopService(new Intent(this, LiveCardService.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onOptionsMenuClosed(Menu menu) {
        super.onOptionsMenuClosed(menu);
        // Nothing else to do, finish the Activity.
        finish();
    }

    /*
     * This class handles connecting and disconnecting from the Locator service.
     */
}
