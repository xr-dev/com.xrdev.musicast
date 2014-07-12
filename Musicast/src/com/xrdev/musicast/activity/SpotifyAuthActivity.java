package com.xrdev.musicast.activity;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.xrdev.musicast.R;
import com.spotify.sdk.android.Spotify;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.authentication.SpotifyAuthentication;
import com.spotify.sdk.android.playback.ConnectionStateCallback;

import java.net.URLEncoder;


public class SpotifyAuthActivity extends Activity implements
        ConnectionStateCallback {

    // TODO: Replace with your client ID
    private static final String CLIENT_ID = "befa95e4d007494ea40efcdbd3e1fff7";
    // TODO: Replace with your redirect URI
    private static final String REDIRECT_URI = "musicast://callback";

    private static final String ACTIVITY_NAME = "SpotifyAuthActivity";

    private WebView authWebView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spotify_auth);

        // Para autenticação é necessário definir os escopos de acesso (array de Strings do quarto parâmetro.
        // https://developer.spotify.com/web-api/using-scopes/
        //SpotifyAuthentication.openAuthWindow(CLIENT_ID, "token", REDIRECT_URI,
        //        new String[]{"user-read-private", "playlist-read-private", "playlist-modify", "playlist-modify-private"}, null, this);

        /**
         * @TODO:
         * Construir um WebView para realizar o login. Fazer o login diretamente sem entrar num browser. A resposta vai ser tratada pelo callback no onNewIntent().
         */

        // Para autenticação é necessário definir os escopos de acesso (parâmetros &scope da URL)
        // https://developer.spotify.com/web-api/using-scopes/

        StringBuffer buffer = new StringBuffer("https://accounts.spotify.com/authorize/?");
        buffer.append("client_id=" + URLEncoder.encode(CLIENT_ID));
        buffer.append("&response_type=token");
        buffer.append("&redirect_uri=" + URLEncoder.encode(REDIRECT_URI));
        buffer.append("&scope="
                + "user-read-private" + "%20"
                + "playlist-read-private" + "%20"
                + "playlist-modify" + "%20"
                + "playlist-modify-private");


        Log.i(ACTIVITY_NAME, "URL gerada para autenticação: " + buffer.toString());

        authWebView = (WebView) findViewById(R.id.spotifyAuthWebView);
        authWebView.getSettings().setJavaScriptEnabled(true);
        authWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    SpotifyAuthActivity.this.startActivity(intent);


                }   catch(ActivityNotFoundException e) {

                    Log.e(ACTIVITY_NAME,"Não conseguiu carregar a URL " + url);
                }

                return super.shouldOverrideUrlLoading(view, url);


            }


            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl)
            {
                if (failingUrl.contains("?")) {
                    Log.v("LOG", "failing url:"+ failingUrl);
                    final int sdkVersion = Integer.parseInt(Build.VERSION.SDK);
                    if (sdkVersion > Build.VERSION_CODES.GINGERBREAD) {
                        String[] temp;
                        temp = failingUrl.split("\\?");
                        view.loadUrl(temp[0]); // load page without internal link

                        try {
                            Thread.sleep(400);
                        } catch (InterruptedException e) {

                            e.printStackTrace();
                        }
                    }

                    view.loadUrl(failingUrl);  // try again
                }
            }
        });
        //authWebView.loadUrl(authURL);
        authWebView.loadUrl(buffer.toString());


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.spotify_player, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Log.i(ACTIVITY_NAME,"Entrando no onNewIntent");

        Uri uri = intent.getData();
        if (uri != null) {
            Log.d(ACTIVITY_NAME, uri.toString());
            AuthenticationResponse response = SpotifyAuthentication.parseOauthResponse(uri);
            Spotify spotify = new Spotify(response.getAccessToken());
            String accessToken = response.getAccessToken();

            Log.i(ACTIVITY_NAME,"Access token obtido: " + accessToken);
            Toast.makeText(getApplicationContext(),"Access Token obtido: " + accessToken, Toast.LENGTH_SHORT);
            Intent intent1 = new Intent(SpotifyAuthActivity.this, LoginTest.class);
            startActivity(intent1);
        }

    }


    @Override
    public void onLoggedIn() {
        Log.d(ACTIVITY_NAME, "User logged in");

    }

    @Override
    public void onLoggedOut() {
        Log.d(ACTIVITY_NAME, "User logged out");

    }

    @Override
    public void onTemporaryError() {
        Log.d(ACTIVITY_NAME, "Temporary error occurred");

    }

    @Override
    public void onNewCredentials(String s) {
        Log.d(ACTIVITY_NAME, "User credentials blob received");

    }

    @Override
    public void onConnectionMessage(String s) {
        Log.d(ACTIVITY_NAME, "Received connection message: " + s);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
