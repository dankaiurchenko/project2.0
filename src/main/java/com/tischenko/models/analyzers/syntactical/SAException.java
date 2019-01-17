package com.tischenko.models.analyzers.syntactical;

import com.tischenko.models.Program;
import com.tischenko.models.analyzers.CompilerException;

class SAException extends CompilerException {
  SAException(Program.Token token, String cause, int number) {
      super(number, cause, token.getLine(), token.getToken(), "Syntax analyzer");
  }
}
