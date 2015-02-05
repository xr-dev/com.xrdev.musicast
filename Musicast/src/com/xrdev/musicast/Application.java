package com.xrdev.musicast;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.google.android.gms.cast.ApplicationMetadata;
import com.google.gson.Gson;
import com.google.sample.castcompanionlibrary.cast.VideoCastManager;
import com.google.sample.castcompanionlibrary.cast.callbacks.BaseCastConsumerImpl;
import com.google.sample.castcompanionlibrary.cast.callbacks.VideoCastConsumerImpl;
import com.xrdev.musicast.activity.MusicastActivity;
import com.xrdev.musicast.model.JsonModel;
import com.xrdev.musicast.model.QueueList;

/**
 * Created by Guilherme on 07/10/2014.
 */
public class Application extends MultiDexApplication {

    private static String CAST_APPLICATION_ID;
    private static String CHANNEL_NAMESPACE;
    private static final String TAG = "Application";
    private static VideoCastManager mCastMgr = null;
    private static VideoCastConsumerImpl mCastConsumer = null;
    private static BaseCastConsumerImpl mBaseConsumer = null;
    private static QueueList mQueue = null;
    private static OnMessageReceived mCallback;



    public interface OnMessageReceived {
        public void onMessageReceived(String message);
        public void onDisconnected();
    }
    

    @Override
    public void onCreate(){
        super.onCreate();

        final Context mContext = this;

        new Runnable() {

            @Override
            public void run() {
                // Lógica caso necessária. Usar mContext ao invés de this.
                CAST_APPLICATION_ID = getString(R.string.cast_app_id);
                CHANNEL_NAMESPACE = getString(R.string.cast_channel_namespace);

            }
        }.run();

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig){

    }

    public static void setListener(Activity activity) {
        mCallback = (OnMessageReceived) activity;
    }

    public static QueueList getQueue(String playlistId){
        if (null == mQueue || !mQueue.getPlaylistId().equals(playlistId))
            mQueue = QueueList.initialize(playlistId);

        return mQueue;

    }

    public static VideoCastManager getCastManager(Context context) {
        if (null == mCastMgr) {
            mCastMgr = VideoCastManager.initialize(context, CAST_APPLICATION_ID, null, CHANNEL_NAMESPACE);
            // Parâmetros initialize:
            // Contexto, ID da aplicação, Activity que será o Player Control (null para usar a Activity padrão do CCL), custom channel.
            mCastMgr.enableFeatures(
                    VideoCastManager.FEATURE_NOTIFICATION |
                            VideoCastManager.FEATURE_LOCKSCREEN |
                            VideoCastManager.FEATURE_WIFI_RECONNECT |
                            VideoCastManager.FEATURE_CAPTIONS_PREFERENCE |
                            VideoCastManager.FEATURE_DEBUGGING);

        }
        mCastMgr.setContext(context);

        mCastConsumer = getConsumerImpl();

        mBaseConsumer = getBaseImpl();

        mCastMgr.addVideoCastConsumer(mCastConsumer);
        mCastMgr.addBaseCastConsumer(mBaseConsumer);

        return mCastMgr;
    }

    public static VideoCastConsumerImpl getConsumerImpl() {
        mCastConsumer = new VideoCastConsumerImpl() {

            @Override
            public void onApplicationConnected(ApplicationMetadata appMetadata,
                                               String sessionId, boolean wasLaunched) {
                Log.i(TAG, "CAST APPLICATION CONNECTED");
            }

            @Override
            public void onDataMessageReceived(String message) {
                Log.i(TAG, "CAST RECEIVED MESSAGE:" + message);
                mCallback.onMessageReceived(message);
            }

            @Override
            public void onApplicationDisconnected(int errorCode) {
                Log.i(TAG, "CAST APPLICATION DISCONNECTED");
            }
        };

        return mCastConsumer;

    }

    public static BaseCastConsumerImpl getBaseImpl() {
        mBaseConsumer = new BaseCastConsumerImpl() {
            @Override
        public void onDisconnected(){
                Log.i(TAG, "CAST APPLICATION DISCONNECTED");
                mCallback.onDisconnected();
            }
        };
        return mBaseConsumer;
    }

}
