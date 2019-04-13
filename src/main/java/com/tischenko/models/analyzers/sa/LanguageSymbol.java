package com.tischenko.models.analyzers.sa;

import com.tischenko.models.Token;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;

public class LanguageSymbol {

  private final String stringRepresentation;
  private boolean isTerminal;
  private ArrayList<Rule> rules;
  private final ArrayList<LanguageSymbol> firstPlus = new ArrayList<>();
  private final ArrayList<LanguageSymbol> lastPlus = new ArrayList<>();
  private int line;
  private Token token;

  LanguageSymbol(String stringRepresentation) {
    this.stringRepresentation = stringRepresentation;
    this.isTerminal = true;
    this.rules = new ArrayList<>();
    this.token = new Token(stringRepresentation, 0);
  }

  LanguageSymbol(Token token) {
    if(token.getCode() == 39) {
      this.stringRepresentation = "Const";
    } else if (token.getCode() == 38){
      this.stringRepresentation = "Ident";
    } else {
      this.stringRepresentation = token.getToken();
    }
    this.isTerminal = true;
    this.rules = new ArrayList<>();
    this.line = token.getLine();
    this.token = token;
  }

  int getLine() {
    return line;
  }

  void setRules(ArrayList<Rule> rules) {
    this.rules = rules;
  }

  boolean hasNoRules(){
    return rules == null || rules.isEmpty();
  }

  @SuppressWarnings("SameParameterValue")
  void setTerminal(boolean terminal) {
    isTerminal = terminal;
  }

  boolean notTerminal() {
    return !isTerminal;
  }

  ArrayList<LanguageSymbol> getLastPlus(){
    if(lastPlus.isEmpty()){
      setLastPlus();
    }//
    return lastPlus;
  }

  ArrayList<LanguageSymbol> getFirstPlus(){
    if(firstPlus.isEmpty()){
      setFirstPlus();
    }
    return firstPlus;
  }

  //коли знайшли основу
  boolean hasRule(Rule rule){
    for(Rule r : this.rules){
      if(r.equals(rule)){
        return true;
      }
    }
    return false;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof LanguageSymbol)) return false;
    LanguageSymbol that = (LanguageSymbol) o;
    return Objects.equals(stringRepresentation, that.stringRepresentation);
  }

  @Override
  public int hashCode() {
    return Objects.hash(stringRepresentation);
  }

  public String getStringRepresentation() {
    return stringRepresentation;
  }

  @Override
  public String toString() {
    return " " + stringRepresentation + " "; //+ '\'' +
    //", isTerminal=" + isTerminal;// +
//            ", rules=" + rules;
  }

  ArrayList<Relation> getEqualRelationList(){
    ArrayList<Relation> allEqualsInRules = new ArrayList<>();
    for(Rule rule : rules){
      allEqualsInRules.addAll(rule.getEquals());
    }
    return allEqualsInRules;
  }

  private ArrayList<LanguageSymbol> getLast(){
    ArrayList<LanguageSymbol> last = new ArrayList<>();
    for(Rule rule : rules){
      last.add(rule.getLastSymbol());
    }
    return last;
  }

  private ArrayList<LanguageSymbol> getFirst(){
    ArrayList<LanguageSymbol> first = new ArrayList<>();
    for(Rule rule : rules){
      first.add(rule.getFirstSymbol());
    }
    return first;
  }

  private void setFirstPlus() {
    recursiveFirst(new HashSet<>(), this.firstPlus);
//    System.out.println("FirstPlus   " + this.stringRepresentation + "    " + firstPlus);
  }

  private void setLastPlus() {
    recursiveLast(new HashSet<>(), this.lastPlus);
//    System.out.println("LastPlus   " + this.stringRepresentation + "    " + lastPlus);
  }

  private void recursiveLast(HashSet<LanguageSymbol> visitedSymbols, ArrayList<LanguageSymbol> added){
    if(visitedSymbols.contains(this)){
      return;
    }
    visitedSymbols.add(this);
    for(LanguageSymbol l : this.getLast()){
      if(!added.contains(l)){
        added.add(l);
        if(!visitedSymbols.contains(l)){
          l.recursiveLast(visitedSymbols, added);
        }
      }
    }
  }

  private void recursiveFirst(HashSet<LanguageSymbol> visitedSymbols, ArrayList<LanguageSymbol> added){
    if(visitedSymbols.contains(this)){
      return;
    }
    visitedSymbols.add(this);
    for(LanguageSymbol l : this.getFirst()){
      if(!added.contains(l)){
        added.add(l);
        if(!visitedSymbols.contains(l)){
          l.recursiveFirst(visitedSymbols, added);
        }
      }
    }
  }

  public Token getToken() {
    return token;
  }
}