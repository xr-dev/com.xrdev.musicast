package com.xrdev.musicast.activity;

import com.xrdev.musicast.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {

	static private final String TAG = "TCC";
	static private final String SEARCH_TERM = "searchTerm";
	
	Button searchButton;
	Button spotifyTestButton;
	EditText searchField;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Inicializar objetos da UI.
		searchButton = (Button) findViewById(R.id.searchButton);
		spotifyTestButton = (Button) findViewById(R.id.spotify_test_login);
		searchField = (EditText) findViewById(R.id.youtube_search_field);

		
		// Incluir os listeners aos elementos da UI.
		
		searchButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startYoutubeSearch();
			}

		});
		
		spotifyTestButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                startSpotifyTest();
            }


        });

		
		
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
		
	}
	
	public void startYoutubeSearch() {
		Log.i(TAG, "[MainActivity] Abrindo Activity de resultado.");
		
		// Colocar o conteúdo do campo como um extra no Intent para a Activity de resultado.
		
		Intent intent = new Intent(MainActivity.this, YoutubeResultActivity.class);
		String searchTerm = searchField.getText().toString();
		intent.putExtra(MainActivity.SEARCH_TERM, searchTerm);
		startActivity(intent);
		
		// 
		
	}
	
	public void startSpotifyTest() {
		Log.i(TAG, "[MainActivity] Iniciando teste do Spotify.");

        Intent intent = new Intent(MainActivity.this, SpotifyAuthActivity.class);

        startActivity(intent);
		
	}

}
