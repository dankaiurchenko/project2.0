package com.tischenko.models.analyzers.saRelationTableBased;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Stack;

class Rule {
  private final LinkedList<LanguageSymbol> symbols;

  public Rule() {
    symbols = new LinkedList<>();
  }

  Rule(LinkedList<LanguageSymbol> basis) {
    this.symbols = new LinkedList<>();
    while(!basis.isEmpty()){
      this.symbols.add(basis.pop());
    }
  }

  public void addSymbolToRule(LanguageSymbol symbol){
    symbols.add(symbol);
  }

  private LinkedList<LanguageSymbol> getSymbols() {
    return symbols;
  }

  ArrayList<Relation> getEquals(){
    ArrayList<Relation> equalsInARule = new ArrayList<>();
    for(int i = 0; i < symbols.size() - 1 ; i++){
      equalsInARule.add(new Relation(symbols.get(i), symbols.get(i+1), RelationType.EQUAL));
    }
    return equalsInARule;
   }

  LanguageSymbol getFirstSymbol(){
    return symbols.get(0);
  }

  LanguageSymbol getLastSymbol(){
    return symbols.get(symbols.size() - 1);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Rule)) return false;
    Rule rule = (Rule) o;
    return Objects.equals(getSymbols(), rule.getSymbols());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getSymbols());
  }

  @Override
  public String toString() {
    StringBuilder string = new StringBuilder();
    for(LanguageSymbol l : symbols){
      string.append("   ").append(l.getStringRepresentation());
    }
    return string.toString();
  }
}
