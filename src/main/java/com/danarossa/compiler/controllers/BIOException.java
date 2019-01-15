package com.danarossa.compiler.controllers;

public class BIOException extends Exception {
  public BIOException(Throwable cause) {
    super(cause);
  }

  public BIOException(String message) {
    super(message);
  }
}
