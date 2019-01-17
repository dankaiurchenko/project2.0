package com.tischenko.controllers;

public class BIOException extends Exception {
  public BIOException(Throwable cause) {
    super(cause);
  }

  public BIOException(String message) {
    super(message);
  }
}
