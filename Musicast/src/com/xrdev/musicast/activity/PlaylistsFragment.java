package com.xrdev.musicast.activity;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.xrdev.musicast.R;
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
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // Lógica do clique numa track.
        super.onListItemClick(l, v, position, id);

        PlaylistItem clickedItem = (PlaylistItem) mAdapter.getItem(position);

        mCallback.onPlaylistSelected(clickedItem);

    }

    private TextView noItems(String text) {
        TextView emptyView = new TextView(getActivity());
        //Make sure you import android.widget.LinearLayout.LayoutParams;
        emptyView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        //Instead of passing resource id here I passed resolved color
        //That is, getResources().getColor((R.color.gray_dark))
        emptyView.setTextColor(getResources().getColor(R.color.dark_grey));
        emptyView.setText(text);
        emptyView.setTextSize(12);
        emptyView.setVisibility(View.GONE);
        emptyView.setGravity(Gravity.CENTER_VERTICAL
                | Gravity.CENTER_HORIZONTAL);

        //Add the view to the list view. This might be what you are missing
        ((ViewGroup) getListView().getParent()).addView(emptyView);

        return emptyView;
    }

    @Override
    public void onStart() {
        super.onStart();
        getListView().setEmptyView(
                noItems(getResources().getString(R.string.string_no_playlists_or_logged_out)));
    }


}
