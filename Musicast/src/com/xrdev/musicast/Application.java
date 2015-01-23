package com.xrdev.musicast;

import android.content.Context;
import android.content.res.Configuration;
import android.support.multidex.MultiDexApplication;
import android.util.Log;
import android.widget.ListAdapter;

import com.google.android.gms.cast.ApplicationMetadata;
import com.google.sample.castcompanionlibrary.cast.VideoCastManager;
import com.google.sample.castcompanionlibrary.cast.callbacks.VideoCastConsumerImpl;
import com.xrdev.musicast.model.PlaylistItem;
import com.xrdev.musicast.model.QueueList;

/**
 * Created by Guilherme on 07/10/2014.
 */
public class Application extends MultiDexApplication {

    private static String CAST_APPLICATION_ID;
    private static String CHANNEL_NAMESPACE;
    private static VideoCastManager mCastMgr = null;
    private static VideoCastConsumerImpl mCastConsumer = null;
    private static QueueList mQueue = null;
    

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

        mCastMgr.addVideoCastConsumer(mCastConsumer);

        return mCastMgr;
    }

    public static VideoCastConsumerImpl getConsumerImpl() {
        mCastConsumer = new VideoCastConsumerImpl() {

            @Override
            public void onApplicationConnected(ApplicationMetadata appMetadata,
                                               String sessionId, boolean wasLaunched) {
                Log.i("APPLICATION", "CAST APPLICATION CONNECTED");
            }

            @Override
            public void onDataMessageReceived(String message) {
                Log.i("APPLICATION", "CAST RECEIVED MESSAGE:" + message);
            }
        };

        return mCastConsumer;

    }


}
