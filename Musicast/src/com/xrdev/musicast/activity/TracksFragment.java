package com.xrdev.musicast.activity;

import android.app.ListFragment;
import android.content.Context;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.cast.MediaMetadata;
import com.google.sample.castcompanionlibrary.cast.VideoCastManager;
import com.google.sample.castcompanionlibrary.cast.callbacks.VideoCastConsumerImpl;
import com.google.sample.castcompanionlibrary.cast.exceptions.NoConnectionException;
import com.google.sample.castcompanionlibrary.cast.exceptions.TransientNetworkDisconnectionException;
import com.xrdev.musicast.Application;
import com.xrdev.musicast.model.TrackItem;
import com.xrdev.musicast.utils.JsonConverter;

/**
 * Created by Guilherme on 08/10/2014.
 */
public class TracksFragment extends ListFragment {

    VideoCastManager mCastMgr;
    VideoCastConsumerImpl mConsumerImpl;
    Context mContext;
    JsonConverter jsonConverter = new JsonConverter();

    public static TracksFragment newInstance() {
        TracksFragment tf = new TracksFragment();
        return tf;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // Lógica do clique numa track.
        super.onListItemClick(l, v, position, id);

        TrackItem selectedTrack = (TrackItem) getListAdapter().getItem(position);

        String spotifyId;
        String youtubeId;
        String duration;

        if (selectedTrack.getTrackId() == null)
            spotifyId = "Not found.";
        else
            spotifyId = selectedTrack.getTrackId();

        if (selectedTrack.getYoutubeId() == null)
            youtubeId = "Not found.";
        else
            youtubeId = selectedTrack.getYoutubeId();

        if (selectedTrack.getDuration() == 0)
            duration = "Not found.";
        else
            duration = String.valueOf(selectedTrack.getDuration());


        Toast.makeText(getActivity(),"Selected track: "
                + selectedTrack.getArtists() + " - " + selectedTrack.getName() + "\n"
                + "Duration: " + duration + " seconds. \n"
                + "Spotify ID: " + spotifyId + "\n"
                + "YouTube ID: " + youtubeId,
                Toast.LENGTH_LONG).
                show();

            if (youtubeId != null) {

                mContext = getActivity().getApplicationContext();
                mCastMgr = Application.getCastManager(mContext);

                try {
                    String customMessage = jsonConverter.makeJson(JsonConverter.TYPE_LOAD_VIDEO, selectedTrack);

                    mCastMgr.sendDataMessage(customMessage);
                } catch (TransientNetworkDisconnectionException e) {
                    e.printStackTrace();
                } catch (NoConnectionException e) {
                    e.printStackTrace();
                }

            } else {
                Toast.makeText(getActivity(),"YouTube video not found for this track.", Toast.LENGTH_LONG).show();
            }




    }

}
