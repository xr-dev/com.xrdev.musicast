package com.xrdev.musicast.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.xrdev.musicast.R;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.authentication.SpotifyAuthentication;
import com.spotify.sdk.android.playback.ConnectionStateCallback;
import com.xrdev.musicast.connection.SpotifyHandler;
import com.xrdev.musicast.connection.SpotifyServiceBinder;


public class SpotifyAuthActivity extends Activity implements
        ConnectionStateCallback {

    // TODO: Armazenar os IDs num .properties?
    private static final String TAG = "SpotifyAuthActivity";
    private static final String EXTRA_CODE = "code";
    private SpotifyServiceBinder mSpotifyBinder;

    private Button mLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spotify_auth);

        mLoginButton = (Button) findViewById(R.id.spotify_login_button);

        mSpotifyBinder = new SpotifyServiceBinder(this);
        mSpotifyBinder.bindService();

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSpotifyBinder.getService().login(SpotifyAuthActivity.this);
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.spotify_player, menu);
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


    @Override
    protected void onNewIntent(Intent intent) {
        // Será executado quando o usuário JÁ tiver logado pelo browser, por meio do IntentFilter.
        super.onNewIntent(intent);

        Log.i(TAG,"Entrando no onNewIntent");

        Uri uri = intent.getData();
        if (uri != null) {
            Log.d(TAG, uri.toString());
            AuthenticationResponse response = SpotifyAuthentication.parseOauthResponse(uri);
            // Spotify spotify = new Spotify(response.getAccessToken());

            String accessToken = response.getAccessToken();
            //TODO: Code não está sendo obtido corretamente de response.
            String code = response.getCode();



            // mSpotifyBinder.getService().setAccessToken(accessToken);
            // mSpotifyBinder.getService().setCode(code);

            Intent playlistIntent = new Intent(SpotifyAuthActivity.this, PlaylistsActivity.class);
            playlistIntent.putExtra(EXTRA_CODE, code);
            startActivity(playlistIntent);

            // SpotifyHandler.setAuthCredentials(getApplicationContext(), code);




            Log.i(TAG, "Access token obtido: " + accessToken);
            Log.i(TAG, "Code obtido: " + code);


            // Iniciar o serviço que manterá a Spotify Web API.
            // mSpotifyBinder.getService().initWebApi();

        }

    }

    @Override
    public void onLoggedIn() {
        Log.d(TAG, "User logged in");

    }

    @Override
    public void onLoggedOut() {
        Log.d(TAG, "User logged out");

    }

    @Override
    public void onTemporaryError() {
        Log.d(TAG, "Temporary error occurred");

    }

    @Override
    public void onNewCredentials(String s) {
        Log.d(TAG, "User credentials blob received");

    }

    @Override
    public void onConnectionMessage(String s) {
        Log.d(TAG, "Received connection message: " + s);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
