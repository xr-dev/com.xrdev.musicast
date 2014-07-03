package com.xrdev.musicast.connection;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class SpotifyService extends Service {

	private final IBinder mBinder = new LocalBinder();

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	
	public class LocalBinder extends Binder {
		SpotifyService getService() {
			return SpotifyService.this;
		}
	}
	
}
