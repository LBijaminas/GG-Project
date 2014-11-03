package edu.barry.seminar.glass;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;

public class Locator extends Service implements
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener {
    LocationClient mLocationClient;

    public Locator() {
    }

    @Override
    public void onConnected(Bundle dataBundle) {
        // Display the connection status
        Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();

        Location mCurrentLocation = mLocationClient.getLastLocation();
        Log.i("DEBUG", mCurrentLocation.toString());
    }

    @Override
    public void onDisconnected() {
        // Display the connection status
        Toast.makeText(this, "Disconnected. Please re-connect.",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {}

    protected void onCreate(Bundle savedInstanceState) {
        mLocationClient = new LocationClient(this, this, this);
    }

    public void onStart(Intent intent, int i) {
        super.onStart(intent, i);
        // Connect the client.
        mLocationClient.connect();
    }

    @Override
    public IBinder onBind(Intent intent){
        return null;
    }
}
