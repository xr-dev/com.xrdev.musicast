package com.xrdev.musicast.activity;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xrdev.musicast.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PlayingTrackFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PlayingTrackFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class PlayingTrackFragment extends Fragment {

    private TextView mTrackNameTextView;
    private TextView mArtistsAlbumNameTextView;

    public static PlayingTrackFragment newInstance() {
        PlayingTrackFragment fragment = new PlayingTrackFragment();
        return fragment;
    }
    public PlayingTrackFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_playing_track, container, false);
        mTrackNameTextView = (TextView) v.findViewById(R.id.text_playing_track_name);
        mArtistsAlbumNameTextView = (TextView) v.findViewById(R.id.text_playing_track_artists_album);
        return v;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    public TextView getTrackNameTextView() {
        return mTrackNameTextView;
    }

    public TextView getArtistsAlbumNameTextView() {
        return mArtistsAlbumNameTextView;
    }

    public void setTrackName(String name) {
        mTrackNameTextView.setText(name);
    }

    public void setArtistsAlbumName(String artistsAlbum) {
        mArtistsAlbumNameTextView.setText(artistsAlbum);
    }
}
