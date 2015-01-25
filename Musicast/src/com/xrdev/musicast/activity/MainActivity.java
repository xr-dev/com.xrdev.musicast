package com.xrdev.musicast.activity;

import com.xrdev.musicast.R;
import com.xrdev.musicast.utils.PrefsManager;

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

public class MainActivity extends Activity {

	static private final String TAG = "TCC";
	static private final String SEARCH_TERM = "searchTerm";
	
	Button testAppButton;
	Button testLoginButton;
    Button logoutTest;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Inicializar objetos da UI.
		testAppButton = (Button) findViewById(R.id.button_test_app);
		testLoginButton = (Button) findViewById(R.id.button_test_login);

        logoutTest = (Button) findViewById(R.id.button_test_logout);
		
		// Incluir os listeners aos elementos da UI.
		
		testAppButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                startFragmentsTest();
            }

        });
		
		testLoginButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                startLoginTest();
            }


        });

        logoutTest.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                PrefsManager.clearPrefs(getApplicationContext());
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

	
	public void startLoginTest() {
		Log.i(TAG, "Iniciando teste de login do Spotify. / Starting Spotify Login Test.");

        Intent intent = new Intent(MainActivity.this, SpotifyAuthActivity.class);

        startActivity(intent);
		
	}

    public void startFragmentsTest() {
        Log.i(TAG, "Iniciando teste de Fragments. / Starting Fragments test.");

        Intent intent = new Intent(MainActivity.this, MusicastActivity.class);

        startActivity(intent);
    }

}
