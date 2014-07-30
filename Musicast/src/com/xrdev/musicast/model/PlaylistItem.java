package com.xrdev.musicast.model;

/**
 * Created by Guilherme on 20/07/2014.
 */
public class PlaylistItem {
    private String name;
    private int numTracks;


    public PlaylistItem(String name, int numTracks) {
        this.name = name;
        this.numTracks = numTracks;
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

    public void setNumTracks(int numTracks) {
        this.numTracks = numTracks;
    }
}
