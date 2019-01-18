package com.tischenko.models;

import com.tischenko.models.analyzers.la.IdentifierTypes;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.Objects;

@SuppressWarnings("unused")
public class Ident {
  private transient IntegerProperty number;
  private transient StringProperty token;
  private transient StringProperty typeName;
  private transient IdentifierTypes type;

  public Ident(int number, String token, IdentifierTypes type) {
    this.number = new SimpleIntegerProperty(number);
    this.token = new SimpleStringProperty(token);
    this.typeName = new SimpleStringProperty(type.toString());
    this.type = type;
  }

  public Ident(String token) {
    this.token = new SimpleStringProperty(token);
  }

  public String getToken() {
    return token.get();
  }

  public int getNumber() {
    return number.get();
  }

  public IntegerProperty numberProperty() {
    return number;
  }

  public StringProperty tokenProperty() {
    return token;
  }

  public IdentifierTypes getType() {
    return type;
  }

  public String getTypeName() {
    return typeName.get();
  }

  public StringProperty typeNameProperty() {
    return typeName;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Ident)) return false;
    Ident identifier = (Ident) o;
    return Objects.equals(getToken(), identifier.getToken());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getToken());
  }

  @Override
  public String toString() {
    return "\n" +
            " " + number.getValue() +
            " " + token.getValue() +
            " " + typeName.getValue();
  }
}
