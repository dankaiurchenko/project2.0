package com.tischenko.models.analyzers.saRelationTableBased;

import com.tischenko.models.analyzers.CompilerException;

class SyntacticalAnalyzerException extends CompilerException {

  public SyntacticalAnalyzerException(String message, int line, String token, String analyzer) {
    super(0, message, line, token, analyzer);
  }
}
