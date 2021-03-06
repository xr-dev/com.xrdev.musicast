package com.xrdev.musicast.activity;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.sample.castcompanionlibrary.cast.VideoCastManager;
import com.google.sample.castcompanionlibrary.cast.exceptions.NoConnectionException;
import com.google.sample.castcompanionlibrary.cast.exceptions.TransientNetworkDisconnectionException;
import com.xrdev.musicast.Application;
import com.xrdev.musicast.R;
import com.xrdev.musicast.model.PlaylistItem;
import com.xrdev.musicast.model.LocalQueue;
import com.xrdev.musicast.model.TrackItem;
import com.xrdev.musicast.utils.JsonConverter;

/**
 * Created by Guilherme on 08/10/2014.
 */
public class TracksFragment extends ListFragment {

    VideoCastManager mCastMgr;
    PlaylistItem mPlaylist;
    JsonConverter jsonConverter;
    LocalQueue mLocalQueue;

    public static TracksFragment newInstance() {
        TracksFragment tf = new TracksFragment();
        return tf;
    }



    public void setQueue(LocalQueue queue) {
        this.mLocalQueue = queue;
    }

    public PlaylistItem getPlaylist() {
        return mPlaylist;
    }

    public void setPlaylist(PlaylistItem mPlaylist) {
        this.mPlaylist = mPlaylist;
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // Lógica do clique numa track.
        super.onListItemClick(l, v, position, id);

        TrackItem selectedTrack = (TrackItem) getListAdapter().getItem(position);

        String spotifyId;
        String youtubeId;
        String duration;
        String youtubeIndex;

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

            youtubeIndex = String.valueOf(selectedTrack.getQueueIndex());




            if (selectedTrack.wasFound()) {
                mCastMgr = Application.getCastManager(getActivity().getApplicationContext());

                jsonConverter = Application.getConverter(getActivity().getApplicationContext());


                // Foi encontrado vídeo, enviar mensagem.
                try {
                    mCastMgr.sendDataMessage(
                            jsonConverter.makeLoadPlaylistJson(mLocalQueue, selectedTrack)
                    );

                /*mCastMgr.sendDataMessage(
                        jsonConverter.makeJson(JsonConverter.TYPE_PLAY_VIDEO_AT, selectedTrack)
                ); */

                    //String customMessage = jsonConverter.makeLoadPlaylistJson(JsonConverter.TYPE_LOAD_VIDEO, selectedTrack);

                    //mCastMgr.sendDataMessage(customMessage);
                } catch (TransientNetworkDisconnectionException e) {
                    e.printStackTrace();
                } catch (NoConnectionException e) {
                    e.printStackTrace();
                }

                Toast.makeText(getActivity(),"Selected track: "
                                + selectedTrack.getArtists() + " - " + selectedTrack.getName() + "\n"
                                + "Duration: " + duration + " seconds. \n"
                                + "Spotify ID: " + spotifyId + "\n"
                                + "YouTube ID: " + youtubeId + "\n"
                                + "YouTube index: " + youtubeIndex,
                        Toast.LENGTH_LONG).
                        show();

            } else {
                Toast.makeText(getActivity(),R.string.video_not_found, Toast.LENGTH_LONG).show();
            }





    }

}
