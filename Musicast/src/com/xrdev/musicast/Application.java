package com.xrdev.musicast;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.google.android.gms.cast.ApplicationMetadata;
import com.google.sample.castcompanionlibrary.cast.VideoCastManager;
import com.google.sample.castcompanionlibrary.cast.callbacks.BaseCastConsumerImpl;
import com.google.sample.castcompanionlibrary.cast.callbacks.VideoCastConsumerImpl;
import com.xrdev.musicast.model.LocalQueue;
import com.xrdev.musicast.utils.JsonConverter;
import com.xrdev.musicast.utils.PrefsManager;

/**
 * Created by Guilherme on 07/10/2014.
 */
public class Application extends MultiDexApplication {

    private static final String CAST_APPLICATION_ID = "E7378308";
    private static final String CHANNEL_NAMESPACE = "urn:x-cast:com.xrdev.musicast";

    private static final String TAG = "Application";

    private static VideoCastManager mCastMgr = null;
    private static VideoCastConsumerImpl mCastConsumer = null;
    private static BaseCastConsumerImpl mBaseConsumer = null;
    private static LocalQueue mLocalQueue = null;
    private static OnMessageReceived mCallback;

    public static final int MODE_UNSTARTED = 0;
    public static final int MODE_SOLO = 1;
    public static final int MODE_PARTY = 2;
    private static int mMode;

    private static JsonConverter mJsonConverter;


    public interface OnMessageReceived {
        public void onMessageReceived(String message);
        public void onDisconnected();
        public void onConnected();
        public void onModeChanged();
    }
    

    @Override
    public void onCreate(){
        super.onCreate();

        new Runnable() {

            @Override
            public void run() {
                // Lógica caso necessária. Usar mContext ao invés de this.
                mMode = MODE_UNSTARTED;
            }
        }.run();

    }


    @Override
    public void onConfigurationChanged(Configuration newConfig){

    }

    public static void setListener(Activity activity) {
        mCallback = (OnMessageReceived) activity;
    }

    public static LocalQueue getQueue(String playlistId){
        if (null == mLocalQueue || !mLocalQueue.getPlaylistId().equals(playlistId))
            mLocalQueue = LocalQueue.initialize(playlistId);

        return mLocalQueue;

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

            mCastConsumer = getConsumerImpl();

            mBaseConsumer = getBaseImpl();

            mCastMgr.addVideoCastConsumer(mCastConsumer);
            mCastMgr.addBaseCastConsumer(mBaseConsumer);

        }
        mCastMgr.setContext(context);

        return mCastMgr;
    }

    public static VideoCastConsumerImpl getConsumerImpl() {
        mCastConsumer = new VideoCastConsumerImpl() {

            @Override
            public void onApplicationConnected(ApplicationMetadata appMetadata,
                                               String sessionId, boolean wasLaunched) {
                Log.i(TAG, "CAST APPLICATION CONNECTED");
                mCallback.onConnected();
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


    public static void setMode(int mode){
        mCallback.onModeChanged();
        mMode = mode;
    }

    public static int getMode() {
        return mMode;
    }

    public static JsonConverter getConverter() {

        if (mJsonConverter == null) {
            mJsonConverter = new JsonConverter();
        }

        return mJsonConverter;
    }
}
