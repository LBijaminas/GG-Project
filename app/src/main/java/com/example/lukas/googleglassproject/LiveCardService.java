package com.example.lukas.googleglassproject;

import com.google.android.glass.timeline.LiveCard;
import com.google.android.glass.timeline.LiveCard.PublishMode;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.Parcelable;
import android.util.Log;
import android.widget.RemoteViews;
import android.hardware.SensorManager;
import android.content.Context;

/**
 * A {@link Service} that publishes a {@link LiveCard} in the timeline.
 */
public class LiveCardService extends Service {

    private static final String LIVE_CARD_TAG = "LiveCardService";

    private LiveCard mLiveCard;

    private Compass mCompass;

    private Location currentLocation;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // get the sensor manager from the device
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        this.currentLocation = new Location("Lukas");
        this.currentLocation.setLatitude(25.879738);
        this.currentLocation.setLongitude(-80.197840);

        String b_name = DistanceCalculator.scanForBuilding(25.879738, -80.197840, 0);

        //String b_name = (new Buildings()).GetBuildingName(25.879738, -80.19783999999999);

        try {
            Log.i("B_NAME", b_name);
        } catch (Exception e) {
            Log.i("ERROR", "Building not found");
        }

//        Intent i= new Intent(this, Locator.class);
//        // potentially add data to the intent
//        i.putExtra("KEY1", "Value to be used by the service");
//        this.startService(i);


        //ReverseGeocodeLookupTask task = new ReverseGeocodeLookupTask();
        //task.applicationContext = this;
        //task.execute();
        // and create the compass
        //this.mCompass = new Compass(sensorManager);

        // also we need to start the compass
        //this.mCompass.start();


    }

    public class ReverseGeocodeLookupTask extends AsyncTask<Void, Void, String> {
        protected Context applicationContext;

        @Override
        protected void onPreExecute() {
            ;
        }

        @Override
        protected String doInBackground(Void... params) {
            String localityName = "";

            if (currentLocation != null) {
                //Log.i("LUkas", RevGeoCoder.RevGeocode(currentLocation));
            }

            return localityName;
        }

        @Override
        protected void onPostExecute(String result) {
            ;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mLiveCard == null) {
            mLiveCard = new LiveCard(this, LIVE_CARD_TAG);

            RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.live_card);
            mLiveCard.setViews(remoteViews);

            // Display the options menu when the live card is tapped.
            Intent menuIntent = new Intent(this, LiveCardMenuActivity.class);
            mLiveCard.setAction(PendingIntent.getActivity(this, 0, menuIntent, 0));
            mLiveCard.publish(PublishMode.REVEAL);

        } else {
            mLiveCard.navigate();
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        if (mLiveCard != null && mLiveCard.isPublished()) {
            mLiveCard.unpublish();
            mLiveCard = null;
        }
        super.onDestroy();
    }
}
