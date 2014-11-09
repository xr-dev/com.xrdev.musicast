package com.xrdev.musicast.activity;

import android.app.ListFragment;
import android.content.Context;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.cast.MediaInfo;
import com.google.android.gms.cast.MediaMetadata;
import com.google.sample.castcompanionlibrary.cast.VideoCastManager;
import com.xrdev.musicast.Application;
import com.xrdev.musicast.model.TrackItem;
import com.xrdev.musicast.utils.YouTubeMp4Extractor;

import java.io.IOException;

/**
 * Created by Guilherme on 08/10/2014.
 */
public class TracksFragment extends ListFragment {

    VideoCastManager mCastMgr;
    Context mContext;

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
        String mp4Path;

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

        MediaMetadata mediaMetadata = new MediaMetadata(MediaMetadata.MEDIA_TYPE_MOVIE);
        mediaMetadata.putString(MediaMetadata.KEY_TITLE, selectedTrack.getArtists() + " - " + selectedTrack.getName());

        // Extrair o link MP4 usando o YouTubeID:
            if (selectedTrack.getYoutubeMp4() != null) {
                MediaInfo mediaInfo = new MediaInfo.Builder(
                        "http://playground.html5rocks.com/samples/html5_misc/chrome_japan.mp4")
                        .setContentType("video/mp4")
                        .setStreamType(MediaInfo.STREAM_TYPE_BUFFERED)
                        .setMetadata(mediaMetadata)
                        .build();

                mContext = getActivity().getApplicationContext();
                mCastMgr = Application.getCastManager(mContext);

                mCastMgr.startCastControllerActivity(mContext, mediaInfo, 0, true);

            } else {
                Toast.makeText(getActivity(),"YouTube video not found for this track.", Toast.LENGTH_LONG).show();
            }




    }

}
