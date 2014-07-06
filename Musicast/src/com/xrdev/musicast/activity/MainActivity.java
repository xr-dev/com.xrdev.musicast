package com.xrdev.musicast.activity;

import java.io.File;

import com.xrdev.musicast.R;
import com.xrdev.musicast.R.layout;
import com.xrdev.musicast.R.menu;
import com.xrdev.musicast.connection.SpotifyWrapper;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

	static private final String TAG = "TCC";
	static private final String SEARCH_TERM = "searchTerm";
	
	Button searchButton;
	Button jniTestButton;
	EditText searchField;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Inicializar objetos da UI.
		searchButton = (Button) findViewById(R.id.searchButton);
		jniTestButton = (Button) findViewById(R.id.testeStringJNI);
		searchField = (EditText) findViewById(R.id.searchField); 

		
		// Incluir os listeners aos elementos da UI.
		
		searchButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startYoutubeSearch();
			}

		});
		
		jniTestButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// startJNITest();
				spotifyInit();
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
		
		Intent intent = new Intent(MainActivity.this, ResultActivity.class);
		String searchTerm = searchField.getText().toString();
		intent.putExtra(MainActivity.SEARCH_TERM, searchTerm);
		startActivity(intent);
		
		// 
		
	}
	
	public void startJNITest() {
		/**Log.i(TAG, "[MainActivity] Iniciando teste de JNI/NDK.");
		
		String returnString = SpotifyWrapper.testeString();
		
		Toast.makeText(this, 
				"Texto retornado da JNI: " + returnString, 
				Toast.LENGTH_LONG)
				.show();
		
		**/
		

		Log.i(TAG, "[MainActivity] Iniciando teste de inicialização de sessão do Spotify.");
		
	}
	
	private void spotifyInit() {
		Log.i(TAG, "[MainActivity] Iniciando teste de inicialização de sessão do Spotify.");
		
		String cache = File.separator + "data" + File.separator + "data" + File.separator + "com.xrdev.musicast" + File.separator + getCacheDir().getName();//set to empty string to disable cache
		String traceFile = cache + File.separator + "trace_file.txt";

		try {
		    File dir = new File(cache);
		    if (!dir.exists()) {
		    	dir.mkdir();
		    }
		    File f = new File(traceFile);
		    if (!f.exists()) {
		    	Log.w(getClass().getSimpleName(), "trace file does not exist, creating...");
		    	f.createNewFile();
		    }
		    
		    /**
		     * TODO: Debug com nome de usuário.
		     */
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		    String responseString = SpotifyWrapper.init("gsb.barreto@gmail.com", "909750", cache, traceFile);
		   
		    Toast.makeText(this, 
					"Texto retornado da JNI: " + responseString, 
					Toast.LENGTH_LONG)
					.show();
		
	}

}
