package com.xrdev.musicast;

import android.content.Context;
import android.content.res.Configuration;
import android.support.multidex.MultiDexApplication;

/**
 * Created by Guilherme on 07/10/2014.
 */
public class ApplicationState extends MultiDexApplication {
    @Override
    public void onCreate(){
        super.onCreate();

        final Context mContext = this;

        new Runnable() {

            @Override
            public void run() {
                // Lógica caso necessária. Usar mContext ao invés de this.
            }
        }.run();

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig){

    }

}
