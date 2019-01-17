package com.tischenko.controllers;

import com.tischenko.models.Program;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class IdentifiersController {
  private ObservableList<Program.Identifier> identifiers;

  // таблиця ідентифікаторів
  @FXML private TableView<Program.Identifier> identifiersTable;
  @FXML private TableColumn<Program.Identifier, Integer> identifierNumberColumn;
  @FXML private TableColumn<Program.Identifier, Integer> identifierNameColumn;
  @FXML private TableColumn<Program.Identifier, String> identifierTypeColumn;

  public IdentifiersController(ObservableList<Program.Identifier> identifiers) {
    this.identifiers = identifiers;
  }


  void show(){
    if(!identifiers.isEmpty()){
      identifiersTable.setItems(identifiers);
      identifierNameColumn.setCellValueFactory(new PropertyValueFactory<>("token"));
      identifierNumberColumn.setCellValueFactory(new PropertyValueFactory<>("number"));
      identifierTypeColumn.setCellValueFactory(new PropertyValueFactory<>("typeName"));
    }
  }
}
