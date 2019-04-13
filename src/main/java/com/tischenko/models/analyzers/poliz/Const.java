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
    if(value - (int)value == 0) {
     return Integer.toString((int)value);
    }else  return Double.toString(value);
  }
}
