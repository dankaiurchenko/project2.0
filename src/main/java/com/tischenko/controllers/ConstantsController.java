package com.tischenko.controllers;

import com.tischenko.models.Const;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

@SuppressWarnings("ALL")
public class ConstantsController {
  private final ObservableList<Const> constants;

  // таблиця констант
  @FXML
  private TableView<Const> constantsTable;
  @FXML
  private TableColumn<Const, Integer> constantNumberColumn;
  @FXML
  private TableColumn<Const, Number> constantValueColumn;

  ConstantsController(ObservableList<Const> constants) {
    this.constants = constants;
  }

  void show() {
    if (!constants.isEmpty()) {
      constantsTable.setItems(constants);
      constantNumberColumn.setCellValueFactory(new PropertyValueFactory<>("number"));
      constantValueColumn.setCellValueFactory(new PropertyValueFactory<>("token"));
    }
  }
}
