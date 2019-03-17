package com.tischenko.models.analyzers.saRelationTableBased;


import com.tischenko.controllers.BIOException;

import java.util.ArrayList;

class GrammarParser {
  private final PrecedenceRelationController precedenceRelationController;



  public GrammarParser(PrecedenceRelationController precedenceRelationController) {
    this.precedenceRelationController = precedenceRelationController;
  }

  public String getName(String line){
    return line.split("::=")[0].trim();
  }

  public void parseSymbol(String line, LanguageSymbol symbol) throws BIOException {
//    new PrintStream(System.out, true, StandardCharsets.UTF_8).println(line);
    String[] rules;
    String[] nameAndRule = line.split("::=");
    if(nameAndRule.length == 2){
      rules = nameAndRule[1].trim().split("\\|");
      if(!symbol.hasNoRules()){
        throw new BIOException("Symbol is doubled");
      }
      symbol.setRules(parseRules(rules));
      symbol.setTerminal(false);
    } else throw new BIOException("Invalid grammar rule");
  }



  private ArrayList<Rule> parseRules(String[] rules) {
    ArrayList<Rule> parsedRules =  new ArrayList<>();
    LanguageSymbol symbol;
    String[] tokensInARule;
    for(String oneRule : rules){
      tokensInARule = oneRule.trim().split("[ ]+");
      Rule newRule = new Rule();
      for(String symbolSTR : tokensInARule){
        symbol = precedenceRelationController.getSymbol(symbolSTR);
        newRule.addSymbolToRule(symbol);
      }
      parsedRules.add(newRule);
    }
    return parsedRules;
  }

}
