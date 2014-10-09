package com.xrdev.musicast.connection.chromecast;

import android.support.v7.media.MediaRouter;

import com.google.android.gms.cast.CastDevice;

/**
 * Created by Guilherme on 07/10/2014.
 */
public class MediaRouterCallback extends MediaRouter.Callback {
    @Override
    public void onRouteSelected(MediaRouter router, MediaRouter.RouteInfo info) {
        CastDevice mSelectedDevice = CastDevice.getFromBundle(info.getExtras());
        String routeId = info.getId();
    }

    @Override
    public void onRouteUnselected(MediaRouter router, MediaRouter.RouteInfo info) {
        //teardown();
        //mSelectedDevice = null;
    }

}
