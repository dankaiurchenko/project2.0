package com.danarossa.compiler.controllers;

import com.danarossa.compiler.models.analyzers.syntactical2.SyntaxAnalyzer2;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class TransitionsController {
  private ObservableList<SyntaxAnalyzer2.dumpState> transitions;


  // таблиця переходів
  @FXML private TableView<SyntaxAnalyzer2.dumpState> transitionTable;
  @FXML private TableColumn<SyntaxAnalyzer2.dumpState, Integer> transitionNumberColumn;
  @FXML private TableColumn<SyntaxAnalyzer2.dumpState, Integer> transitionStateColumn;
  @FXML private TableColumn<SyntaxAnalyzer2.dumpState, String> transitionTokenColumn;
  @FXML private TableColumn<SyntaxAnalyzer2.dumpState, String> transitionStackValueColumn;

  TransitionsController(ObservableList<SyntaxAnalyzer2.dumpState> transitions) {
    this.transitions = transitions;
  }

  void show(){
//    System.out.println(program.getTransitionTable());
    if(!transitions.isEmpty()){
      //заповнюємо таблиці токенів, ід, конст
      transitionTable.setItems(transitions);
      transitionNumberColumn.setCellValueFactory(new PropertyValueFactory<>("number"));
      transitionStateColumn.setCellValueFactory(new PropertyValueFactory<>("state"));
      transitionTokenColumn.setCellValueFactory(new PropertyValueFactory<>("token"));
      transitionStackValueColumn.setCellValueFactory(new PropertyValueFactory<>("stackValue"));
    }
  }
}
