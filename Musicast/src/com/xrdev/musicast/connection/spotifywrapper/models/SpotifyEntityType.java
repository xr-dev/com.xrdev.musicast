package com.xrdev.musicast.connection.spotifywrapper.models;

public enum SpotifyEntityType {

  ALBUM("album"),
  TRACK("track"),
  ARTIST("artist"),
  USER("user"),
  PLAYLIST("playlist");

  public final String type;

  SpotifyEntityType(String type) {
   this.type = type;
  }

  public String getType() {
    return this.type;
  }

}
