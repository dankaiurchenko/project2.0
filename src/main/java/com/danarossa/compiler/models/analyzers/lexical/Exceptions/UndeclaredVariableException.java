package com.danarossa.compiler.models.analyzers.lexical.Exceptions;

public class UndeclaredVariableException extends ScannerException  {
  public UndeclaredVariableException(int number, String errorToken, int numberOfRow) {
    super(number, errorToken, numberOfRow, "You must have missed to declare the variable");
  }

}
