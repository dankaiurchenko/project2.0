package com.tischenko.models.analyzers;

import com.tischenko.models.Token;

@SuppressWarnings("SameParameterValue")
public class SAException extends CompilerException {
  public SAException(Token token, String cause, int number) {
    super(number, cause, token.getLine(), token.getToken(), "Syntax analyzer");
  }
}
