package com.xrdev.musicast.model;

import android.widget.ListAdapter;

import java.util.ArrayList;

/**
 * Created by Guilherme on 22/01/2015.
 */
public class QueueList {
    private static String mPlaylistId;
    private static QueueList sInstance;
    private ArrayList<TrackItem> mValidTracks;

    public static QueueList initialize(String playlistId){

        sInstance = new QueueList(playlistId);

        return sInstance;

    }

    public QueueList(String playlistId) {
        mPlaylistId = playlistId;
        mValidTracks = new ArrayList<TrackItem>();
    }

    public String getPlaylistId() {
        return mPlaylistId;
    }

    public void setPlaylistId(String playlistId) {
        mPlaylistId = playlistId;
    }

    public static void setPlaylist(String playlist) {
        mPlaylistId = playlist;
    }


    public ArrayList<TrackItem> getValidTracks() {
        return mValidTracks;
    }

    public void addValidTrack(TrackItem track) {
        mValidTracks.add(track);
    }

    public ArrayList<String> getValidIds(){
        ArrayList<String> validIds = new ArrayList<String>();

        for (TrackItem trackItem : mValidTracks) {
            validIds.add(trackItem.getYoutubeId());
        }

        return validIds;
    }

}
