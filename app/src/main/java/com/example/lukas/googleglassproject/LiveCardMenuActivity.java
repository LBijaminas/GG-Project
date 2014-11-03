package com.example.lukas.googleglassproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import edu.barry.seminar.glass.Locator;

/**
 * A transparent {@link Activity} displaying a "Stop" options menu to remove the {Live Card}.
 */
public class LiveCardMenuActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState){
        //creates a new intent on the creation of the activity and starts the service
        Intent locationIntent = new Intent(this, Locator.class);
        startService(locationIntent);

        /*
         * TODO: Initialize the GUI here.
         */
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
}
