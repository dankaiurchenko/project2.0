package com.danarossa.compiler.models.analyzers.lexical.Exceptions;

import com.danarossa.compiler.models.analyzers.CompilerException;

public class ScannerException extends CompilerException  {
  public ScannerException(int number, String errorToken, int numberOfRow) {
    this(number, errorToken, numberOfRow, "Cannot resolve symbol");
  }
  ScannerException(int number, String errorToken, int numberOfRow, String cause) {
    super(number, cause, numberOfRow, errorToken, "Lexical analyzer");
  }

}


