package com.tischenko.models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

@SuppressWarnings("unused")
public class Token {

  private transient StringProperty token;
  private transient IntegerProperty code;
  private transient IntegerProperty idCode;
  private transient IntegerProperty constantCode;
  private transient IntegerProperty number;
  private transient IntegerProperty line;

  Token(String token, int code, int idCode, int constantCode, int tokenNumber, int line) {
    this.token = new SimpleStringProperty(token);
    this.code = new SimpleIntegerProperty(code);
    this.idCode = new SimpleIntegerProperty(idCode);
    this.constantCode = new SimpleIntegerProperty(constantCode);
    this.number = new SimpleIntegerProperty(tokenNumber);
    this.line = new SimpleIntegerProperty(line);
  }

  public Token(String token, int line) {
    this.token = new SimpleStringProperty(token);
    this.code = new SimpleIntegerProperty(0);
    this.line = new SimpleIntegerProperty(line);
  }

  @Override
  public String toString() {
    return " \n" + token.getValue() + " " + line.getValue() + " " + code.getValue()
            + " " + idCode.getValue() + " " + constantCode.getValue();
  }

  public int getCode() {
    return code.getValue();
  }

  public int getNumber() {
    return number.getValue();
  }

  public int getLine() {
    return line.getValue();
  }

  public String getToken() {
    return token.getValue();
  }

  public int getIdentCode() {
    return idCode.get();
  }

  public int getConstantCode() {
    return constantCode.get();
  }

  public StringProperty tokenProperty() {
    return token;
  }

  public IntegerProperty codeProperty() {
    return code;
  }

  public IntegerProperty idCodeProperty() {
    return idCode;
  }

  public IntegerProperty constantCodeProperty() {
    return constantCode;
  }

  public IntegerProperty numberProperty() {
    return number;
  }

  public IntegerProperty lineProperty() {
    return line;
  }
}
