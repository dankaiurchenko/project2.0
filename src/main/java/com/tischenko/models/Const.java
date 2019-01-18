package com.tischenko.models;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.Objects;

@SuppressWarnings("unused")
public class Const {
  private transient DoubleProperty token;
  private transient IntegerProperty number;

  public Const(int number, double token) {
    this.number = new SimpleIntegerProperty(number);
    this.token = new SimpleDoubleProperty(token);
  }

  public Const(double token) {
    this.token = new SimpleDoubleProperty(token);
  }

  public int getNumber() {
    return number.get();
  }

  public double getToken() {
    return token.get();
  }

  public DoubleProperty tokenProperty() {
    return token;
  }

  public IntegerProperty numberProperty() {
    return number;
  }

  @Override
  public String toString() {
    return number.getValue() + "  " + token.getValue().toString() + "\n";
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Const)) return false;
    Const aConst = (Const) o;
    return Objects.equals(token, aConst.token);
  }

  @Override
  public int hashCode() {
    return Objects.hash(token);
  }

}
