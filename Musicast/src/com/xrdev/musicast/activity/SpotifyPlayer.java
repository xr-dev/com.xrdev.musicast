package com.xrdev.musicast.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import com.xrdev.musicast.R;
import com.spotify.sdk.android.Spotify;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.authentication.SpotifyAuthentication;
import com.spotify.sdk.android.playback.ConnectionStateCallback;
import com.spotify.sdk.android.playback.Player;
import com.spotify.sdk.android.playback.PlayerNotificationCallback;



public class SpotifyPlayer extends Activity implements
        PlayerNotificationCallback, ConnectionStateCallback {

    // TODO: Replace with your client ID
    private static final String CLIENT_ID = "befa95e4d007494ea40efcdbd3e1fff7";
    // TODO: Replace with your redirect URI
    private static final String REDIRECT_URI = "musicast://callback";

    private static final String ACTIVITY_NAME = "SpotifyPlayer";

    private Player mPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spotify_player);

        // Para autenticação é necessário definir os escopos de acesso (array de Strings do quarto parâmetro.
        // https://developer.spotify.com/web-api/using-scopes/
        SpotifyAuthentication.openAuthWindow(CLIENT_ID, "token", REDIRECT_URI,
                new String[]{"user-read-private", "streaming"}, null, this);


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

        Log.i(ACTIVITY_NAME,"Entrando no onNewIntent");

        Uri uri = intent.getData();
        if (uri != null) {
            AuthenticationResponse response = SpotifyAuthentication.parseOauthResponse(uri);
            Spotify spotify = new Spotify(response.getAccessToken());
            mPlayer = spotify.getPlayer(this, "Musicast", this, new Player.InitializationObserver() {
                @Override
                public void onInitialized() {
                    mPlayer.addConnectionStateCallback(SpotifyPlayer.this);
                    mPlayer.addPlayerNotificationCallback(SpotifyPlayer.this);
                    mPlayer.play("spotify:track:2TpxZ7JUBn3uw46aR7qd6V");
                }

                @Override
                public void onError(Throwable throwable) {
                    Log.e(ACTIVITY_NAME, "Could not initialize player: " + throwable.getMessage());
                }
            });
        }

    }


    @Override
    public void onLoggedIn() {
        Log.d(ACTIVITY_NAME, "User logged in");

    }

    @Override
    public void onLoggedOut() {
        Log.d(ACTIVITY_NAME, "User logged out");

    }

    @Override
    public void onTemporaryError() {
        Log.d(ACTIVITY_NAME, "Temporary error occurred");

    }

    @Override
    public void onNewCredentials(String s) {
        Log.d(ACTIVITY_NAME, "User credentials blob received");

    }

    @Override
    public void onConnectionMessage(String s) {
        Log.d(ACTIVITY_NAME, "Received connection message: " + s);

    }

    @Override
    public void onPlaybackEvent(EventType eventType) {
        Log.d(ACTIVITY_NAME, "Playback event received: " + eventType.name());
    }

    @Override
    protected void onDestroy() {
        Spotify.destroyPlayer(this);
        super.onDestroy();
    }
}
