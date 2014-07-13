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
import com.spotify.sdk.android.Spotify;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.authentication.SpotifyAuthentication;
import com.spotify.sdk.android.playback.ConnectionStateCallback;
import com.xrdev.musicast.connection.SpotifyService;
import com.xrdev.musicast.connection.SpotifyServiceBinder;


public class SpotifyAuthActivity extends Activity implements
        ConnectionStateCallback {

    // TODO: Armazenar os IDs num .properties?
    private static final String TAG = "SpotifyAuthActivity";
    private SpotifyServiceBinder mSpotifyBinder;

    private Button mLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spotify_auth);

        mLoginButton = (Button) findViewById(R.id.spotify_login_button);

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLoginWindow();
            }
        });

        mSpotifyBinder = new SpotifyServiceBinder(this);
        mSpotifyBinder.bindService();

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
        super.onNewIntent(intent);

        Log.i(TAG,"Entrando no onNewIntent");

        Uri uri = intent.getData();
        if (uri != null) {
            Log.d(TAG, uri.toString());
            AuthenticationResponse response = SpotifyAuthentication.parseOauthResponse(uri);
            // Spotify spotify = new Spotify(response.getAccessToken());
            String accessToken = response.getAccessToken();


            Log.i(TAG, "Access token obtido: " + accessToken);

            // Iniciar o serviço que manterá a Spotify Web API.

            Intent intent1 = new Intent(SpotifyAuthActivity.this, SpotifyResultActivity.class);
            intent1.putExtra("accessToken",accessToken);
            startActivity(intent1);
        }

    }


    public void openLoginWindow() {

        // Para autenticação é necessário definir os escopos de acesso (array de Strings do quarto parâmetro.
        // https://developer.spotify.com/web-api/using-scopes/

        mSpotifyBinder.getService().login(this);

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
