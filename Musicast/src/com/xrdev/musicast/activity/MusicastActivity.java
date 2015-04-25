package com.xrdev.musicast.activity;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.media.MediaRouteSelector;
import android.support.v7.media.MediaRouter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.sample.castcompanionlibrary.cast.BaseCastManager;
import com.google.sample.castcompanionlibrary.cast.VideoCastManager;
import com.google.sample.castcompanionlibrary.cast.exceptions.NoConnectionException;
import com.google.sample.castcompanionlibrary.cast.exceptions.TransientNetworkDisconnectionException;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.spotify.sdk.android.authentication.LoginActivity;
import com.xrdev.musicast.Application;
import com.xrdev.musicast.R;
import com.xrdev.musicast.adapter.PlaylistAdapter;
import com.xrdev.musicast.adapter.QueueAdapter;
import com.xrdev.musicast.adapter.TrackAdapter;
import com.xrdev.musicast.connection.SpotifyManager;
import com.xrdev.musicast.connection.YouTubeManager;
import com.xrdev.musicast.model.JsonModel;
import com.xrdev.musicast.model.PlaylistItem;
import com.xrdev.musicast.model.LocalQueue;
import com.xrdev.musicast.model.TrackItem;
import com.xrdev.musicast.utils.JsonConverter;
import com.xrdev.musicast.utils.PrefsManager;

import java.util.ArrayList;

public class MusicastActivity extends ActionBarActivity
    implements PlaylistsFragment.OnPlaylistSelectedListener, Application.OnMessageReceived, ModeFragment.OnModeSelectedListener,
                TrackAdapter.OnAddedTrackListener, QueueAdapter.OnVotedTrackListener {


    private final static String TAG = "MusicastActivity";
    public final static String EXTRA_CODE = "code";
    public final static String EXTRA_IS_LOGIN_SKIPPED = "isLoginSkipped";
    public final static String EXTRA_WAS_LOGIN_PROMPTED = "wasLoginPrompted";
    private final static String CAST_APP_ID = "E3FA9FF0";

    private Menu mMenu;

    private PlaylistItem mPlaylistSelected;
    private LocalQueue mLocalQueue;

    private TrackAdapter mTrackAdapter;
    private PlaylistAdapter mPlaylistAdapter;
    private QueueAdapter mQueueAdapter;

    private RelativeLayout mTopSlidingContainer;
    private SlidingUpPanelLayout mSlidingUpLayout;
    private FragmentManager mFragmentManager;
    private PlaylistsFragment mPlaylistsFragment;
    private TracksFragment mTracksFragment;
    private PlayingTrackFragment mPlayingTrackFragment;
    private ModeFragment mModeFragment;
    private CastSelectorFragment mCastSelectorFragment;
    private GuestFragment mGuestFragment;
    private ListView mPlayQueueListView;
    private ListView mTracksView;

    private RelativeLayout mMediaControlsContainer;
    private ImageButton mPlayPauseButton;
    private ImageButton mPrevButton;
    private ImageButton mNextButton;
    private ImageView mCollapseExpandImg;
    private TextView mTrackName;
    private TextView mArtistsAlbumName;

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
    private int status;
    private static final int UNSTARTED = -1;
    private static final int IDLE = 0;
    private static final int PLAYING = 1;
    private static final int PAUSED = 2;
    private static final int BUFFERING = 3;

    private static final int LAYOUT_INIT = -1;
    private int mLayoutMode = LAYOUT_INIT;

    boolean isChromecastConnected;


    JsonConverter jsonConverter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        jsonConverter = Application.getConverter(getApplicationContext());

        Log.d(TAG, "MusicastActivity.onCreate()");

        setContentView(R.layout.activity_musicast);
        mSlidingUpLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        mSlidingUpLayout.hidePanel();

        isChromecastConnected = false;

        /**
         * ONCREATE DA ACTIVITY: Instanciar apenas objetos que serão comuns aos Fragments.
         */

        if (savedInstanceState != null) {
            Log.d(TAG, "MusicastActivity: retornando de uma instância salva, pulando o onCreate().");
            return;
        }

        Application.setListener(this);
        Log.d(TAG,"Listener configurado à classe global.");

        setTitle(R.string.app_name);

        initializeCastUi();

        // Estabelecer sessão com o chromecast:
        mCastMgr.reconnectSessionIfPossible(getApplicationContext(), false, 10); // context, showDialog, timeout.

        initializeFragments();

        this.status = UNSTARTED;

        initializeSlidingPanel();

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
/*        mCastMgr = Application.getCastManager(this);
        mCastMgr.incrementUiCounter();
        if (mCastMgr.isConnected()) {
            sendMessage(jsonConverter.makeGenericTypeJson(JsonConverter.TYPE_GET_STATUS));
        }*/

        mCastMgr = Application.getCastManager(this);

        try {
            if (mCastMgr.isConnected()) {
                sendMessage(jsonConverter.makeGenericTypeJson(JsonConverter.TYPE_GET_STATUS));
            }
        } catch (Exception e) {
            Log.d(TAG, "Erro ao enviar mensagem no onResume. SEM DATANAMESPACE?" );
            Toast.makeText(getApplicationContext(), "Erro ao enviar mensagem. Namespace nulo?", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {

        if (mFirstTracksTask != null)
            mFirstTracksTask.cancel(true);

        if (mTracksBackgroundTask != null)
            mTracksBackgroundTask.cancel(true);

        if (mSlidingUpLayout.isPanelExpanded()) {
            mSlidingUpLayout.collapsePanel();
        } else {
            if (mFragmentManager.getBackStackEntryCount() > 0) {
                mFragmentManager.popBackStack();
                mMenu.findItem(R.id.action_add_to_queue).setVisible(false);
                mMenu.findItem(R.id.action_swap).setVisible(false);
                actionBar.setSubtitle(R.string.playlists);
            } else {
                super.onBackPressed();
            }
        }

    }

    @Override
	public boolean onCreateOptionsMenu(Menu menu) {

        super.onCreateOptionsMenu(menu);

        mMenu = menu;

        getMenuInflater().inflate(R.menu.menu, menu);

        if (mCastMgr != null)
            mCastMgr.addMediaRouterButton(menu, R.id.media_route_menu_item);
        else {
            mCastMgr = Application.getCastManager(this);
            mCastMgr.addMediaRouterButton(menu, R.id.media_route_menu_item);
        }
        // Esconder os botões da ActionBar exclusivos da TracksFragment.

        mMenu.findItem(R.id.action_add_to_queue).setVisible(false);
        mMenu.findItem(R.id.action_swap).setVisible(false);

        if (PrefsManager.getAccessToken(this) != null){
            mMenu.findItem(R.id.action_logout).setVisible(true);
            mMenu.findItem(R.id.action_login).setVisible(false);
        } else {
            mMenu.findItem(R.id.action_logout).setVisible(false);
            mMenu.findItem(R.id.action_login).setVisible(true);
        }

        return true;
	}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_add_to_queue :
                addToQueue();
                return true;
            case R.id.action_swap :
                swapPlaylist();
                return true;
            case R.id.action_logout :
                PrefsManager.clearPrefs(this);
                SpotifyManager.logoutFromWebView(this);
                Toast.makeText(getApplicationContext(),getString(R.string.string_logout_successful),Toast.LENGTH_SHORT).show();
            case R.id.action_login :
                startActivity(
                        new Intent(MusicastActivity.this, SpotifyAuthActivity.class)
                );
            default:
                return super.onOptionsItemSelected(item);
        }
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

    private void initializeCastUi(){
        actionBar = getSupportActionBar();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            // Show the Up button in the action bar.
            actionBar.setDisplayShowHomeEnabled(false);
        }

        // -------------------- VERIFICAR GOOGLE PLAY SERVICES, PREPARAR CAST --------------------
        BaseCastManager.checkGooglePlayServices(this);
        mCastMgr = Application.getCastManager(this);
    }

    private void initializeFragments(){
        // -------------------- PREPARAR LISTFRAGMENTS E OBJETOS BÁSICOS --------------------
        // Inicializar adapters:
        mPlaylistAdapter = new PlaylistAdapter(this);
        mTrackAdapter = new TrackAdapter(this);
        mQueueAdapter = new QueueAdapter(this);

        mPlayQueueListView = (ListView) findViewById(R.id.play_queue_list);

        mFragmentManager = getFragmentManager();

        // No onCreate(), iniciar o fragment de Playlist que seria o inicial
        if (mPlaylistsFragment == null) {
            mPlaylistsFragment = PlaylistsFragment.newInstance();
        }

        if (mPlayingTrackFragment == null) {
            mPlayingTrackFragment = PlayingTrackFragment.newInstance();
        }

        if (mModeFragment == null) {
            mModeFragment = ModeFragment.newInstance();
        }

        if (mCastSelectorFragment == null) {
            mCastSelectorFragment = CastSelectorFragment.newInstance();
        }

        if (mGuestFragment == null) {
            mGuestFragment = GuestFragment.newInstance();
        }

        mPlaylistsFragment.setListAdapter(mPlaylistAdapter);
        mPlayQueueListView.setAdapter(mQueueAdapter);

        // Attach no fragment:

        mFragmentManager.beginTransaction()
                .add(R.id.playing_track_container, mPlayingTrackFragment)
                .commit();

        mFragmentManager.beginTransaction()
                .add(R.id.main_container, mCastSelectorFragment)
                .commit();

        displayBackStack(mFragmentManager);

    }

    private void initializeSlidingPanel(){
        // -------------------- INSTANCIAR VIEWS DO SLIDING PANEL --------------------

        mMediaControlsContainer = (RelativeLayout) findViewById(R.id.media_controls_layout);


        mPlayPauseButton = (ImageButton) findViewById(R.id.imagebutton_play_pause);
        mPrevButton = (ImageButton) findViewById(R.id.imagebutton_previous);
        mNextButton = (ImageButton) findViewById(R.id.imagebutton_next);

        mTrackName = mPlayingTrackFragment.getTrackNameTextView();
        mArtistsAlbumName = mPlayingTrackFragment.getArtistsAlbumNameTextView();

        mCollapseExpandImg = (ImageView) findViewById(R.id.collapse_expand_img);

        mTopSlidingContainer = (RelativeLayout) findViewById(R.id.top_container);

        mPlayPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (status == PLAYING) {
                    sendMessage(jsonConverter.makeGenericTypeJson(JsonConverter.TYPE_PAUSE_VIDEO));
                } else {
                    sendMessage(jsonConverter.makeGenericTypeJson(JsonConverter.TYPE_PLAY_VIDEO));
                }
            }
        });

        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage(jsonConverter.makeGenericTypeJson(JsonConverter.TYPE_PLAY_PREVIOUS));
            }
        });

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage(jsonConverter.makeGenericTypeJson(JsonConverter.TYPE_PLAY_NEXT));
            }
        });

        mSlidingUpLayout.setDragView(mTopSlidingContainer);

        /**
         * Listener para o SlidingPanel, checando quando o painel é expandido ou recolhido.
         */
        mSlidingUpLayout.setPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
            }

            @Override
            public void onPanelExpanded(View panel) {
                Log.i(TAG, "onPanelExpanded");
                mCollapseExpandImg.setImageResource(R.drawable.ic_action_collapse);
            }

            @Override
            public void onPanelCollapsed(View panel) {
                Log.i(TAG, "onPanelCollapsed");
                mCollapseExpandImg.setImageResource(R.drawable.ic_action_expand);

            }

            @Override
            public void onPanelAnchored(View panel) {

            }

            @Override
            public void onPanelHidden(View panel) {

            }
        });

    }

    public void onPlaylistSelected(PlaylistItem clickedPlaylist) {
        mPlaylistSelected = clickedPlaylist;

        mLocalQueue = Application.getQueue(clickedPlaylist.getPlaylistId());

        if (mTracksFragment == null) {
            mTracksFragment = TracksFragment.newInstance();
        }

        mTracksFragment.setQueue(mLocalQueue);
        mTracksFragment.setListAdapter(mTrackAdapter);

        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.replace(R.id.main_container, mTracksFragment);
        transaction.addToBackStack(null);
        transaction.commit();

        mFragmentManager.executePendingTransactions();

        displayBackStack(mFragmentManager);

        actionBar.setSubtitle(mPlaylistSelected.getName());

        if (isAdmin())
            mMenu.findItem(R.id.action_swap).setVisible(true);

        mMenu.findItem(R.id.action_add_to_queue).setVisible(true);

        mFirstTracksTask = new TrackDownloader().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");

    }

    public void onMessageReceived(String message) {
        Gson gson = new Gson();
        JsonModel obj = gson.fromJson(message, JsonModel.class);
        String type = obj.getType();
        if (type.equals("session")) {
            String connection = obj.getMessage();
            if (connection.equals("connected")) {
                // Chromecast acabou de avisar que está conectado. Solicitar o status de reprodução e metadados.
                sendMessage(jsonConverter.makeGenericTypeJson(JsonConverter.TYPE_GET_STATUS));
            }
        }
        if (type.equals("stateChange") || type.equals("statusCheck")) {
            // Chromecast enviou alteração de estado do player, atualizar o app.
            int status = Integer.parseInt(obj.getMessage());
            updateStatus(status);
        }
        if (type.equals("modeInfo")) {
            // Modo atual (SOLO ou PARTY)
            int mode = Integer.parseInt(obj.getMessage());
            String adminUuid = obj.getUUID();
            Application.setAdmin(adminUuid);
            // IMPORTANTE SETAR O ADMIN ANTES DO MODO!
            Application.setMode(mode);
        }
        if (type.equals("trackInfo")) {
            // Pega o JSON encapsulado no campo "message" e o transforma num objeto TrackItem.
            TrackItem trackInfo = obj.getTrackInfo();
            updateTrackInfo(trackInfo);
        }
        if (type.equals("queueInfo")) {
            // Pega o JSON encapsulado no campo "message" e o transforma num objeto TrackItem.
            ArrayList<TrackItem> remoteTracks = obj.getTracksMetadata();
            updatePlayQueue(remoteTracks);
        }
        if (type.equals("trackIndex")) {
            // Pega o JSON encapsulado no campo "message" e o transforma num objeto TrackItem.
            String index = obj.getMessage();
            highlightPlayingNow(index);
        }

        if (type.equals("feedback")) {
            String feedbackType = obj.getMessage();

            if (feedbackType.equals("FEEDBACK_TRACK_ADD"))
                Toast.makeText(this, R.string.feedback_track_add, Toast.LENGTH_SHORT).show();

            if (feedbackType.equals("FEEDBACK_ERR_NO_ADMIN_RIGHTS"))
                Toast.makeText(this, R.string.feedback_error_not_admin, Toast.LENGTH_SHORT).show();

        }
    }


    public void onDisconnected() {
        updateTrackInfo(null);
    }

    public void onConnected() {
        mCastMgr = Application.getCastManager(this);
        mCastMgr.incrementUiCounter();
        if (mCastMgr.isConnected()) {
            isChromecastConnected = true;
            sendMessage(jsonConverter.makeGenericTypeJson(JsonConverter.TYPE_GET_STATUS));
        }
    }

    public void onModeChanged(){
        int newMode = Application.getMode();
        updateLayout(mLayoutMode, newMode);
    }

    private void updateStatus(int status) {
        this.status = status;
        switch (status) {
            case UNSTARTED:
                break;
            case IDLE:
                mPlayPauseButton.setImageResource(R.drawable.ic_action_play);
                break;
            case PLAYING:
                mPlayPauseButton.setImageResource(R.drawable.ic_action_pause);
                break;
            case PAUSED:
                mPlayPauseButton.setImageResource(R.drawable.ic_action_play);
                break;
            case BUFFERING:
                break;
        }
    }

    public void onAdminChanged(String admin) {
        // TODO:  lógica para atualizar interface em alteração de admin.
        if (!isAdmin())
            mMediaControlsContainer.setVisibility(View.GONE);
        else
            mMediaControlsContainer.setVisibility(View.VISIBLE);
    }


    private void updateTrackInfo(TrackItem track) {
        if (null != track) {
            String artists = track.getArtists();
            String name = track.getName();
            String album = track.getAlbum();
            mSlidingUpLayout.showPanel();
            mPlayingTrackFragment.setTrackName(name);
            mPlayingTrackFragment.setArtistsAlbumName(artists + " - " + album);
        } else {
            mSlidingUpLayout.hidePanel();
        }
    }

    private void updatePlayQueue(ArrayList<TrackItem> tracks) {
        mQueueAdapter.clear();
        for (TrackItem track : tracks) {
            mQueueAdapter.add(track);
        }
    }

    private void highlightPlayingNow(String index) {
        int i = Integer.parseInt(index);
        mQueueAdapter.setHighlight(i);
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

    protected void sendMessage(String message) {
        try {
            mCastMgr.sendDataMessage(message);
            Log.d(TAG, "Message sent: " + message);
        } catch (TransientNetworkDisconnectionException e) {
            e.printStackTrace();
        } catch (NoConnectionException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {

        }
    }

    private void updateLayout(int fromLayout, int toLayout) {

        if (fromLayout != toLayout) {

            switch (toLayout) {
                case Application.MODE_UNSTARTED :
                    mFragmentManager.beginTransaction()
                            .replace(R.id.main_container, mModeFragment)
                            .commit();
                    break;
                case Application.MODE_SOLO :
                    actionBar.setTitle(R.string.solo_mode);
                    actionBar.setSubtitle(R.string.playlists);
                    loadPlaylistsFragment();
                    break;
                case Application.MODE_PARTY :
                    if (isAdmin())
                        actionBar.setTitle(R.string.party_mode_host);
                    else
                        actionBar.setTitle(R.string.party_mode_guest);

                    actionBar.setSubtitle(R.string.playlists);

                    Intent fromIntent = getIntent();

                    boolean wasLoginPrompted = fromIntent.getBooleanExtra(EXTRA_WAS_LOGIN_PROMPTED, false);

                    if (wasLoginPrompted){
                        loadPlaylistsFragment();
                    } else {
                        mFragmentManager.beginTransaction()
                                .replace(R.id.main_container, mGuestFragment)
                                .commit();
                    }
                    break;
            }

            mLayoutMode = toLayout;
        }
    }

    protected void loadPlaylistsFragment() {
        mPlaylistsTask = new PlaylistDownloader().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");
        mFragmentManager.beginTransaction()
                .replace(R.id.main_container, mPlaylistsFragment)
                .commit();
        mPlaylistAdapter.notifyDataSetChanged();
    }
    @Override
    public void onModeSelected(int mode) {
        sendMessage(jsonConverter.makeModeJson(mode));
    }

    /**
	 * Inner classes para execução em segundo plano (AsyncTask):
	 */

    @Override
    public void onTrackAdded(TrackItem track) {
        String msg = jsonConverter.makeTrackVoteJson(track);
        sendMessage(msg);
    }


    @Override
    public void onTrackVoted(TrackItem track) {
        String msg = jsonConverter.makeTrackVoteJson(track);
        sendMessage(msg);
    }

    private void addToQueue() {

        if (mLocalQueue != null) {

            new AlertDialog.Builder(this)
                    .setTitle(R.string.add_tracks_to_queue_dialog_title)
                    .setMessage(R.string.add_tracks_to_queue_dialog_message)
                    .setNegativeButton(R.string.cancel, null)
                    .setPositiveButton(R.string.add_tracks_to_queue_dialog_yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String msg = jsonConverter.makeAddToQueueJson(mLocalQueue);
                            sendMessage(msg);
                        }
                    })
                    .show();
        } else {
            Toast.makeText(this, getString(R.string.error_no_tracks_on_playlist), Toast.LENGTH_LONG).show();
        }
    }

    private void swapPlaylist() {
        if (mLocalQueue != null) {
            if (isAdmin()) {
                new AlertDialog.Builder(this)
                        .setTitle(R.string.swap_playlist_dialog_title)
                        .setMessage(R.string.swap_playlist_dialog_message)
                        .setNegativeButton(R.string.cancel, null)
                        .setPositiveButton(R.string.swap_playlist_dialog_yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String msg = jsonConverter.makeSwapPlaylistJson(mLocalQueue);
                                sendMessage(msg);
                            }
                        })
                        .show();
            } else {
                Toast.makeText(this, getString(R.string.error_not_admin_swap), Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, getString(R.string.error_no_tracks_on_playlist), Toast.LENGTH_LONG).show();
        }
    }

    private boolean isAdmin() {
        if (Application.getAdmin() == null)
            return false;

        if (Application.getAdmin().equals(PrefsManager.getUUID(this)))
            return true;
        else
            return false;
    }

    public class PlaylistDownloader extends AsyncTask<String, Void, ArrayList<PlaylistItem>>{
        ProgressDialog pd;
        Intent fromIntent;

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

            fromIntent = getIntent();
        }

        @Override
        protected ArrayList<PlaylistItem> doInBackground(String... arg0) {
            Log.i(TAG, "AsyncTask: Entrando no doInBackground.");

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
                boolean isLoginSkipped = fromIntent.getBooleanExtra(EXTRA_IS_LOGIN_SKIPPED, false);

                if (!isLoginSkipped || isAdmin())
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
                ArrayList<TrackItem> spotifyItems = SpotifyManager.getPlaylistTracks(mPlaylistSelected, REQUEST_LIMIT, mRequestOffset, getApplicationContext()
                );

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

            mTracksBackgroundTask = new BackgroundDownloader(mTrackAdapter).execute();

		}

	}

    public class BackgroundDownloader extends AsyncTask<String, Void, Void>{

        private TrackAdapter adapter;

        public BackgroundDownloader(TrackAdapter adapter) {
            super();
            this.adapter = adapter;
        }


        /*@Override
        protected void onPreExecute() {

        }*/

        @Override
        protected Void doInBackground(String... arg0) {
            Log.i(TAG, "AsyncTask em Background: Entrando no doInBackground.");


            int mFoundCount = 0;

            if (mPlaylistSelected != null){
                int totalTracks = mPlaylistSelected.getNumTracksInt();
                while (mRequestOffset < totalTracks) {

                    if (isCancelled())
                        break;

                    final ArrayList<TrackItem> spotifyItems = SpotifyManager.getPlaylistTracks(mPlaylistSelected, REQUEST_LIMIT, mRequestOffset, getApplicationContext());

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
                YouTubeManager.associateYouTubeData(currentItem, mLocalQueue);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }

            for (int i = 0; i < adapter.getCount(); i++) {
                if(isCancelled())
                    break;

                TrackItem currentItem = adapter.getItem(i);

                if (currentItem.wasFound())
                    mFoundCount++;
            }


            Log.d(TAG, "Videos found: " + mFoundCount);


            return null;
        }

/*        @Override
        protected void onPostExecute() {

        }*/

    }
	
}
