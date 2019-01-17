package com.tischenko.controllers;

import com.tischenko.models.Program;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class TokensController {

  private ObservableList<Program.Token> tokens;

  //таблиця розпарсених лексем
  @FXML private TableView<Program.Token> tokensTable;
  @FXML private TableColumn<Program.Token, Integer> tokenNumberColumn;
  @FXML private TableColumn<Program.Token, Integer> tokenLineColumn;
  @FXML private TableColumn<Program.Token, String>  tokenTokenColumn;
  @FXML private TableColumn<Program.Token, Integer> tokenCodeColumn;
  @FXML private TableColumn<Program.Token, Integer> tokenConstantCodeColumn;
  @FXML private TableColumn<Program.Token, Integer> tokenIdentifierCodeColumn;


  TokensController(ObservableList<Program.Token> tokens) {
    this.tokens = tokens;
  }

  void show(){
    if(!tokens.isEmpty()){
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
