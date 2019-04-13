package com.tischenko.models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.util.*;

import static java.lang.Integer.parseInt;

public class Program {

  private String sourceCode;
  private String programName;
  private File programFile;
  private HashMap<String, String> tableOfTokens = new HashMap<>();
  private ObservableList<Token> tokens = FXCollections.observableArrayList();
  private ObservableList<Ident> idents = FXCollections.observableArrayList();
  private ObservableList<Const> consts = FXCollections.observableArrayList();

  public Program() {
  }

  public void setSourceCode(String sourceCode) {
    this.sourceCode = sourceCode;
  }

  public boolean hasIdent(Ident identifier) {
    return idents.contains(identifier);
  }

  public boolean hasConsts(Const constant) {
    return consts.contains(constant);
  }

  public void addIdent(Ident identifier) {
    this.idents.add(identifier);
  }

  public void addConst(Const constant) {
    this.consts.add(constant);
  }

  public void addToken(String token, int code, int id, int constant, int number, int line) {
    tokens.add(new Token(token, code, id, constant, number, line));
  }

  public Token getToken(int n) {
    if (n == tokens.size()) return tokens.get(n - 1);
    return tokens.get(n);
  }

  public int getCode(String token) {
    return parseInt(tableOfTokens.get(token));
  }

  public HashMap<String, String> getTableOfTokens() {
    return tableOfTokens;
  }

  public void setTableOfTokens(HashMap<String, String> tableOfTokens) {
    this.tableOfTokens = tableOfTokens;
  }

  public ObservableList<Ident> getIdents() {
    return idents;
  }

  public ObservableList<Token> getTokens() {
    return tokens;
  }

  public ObservableList<Const> getConsts() {
    return consts;
  }

  public String getProgramName() {
    return programName;
  }

  public void setProgramName(String programName) {
    this.programName = programName;
  }

  public File getProgramFile() {
    return programFile;
  }

  public void setProgramFile(File programFile) {
    this.programFile = programFile;
  }

  public Program cleanup() {
    tokens = FXCollections.observableArrayList();
    tableOfTokens = new HashMap<>();
    idents = FXCollections.observableArrayList();
    consts = FXCollections.observableArrayList();
    return this;
  }

  @Override
  public String toString() {
    return "Program{" +
            "sourceCode='" + sourceCode + '\'' +
            ", programName='" + programName + '\'' +
            ", programFile=" + programFile +
            ", tableOfTokens=" + tableOfTokens +
            ", tokens=" + tokens +
            ", idents=" + idents +
            ", consts=" + consts +
            '}';
  }

}
