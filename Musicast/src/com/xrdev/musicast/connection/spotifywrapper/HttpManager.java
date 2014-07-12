package com.xrdev.musicast.connection.spotifywrapper;

import com.xrdev.musicast.connection.spotifywrapper.UtilProtos.Url;
import com.xrdev.musicast.connection.spotifywrapper.exceptions.BadRequestException;
import com.xrdev.musicast.connection.spotifywrapper.exceptions.EmptyResponseException;
import com.xrdev.musicast.connection.spotifywrapper.exceptions.ServerErrorException;
import com.xrdev.musicast.connection.spotifywrapper.exceptions.WebApiException;

import java.io.IOException;

/**
 * A simple HTTP connection interface.
 */
public interface HttpManager {

  /**
   * Perform an HTTP GET request to the specified URL.
   *
   * @param url the {@link Url} to HTTP GET.
   * @return a String containing the body of the HTTP GET response.
   */
  String get(Url url) throws WebApiException, IOException;

  /**
   * Perform an HTTP POST request to the specified URL.
   *
   * @param url the {@link Url} to HTTP POST.
   * @return a String containing the body of the HTTP POST response.
   */
  String post(Url url) throws WebApiException, IOException;

  /**
   * Perform an HTTP DELETE request to the specified URL.
   *
   * @param url the {@link Url} to HTTP DELEte.
   * @return a String containing the body of the HTTP DELETE response.
   */
  String delete(Url url);

  /**
   * Perform an HTTP PUT request to the specified URL.
   *
   * @param url the {@link Url} to HTTP PUT.
   * @return a String containing the body of the HTTP PUTresponse.
   */
  String put(Url url);

}
