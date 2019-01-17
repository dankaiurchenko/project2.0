package com.tischenko.models.analyzers;

import com.tischenko.models.Program;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

abstract public class AbstractAnalyzer {
  protected final Program program;

  protected final ObservableList<CompilerException> exceptions = FXCollections.observableArrayList();

  protected AbstractAnalyzer(Program program) {
    this.program = program;
  }

  public abstract boolean analyze();

  public ObservableList<CompilerException> getExceptions() {
    return exceptions;
  }
}
