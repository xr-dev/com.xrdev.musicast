package com.xrdev.musicast.activity;

import java.util.ArrayList;

import com.xrdev.musicast.R;
import com.xrdev.musicast.R.layout;
import com.xrdev.musicast.R.menu;
import com.xrdev.musicast.adapter.VideoListAdapter;
import com.xrdev.musicast.model.VideoItem;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;

public class ResultActivity extends ListActivity {

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
			searchTerm = (String) extras.get(ResultActivity.SEARCH_TERM);
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
	 * Inner class para execu��o em segundo plano (AsyncTask):
	 */

	public class VideoInfoDownloader extends AsyncTask<String, Void, ArrayList<VideoItem>>{
		ProgressDialog pd;
		public VideoInfoDownloader() {
			super();
		}
		
		
		@Override
		protected void onPreExecute() {
			// Preparar o spinner.
			pd = new ProgressDialog(ResultActivity.this);
			pd.setMessage(getString(R.string.string_loading));
			pd.show();
		}
		
		@Override
		protected ArrayList<VideoItem> doInBackground(String... arg0) {
			Log.i(TAG, "ResultActivity/AsyncTask: Entrando no doInBackground.");
			try {
				ArrayList<VideoItem> resultItems = new ArrayList<VideoItem>();
				
				Thread.sleep(300);
				VideoItem item = new VideoItem("http://test", searchTerm + " (teste 1)", "Descri��o", 0);
				resultItems.add(item);
				
				Thread.sleep(300);
				VideoItem item2 = new VideoItem("http://test", searchTerm + " (teste 2)", "Descri��o", 0);
				resultItems.add(item2);
				
				return resultItems;
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
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
