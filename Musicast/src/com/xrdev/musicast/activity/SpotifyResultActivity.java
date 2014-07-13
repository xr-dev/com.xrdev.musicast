package com.xrdev.musicast.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.xrdev.musicast.R;

public class SpotifyResultActivity extends Activity {

    TextView mTextView;
    String accessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spotify_result);

        mTextView = (TextView) findViewById(R.id.textview_spotify_result);

        Intent recvIntent = getIntent();
        Bundle extras = recvIntent.getExtras();

        // Iniciar a task que faz o download.
        if (extras != null) {
            accessToken = (String) extras.get("accessToken");
        }

        mTextView.append("\n Access token: " + accessToken);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.spotify_result, menu);
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
