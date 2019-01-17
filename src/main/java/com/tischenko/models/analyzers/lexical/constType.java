package com.tischenko.models.analyzers.lexical;

import java.io.Serializable;

public enum constType implements Serializable {
  PROGRAM("Program"),
  INTEGER("integer"),
  SHORT("short"),
  LABEL("label");

  constType(String name) {
    this.name = name;
  }

  private final String name;

  static public constType getType(String token) {
    if (PROGRAM.name.equals(token)) return PROGRAM;
    if (INTEGER.name.equals(token)) return INTEGER;
    if (SHORT.name.equals(token)) return SHORT;
    if (LABEL.name.equals(token)) return LABEL;
    return null;
  }


}