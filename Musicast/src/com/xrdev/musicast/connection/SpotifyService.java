package com.xrdev.musicast.connection;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.SettableFuture;
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

import javax.annotation.Nullable;

public class SpotifyService extends Service {
    // IDs:
    private final String TAG = "SpotifyService";
    private static final String CLIENT_ID = "befa95e4d007494ea40efcdbd3e1fff7";
    private static final String REDIRECT_URI = "musicast://callback";
    private static final String CLIENT_SECRET = "cffb5db7d8eb4910b3a95527fcee6899";

    // Login:
    private SharedPreferences prefs;
    private final String PREFS_NAME = "MusicastPrefs";
    private final String KEY_ACCESS_TOKEN = "accessToken";
    private final String KEY_REFRESH_TOKEN = "refreshToken";
    private final String KEY_CODE = "code";
    private String accessToken;
    private String refreshToken;
    private String code;


    // Api:
    private Api spotifyWebApi;
    private User user;

    private final IBinder mBinder = new LocalBinder();

    public SpotifyService() {
    }

    @Override
    // onCreate é chamado apenas quando o serviço é iniciado.
    public void onCreate() {
        //TODO: Implementar inicialização da Web API. Implementar a busca pelo SharedPreferences.
        Log.d(TAG, "SpotifyService onCreate()");

        prefs = getSharedPreferences(PREFS_NAME, 0);

        // Buscar o accessToken das preferências.

        code = getCode();


        if (code == null) {
            //AuthCode não existe. Fazer login.
            Intent intent = new Intent(SpotifyService.this, SpotifyAuthActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            // AuthCode existe. Redirecionar.
            initWebApi();
        }

        super.onCreate();
    }

    @Override
    // onStartCommand roda sempre que o serviço é chamado. Vai informar o comportamento de destruição do serviço.
    public int onStartCommand(Intent intent, int flags, int startId) {
        // START_NOT_STICKY: Android vai matar o processo caso nenhuma outra chamada esteja pendente para o Service.
        // START_STICKY: Android só vai destruir o processo se estiver com pouca memória. É necessário destruir o serviço manualmente.
        return Service.START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public void login(Activity activity) {
        Log.d(TAG, "Executando login() pelo SpotifyService. / Running login() on SpotifyService.");
        SpotifyAuthentication.openAuthWindow(CLIENT_ID, "code", REDIRECT_URI,
                new String[]{"user-read-private", "playlist-read-private", "playlist-modify", "playlist-modify-private"}, null, activity);

    }

    public void initWebApi() {
        Log.d(TAG, "Executando initWebApi(). / Running initWebApi(). ");

        code = getCode();

        spotifyWebApi = Api.builder()
                .clientId(CLIENT_ID)
                .clientSecret(CLIENT_SECRET)
                .redirectURI(REDIRECT_URI)
                .build();

        /* MÉTODO ASSÍNCRONO:
        final SettableFuture<AuthorizationCodeCredentials> authorizationCodeCredentialsFuture = spotifyWebApi.authorizationCodeGrant(code).build().getAsync();

        Futures.addCallback(authorizationCodeCredentialsFuture, new FutureCallback<AuthorizationCodeCredentials>(){
            @Override
            public void onSuccess(AuthorizationCodeCredentials authorizationCodeCredentials) {
                 *//* The tokens were retrieved successfully! *//*

                accessToken = authorizationCodeCredentials.getAccessToken();
                refreshToken = authorizationCodeCredentials.getRefreshToken();

                Log.d(TAG, "Token obtido pelo AuthorizationCodeCredentials: / Token obtained via AuthenticationCodeCredentials:  " + accessToken);
                Log.d(TAG, "Refresh Token obtido pelo AuthorizationCodeCredentials: / Refresh Token obtained via AuthenticationCodeCredentials:  " + refreshToken);
                setAccessToken(accessToken);
                setRefreshToken(refreshToken);
            }

            @Override
            public void onFailure(Throwable throwable) {
            *//* Let's say that the client id is invalid, or the code has been used more than once,
              * the request will fail. Why it fails is written in the throwable's message. *//*

            }

        });*/

        try {
            AuthorizationCodeCredentials authorizationCodeCredentials= spotifyWebApi.authorizationCodeGrant(code).build().get();

            accessToken = authorizationCodeCredentials.getAccessToken();
            refreshToken = authorizationCodeCredentials.getRefreshToken();

            if (accessToken == null) {
                Log.e(TAG, "Não foi possível obter tokens pelo AuthorizationCodeCredentials. / Unable to get tokens via AuthorizationCodeCredentials");
            } else {
                Log.d(TAG, "Token obtido pelo AuthorizationCodeCredentials: / Token obtained via AuthenticationCodeCredentials:  " + accessToken);
                Log.d(TAG, "Refresh Token obtido pelo AuthorizationCodeCredentials: / Refresh Token obtained via AuthenticationCodeCredentials:  " + refreshToken);
                setAccessToken(accessToken);
                setRefreshToken(refreshToken);
            }

        } catch (Exception e) {
            Log.e(TAG,"Não foi possível fazer login à Web API. / Unable to login to Web API.");
            e.printStackTrace();
        }

        Intent intent = new Intent(SpotifyService.this, PlaylistsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);


    }

    public void setAccessToken(String token) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_ACCESS_TOKEN, token);
        editor.apply();
        spotifyWebApi.setAccessToken(token);
    }

    public void setCode(String code) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_CODE, code);
        editor.apply();
    }

    public void setRefreshToken(String token) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_REFRESH_TOKEN, token);
        editor.apply();
        spotifyWebApi.setRefreshToken(token);
    }

    public String getAccessToken() {
        return prefs.getString(KEY_ACCESS_TOKEN,null);
    }

    public String getCode() {
        return prefs.getString(KEY_CODE, null);
    }

    public String getRefreshToken() {
        return prefs.getString(KEY_REFRESH_TOKEN, null);
    }

    public User getCurrentUser() {
        try {
            return spotifyWebApi.getMe().accessToken(accessToken).build().get();
        } catch (Exception e) {
            Log.e(TAG, "Não foi possível obter os dados do usuário atual. / Unable to get data about current user.");
            e.printStackTrace();
        }
        return null;

       /* MÉTODO ASSÍNCRONO:
       accessToken = getAccessToken();

        SettableFuture<User> userFuture = spotifyWebApi.getMe().accessToken(accessToken).build().getAsync();

        Futures.addCallback(userFuture, new FutureCallback<User>() {
            @Override
            public void onSuccess(@Nullable User result) {
                if (user == null) {
                    Log.d(TAG, "Future executado com sucesso, porém nenhum usuário foi retornado. / Future executed successfully, but no User was returned.");
                } else {
                    user = result;
                    Log.d(TAG, "Usuário obtido via FutureCallback.");
                }

            }

            @Override
            public void onFailure(Throwable t) {
                user = null;
                Log.d(TAG, "Não foi possível obter o usuário via FutureCallback. Future.onFailure / Unable to get User via FutureCallback. Future.onFailure: ");
                t.printStackTrace();
            }
        });*/

    }

    public ArrayList<PlaylistItem> getUserPlaylists() {

        User currentUser = getCurrentUser();
        ArrayList<PlaylistItem> result = new ArrayList<PlaylistItem>();

        final UserPlaylistsRequest request = spotifyWebApi.getPlaylistsForUser(currentUser.getId()).build();

        try {
            final Page<SimplePlaylist> playlistsPage = request.get();

            for (SimplePlaylist playlist : playlistsPage.getItems()) {
                // TODO: Melhorar os dados que vão ser incluídos no Adapter de playlists.

                String name = playlist.getName();
                int numTracks = playlist.getTracks().getTotal();
                result.add(new PlaylistItem(name, numTracks));

            }

        } catch (Exception e) {
            Log.e(TAG,"Erro ao obter listas de reprodução do usuário. / Unable to get user playlists.");
        }

        return result;

    }

    public class LocalBinder extends Binder {
        SpotifyService getService() {
            return SpotifyService.this;
        }
    }

}
