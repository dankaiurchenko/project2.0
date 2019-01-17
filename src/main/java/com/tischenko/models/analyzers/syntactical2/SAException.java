package com.tischenko.models.analyzers.syntactical2;

import com.tischenko.models.Program;
import com.tischenko.models.analyzers.CompilerException;

@SuppressWarnings("SameParameterValue")
class SAException extends CompilerException {
  SAException(Program.Token token, String cause, int number) {
      super(number, cause, token.getLine(), token.getToken(), "Syntax analyzer");
  }
}
