package com.xrdev.musicast.connection.spotifywrapper.methods;

import com.google.common.util.concurrent.SettableFuture;
import net.sf.json.JSONObject;
import com.xrdev.musicast.connection.spotifywrapper.JsonUtil;
import com.xrdev.musicast.connection.spotifywrapper.exceptions.*;
import com.xrdev.musicast.connection.spotifywrapper.models.Artist;

import java.io.IOException;

public class ArtistRequest extends AbstractRequest {

  protected ArtistRequest(Builder builder) {
    super(builder);
  }

  public static Builder builder() {
    return new Builder();
  }

  public SettableFuture<Artist> getAsync() {
    SettableFuture<Artist> artistFuture = SettableFuture.create();

    try {
      final String jsonString = getJson();
      final JSONObject jsonObject = JSONObject.fromObject(jsonString);

      artistFuture.set(JsonUtil.createArtist(jsonObject));
    } catch (Exception e) {
      artistFuture.setException(e);
    }

    return artistFuture;
  }

  public Artist get() throws IOException, WebApiException {
    final String jsonString = getJson();

    return JsonUtil.createArtist(JSONObject.fromObject(jsonString));
  }

  public static final class Builder extends AbstractRequest.Builder<Builder> {

    public ArtistRequest build() {
      return new ArtistRequest(this);
    }

  }
}
