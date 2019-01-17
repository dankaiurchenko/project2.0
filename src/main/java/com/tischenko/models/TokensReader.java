package com.tischenko.models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.HashMap;
import java.util.Scanner;

public class TokensReader {
  private transient ObservableList<Token> tableOfTokens = FXCollections.observableArrayList();
  private transient String tokensText;
  private transient HashMap<String, String> mapOfTokens = new HashMap<>();

  public TokensReader(Scanner scanner) {
    StringBuilder tokens = new StringBuilder();
    while (scanner.hasNext()) {
      String token = scanner.next();
      int tokenNumber = scanner.nextInt();
      tokens.append(token).append(System.getProperty("line.separator"));
      tokens.append(String.valueOf(tokenNumber)).append(System.getProperty("line.separator"));
      tableOfTokens.add(new Token (tokenNumber, token));
      mapOfTokens.put(Integer.toString(tokenNumber), token);
      mapOfTokens.put(token, Integer.toString(tokenNumber));
    }
    tokensText = tokens.toString();
  }

  public String getTokensText() {
    return tokensText;
  }

  public HashMap<String, String> getMapOfTokens() {
    return mapOfTokens;
  }

  public ObservableList<Token> getTableOfTokens() {
    return tableOfTokens;
  }

  @Override
  public String toString() {
    return "TokensReader{" +
            "tableOfTokens=" + tableOfTokens +
            ", tokensText='" + tokensText + '\'' +
            ", mapOfTokens=" + mapOfTokens +
            '}';
  }

  public class Token{
    private IntegerProperty code;
    private StringProperty token;

    public Token(int code, String token) {
      this.code = new SimpleIntegerProperty(code);
      this.token = new SimpleStringProperty(token);
    }

    public int getCode() {
      return code.get();
    }

    public IntegerProperty codeProperty() {
      return code;
    }

    public String getToken() {
      return token.get();
    }

    public StringProperty tokenProperty() {
      return token;
    }
  }
}
