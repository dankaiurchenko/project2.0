package com.tischenko.models.analyzers.sa;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.LinkedList;
import java.util.Stack;

@SuppressWarnings("unused")
public class StateDump {
  private final StringProperty stack;
  private final StringProperty relation;
  private final StringProperty inputStream;

  StateDump(Stack<LanguageSymbol> stack, RelationType relation, LinkedList<LanguageSymbol> inputStream) {
    this.stack = new SimpleStringProperty(stack.toString());
    this.relation = new SimpleStringProperty(relation.getSign());
    this.inputStream = new SimpleStringProperty(inputStream.toString());
  }


  public String getStack() {
    return stack.get();
  }

  public StringProperty stackProperty() {
    return stack;
  }

  public String getRelation() {
    return relation.get();
  }

  public StringProperty relationProperty() {
    return relation;
  }

  public String getInputStream() {
    return inputStream.get();
  }

  public StringProperty inputStreamProperty() {
    return inputStream;
  }
}
