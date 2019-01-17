package com.tischenko.models.analyzers.lexical.Exceptions;

public class EndOfFileException extends Exception {
  public EndOfFileException() {
    super("This is the end of the file. File parsed.");
  }
}