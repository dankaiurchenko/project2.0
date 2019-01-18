package com.tischenko.models.analyzers.la.Exceptions;

import com.tischenko.models.analyzers.CompilerException;

public class ScannerException extends CompilerException {
  public ScannerException(int number, String errorToken, int numberOfRow) {
    this(number, errorToken, numberOfRow, "Cannot resolve symbol");
  }
  ScannerException(int number, String errorToken, int numberOfRow, String cause) {
    super(number, cause, numberOfRow, errorToken, "Lexical analyzer");
  }

}


