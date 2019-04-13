package com.tischenko.models.analyzers.poliz;

import com.tischenko.controllers.BIOException;
import com.tischenko.models.Ident;
import com.tischenko.models.analyzers.sa.LanguageSymbol;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.HashMap;
import java.util.LinkedList;

public abstract class AbstractPoliz {
  LinkedList<ProgramPart> poliz = new LinkedList<>();
  HashMap<String, Id> ids = new HashMap<>();

  public AbstractPoliz(ObservableList<Ident> ids) {
    for(Ident id : ids){
      this.ids.put(id.getToken(), new Id(id.getToken()));
    }
  }

  ObservableList<PolizStateDump> polizStateDumps = FXCollections.observableArrayList();

  public abstract void addToPoliz(LinkedList<LanguageSymbol> basis);

  @Override
  public String toString() {
    return poliz.toString();
  }

  public abstract void execute() throws BIOException;

  public ObservableList<PolizStateDump> getPolizStateDumps(){
    return polizStateDumps;
  }
}
