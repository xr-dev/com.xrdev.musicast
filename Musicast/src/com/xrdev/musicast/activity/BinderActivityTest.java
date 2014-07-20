package com.xrdev.musicast.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import com.xrdev.musicast.R;
import com.xrdev.musicast.connection.SpotifyServiceBinder;

public class BinderActivityTest extends Activity {

    private static final String TAG = "Teste Binder -> Redirecionamento para Auth.";
    private SpotifyServiceBinder mSpotifyBinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binder_activity_test);

        Log.d(TAG, "BinderActivityTest.onCreate()");

        mSpotifyBinder = new SpotifyServiceBinder(this);

        mSpotifyBinder.bindService();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.binder_activity_test, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
