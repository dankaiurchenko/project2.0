package com.danarossa.compiler.models.analyzers.lexical.Exceptions;

public class DuplicatedVariableException extends ScannerException {
  public DuplicatedVariableException(int number, String errorToken, int numberOfRow) {
    super(number, errorToken, numberOfRow, "You have already declared such variable");
  }

}
