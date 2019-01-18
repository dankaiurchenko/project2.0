package com.tischenko.models.analyzers.la;

import java.io.Serializable;

public enum IdentifierTypes implements Serializable {
  PROGRAM("Program"),
  INTEGER("integer"),
  SHORT("short"),
  LABEL("label");

  private final String name;

  IdentifierTypes(String name) {
    this.name = name;
  }

  static public IdentifierTypes getType(String token) {
    if (PROGRAM.name.equals(token)) return PROGRAM;
    if (INTEGER.name.equals(token)) return INTEGER;
    if (SHORT.name.equals(token)) return SHORT;
    if (LABEL.name.equals(token)) return LABEL;
    return null;
  }


}