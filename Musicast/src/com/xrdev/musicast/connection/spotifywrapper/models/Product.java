package com.xrdev.musicast.connection.spotifywrapper.models;

public enum Product {
  PREMIUM("premium"), FREE("free");

  public final String type;

  Product(String type) {
    this.type = type;
  }

  public String getType() {
    return type;
  }

}
