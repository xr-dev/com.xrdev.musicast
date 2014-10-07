package com.xrdev.musicast.activity;

import com.xrdev.musicast.R;
import com.xrdev.musicast.connection.PrefsHandler;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

public class MainActivity extends Activity {

	static private final String TAG = "TCC";
	static private final String SEARCH_TERM = "searchTerm";
	
	Button searchButton;
	Button spotifyTestLogin;
    Button spotifyTestAPI;
    Button logoutTest;
	EditText searchField;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Inicializar objetos da UI.
		searchButton = (Button) findViewById(R.id.searchButton);
		spotifyTestLogin = (Button) findViewById(R.id.spotify_test_login);
        spotifyTestAPI = (Button) findViewById(R.id.spotify_test_api_init);

        logoutTest = (Button) findViewById(R.id.logout_button);

		searchField = (EditText) findViewById(R.id.youtube_search_field);

		
		// Incluir os listeners aos elementos da UI.
		
		searchButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startYoutubeSearch();
			}

		});
		
		spotifyTestLogin.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                startLoginTest();
            }


        });

        spotifyTestAPI.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                startApiTest();
            }


        });

        logoutTest.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                PrefsHandler.clearPrefs(getApplicationContext());
                Toast.makeText(getApplicationContext(),"Logged out successfully",Toast.LENGTH_SHORT).show();
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
		Log.i(TAG, "Abrindo Activity de resultado. / Opening Result Activity.");
		
		// Colocar o conteúdo do campo como um extra no Intent para a Activity de resultado.
		
		Intent intent = new Intent(MainActivity.this, YoutubeResultActivity.class);
		String searchTerm = searchField.getText().toString();
		intent.putExtra(MainActivity.SEARCH_TERM, searchTerm);
		startActivity(intent);

		
	}
	
	public void startLoginTest() {
		Log.i(TAG, "Iniciando teste de login do Spotify. / Starting Spotify Login Test.");

        Intent intent = new Intent(MainActivity.this, SpotifyAuthActivity.class);

        startActivity(intent);
		
	}

    public void startApiTest() {
        Log.i(TAG, "Iniciando teste da API do Spotify. / Starting Spotify API test.");

        Intent intent = new Intent(MainActivity.this, PlaylistsActivity.class);

        startActivity(intent);
    }

}
