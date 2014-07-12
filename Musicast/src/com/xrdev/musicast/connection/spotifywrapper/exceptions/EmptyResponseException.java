package com.xrdev.musicast.connection.spotifywrapper.exceptions;

public class EmptyResponseException extends WebApiException {

  public EmptyResponseException() {
    super();
  }

  public EmptyResponseException(String message) {
    super(message);
  }
}
