package com.tischenko.controllers;

import com.tischenko.models.analyzers.CompilerException;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class ExceptionsController {

  private ObservableList<CompilerException> exceptions;

  // таблиця виключень
  @FXML private TableView<CompilerException> exceptionsTable;
  @FXML private TableColumn<CompilerException, Integer> exceptionNumberColumn;
  @FXML private TableColumn<CompilerException, String> exceptionMessageColumn;
  @FXML private TableColumn<CompilerException, Integer> exceptionLineColumn;
  @FXML private TableColumn<CompilerException, String> exceptionTokenColumn;
  @FXML private TableColumn<CompilerException, String> exceptionAnalyzerColumn;


  ExceptionsController(ObservableList<CompilerException> exceptions) {
    this.exceptions = exceptions;
  }

  void show(){
    exceptionsTable.setItems(exceptions);
    exceptionNumberColumn.setCellValueFactory(new PropertyValueFactory<>("number"));
    exceptionMessageColumn.setCellValueFactory(new PropertyValueFactory<>("message"));
    exceptionLineColumn.setCellValueFactory(new PropertyValueFactory<>("line"));
    exceptionAnalyzerColumn.setCellValueFactory(new PropertyValueFactory<>("analyzer"));
    exceptionTokenColumn.setCellValueFactory(new PropertyValueFactory<>("token"));
  }
}
