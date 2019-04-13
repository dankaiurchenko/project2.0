package com.tischenko.models.analyzers.poliz;

public class Id implements Valuable, ProgramPart {
 private int value = 0;
 private String name;

  public Id(String name) {
    this.name = name;
  }

  @Override
  public double getValue() {
    return value;
  }

  public void setValue(double value) {
    this.value = (int)value;
  }

  public String getName() {
    return name;
  }

  @Override
  public String toString() {
    return  name;
  }
}
