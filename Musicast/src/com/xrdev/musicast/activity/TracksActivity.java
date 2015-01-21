package com.xrdev.musicast.activity;

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.media.MediaRouteSelector;
import android.support.v7.media.MediaRouter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.sample.castcompanionlibrary.cast.BaseCastManager;
import com.google.sample.castcompanionlibrary.cast.VideoCastManager;
import com.google.sample.castcompanionlibrary.widgets.MiniController;
import com.xrdev.musicast.Application;
import com.xrdev.musicast.R;
import com.xrdev.musicast.adapter.TrackAdapter;
import com.xrdev.musicast.connection.SpotifyManager;
import com.xrdev.musicast.connection.YouTubeManager;
import com.xrdev.musicast.model.PlaylistItem;
import com.xrdev.musicast.model.TrackItem;
import com.xrdev.musicast.utils.JsonConverter;

import java.util.ArrayList;

public class TracksActivity extends ActionBarActivity {


    private final static String TAG = "TracksActivity";
    private final static String EXTRA_CODE = "code";
    private final static String CAST_APP_ID = "E3FA9FF0";

    private TracksFragment tracksFragment;
    private TrackAdapter mAdapter;
    private PlaylistItem playlistItem;

    private ActionBar actionBar;
    private MenuItem mediaRouteMenuItem;

    private VideoCastManager mCastMgr;
    private MiniController mMiniController;
    private MediaRouter mMediaRouter;
    private MediaRouteSelector mMediaRouteSelector;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_tracks);

        Log.d(TAG, "TracksActivity.onCreate()");

        // Buscar dados do Intent:
        Intent fromIntent = getIntent();
        playlistItem = new PlaylistItem(fromIntent);
        setTitle(playlistItem.getName());

        actionBar = getSupportActionBar();

        // -------------------- VERIFICAR GOOGLE PLAY SERVICES, PREPARAR CAST --------------------

        BaseCastManager.checkGooglePlayServices(this);

        mCastMgr = Application.getCastManager(this);

        mMiniController = (MiniController) findViewById(R.id.miniController1);
        mCastMgr.addMiniController(mMiniController);



        // -------------------- PREPARAR LISTFRAGMENT --------------------
		// Criar o adapter.
		//mAdapter = new TrackAdapter(getApplicationContext());
        mAdapter = new TrackAdapter(this);
        FragmentManager fm = getFragmentManager();

        // Fazer o attach do adapter à ListFragment:

        tracksFragment = (TracksFragment) fm.findFragmentById(R.id.fragment_tracks_list);

        if (tracksFragment == null) {
            tracksFragment = TracksFragment.newInstance();
        }

        tracksFragment.setListAdapter(mAdapter);

        // Estabelecer sessão com o chromecast:
        mCastMgr.reconnectSessionIfPossible(getApplicationContext(), false, 10); // context, showDialog, timeout.

        new TrackDownloader().execute();

        // Iniciar o FragmentManager e incluir o fragment da lista ao layout:

        //if (fm.findFragmentById(android.R.id.content) == null) {
        //    fm.beginTransaction().add(android.R.id.content, tracksFragment).commit();
       // }

  /*      // -------------------- PREPARAR MEDIAROUTER --------------------

        mMediaRouter = MediaRouter.getInstance(getApplicationContext());

        mMediaRouteSelector = new MediaRouteSelector.Builder()
                .addControlCategory(CastMediaControlIntent.categoryForCast(CAST_APP_ID))
                .build();*/



    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mCastMgr != null)
            mCastMgr.decrementUiCounter();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCastMgr = Application.getCastManager(this);
        mCastMgr.incrementUiCounter();
    }

    @Override
	public boolean onCreateOptionsMenu(Menu menu) {

        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.result, menu);

        // Usar o CastManager para adicionar o action provider ao botão do menu.

        //mediaRouteMenuItem = mCastMgr.addMediaRouterButton(menu, R.id.media_route_menu_item);

        mCastMgr.addMediaRouterButton(menu, R.id.media_route_menu_item);
/*
        // Construir o seletor que será usado no menu.
        mMediaRouteSelector = new MediaRouteSelector.Builder()
                .addControlCategory(CastMediaControlIntent.categoryForCast(CAST_APP_ID))
                .build();

        // Designar um ActionProvider ao botão do menu:
        MenuItem mediaRouteMenuItem = menu.findItem(R.id.media_route_menu_item);

        MediaRouteActionProvider mediaRouteActionProvider =
                (MediaRouteActionProvider) MenuItemCompat.getActionProvider(mediaRouteMenuItem);
        mediaRouteActionProvider.setRouteSelector(mMediaRouteSelector);
*/

        return true;
	}

    @Override
    public void onDestroy(){
        super.onDestroy();
        mCastMgr.removeMiniController(mMiniController);
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
                // Buscar IDs e metadados do Spotify.
                ArrayList<TrackItem> spotifyItems = SpotifyManager.getPlaylistTracks(playlistItem);

                // Associar aos IDs do YouTube.

                ArrayList<TrackItem> result = YouTubeManager.associateYouTubeData(spotifyItems);

                return result;

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
                SpotifyManager.startLoginActivity(getApplicationContext());
            } else {
                for (TrackItem item : resultItems) {
                    mAdapter.add(item);
                }
            }

		}

	}
	
}
