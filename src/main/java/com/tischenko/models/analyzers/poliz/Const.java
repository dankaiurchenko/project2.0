package com.tischenko.models.analyzers.poliz;


public class Const implements Valuable, ProgramPart {
  private double value;

  public Const(double value) {
    this.value = value;
  }

  @Override
  public double getValue() {
    return value;
  }

  @Override
  public String toString() {
    return  Double.toString(value);
  }
}
