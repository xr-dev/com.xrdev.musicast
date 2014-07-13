package com.xrdev.musicast.connection;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.spotify.sdk.android.authentication.SpotifyAuthentication;
import com.xrdev.musicast.activity.SpotifyAuthActivity;

public class SpotifyService extends Service {
    private final String TAG = "SpotifyService";
    private static final String CLIENT_ID = "befa95e4d007494ea40efcdbd3e1fff7";
    private static final String REDIRECT_URI = "musicast://callback";
    private final IBinder mBinder = new LocalBinder();

    public SpotifyService() {
    }

    @Override
    // onCreate é chamado apenas quando o serviço é iniciado.
    public void onCreate() {
        //TODO: Implementar inicialização da Web API.
        Log.d(TAG, "onCreate()");
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

    public void login(Context context) {
        Log.d(TAG, "Executando login() pelo SpotifyService. / Running login() on SpotifyService.");
        SpotifyAuthentication.openAuthWindow(CLIENT_ID, "token", REDIRECT_URI,
                new String[]{"user-read-private", "playlist-read-private", "playlist-modify", "playlist-modify-private"}, null, (Activity) context);

    }



    public class LocalBinder extends Binder {
        SpotifyService getService() {
            return SpotifyService.this;
        }
    }

}
