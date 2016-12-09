package com.github.eirslett.maven.plugins.frontend.lib;

public class FrontendException extends Exception {

  FrontendException(String message) {
    super(message);
  }

  public FrontendException(String message, Throwable cause){
    super(message, cause);
  }
}
