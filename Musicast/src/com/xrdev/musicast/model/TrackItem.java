package com.xrdev.musicast.model;

import com.xrdev.musicast.connection.YouTubeManager;
import com.xrdev.musicast.connection.spotifywrapper.models.SimpleAlbum;
import com.xrdev.musicast.connection.spotifywrapper.models.SimpleArtist;
import com.xrdev.musicast.connection.spotifywrapper.models.Track;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Guilherme on 20/07/2014.
 */
public class TrackItem {
    private String trackId;
    private String name;
    private String artists;
    private int duration;
    private String album;
    private int initialPos;
    private ArrayList<String> votes;

    private String youtubeId;
    public static String VIDEO_NOT_FOUND = "0";

    private int queueIndex;


    public TrackItem(Track apiTrack) {
        this.trackId = apiTrack.getId();
        this.name = apiTrack.getName();
        this.duration = apiTrack.getDuration() / 1000; // Duração no Spotify API é em milissegundos. Transformar em segundos.
        this.album = apiTrack.getAlbum().getName();
        this.artists = getArtistsFromApi(apiTrack);
        this.votes = new ArrayList<String>();
    }

    public String getTrackId() {
        return trackId;
    }

    public void setTrackId(String trackId) {
        this.trackId = trackId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getArtistsFromApi(Track apiTrack) {
        List<SimpleArtist> apiList = apiTrack.getArtists();

        Iterator iterator = apiList.iterator();

        String artistsString = "";

        while (iterator.hasNext()) {
            SimpleArtist simpleArtist = (SimpleArtist) iterator.next();
            artistsString += simpleArtist.getName();
            if (iterator.hasNext())
                artistsString += ", ";
        }
        return artistsString;
    }

    public String getArtists(){
        return artists;
    }

    public String getAlbum() {
        return album;
    }

    public String getYoutubeId() {
        return youtubeId;
    }

    public void setYoutubeId(String youtubeId) {
        this.youtubeId = youtubeId;
    }

    public int getQueueIndex() {
        return queueIndex;
    }

    public void setQueueIndex(int queueIndex) {
        this.queueIndex = queueIndex;
    }

    public boolean wasSearched(){
        if (youtubeId != null)
            return true;
        else
            return false;
    }

    public boolean wasFound(){
        if (null == youtubeId) {
            return false;
        }

        if (youtubeId.equals(VIDEO_NOT_FOUND))
            return false;
        else
            return true;
    }

    public int getInitialPos() {
        return initialPos;
    }

    public void setInitialPos(int initialPos) {
        this.initialPos = initialPos;
    }

    public ArrayList<String> getVotes() {
        return votes;
    }

    public boolean hasVoted(String uuid) {
        if (votes.contains(uuid)) {
            return true;
        } else {
            return false;
        }
    }

    public void setVotes(ArrayList<String> votes) {
        this.votes = votes;
    }

    public int getVoteCount() {
        return votes.size();
    }
}
