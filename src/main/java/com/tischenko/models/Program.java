package com.tischenko.models;

import com.tischenko.models.analyzers.lexical.constType;
import com.tischenko.models.analyzers.syntactical2.SyntaxAnalyzer2;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.util.*;

import static java.lang.Integer.parseInt;

public class Program implements Serializable{

  private transient String sourceCode;
  private transient String programName;
  private transient File programFile;
  private transient HashMap<String, String> tableOfTokens = new HashMap<>();
  private transient ObservableList<SyntaxAnalyzer2.dumpState> transitionTable = FXCollections.observableArrayList();
  private transient ObservableList<Token> tokens = FXCollections.observableArrayList();
  private transient ObservableList<Identifier> identifiers = FXCollections.observableArrayList();
  private transient ObservableList<Const> constants = FXCollections.observableArrayList();

  public Program() {
  }

  public void setSourceCode(String sourceCode) {
    this.sourceCode = sourceCode;
  }

  public void setProgramName(String programName) {
    this.programName = programName;
  }

  public void setProgramFile(File programFile) {
    this.programFile = programFile;
  }

  public void setTableOfTokens(HashMap<String, String> tableOfTokens) {
    this.tableOfTokens = tableOfTokens;
  }

  public boolean hasIdentifier(Identifier identifier) {
    return identifiers.contains(identifier);
  }

  public boolean hasConstant(Const constant) {
    return constants.contains(constant);
  }

  public void addIdentifier(Identifier identifier) {
    this.identifiers.add(identifier);
  }

  public void addConstant(Const constant) {
    this.constants.add(constant);
  }

  public void addToken(String token, int code, int id, int constant, int number, int line) {
    tokens.add(new Token(token, code, id, constant, number, line));
  }

  public Token getToken(int n) {
    if(n == tokens.size()) return tokens.get(n-1);
    return tokens.get(n);
  }

  public Token getLastToken() {
    return this.tokens.get(tokens.size() - 1);
  }

  public int getCode(String token) {
    return parseInt(tableOfTokens.get(token));
  }

  public HashMap<String, String> getTableOfTokens() {
    return tableOfTokens;
  }

  public ObservableList<Identifier> getIdentifiers() {
    return identifiers;
  }

  public ObservableList<Token> getTokens() {
    return tokens;
  }

  public ObservableList<Const> getConstants() {
    return constants;
  }

  public String getSourceCode() {
    return sourceCode;
  }

  public String getProgramName() {
    return programName;
  }

  public File getProgramFile() {
    return programFile;
  }

  public Program cleanup() {
    tokens = FXCollections.observableArrayList();
    tableOfTokens = new HashMap<>();
    identifiers = FXCollections.observableArrayList();
    constants = FXCollections.observableArrayList();
    return this;
  }

  public void clearTransitions() {
    transitionTable = FXCollections.observableArrayList();
  }

  public void addDumpState(SyntaxAnalyzer2.dumpState state){
    transitionTable.add(state);
  }

  public ObservableList<SyntaxAnalyzer2.dumpState> getTransitionTable() {
    return transitionTable;
  }

  @Override
  public String toString() {
    return "Program{" +
            "sourceCode='" + sourceCode + '\'' +
            ", programName='" + programName + '\'' +
            ", programFile=" + programFile +
            ", tableOfTokens=" + tableOfTokens +
            ", tokens=" + tokens +
            ", identifiers=" + identifiers +
            ", constants=" + constants +
            '}';
  }



  static public class Identifier implements Serializable{

    private transient IntegerProperty number;
    private transient StringProperty token;
    private transient StringProperty typeName;
    private transient constType type;

    public Identifier(int number, String token, constType type) {
      this.number = new SimpleIntegerProperty(number);
      this.token = new SimpleStringProperty(token);
      this.typeName = new SimpleStringProperty(type.toString());
      this.type = type;
    }

    public Identifier(String token) {
      this.token = new SimpleStringProperty(token);
    }

    private void initMethod(){
      this.number = new SimpleIntegerProperty();
      this.token = new SimpleStringProperty();
      this.typeName = new SimpleStringProperty();
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

    public constType getType() {
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
      if (!(o instanceof Identifier)) return false;
      Identifier identifier = (Identifier) o;
      return Objects.equals(getToken(), identifier.getToken());
    }

    @Override
    public int hashCode() {
      return Objects.hash(getToken());
    }

    private void writeObject(ObjectOutputStream s) throws IOException {
      s.defaultWriteObject();
      s.writeInt(number.intValue());
      s.writeUTF(token.getValueSafe());
      s.writeUTF(typeName.getValueSafe());
      s.writeObject(type);
    }

    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
      initMethod();
      number.set(s.readInt());
      token.set(s.readUTF());
      typeName.set(s.readUTF());
      type = (constType) s.readObject();
    }

    @Override
    public String toString() {
      return "\n" +
              " " + number.getValue() +
              " " + token.getValue() +
              " " + typeName.getValue();
    }
  }

  static public class Token implements Serializable {

    private transient StringProperty token;
    private transient IntegerProperty code;
    private transient IntegerProperty idCode;
    private transient IntegerProperty constantCode;
    private transient IntegerProperty number;
    private transient IntegerProperty line;

    public Token() {
      initMethod();
    }

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

    private void initMethod(){
      this.token = new SimpleStringProperty();
      this.code = new SimpleIntegerProperty();
      this.idCode = new SimpleIntegerProperty();
      this.constantCode = new SimpleIntegerProperty();
      this.number = new SimpleIntegerProperty();
      this.line = new SimpleIntegerProperty();
    }

    private void writeObject(ObjectOutputStream s) throws IOException {
      s.defaultWriteObject();
      s.writeUTF(token.getValueSafe());
      s.writeInt(code.intValue());
      s.writeInt(idCode.intValue());
      s.writeInt(constantCode.intValue());
      s.writeInt(number.intValue());
      s.writeInt(line.intValue());
    }

    private void readObject(ObjectInputStream s) throws IOException {
      initMethod();
      token.set(s.readUTF());
      code.set(s.readInt());
      idCode.set(s.readInt());
      constantCode.set(s.readInt());
      number.set(s.readInt());
      line.set(s.readInt());
    }

    @Override
    public String toString() {
      return  " \n" + token.getValue() + " " + line.getValue() + " "  + code.getValue()
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

    public int getIdCode() {
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

  static public class Const implements Serializable {
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
      return number.getValue() + "  " +  token.getValue().toString() + "\n" ;
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

    private void writeObject(ObjectOutputStream s) throws IOException {
      s.defaultWriteObject();
      s.writeDouble(token.getValue());
      s.writeInt(number.intValue());
    }

    private void initMethod(){
      this.number = new SimpleIntegerProperty();
      this.token = new SimpleDoubleProperty();
    }

    private void readObject(ObjectInputStream s) throws IOException {
      initMethod();
      token.set(s.readDouble());
      number.set(s.readInt());
    }

  }
}
