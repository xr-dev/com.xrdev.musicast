package com.xrdev.musicast.connection;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by Guilherme on 13/07/2014.
 */
public class SpotifyServiceBinder extends Binder {

    private final String TAG = "SpotifyServiceBinder";
    private Context mContext;
    private SpotifyService mBoundService;
    private boolean mIsBound;

    public SpotifyServiceBinder(Context context) {
        mContext = context;
    }

    public SpotifyService getService() {

        if (mBoundService == null) {
            Log.e(TAG, "Serviço não estava vinculado. / Service was not bound.");
            throw new RuntimeException("Service was not bound.");
        }

        return mBoundService;
    }

    public void bindService(){
        mContext.bindService(new Intent(mContext,SpotifyService.class), mConnection, Context.BIND_AUTO_CREATE);
    }


    public void doUnbindService() {
        Log.i(TAG, "Serviço desvinculado a pedido do app. / Service unbound by app.");
        if (mIsBound) {
            mContext.unbindService(mConnection);
            mIsBound = false;
        }
    }


    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            SpotifyService.LocalBinder binder = (SpotifyService.LocalBinder) service;
            mBoundService = binder.getService();
            mIsBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName className) {
            mBoundService = null;
            mIsBound = false;
            Log.e(TAG, "Serviço desconectado / Service disconnected.");
        }
    };

}
