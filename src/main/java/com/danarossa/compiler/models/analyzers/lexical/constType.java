package com.danarossa.compiler.models.analyzers.lexical;

import java.io.Serializable;

public enum constType implements Serializable {
  INT(1),
  DOUBLE(2);

  constType(int code) {
    this.code = code;
  }

  public int getCode() {
    return code;
  }
  private final int code;
}