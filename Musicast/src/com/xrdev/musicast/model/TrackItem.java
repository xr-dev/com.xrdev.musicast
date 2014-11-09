package com.xrdev.musicast.model;

import com.xrdev.musicast.connection.spotifywrapper.models.SimpleAlbum;
import com.xrdev.musicast.connection.spotifywrapper.models.SimpleArtist;
import com.xrdev.musicast.connection.spotifywrapper.models.Track;

import java.util.Iterator;
import java.util.List;

/**
 * Created by Guilherme on 20/07/2014.
 */
public class TrackItem {
    private String trackId;
    private String name;
    private int duration;
    private List<SimpleArtist> artistsList;
    private SimpleAlbum album;

    private String youtubeId;
    private String youtubeMp4;



    public TrackItem(Track apiTrack) {
        this.trackId = apiTrack.getId();
        this.name = apiTrack.getName();
        this.duration = apiTrack.getDuration() / 1000; // Duração no Spotify API é em milissegundos. Transformar em segundos.
        this.artistsList = apiTrack.getArtists();
        this.album = apiTrack.getAlbum();
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

    public String getArtists() {
        Iterator iterator = artistsList.iterator();

        String artistsString = "";

        while (iterator.hasNext()) {
            SimpleArtist simpleArtist = (SimpleArtist) iterator.next();
            artistsString += simpleArtist.getName();
            if (iterator.hasNext())
                artistsString += ", ";
        }
        return artistsString;
    }

    public String getAlbum() {
        return album.getName();
    }

    public String getYoutubeId() {
        return youtubeId;
    }

    public void setYoutubeId(String youtubeId) {
        this.youtubeId = youtubeId;
    }

    public String getYoutubeMp4() {
        return youtubeMp4;
    }

    public void setYoutubeMp4(String youtubeMp4) {
        this.youtubeMp4 = youtubeMp4;
    }
}
