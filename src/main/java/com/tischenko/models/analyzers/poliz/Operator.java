package com.tischenko.models.analyzers.poliz;

public class Operator  implements ProgramPart{
  String operator;

  public Operator(String operator) {
    this.operator = operator;
  }

  public String getOperator() {
    return operator;
  }

  @Override
  public String toString() {
    return operator ;
  }
}
