package com.xrdev.musicast;

import android.content.Context;
import android.content.res.Configuration;
import android.support.multidex.MultiDexApplication;

import com.google.sample.castcompanionlibrary.cast.BaseCastManager;
import com.google.sample.castcompanionlibrary.cast.VideoCastManager;

/**
 * Created by Guilherme on 07/10/2014.
 */
public class Application extends MultiDexApplication {

    private static String CAST_APPLICATION_ID;
    private static String CHANNEL_NAMESPACE;
    private static VideoCastManager mCastMgr = null;
    

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
                // Inserir o namespace "musicastChannel" nas Strings.
            }
        }.run();

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig){

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
        return mCastMgr;
    }


}
