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

import java.util.ArrayList;

public class TracksActivity extends ActionBarActivity {


    private final static String TAG = "TracksActivity";
    private final static String EXTRA_CODE = "code";
    private final static String CAST_APP_ID = "E3FA9FF0";

    private TracksFragment tracksFragment;
    private TrackAdapter mAdapter;
    private PlaylistItem playlistItem;
    private int mTotalTracks;

    private ActionBar actionBar;
    private MenuItem mediaRouteMenuItem;

    private VideoCastManager mCastMgr;
    private MiniController mMiniController;
    private MediaRouter mMediaRouter;
    private MediaRouteSelector mMediaRouteSelector;

    private AsyncTask mFirstRunTask;
    private AsyncTask mBackgroundTask;

    private int mRequestOffset;
    private static int REQUEST_LIMIT = 30;


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

        mFirstRunTask = new TrackDownloader().execute();

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

    @Override
    public void onStop(){
        super.onStop();

        if (mFirstRunTask != null)
            mFirstRunTask.cancel(true);

        if (mBackgroundTask != null)
            mBackgroundTask.cancel(true);
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
			Log.i(TAG, "AsyncTask Spotify: Entrando no doInBackground.");

            if (playlistItem != null){
                mRequestOffset = 0;
                mTotalTracks = playlistItem.getNumTracksInt();

                Log.d(TAG, "Buscando músicas da Playlist ID / Fetching tracks from Playlist ID: " + playlistItem.getPlaylistId());
                Log.d(TAG, "Owner ID: " + playlistItem.getOwnerId());
                // Buscar IDs e metadados do Spotify.
                ArrayList<TrackItem> spotifyItems = SpotifyManager.getPlaylistTracks(playlistItem, REQUEST_LIMIT, mRequestOffset);

                mRequestOffset += REQUEST_LIMIT;

                return spotifyItems;

            } else {
                return null;
            }
		}
		
		@Override
		protected void onPostExecute(ArrayList<TrackItem> spotifyItems) {
			super.onPostExecute(spotifyItems);
			if (pd.isShowing()) {
				pd.dismiss();
			}

            if (spotifyItems == null) {
                // TODO: Alterar esse método. Uma lista de reprodução vazia tem null como resultado esperado.
                SpotifyManager.startLoginActivity(getApplicationContext());
            } else {
                for (TrackItem item : spotifyItems) {
                    mAdapter.add(item);
                }
            }

            mBackgroundTask = new backgroundDownloader(mAdapter).execute();

		}

	}

    public class backgroundDownloader extends AsyncTask<String, Void, Void>{

        private TrackAdapter adapter;

        public backgroundDownloader(TrackAdapter adapter) {
            super();
            this.adapter = adapter;
        }


        /*@Override
        protected void onPreExecute() {

        }*/

        @Override
        protected Void doInBackground(String... arg0) {
            Log.i(TAG, "AsyncTask em Background: Entrando no doInBackground.");



            if (playlistItem != null){
                while (mRequestOffset < mTotalTracks) {

                    if (isCancelled())
                        break;

                    final ArrayList<TrackItem> spotifyItems = SpotifyManager.getPlaylistTracks(playlistItem, REQUEST_LIMIT, mRequestOffset);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            for (TrackItem item : spotifyItems) {
                                adapter.add(item);
                            }
                        }
                    });

                    mRequestOffset += REQUEST_LIMIT;

                }

            } else {
                return null;
            }

            for (int i = 0; i < adapter.getCount(); i++) {
                if(isCancelled())
                    break;

                TrackItem currentItem = adapter.getItem(i);
                YouTubeManager.associateYouTubeData(currentItem);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }

            return null;
        }

/*        @Override
        protected void onPostExecute() {

        }*/

    }
	
}
