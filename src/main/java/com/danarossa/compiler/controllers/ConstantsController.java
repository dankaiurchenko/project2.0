package com.danarossa.compiler.controllers;

import com.danarossa.compiler.models.Program;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class ConstantsController {
  private ObservableList<Program.Const> constants;


  // таблиця констант
  @FXML
  private TableView<Program.Const> constantsTable;
  @FXML private TableColumn<Program.Const, Integer> constantNumberColumn;
  @FXML private TableColumn<Program.Const, Number> constantValueColumn;

  ConstantsController(ObservableList<Program.Const> constants) {
    this.constants = constants;
  }

  void show(){
    if(!constants.isEmpty()){
      constantsTable.setItems(constants);
      constantNumberColumn.setCellValueFactory(new PropertyValueFactory<>("number"));
      constantValueColumn.setCellValueFactory(new PropertyValueFactory<>("token"));
    }
  }
}
