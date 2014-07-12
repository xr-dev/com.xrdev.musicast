package com.xrdev.musicast.activity;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import com.xrdev.musicast.R;
import com.xrdev.musicast.adapter.VideoListAdapter;
//import com.xrdev.musicast.connection.YouTubeHandler;
import com.xrdev.musicast.model.VideoItem;

import java.util.ArrayList;

public class SpotifyResultActivity extends ListActivity {

	private String searchTerm;
	private static final String SEARCH_TERM = "searchTerm";
	private VideoListAdapter mAdapter;
	private final static String TAG = "TCC";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Obter a string com o termo de pesquisa enviado pela MainActivity.
		Intent recvIntent = getIntent();
		Bundle extras = recvIntent.getExtras();
		
		// Iniciar a task que faz o download.
		if (extras != null) {
			searchTerm = (String) extras.get(SpotifyResultActivity.SEARCH_TERM);
			new VideoInfoDownloader().execute(searchTerm);
		}
		
		// Criar o adapter.
		mAdapter = new VideoListAdapter(getApplicationContext());
		
		// Fazer o attach do adapter � ListView:
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

	public class VideoInfoDownloader extends AsyncTask<String, Void, ArrayList<VideoItem>>{
		ProgressDialog pd;
		public VideoInfoDownloader() {
			super();
		}
		
		
		@Override
		protected void onPreExecute() {
			// Preparar o spinner.
			pd = new ProgressDialog(SpotifyResultActivity.this);
			pd.setMessage(getString(R.string.string_loading));
			pd.show();
		}
		
		@Override
		protected ArrayList<VideoItem> doInBackground(String... arg0) {
			Log.i(TAG, "ResultActivity/AsyncTask: Entrando no doInBackground.");
			//ArrayList<VideoItem> resultItems = YouTubeHandler.searchVideos(searchTerm);

			//return resultItems;
			return null;
		}
		
		@Override
		protected void onPostExecute(ArrayList<VideoItem> resultItems) {
			super.onPostExecute(resultItems);
			Log.i(TAG, "ResultActivity/AsyncTast: Entrando no PostExecute.");
			if (pd.isShowing()) {
				pd.dismiss();
			}
			
			for (VideoItem item : resultItems) {
				mAdapter.add(item);
			}
			
		}
		
		

	}
	
}
