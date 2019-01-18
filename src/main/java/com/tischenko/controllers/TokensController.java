package com.tischenko.controllers;

import com.tischenko.models.Token;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class TokensController {

  private final ObservableList<Token> tokens;

  //таблиця розпарсених лексем
  @FXML
  private TableView<Token> tokensTable;
  @FXML
  private TableColumn<Token, Integer> tokenNumberColumn;
  @FXML
  private TableColumn<Token, Integer> tokenLineColumn;
  @FXML
  private TableColumn<Token, String> tokenTokenColumn;
  @FXML
  private TableColumn<Token, Integer> tokenCodeColumn;
  @FXML
  private TableColumn<Token, Integer> tokenConstantCodeColumn;
  @FXML
  private TableColumn<Token, Integer> tokenIdentifierCodeColumn;


  TokensController(ObservableList<Token> tokens) {
    this.tokens = tokens;
  }

  void show() {
    if (!tokens.isEmpty()) {
      //заповнюємо таблиці токенів, ід, конст
      tokensTable.setItems(tokens);
      tokenCodeColumn.setCellValueFactory(new PropertyValueFactory<>("code"));
      tokenConstantCodeColumn.setCellValueFactory(new PropertyValueFactory<>("constantCode"));
      tokenIdentifierCodeColumn.setCellValueFactory(new PropertyValueFactory<>("idCode"));
      tokenLineColumn.setCellValueFactory(new PropertyValueFactory<>("line"));
      tokenNumberColumn.setCellValueFactory(new PropertyValueFactory<>("number"));
      tokenTokenColumn.setCellValueFactory(new PropertyValueFactory<>("token"));
    }
  }
}
