package com.xrdev.musicast.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.xrdev.musicast.Application;
import com.xrdev.musicast.R;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.xrdev.musicast.utils.PrefsManager;
import com.xrdev.musicast.connection.SpotifyManager;


public class SpotifyAuthActivity extends Activity {

    private static final String TAG = "SpotifyAuthActivity";
    private static final String EXTRA_CODE = "code";
    private Button mLoginButton;
    private Button mSkipButton;
    private TextView mAuxText;
    private int REQUEST_CODE = SpotifyManager.REQUEST_CODE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spotify_auth);

        mLoginButton = (Button) findViewById(R.id.spotify_login_button);
        mSkipButton = (Button) findViewById(R.id.spotify_login_skip);
        mAuxText = (TextView) findViewById(R.id.textAux);

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // mSpotifyBinder.getService().login(SpotifyAuthActivity.this);
                SpotifyManager.openLoginPrompt(SpotifyAuthActivity.this);
            }
        });

        mSkipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // mSpotifyBinder.getService().login(SpotifyAuthActivity.this);
                Intent intent = new Intent(SpotifyAuthActivity.this, MusicastActivity.class);
                intent.putExtra(MusicastActivity.EXTRA_IS_LOGIN_SKIPPED, true);
                intent.putExtra(MusicastActivity.EXTRA_WAS_LOGIN_PROMPTED, true);
                startActivity(intent);
            }
        });

        String thisUuid = PrefsManager.getUUID(this);
        String hostUuid = Application.getAdmin();

        if (thisUuid.equals(hostUuid) || Application.getMode() == Application.MODE_SOLO) {
            mAuxText.setText(R.string.login_required_for_admin);
            mSkipButton.setVisibility(View.GONE);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mLoginButton.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            mLoginButton.setLayoutParams(params);
        } else {
            mAuxText.setText(R.string.login_optional_for_guest);
            mSkipButton.setVisibility(View.VISIBLE);
        }
    }


/*    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.spotify_player, menu);
        return true;
    }*/

/*    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }*/


/*    @Override
    protected void onNewIntent(Intent intent) {
        // Será executado quando o usuário JÁ tiver logado pelo browser, por meio do IntentFilter.
        super.onNewIntent(intent);

        Log.i(TAG,"Entrando no onNewIntent");

        Uri uri = intent.getData();
        if (uri != null) {
            Log.d(TAG, uri.toString());
            AuthenticationResponse response = SpotifyAuthentication.parseOauthResponse(uri);
            String accessToken = response.getAccessToken();
            String code = response.getCode();

            PrefsManager.setCodeToPrefs(this, code);

            Intent playlistIntent = new Intent(SpotifyAuthActivity.this, MusicastActivity.class);
            playlistIntent.putExtra(EXTRA_CODE, code);
            startActivity(playlistIntent);

            Log.i(TAG, "Access token obtido: " + accessToken);
            Log.i(TAG, "Code obtido: " + code);

        }

    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);
            if (response.getType() == AuthenticationResponse.Type.CODE) {

                String accessToken = response.getAccessToken();
                String code = response.getCode();

                PrefsManager.setCodeToPrefs(this, code);

                Intent redirectIntent = new Intent(SpotifyAuthActivity.this, MusicastActivity.class);
                redirectIntent.putExtra(EXTRA_CODE, code);
                redirectIntent.putExtra(MusicastActivity.EXTRA_WAS_LOGIN_PROMPTED, true);


                Log.i(TAG, "Access token obtido: " + accessToken);
                Log.i(TAG, "Code obtido: " + code);

                startActivity(redirectIntent);
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
