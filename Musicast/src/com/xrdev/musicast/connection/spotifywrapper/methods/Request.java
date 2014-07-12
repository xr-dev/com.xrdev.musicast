package com.xrdev.musicast.connection.spotifywrapper.methods;

import com.xrdev.musicast.connection.spotifywrapper.HttpManager;
import com.xrdev.musicast.connection.spotifywrapper.UtilProtos.Url;

public interface Request {

  public static interface Builder {
    Builder httpManager(HttpManager httpManager);
    Builder host(String host);
    Builder port(int port);
    Builder scheme(Url.Scheme scheme);
    AbstractRequest build();
  }

  Url toUrl();

}
