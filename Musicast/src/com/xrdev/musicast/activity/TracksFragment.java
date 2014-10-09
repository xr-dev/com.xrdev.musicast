package com.xrdev.musicast.activity;

import android.app.ListFragment;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.xrdev.musicast.model.TrackItem;

/**
 * Created by Guilherme on 08/10/2014.
 */
public class TracksFragment extends ListFragment {

    public static TracksFragment newInstance() {
        TracksFragment tf = new TracksFragment();
        return tf;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // Lógica do clique numa track.
        super.onListItemClick(l, v, position, id);

        TrackItem selectedTrack = (TrackItem) getListAdapter().getItem(position);

        String toastSpotifyId;
        String toastYoutubeId;
        String toastDuration;

        if (selectedTrack.getTrackId() == null)
            toastSpotifyId = "Not found.";
        else
            toastSpotifyId = selectedTrack.getTrackId();

        if (selectedTrack.getYoutubeId() == null)
            toastYoutubeId = "Not found.";
        else
            toastYoutubeId = selectedTrack.getYoutubeId();

        if (selectedTrack.getDuration() == 0)
            toastDuration = "Not found.";
        else
            toastDuration = String.valueOf(selectedTrack.getDuration());


        Toast.makeText(getActivity(),"Selected track: "
                + selectedTrack.getArtists() + " - " + selectedTrack.getName() + "\n"
                + "Duration: " + toastDuration + " seconds. \n"
                + "Spotify ID: " + toastSpotifyId + "\n"
                + "YouTube ID: " + toastYoutubeId,
                Toast.LENGTH_LONG).
                show();

    }

}
