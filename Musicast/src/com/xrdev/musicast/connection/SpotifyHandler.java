package com.xrdev.musicast.connection;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.spotify.sdk.android.authentication.SpotifyAuthentication;
import com.xrdev.musicast.activity.PlaylistsActivity;
import com.xrdev.musicast.activity.SpotifyAuthActivity;
import com.xrdev.musicast.connection.spotifywrapper.Api;
import com.xrdev.musicast.connection.spotifywrapper.methods.UserPlaylistsRequest;
import com.xrdev.musicast.connection.spotifywrapper.models.AuthorizationCodeCredentials;
import com.xrdev.musicast.connection.spotifywrapper.models.Page;
import com.xrdev.musicast.connection.spotifywrapper.models.SimplePlaylist;
import com.xrdev.musicast.connection.spotifywrapper.models.User;
import com.xrdev.musicast.model.PlaylistItem;

import java.util.ArrayList;

/**
 * Created by Guilherme on 22/07/2014.
 */
public class SpotifyHandler {
    // IDs:
    private static final String TAG = "SpotifyHandler";
    private static final String CLIENT_ID = "befa95e4d007494ea40efcdbd3e1fff7";
    private static final String REDIRECT_URI = "musicast://callback";
    private static final String CLIENT_SECRET = "cffb5db7d8eb4910b3a95527fcee6899";

    // Login:

    private static final String PREFS_NAME = "MusicastPrefs";
    private static final String KEY_ACCESS_TOKEN = "accessToken";
    private static final String KEY_REFRESH_TOKEN = "refreshToken";
    private static final String KEY_CODE = "code";

    // Api:
    private static Api api = Api.builder()
            .clientId(CLIENT_ID)
            .clientSecret(CLIENT_SECRET)
            .redirectURI(REDIRECT_URI)
            .build();

    public static ArrayList<PlaylistItem> getUserPlaylists(Context context) {

        User currentUser = getCurrentUser(context);

        // Nenhum usuário atual, não retornar nenhuma playlist.
        if (currentUser == null)
            return null;

        ArrayList<PlaylistItem> result = new ArrayList<PlaylistItem>();

        final UserPlaylistsRequest request = api.getPlaylistsForUser(currentUser.getId()).build();


        try {
            final Page<SimplePlaylist> playlistsPage = request.get();

            for (SimplePlaylist playlist : playlistsPage.getItems()) {
                // TODO: Melhorar os dados que vão ser incluídos no Adapter de playlists.

                String name = playlist.getName();
                int numTracks = playlist.getTracks().getTotal();
                result.add(new PlaylistItem(name, numTracks));

            }

        } catch (Exception e) {
            Log.e(TAG, "Erro ao obter listas de reprodução do usuário. / Unable to get user playlists. Error: " + e.getMessage());
        }

        return result;

    }

    public static User getCurrentUser(Context context) {
        try {
            String accessToken = getAccessTokenFromPrefs(context);

            Log.d(TAG, "Access token obtido pelo getCurrentUser(): / Access token obtained on getCurrentUser(): " + accessToken);

            if (accessToken == null) {
                return null;
            } else {
                return api.getMe().accessToken(accessToken).build().get();
            }
        } catch (Exception e) {
            Log.e(TAG, "Não foi possível obter os dados do usuário atual. / Unable to get data about current user. Error: " + e.getMessage());
        }
        return null;
    }


    public static void login(Activity activity) {
        Log.d(TAG, "Executando login() pelo SpotifyService. / Running login() on SpotifyService.");
        SpotifyAuthentication.openAuthWindow(CLIENT_ID, "code", REDIRECT_URI,
                new String[]{"user-read-private", "playlist-read-private", "playlist-modify", "playlist-modify-private"}, null, activity);
    }

    public static void setAuthCredentials(Context context, String code) {
        try {

            setCodeToPrefs(context, code);

            AuthorizationCodeCredentials authorizationCodeCredentials = api.authorizationCodeGrant(code).build().get();

            String accessToken = authorizationCodeCredentials.getAccessToken();
            String refreshToken = authorizationCodeCredentials.getRefreshToken();

            if (accessToken == null) {
                Log.e(TAG, "Não foi possível obter tokens pelo AuthorizationCodeCredentials. / Unable to get tokens via AuthorizationCodeCredentials");
            } else {
                Log.d(TAG, "Token obtido pelo AuthorizationCodeCredentials: / Token obtained via AuthenticationCodeCredentials:  " + accessToken);
                Log.d(TAG, "Refresh Token obtido pelo AuthorizationCodeCredentials: / Refresh Token obtained via AuthenticationCodeCredentials:  " + refreshToken);
                api.setAccessToken(accessToken);
                api.setRefreshToken(refreshToken);
                setAccessTokenToPrefs(context, accessToken);
                setRefreshTokenToPrefs(context, refreshToken);
            }



        } catch (Exception e) {
            Log.e(TAG,"Não foi possível fazer login à Web API. / Unable to login to Web API. Error: " + e.getMessage());
            e.printStackTrace();
        }

    }

    public static void startLoginActivity(Context context) {
        Intent intent = new Intent(context, SpotifyAuthActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void openLoginPrompt(Activity activity) {
        Log.d(TAG, "Abrindo browser pelo SpotifyHandler. / Opening browser via SpotifyHandler.");
        SpotifyAuthentication.openAuthWindow(CLIENT_ID, "code", REDIRECT_URI,
                new String[]{"user-read-private", "playlist-read-private", "playlist-modify", "playlist-modify-private"}, null, activity);
    }

    public static void setCodeToPrefs(Context context, String code) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_CODE, code);
        editor.apply();
    }

    public static String getCodeFromPrefs(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        return prefs.getString(KEY_CODE, null);
    }

    public static void setAccessTokenToPrefs(Context context, String accessToken) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_ACCESS_TOKEN, accessToken);
        editor.apply();
    }

    public static String getAccessTokenFromPrefs(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        return prefs.getString(KEY_ACCESS_TOKEN, null);
    }

    public static void setRefreshTokenToPrefs(Context context, String refreshToken) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_REFRESH_TOKEN, refreshToken);
        editor.apply();
    }

    public static String getRefreshTokenFromPrefs(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        return prefs.getString(KEY_REFRESH_TOKEN, null);
    }

}
