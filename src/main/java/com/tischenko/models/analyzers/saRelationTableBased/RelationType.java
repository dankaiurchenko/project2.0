package com.tischenko.models.analyzers.saRelationTableBased;

public enum RelationType {
  GREATER(">"), LESS("<"), EQUAL("="), UNDEFINED("");

  RelationType(String sign) {
    this.sign = sign;
  }

  final String sign;

  String getSign(){
    return sign;
  }
}
