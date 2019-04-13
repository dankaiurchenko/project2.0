package com.tischenko.models.analyzers.poliz;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.LinkedList;
import java.util.Stack;

public class PolizStateDump {

  private IntegerProperty number;
  private StringProperty stack;
  private StringProperty poliz;

  PolizStateDump(int number, Stack<Valuable> stack, String poliz) {
    this.number = new SimpleIntegerProperty(number);
    this.stack = new SimpleStringProperty(stack.toString());
    this.poliz = new SimpleStringProperty(poliz);
  }

  PolizStateDump(int number, String result) {
    this.number = new SimpleIntegerProperty(number);
    this.stack = new SimpleStringProperty("The Result is :  " );
    this.poliz = new SimpleStringProperty(result);
  }

  public String getStack() {
    return stack.get();
  }

  public StringProperty stackProperty() {
    return stack;
  }

  public String getPoliz() {
    return poliz.get();
  }

  public StringProperty polizProperty() {
    return poliz;
  }

  public int getNumber() {
    return number.get();
  }

  public IntegerProperty numberProperty() {
    return number;
  }
}
