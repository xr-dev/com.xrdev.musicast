package com.xrdev.musicast.connection;

import android.app.Activity;

import com.spotify.sdk.android.authentication.SpotifyAuthentication;
import com.xrdev.musicast.activity.MainActivity;

/**
 * Created by Guilherme on 12/07/2014.
 */
public class SpotifyHandler {

    public static void login(final String CLIENT_ID, final String REDIRECT_URI, Activity hostActivity) {
        SpotifyAuthentication.openAuthWindow(CLIENT_ID, "token", REDIRECT_URI,
                new String[]{"user-read-private", "playlist-read-private", "playlist-modify", "playlist-modify-private"}, null, hostActivity);
    }

    public static void openAuthWindow() {

    }

}
