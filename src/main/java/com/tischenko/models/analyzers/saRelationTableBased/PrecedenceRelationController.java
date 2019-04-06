package com.tischenko.models.analyzers.saRelationTableBased;

import com.tischenko.controllers.BIOException;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class PrecedenceRelationController {

  private final ArrayList<LanguageSymbol> parsedGrammar = new ArrayList<>();
  private final ArrayList<Relation> relationList = new ArrayList<>();
  private final LinkedHashMap<String, LinkedHashMap<String, String>> relationTable = new LinkedHashMap<>();
  private int maxTokenLength;

  public PrecedenceRelationController(BufferedReader reader) throws BIOException, IOException {
    GrammarParser parser = new GrammarParser(this);
    LanguageSymbol newSymbol;
    for (String languageRule = reader.readLine(); languageRule != null; languageRule = reader.readLine()) {
      if (!languageRule.trim().isEmpty()) {
        String name = parser.getName(languageRule);
        newSymbol = getSymbol(name);
        parser.parseSymbol(languageRule, newSymbol);
        setMaxTokenLength(name);
      }
    }
    parsedGrammar.add(new LanguageSymbol("#"));
    initializeRelationTable();
  }

  public int getMaxTokenLength() {
    return maxTokenLength;
  }

  private void setMaxTokenLength(String string) {
    this.maxTokenLength = string.length();
  }

  private void initializeRelationTable() {
    ArrayList<Relation> equalRelationTable = defineEqualRelationTable();

    defineLessTokenRelation(equalRelationTable);

    defineGreaterRelation(equalRelationTable);

    convertIntoHashMap();
  }

  private void defineGreaterRelation(ArrayList<Relation> equalRelationTable) {
    RelationType demandedRelation = RelationType.GREATER;
    for (Relation relation : equalRelationTable) {
      LanguageSymbol firsSymbol = relation.getFirstSymbol();
      LanguageSymbol secondSymbol = relation.getSecondSymbol();
      if (relation.getFirstSymbol().notTerminal()) {
        for (LanguageSymbol lastPlusFromFirstSymbol : firsSymbol.getLastPlus()) {
          Relation greaterRelation = new Relation(lastPlusFromFirstSymbol, secondSymbol, demandedRelation);
          tryToAddToTable(demandedRelation, lastPlusFromFirstSymbol, secondSymbol, greaterRelation);
          for (LanguageSymbol firstPlusFromSecondSymbol : secondSymbol.getFirstPlus()) {
            Relation anotherGreaterRelation = new Relation(lastPlusFromFirstSymbol, firstPlusFromSecondSymbol, demandedRelation);
            tryToAddToTable(demandedRelation, lastPlusFromFirstSymbol, firstPlusFromSecondSymbol, anotherGreaterRelation);
          }
        }
      }
    }
  }

  private void defineLessTokenRelation(ArrayList<Relation> equalRelationTable) {
    RelationType demandedRelation = RelationType.LESS;
    for (Relation relation : equalRelationTable) {
      LanguageSymbol firsSymbol = relation.getFirstSymbol();
      LanguageSymbol secondSymbol = relation.getSecondSymbol();
      if (relation.getSecondSymbol().notTerminal()) {
        for (LanguageSymbol firstPlusFromSecondSymbol : secondSymbol.getFirstPlus()) {
          Relation lessRelation = new Relation(firsSymbol, firstPlusFromSecondSymbol, demandedRelation);
          tryToAddToTable(demandedRelation, firsSymbol, firstPlusFromSecondSymbol, lessRelation);
        }
      }
    }
  }

  private void tryToAddToTable(RelationType demandedRelationType, LanguageSymbol firstSymbol,
                               LanguageSymbol secondSymbol, Relation relation) {
    if (relationList.contains(relation) && getRelationType(relation) != demandedRelationType) {
      System.out.println("Language is not determined (symbol : " + firstSymbol +
              getRelationType(relation).toString() + " and " + demandedRelationType + " then " + secondSymbol);
    } else {
      addToTable(relation);
    }
  }

  private void addToTable(Relation greaterRelation) {
    if (!relationList.contains(greaterRelation))
      relationList.add(greaterRelation);
  }

  private ArrayList<Relation> defineEqualRelationTable() {
    ArrayList<Relation> equalRelationTable = new ArrayList<>();
    for (LanguageSymbol symbol : parsedGrammar) {
      addRelationsWithSharp(symbol);
      ArrayList<Relation> equals = symbol.getEqualRelationList();
      if (equals != null) {
        equalRelationTable.addAll(equals);
        relationList.addAll(equals);
      }
    }
    return equalRelationTable;
  }

  private void addRelationsWithSharp(LanguageSymbol symbol) {
    relationList.add(new Relation(getSymbol("#"), symbol, RelationType.LESS));
    relationList.add(new Relation(symbol, getSymbol("#"), RelationType.GREATER));
  }

  private RelationType getRelationType(Relation relation) {
    return relationList.get(relationList.indexOf(relation)).getRelationType();
  }

  RelationType getRelationType(LanguageSymbol firstSymbol, LanguageSymbol secondSymbol) {
    int index = relationList.indexOf(new Relation(firstSymbol, secondSymbol));
    if (index != -1) {
      return relationList.get(index).getRelationType();
    } else return RelationType.UNDEFINED;
  }

  LanguageSymbol getParent(Rule rule) {
    for (LanguageSymbol languageSymbol : parsedGrammar) {
      if (languageSymbol.hasRule(rule)) {
        return languageSymbol;
      }
    }
    return null;
  }

  LanguageSymbol getSymbol(String symbolSTR) {
    LanguageSymbol symbol = new LanguageSymbol(symbolSTR);
    if (parsedGrammar.contains(symbol)) {
      return parsedGrammar.get(parsedGrammar.indexOf(symbol));
    } else {
      parsedGrammar.add(symbol);
      return symbol;
    }
  }

  LanguageSymbol getAxiom() {
    return parsedGrammar.get(0);
  }

  public boolean relationsIsEmpty() {
    return relationList.isEmpty();
  }


  private void convertIntoHashMap() {
    for (LanguageSymbol first : parsedGrammar) {
      LinkedHashMap<String, String> relationsForFirst = new LinkedHashMap<>();
      for (LanguageSymbol second : parsedGrammar) {
        relationsForFirst.put(second.getStringRepresentation(),
                getRelationType(first, second).getSign());
      }
      relationTable.put(first.getStringRepresentation(), relationsForFirst);
    }

  }

  public LinkedHashMap<String, LinkedHashMap<String, String>> getRelationTable() {
    return relationTable;
  }
}