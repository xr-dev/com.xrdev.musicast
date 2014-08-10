package com.xrdev.musicast.activity;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import com.xrdev.musicast.R;
import com.xrdev.musicast.adapter.TrackAdapter;
import com.xrdev.musicast.connection.SpotifyHandler;
import com.xrdev.musicast.model.PlaylistItem;
import com.xrdev.musicast.model.TrackItem;

import java.util.ArrayList;

public class TracksActivity extends ListActivity {


	private TrackAdapter mAdapter;
    private PlaylistItem playlistItem;
	private final static String TAG = "TracksActivity";
    private final static String EXTRA_CODE = "code";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        Log.d(TAG, "TracksActivity.onCreate()");

        Intent fromIntent = getIntent();
        playlistItem = new PlaylistItem(fromIntent);
        setTitle(playlistItem.getName());

        new TrackDownloader().execute();

		// Criar o adapter.
		mAdapter = new TrackAdapter(getApplicationContext());
		
		// Fazer o attach do adapter à ListView:
		getListView().setAdapter(mAdapter);
		
	}

    @Override
    protected void onPause() {
        super.onPause();
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

	public class TrackDownloader extends AsyncTask<String, Void, ArrayList<TrackItem>>{
		ProgressDialog pd;
        // SpotifyServiceBinder mBinder = new SpotifyServiceBinder(getApplicationContext());

		public TrackDownloader() {
			super();
		}
		
		
		@Override
		protected void onPreExecute() {
			// Preparar o spinner
            pd = new ProgressDialog(TracksActivity.this);
			pd.setMessage(getString(R.string.string_loading));
			pd.show();

		}
		
		@Override
		protected ArrayList<TrackItem> doInBackground(String... arg0) {
			Log.i(TAG, "AsyncTask: Entrando no doInBackground.");

            if (playlistItem != null){
                Log.d(TAG, "Buscando músicas da Playlist ID / Fetching tracks from Playlist ID: " + playlistItem.getPlaylistId());
                Log.d(TAG, "Owner ID: " + playlistItem.getOwnerId());
                return SpotifyHandler.getPlaylistTracks(playlistItem);
            } else {
                return null;
            }
		}
		
		@Override
		protected void onPostExecute(ArrayList<TrackItem> resultItems) {
			super.onPostExecute(resultItems);
			if (pd.isShowing()) {
				pd.dismiss();
			}

            if (resultItems == null) {
                // TODO: Alterar esse método. Uma lista de reprodução vazia tem null como resultado esperado.
                SpotifyHandler.startLoginActivity(getApplicationContext());
            } else {
                for (TrackItem item : resultItems) {
                    mAdapter.add(item);
                }
            }

		}

	}
	
}
