package com.danarossa.compiler.models.analyzers.lexical;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


class StatesController {
  private final transient ObservableList<Transition> transitionArrayList = FXCollections.observableArrayList();

  StatesController(){
    transitionArrayList.add(new Transition("1",  "L",  "2"));
    transitionArrayList.add(new Transition("1", "e",  "2"));
    transitionArrayList.add(new Transition("1", "N",  "3"));
    transitionArrayList.add(new Transition("1", ".", "6"));
    transitionArrayList.add(new Transition("1", "*", "7"));
    transitionArrayList.add(new Transition("1", "<", "8"));
    transitionArrayList.add(new Transition("1", ">", "9"));
    transitionArrayList.add(new Transition("1","=", "10"));
    transitionArrayList.add(new Transition("1", "!", "11"));
    transitionArrayList.add(new Transition("1", "OP", "token"));
    transitionArrayList.add(new Transition("1", ".", "4"));
    transitionArrayList.add(new Transition("1", "void", "error"));
    transitionArrayList.add(new Transition("2", "L", "2"));
    transitionArrayList.add(new Transition("2", "N", "2"));
    transitionArrayList.add(new Transition("2", "e", "2"));
    transitionArrayList.add(new Transition("2", "void", "Ident"));
    transitionArrayList.add(new Transition("3", "N", "3"));
    transitionArrayList.add(new Transition("3", "e", "4"));
    transitionArrayList.add(new Transition("3", "void", "Const"));
    transitionArrayList.add(new Transition("4", "N", "5"));
    transitionArrayList.add(new Transition("4", "void", "error"));
    transitionArrayList.add(new Transition("5", "N", "5"));
    transitionArrayList.add(new Transition("5", "void", "Const"));
    transitionArrayList.add(new Transition("6", ".", "token"));
    transitionArrayList.add(new Transition("6", "void", "error"));
    transitionArrayList.add(new Transition("7", "*", "token"));
    transitionArrayList.add(new Transition("7", "void", "token"));
    transitionArrayList.add(new Transition("8", "=", "token"));
    transitionArrayList.add(new Transition("8", "void", "token"));
    transitionArrayList.add(new Transition("9", "=", "token"));
    transitionArrayList.add(new Transition("9", "void", "token"));
    transitionArrayList.add(new Transition("10", "=", "token"));
    transitionArrayList.add(new Transition("10", "void", "token"));
    transitionArrayList.add(new Transition("11", "=", "token"));
    transitionArrayList.add(new Transition("11", "void", "error"));
  }

  private boolean isNumeric(String str)
  {
    return str.matches("^[-+]?\\d*$");
  }

  Transition getTransition(int alpha, String mark){
    //System.out.println(alpha + "   " + mark  );
    for(Transition transition: transitionArrayList){
      if(transition.alpha.getValue() == alpha && transition.mark.getValue().equals(mark)){
        return transition;
      }
    }
    for(Transition transition: transitionArrayList){
      if(transition.alpha.getValue() == alpha && transition.mark.getValue().equals("void")){
        return transition;
      }
    }
    return null;
  }

  public ObservableList<Transition> getTransitionArrayList() {
    return transitionArrayList;
  }

  @SuppressWarnings("unused")
  class Transition {
    private IntegerProperty alpha;
    private StringProperty mark;
    private StringProperty beta;

    Transition(String alpha, String mark, String beta) {
      this.alpha = new SimpleIntegerProperty(Integer.parseInt(alpha));
      this.mark = new SimpleStringProperty(mark);
      this.beta = new SimpleStringProperty(beta);
    }

    public int getAlpha() {
      return alpha.get();
    }

    public IntegerProperty alphaProperty() {
      return alpha;
    }

    public String getMark() {
      return mark.get();
    }

    public StringProperty markProperty() {
      return mark;
    }

    public String getBeta() {
      return beta.get();
    }

    public StringProperty betaProperty() {
      return beta;
    }



    @Override
    public String toString() {
      return "Transition " +
              "  " + alpha.getValue() +
              "  " + mark.getValue() +
              "  " + beta.getValue() +
              '}';
    }
  }

}
