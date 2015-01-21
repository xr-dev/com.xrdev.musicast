package com.xrdev.musicast.model;

import java.util.ArrayList;

/**
 * Created by Guilherme on 21/01/2015.
 */
public class JsonModel {
    String type;
    String index;
    String videoId;
    ArrayList<TrackItem> trackList;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public ArrayList<TrackItem> getTrackList() {
        return trackList;
    }

    public void setTrackList(ArrayList<TrackItem> trackList) {
        this.trackList = trackList;
    }
}
