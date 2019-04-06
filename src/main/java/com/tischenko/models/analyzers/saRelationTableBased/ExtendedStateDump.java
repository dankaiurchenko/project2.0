package com.tischenko.models.analyzers.saRelationTableBased;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Stack;

public class ExtendedStateDump extends StateDump {
  private final StringProperty basis;

  public ExtendedStateDump(Stack<LanguageSymbol> stack, RelationType relation,
                           LinkedList<LanguageSymbol> inputStream, String  basis) {
    super(stack, relation, inputStream);
    this.basis = new SimpleStringProperty(basis);
  }

  public String getBasis() {
    return basis.get();
  }

  public StringProperty basisProperty() {
    return basis;
  }

}
