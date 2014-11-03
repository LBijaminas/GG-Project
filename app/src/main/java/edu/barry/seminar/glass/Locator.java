package edu.barry.seminar.glass;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class Locator extends Service {
    public Location l;
    LocationManager locationManager;

    private IBinder mBinder = new LocalBinder();

    public class LocalBinder extends Binder {
        Locator getLocator() {
            return Locator.this;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                l = location;
                Log.i("[DEBUG] Location: ", l.toString());
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0,
                locationListener);

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent){
        return mBinder;
    }
}
