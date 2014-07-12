package com.xrdev.musicast.connection.spotifywrapper.methods;

import com.google.common.util.concurrent.SettableFuture;
import com.xrdev.musicast.connection.spotifywrapper.JsonUtil;
import com.xrdev.musicast.connection.spotifywrapper.exceptions.WebApiException;
import com.xrdev.musicast.connection.spotifywrapper.models.Playlist;
import net.sf.json.JSONObject;

import java.io.IOException;

public class AddTrackToPlaylistRequest extends AbstractRequest {

  public AddTrackToPlaylistRequest(Builder builder) {
    super(builder);
  }

  public static Builder builder() {
    return new Builder();
  }

  public SettableFuture<String> getAsync() {
    final SettableFuture<String> addTrackFuture = SettableFuture.create();

    try {
      final String jsonString = postJson();
      if ("".equals(jsonString)) {
        addTrackFuture.set("Created");
      } else {
        final JSONObject jsonObject = JSONObject.fromObject(postJson());
      }
    } catch (Exception e) {
      addTrackFuture.setException(e);
    }

    return addTrackFuture;
  }

  public Playlist get() throws IOException, WebApiException {
    final String jsonString = getJson();
    final JSONObject jsonObject = JSONObject.fromObject(jsonString);

    return JsonUtil.createPlaylist(jsonObject);
  }

  public static final class Builder extends AbstractRequest.Builder<Builder> {

    public Builder position(int position) {
      assert (position >= 0);

      return parameter("position", String.valueOf(position));
    }

    public AddTrackToPlaylistRequest build() {
      header("Content-Type", "application/json");
      return new AddTrackToPlaylistRequest(this);
    }

  }

}