package com.xrdev.musicast.connection.spotifywrapper.exceptions;

public class ServerErrorException extends WebApiException {

  public ServerErrorException(String message) {
    super(message);
  }
}
