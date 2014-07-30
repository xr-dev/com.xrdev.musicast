package com.xrdev.musicast.activity;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import com.xrdev.musicast.R;
import com.xrdev.musicast.adapter.PlaylistAdapter;
import com.xrdev.musicast.connection.SpotifyHandler;
import com.xrdev.musicast.connection.SpotifyServiceBinder;
import com.xrdev.musicast.model.PlaylistItem;

import java.util.ArrayList;

public class PlaylistsActivity extends ListActivity {


	private PlaylistAdapter mAdapter;
	private final static String TAG = "PlaylistsActivity";
    private final static String EXTRA_CODE = "code";
    private SpotifyServiceBinder mSpotifyBinder;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        Log.d(TAG, "PlaylistsActivity.onCreate()");




        new PlaylistDownloader().execute();

		// Criar o adapter.
		mAdapter = new PlaylistAdapter(getApplicationContext());
		
		// Fazer o attach do adapter à ListView:
		getListView().setAdapter(mAdapter);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.result, menu);
		return true;
	}
	
	
	/**
	 * Inner class para execução em segundo plano (AsyncTask):
	 */

	public class PlaylistDownloader extends AsyncTask<String, Void, ArrayList<PlaylistItem>>{
		ProgressDialog pd;
        // SpotifyServiceBinder mBinder = new SpotifyServiceBinder(getApplicationContext());

		public PlaylistDownloader() {
			super();
		}
		
		
		@Override
		protected void onPreExecute() {
			// Preparar o spinner
            pd = new ProgressDialog(PlaylistsActivity.this);
			pd.setMessage(getString(R.string.string_loading));
			pd.show();

		}
		
		@Override
		protected ArrayList<PlaylistItem> doInBackground(String... arg0) {
			Log.i(TAG, "AsyncTask: Entrando no doInBackground.");

            // mBinder.bindService();

            // ArrayList<PlaylistItem> resultItems = mBinder.getService().getUserPlaylists();

            Intent fromIntent = getIntent();

            String code = fromIntent.getStringExtra(EXTRA_CODE);

            // Significa que a PlaylistsActivity foi aberta pelo onNewIntent da AuthActivity, logo será necessário setar os tokens.
            if (code != null) {
                SpotifyHandler.setAuthCredentials(getApplication(), code);
            }

            ArrayList<PlaylistItem> resultItems = SpotifyHandler.getUserPlaylists(getApplicationContext());

            return resultItems;

		}
		
		@Override
		protected void onPostExecute(ArrayList<PlaylistItem> resultItems) {
			super.onPostExecute(resultItems);
			Log.i(TAG, "ResultActivity/AsyncTast: Entrando no PostExecute.");
			if (pd.isShowing()) {
				pd.dismiss();
			}

            if (resultItems == null) {
                SpotifyHandler.startLoginActivity(getApplicationContext());
            } else {
                for (PlaylistItem item : resultItems) {
                    mAdapter.add(item);
                }
            }


			
		}
		
		

	}
	
}
