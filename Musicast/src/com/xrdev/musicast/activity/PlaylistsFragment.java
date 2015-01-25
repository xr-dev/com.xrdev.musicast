package com.xrdev.musicast.activity;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.xrdev.musicast.model.PlaylistItem;

/**
 * Created by Guilherme on 08/10/2014.
 */
public class PlaylistsFragment extends ListFragment {

    Context mContext;
    ListAdapter mAdapter;
    OnPlaylistSelectedListener mCallback;

    public static PlaylistsFragment newInstance() {
        return new PlaylistsFragment();
    }

    public interface OnPlaylistSelectedListener {
        public void onPlaylistSelected(PlaylistItem clickedPlaylist);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallback = (OnPlaylistSelectedListener) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mContext = getActivity().getApplicationContext();
        mAdapter = getListAdapter();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // Lógica do clique numa track.
        super.onListItemClick(l, v, position, id);

        PlaylistItem clickedItem = (PlaylistItem) mAdapter.getItem(position);

        mCallback.onPlaylistSelected(clickedItem);

    }

}
