package com.xrdev.musicast.model;

import android.content.Intent;

/**
 * Created by Guilherme on 20/07/2014.
 */
public class PlaylistItem {
    private String name;
    private int numTracks;
    private String playlistId;
    private String ownerId;

    public static final String EXTRA_NAME = "name";
    public static final String EXTRA_NUMTRACKS = "numTracks";
    public static final String EXTRA_PLAYLISTID = "playlistId";
    public static final String EXTRA_OWNERID = "ownerId";


    public PlaylistItem(String name, int numTracks, String playlistId, String ownerId) {
        this.name = name;
        this.numTracks = numTracks;
        this.playlistId = playlistId;
        this.ownerId = ownerId;
    }

    public PlaylistItem(Intent intent) {
        this.name = intent.getStringExtra(PlaylistItem.EXTRA_NAME);
        this.numTracks = intent.getIntExtra(PlaylistItem.EXTRA_NUMTRACKS, 0);
        this.playlistId = intent.getStringExtra(PlaylistItem.EXTRA_PLAYLISTID);
        this.ownerId = intent.getStringExtra(PlaylistItem.EXTRA_OWNERID);
    }

    public static void packageIntent(Intent intent, PlaylistItem playlistItem){
        intent.putExtra(PlaylistItem.EXTRA_NAME, playlistItem.getName());
        intent.putExtra(PlaylistItem.EXTRA_NUMTRACKS, playlistItem.getNumTracksInt());
        intent.putExtra(PlaylistItem.EXTRA_PLAYLISTID, playlistItem.getPlaylistId());
        intent.putExtra(PlaylistItem.EXTRA_OWNERID, playlistItem.getOwnerId());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumTracks() {
        return Integer.toString(numTracks);
    }

    public int getNumTracksInt() {
        return numTracks;
    }

    public void setNumTracks(int numTracks) {
        this.numTracks = numTracks;
    }

    public String getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(String playlistId) {
        this.playlistId = playlistId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }
}
