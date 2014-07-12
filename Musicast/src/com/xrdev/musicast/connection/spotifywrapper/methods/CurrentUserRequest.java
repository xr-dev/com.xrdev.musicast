package com.xrdev.musicast.connection.spotifywrapper.methods;

import com.google.common.util.concurrent.SettableFuture;
import net.sf.json.JSONObject;
import com.xrdev.musicast.connection.spotifywrapper.JsonUtil;
import com.xrdev.musicast.connection.spotifywrapper.exceptions.WebApiException;
import com.xrdev.musicast.connection.spotifywrapper.models.User;

import java.io.IOException;

public class CurrentUserRequest extends AbstractRequest {

  public CurrentUserRequest(Builder builder) {
    super(builder);
  }

  public SettableFuture<User> getAsync() {
    final SettableFuture<User> userFuture = SettableFuture.create();

    try {
      final JSONObject jsonObject = JSONObject.fromObject(getJson());

      userFuture.set(JsonUtil.createUser(jsonObject));
    } catch (Exception e) {
      userFuture.setException(e);
    }

    return userFuture;
  }

  public User get() throws IOException, WebApiException {
    JSONObject jsonObject = JSONObject.fromObject(getJson());

    return JsonUtil.createUser(jsonObject);
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder extends AbstractRequest.Builder<Builder> {

    public Builder accessToken(String accessToken) {
      return header("Authorization", "Bearer " + accessToken);
    }

    public CurrentUserRequest build() {
      path("/v1/me");
      return new CurrentUserRequest(this);
    }
  }

}
