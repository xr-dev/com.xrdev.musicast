package com.xrdev.musicast.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
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
import com.xrdev.musicast.adapter.PlaylistAdapter;
import com.xrdev.musicast.adapter.TrackAdapter;
import com.xrdev.musicast.connection.SpotifyManager;
import com.xrdev.musicast.connection.YouTubeManager;
import com.xrdev.musicast.model.PlaylistItem;
import com.xrdev.musicast.model.QueueList;
import com.xrdev.musicast.model.TrackItem;

import java.util.ArrayList;

public class MusicastActivity extends ActionBarActivity
    implements PlaylistsFragment.OnPlaylistSelectedListener{


    private final static String TAG = "MusicastActivity";
    private final static String EXTRA_CODE = "code";
    private final static String CAST_APP_ID = "E3FA9FF0";

    private PlaylistItem mPlaylistSelected;
    private QueueList mQueue;

    private TrackAdapter mTrackAdapter;
    private PlaylistAdapter mPlaylistAdapter;

    private FragmentManager mFragmentManager;
    private PlaylistsFragment mPlaylistsFragment;
    private TracksFragment mTracksFragment;

    private Intent mFromApiIntent;

    private ActionBar actionBar;
    private MenuItem mediaRouteMenuItem;

    private VideoCastManager mCastMgr;
//    private MiniController mMiniController;
    private MediaRouter mMediaRouter;
    private MediaRouteSelector mMediaRouteSelector;

    private AsyncTask mPlaylistsTask;
    private AsyncTask mFirstTracksTask;
    private AsyncTask mTracksBackgroundTask;

    private int mRequestOffset;
    private static int REQUEST_LIMIT = 30;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_musicast);
        /**
         * ONCREATE DA ACTIVITY: Instanciar apenas objetos que serão comuns aos dois Fragments.
         */

        Log.d(TAG, "MusicastActivity.onCreate()");

        if (savedInstanceState != null) {
            Log.d(TAG, "MusicastActivity: retornando de uma instância salva, pulando o onCreate().");
            return;
        }

        actionBar = getSupportActionBar();

        // -------------------- VERIFICAR GOOGLE PLAY SERVICES, PREPARAR CAST --------------------

        BaseCastManager.checkGooglePlayServices(this);

        mCastMgr = Application.getCastManager(this);

        //mMiniController = (MiniController) findViewById(R.id.miniController1);
        //mCastMgr.addMiniController(mMiniController);


        // -------------------- PREPARAR LISTFRAGMENTS E OBJETOS BÁSICOS --------------------

        setTitle(R.string.title_activity_spotify_playlists);

        // Verificar se a Activity foi iniciada por um intent do Spotify API:
        mFromApiIntent = getIntent();
        // Inicializar adapters:
        mPlaylistAdapter = new PlaylistAdapter(this);
        mTrackAdapter = new TrackAdapter(this);

        mFragmentManager = getFragmentManager();

        // No onCreate(), iniciar o fragment de Playlist que seria o inicial
        if (mPlaylistsFragment == null) {
            mPlaylistsFragment = PlaylistsFragment.newInstance();
        }

        // mPlaylistsFragment.setArguments(getIntent().getExtras()); - LINHA NECESSÁRIA?
        mPlaylistsFragment.setListAdapter(mPlaylistAdapter);

        // Attach no fragment:
        mFragmentManager.beginTransaction()
                .add(R.id.fragment_container, mPlaylistsFragment)
                .commit();

        displayBackStack(mFragmentManager);

        // Estabelecer sessão com o chromecast:
        mCastMgr.reconnectSessionIfPossible(getApplicationContext(), false, 10); // context, showDialog, timeout.

        // mFirstTracksTask = new TrackDownloader().execute();

        mPlaylistsTask = new PlaylistDownloader().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");

        // mFirstTracksTask = new TrackDownloader().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");


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
    public void onBackPressed() {
        if (mFragmentManager.getBackStackEntryCount() > 0) {
            mFragmentManager.popBackStack();
        } else {
            super.onBackPressed();
        }
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
        //mCastMgr.removeMiniController(mMiniController);
    }

    @Override
    public void onStop(){
        super.onStop();

        if (mFirstTracksTask != null)
            mFirstTracksTask.cancel(true);

        if (mTracksBackgroundTask != null)
            mTracksBackgroundTask.cancel(true);
    }


    public void onPlaylistSelected(PlaylistItem clickedPlaylist) {
        mPlaylistSelected = clickedPlaylist;

        mQueue = Application.getQueue(clickedPlaylist.getPlaylistId());

        if (mTracksFragment == null) {
            mTracksFragment = TracksFragment.newInstance();
        }

        mTracksFragment.setQueue(mQueue);
        mTracksFragment.setListAdapter(mTrackAdapter);

        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, mTracksFragment);
        transaction.addToBackStack(null);
        transaction.commit();

        mFragmentManager.executePendingTransactions();

        displayBackStack(mFragmentManager);

        setTitle(mPlaylistSelected.getName());

        mFirstTracksTask = new TrackDownloader().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");

    }


    private void displayBackStack(FragmentManager fm) {
        int count = fm.getBackStackEntryCount();
        Log.d(TAG, "Backstack: There are " + count + " entries");
        for(int i = 0; i<count; i++) {
            // Display Backstack-entry data like
            String name = fm.getBackStackEntryAt(i).getName();
            Log.d(TAG, "Backstack entry " + i + ": " + name);
        }
    }


    /**
	 * Inner classes para execução em segundo plano (AsyncTask):
	 */

    public class PlaylistDownloader extends AsyncTask<String, Void, ArrayList<PlaylistItem>>{
        ProgressDialog pd;
        // SpotifyServiceBinder mBinder = new SpotifyServiceBinder(getApplicationContext());

        public PlaylistDownloader() {
            super();
        }


        @Override
        protected void onPreExecute() {
            // Preparar o spinner
            pd = new ProgressDialog(MusicastActivity.this);
            pd.setMessage(getString(R.string.string_loading));
            pd.show();

        }

        @Override
        protected ArrayList<PlaylistItem> doInBackground(String... arg0) {
            Log.i(TAG, "AsyncTask: Entrando no doInBackground.");

            // mBinder.bindService();

            // ArrayList<PlaylistItem> resultItems = mBinder.getService().getUserPlaylists();

            Intent fromIntent = getIntent();

            String code = fromIntent.getStringExtra(EXTRA_CODE);

            // Significa que a PlaylistsActivity foi aberta pelo onNewIntent da AuthActivity, logo será necessário setar os tokens.
            if (code != null) {
                SpotifyManager.setAuthCredentials(getApplication());
            }

            ArrayList<PlaylistItem> resultItems = SpotifyManager.getUserPlaylists(getApplicationContext());

            return resultItems;

        }

        @Override
        protected void onPostExecute(ArrayList<PlaylistItem> resultItems) {
            super.onPostExecute(resultItems);
            Log.i(TAG, "ResultActivity/AsyncTast: Entrando no PostExecute.");
            if (pd.isShowing()) {
                pd.dismiss();
            }

            if (resultItems == null) {
                SpotifyManager.startLoginActivity(getApplicationContext());
            } else {
                for (PlaylistItem item : resultItems) {
                    mPlaylistAdapter.add(item);
                }
            }



        }



    }


    public class TrackDownloader extends AsyncTask<String, Void, ArrayList<TrackItem>>{
		ProgressDialog pd;
        // SpotifyServiceBinder mBinder = new SpotifyServiceBinder(getApplicationContext());

		public TrackDownloader() {
			super();
		}
		
		
		@Override
		protected void onPreExecute() {

            // verificar se já fora criado um TracksFragment antes, se sim, ver se a lista selecionada é
            // mesma e não recarregar os dados, senão limpar o Adapter e recarregar.
            mTrackAdapter.clear();


			// Preparar o spinner
            Log.i(TAG, "AsyncTask Spotify: Iniciando onPreExecute.");
            pd = new ProgressDialog(MusicastActivity.this);
			pd.setMessage(getString(R.string.string_loading));
			pd.show();
            Log.i(TAG, "AsyncTask Spotify: Finalizando onPreExecute.");
		}
		
		@Override
		protected ArrayList<TrackItem> doInBackground(String... arg0) {
			Log.i(TAG, "AsyncTask Spotify: Entrando no doInBackground.");

            if (isCancelled())
                return null;

            if (mPlaylistSelected != null){
                mRequestOffset = 0;
                int totalTracks;
                totalTracks = mPlaylistSelected.getNumTracksInt();

                Log.d(TAG, "Buscando músicas da Playlist ID / Fetching tracks from Playlist ID: " + mPlaylistSelected.getPlaylistId());
                Log.d(TAG, "Owner ID: " + mPlaylistSelected.getOwnerId());
                // Buscar IDs e metadados do Spotify.
                ArrayList<TrackItem> spotifyItems = SpotifyManager.getPlaylistTracks(mPlaylistSelected, REQUEST_LIMIT, mRequestOffset);

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
                    mTrackAdapter.add(item);
                }
            }

            mTracksBackgroundTask = new backgroundDownloader(mTrackAdapter).execute();

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



            if (mPlaylistSelected != null){
                int totalTracks = mPlaylistSelected.getNumTracksInt();
                while (mRequestOffset < totalTracks) {

                    if (isCancelled())
                        break;

                    final ArrayList<TrackItem> spotifyItems = SpotifyManager.getPlaylistTracks(mPlaylistSelected, REQUEST_LIMIT, mRequestOffset);

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
                YouTubeManager.associateYouTubeData(currentItem, mQueue);
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
