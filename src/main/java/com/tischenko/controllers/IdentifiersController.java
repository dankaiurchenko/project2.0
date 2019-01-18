package com.tischenko.controllers;

import com.tischenko.models.Ident;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class IdentifiersController {
  private final ObservableList<Ident> identifiers;

  // таблиця ідентифікаторів
  @FXML
  private TableView<Ident> identifiersTable;
  @FXML
  private TableColumn<Ident, Integer> identifierNumberColumn;
  @FXML
  private TableColumn<Ident, Integer> identifierNameColumn;
  @FXML
  private TableColumn<Ident, String> identifierTypeColumn;

  IdentifiersController(ObservableList<Ident> identifiers) {
    this.identifiers = identifiers;
  }

  void show() {
    if (!identifiers.isEmpty()) {
      identifiersTable.setItems(identifiers);
      identifierNameColumn.setCellValueFactory(new PropertyValueFactory<>("token"));
      identifierNumberColumn.setCellValueFactory(new PropertyValueFactory<>("number"));
      identifierTypeColumn.setCellValueFactory(new PropertyValueFactory<>("typeName"));
    }
  }
}
