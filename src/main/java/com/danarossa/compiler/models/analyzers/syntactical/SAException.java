package com.danarossa.compiler.models.analyzers.syntactical;

import com.danarossa.compiler.models.Program;
import com.danarossa.compiler.models.analyzers.CompilerException;

class SAException extends CompilerException {
  SAException(Program.Token token, String cause, int number) {
      super(number, cause, token.getLine(), token.getToken(), "Syntax analyzer");
  }
}
