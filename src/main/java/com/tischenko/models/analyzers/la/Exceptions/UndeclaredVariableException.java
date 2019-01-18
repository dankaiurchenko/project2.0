package com.tischenko.models.analyzers.la.Exceptions;

public class UndeclaredVariableException extends ScannerException  {
  public UndeclaredVariableException(int number, String errorToken, int numberOfRow) {
    super(number, errorToken, numberOfRow, "You must have missed to declare the variable");
  }

}
