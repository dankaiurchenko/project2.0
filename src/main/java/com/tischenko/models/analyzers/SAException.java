package com.tischenko.models.analyzers;

import com.tischenko.models.Program;
import com.tischenko.models.analyzers.CompilerException;

@SuppressWarnings("SameParameterValue")
public class SAException extends CompilerException {
  public SAException(Program.Token token, String cause, int number) {
      super(number, cause, token.getLine(), token.getToken(), "Syntax analyzer");
  }
}
