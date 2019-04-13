package com.tischenko.models.analyzers.sa;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.LinkedList;
import java.util.Stack;

public class ExtendedStateDump extends StateDump {
  private final StringProperty basis;
  private StringProperty poliz;

  public ExtendedStateDump(Stack<LanguageSymbol> stack, RelationType relation,
                           LinkedList<LanguageSymbol> inputStream, String  basis,  String poliz) {
    super(stack, relation, inputStream);
    this.basis = new SimpleStringProperty(basis);
    this.poliz = new SimpleStringProperty(poliz);
  }

  public String getBasis() {
    return basis.get();
  }

  public StringProperty basisProperty() {
    return basis;
  }

  public String getPoliz() {
    return poliz.get();
  }

  public StringProperty polizProperty() {
    return poliz;
  }
}
