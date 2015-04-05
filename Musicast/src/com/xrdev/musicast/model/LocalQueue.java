package com.xrdev.musicast.model;

import java.util.ArrayList;

/**
 * Created by Guilherme on 22/01/2015.
 */
public class LocalQueue {
    private static String mPlaylistId;
    private static LocalQueue sInstance;
    private ArrayList<TrackItem> mValidTracks;

    public static LocalQueue initialize(String playlistId){

        sInstance = new LocalQueue(playlistId);

        return sInstance;

    }

    public LocalQueue(String playlistId) {
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

    public void setPositions(){
        int count = 0;
        for (TrackItem trackItem : mValidTracks) {
            trackItem.setInitialPos(count);
            trackItem.setCurrentPos(count);
            count++;
        }
    }

}
