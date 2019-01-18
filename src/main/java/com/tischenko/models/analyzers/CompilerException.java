package com.tischenko.models.analyzers;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

@SuppressWarnings("unused")
public class CompilerException implements Serializable {
  private transient IntegerProperty number;
  private transient StringProperty message;
  private transient IntegerProperty line;
  private transient StringProperty token;
  private transient StringProperty analyzer;


  protected CompilerException(int number, String message, int line, String token, String analyzer) {
    this.number = new SimpleIntegerProperty(number);
    this.message = new SimpleStringProperty(message);
    this.line = new SimpleIntegerProperty(line);
    this.token = new SimpleStringProperty(token);
    this.analyzer = new SimpleStringProperty(analyzer);
  }

  private void writeObject(ObjectOutputStream s) throws IOException {
    s.defaultWriteObject();
    s.writeInt(number.intValue());
    s.writeUTF(message.getValueSafe());
    s.writeInt(line.intValue());
    s.writeUTF(token.getValueSafe());
    s.writeUTF(analyzer.getValueSafe());
  }

  private void readObject(ObjectInputStream s) throws IOException {
    initMethod();
    number.set(s.readInt());
    message.set(s.readUTF());
    line.set(s.readInt());
    token.set(s.readUTF());
    analyzer.set(s.readUTF());
  }


  private void initMethod() {
    this.number = new SimpleIntegerProperty();
    this.message = new SimpleStringProperty();
    this.line = new SimpleIntegerProperty();
    this.token = new SimpleStringProperty();
    this.analyzer = new SimpleStringProperty();

  }

  public int getNumber() {
    return number.get();
  }


  public String getMessage() {
    return message.get();
  }


  public int getLine() {
    return line.get();
  }


  public String getToken() {
    return token.get();
  }

  public StringProperty tokenProperty() {
    return token;
  }

  public String getAnalyzer() {
    return analyzer.get();
  }

  @Override
  public String toString() {
    return "message=" + message.getValue() +
            ", line=" + line.getValue() +
            ", token=" + token.getValue() +
            '\n';
  }
}
