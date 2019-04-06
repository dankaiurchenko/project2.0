package com.tischenko.controllers;

import com.tischenko.models.analyzers.saRelationTableBased.ExtendedStateDump;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class SyntaxTabController {

  // таблиця ходу розбору
  @FXML private TableView<ExtendedStateDump> analyzeFlowTable;
  @FXML private TableColumn<ExtendedStateDump, String> stackColumn2;
  @FXML private TableColumn<ExtendedStateDump, String> relationColumn;
  @FXML private TableColumn<ExtendedStateDump, String> inputColumn;
  @FXML private TableColumn<ExtendedStateDump, String> basisColumn;

  void show(ObservableList<ExtendedStateDump> stateDumps) {
    clearTable();
    if(stateDumps != null && !stateDumps.isEmpty()){
      analyzeFlowTable.setItems(stateDumps);
      stackColumn2.setCellValueFactory(new PropertyValueFactory<>("stack"));
      relationColumn.setCellValueFactory(new PropertyValueFactory<>("relation"));
      inputColumn.setCellValueFactory(new PropertyValueFactory<>("inputStream"));
      basisColumn.setCellValueFactory(new PropertyValueFactory<>("basis"));
    }
  }

  private void clearTable() {
    analyzeFlowTable.getItems().clear();
  }

}
