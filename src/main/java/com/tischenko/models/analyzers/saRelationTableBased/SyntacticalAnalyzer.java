package com.tischenko.models.analyzers.saRelationTableBased;

import com.tischenko.models.Program;
import com.tischenko.models.Token;
import com.tischenko.models.analyzers.AbstractAnalyzer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Stack;

public class SyntacticalAnalyzer extends AbstractAnalyzer {
  private final PrecedenceRelationController precedenceRelationController;
  private final Stack<LanguageSymbol> stack = new Stack<>();
  private final ObservableList<ExtendedStateDump> stateDumps  = FXCollections.observableArrayList();


  public SyntacticalAnalyzer(Program program,
                             PrecedenceRelationController precedenceRelationController) {
    super(program);
    this.precedenceRelationController = precedenceRelationController;
    stack.push(new LanguageSymbol("#"));
  }


  @Override
  public boolean analyze() {
    LinkedList<LanguageSymbol> tokens = newQueue();
    LinkedList<LanguageSymbol> basis = new LinkedList<>();
    String basisString = "";
    loop:while(!tokens.isEmpty()){
      RelationType relationType = getRelationType(stack.peek(), tokens.getFirst());
      stateDumps.add(new ExtendedStateDump(stack, relationType, tokens, basisString));
      basisString = "";
      switch (relationType){
        case LESS:
        case EQUAL: stack.push(tokens.removeFirst()); break;
        case GREATER:{
          LanguageSymbol s = stack.pop();
          while(getRelationType(stack.peek(),s).equals(RelationType.EQUAL)){
            basis.push(s);
            s = stack.pop();
          }
          if(getRelationType(stack.peek(),s).equals(RelationType.LESS)){
            basis.push(s);
            if (isSuccessConditionTrue(basis, tokens)) {
              return true;
            }
            basisString = getString(basis);
            LanguageSymbol parent = precedenceRelationController.getParent(new Rule(basis));
              if(parent != null){
                stack.push(parent);
            } else{
              addException("Can't replace the basis with a token (invalid grammar)", stack.peek());
              break loop;
            }
          } else {
            addException("Invalid sequence of symbols (no basis found)", stack.peek());
            break loop;
          }
          break;
        }
        case UNDEFINED: {
          addException("No relationType between two symbols found"+ stack.peek() + "   " , tokens.getFirst());
          break loop;
        }
      }
    }
    return false;
  }

  private String getString(LinkedList<LanguageSymbol> basis) {
    Iterator iterator = basis.iterator();
    StringBuilder s = new StringBuilder();
    while(iterator.hasNext()){
      s.append(iterator.next());
    }
    return s.toString();

  }

  public ObservableList<ExtendedStateDump> getStateDumps() {
    return stateDumps;
  }

  private LinkedList<LanguageSymbol> newQueue() {
    LinkedList<LanguageSymbol> list = new LinkedList<>();
    for(Token token : program.getTokens()){
      list.add(new LanguageSymbol(token));
    }
    list.add(new LanguageSymbol("#"));
    return list;
  }

  private void addException(String message, LanguageSymbol basicLastSymbol){
    exceptions.add(new SyntacticalAnalyzerException(message, basicLastSymbol.getLine(), basicLastSymbol.getStringRepresentation(), "Syntactical analyzer"));
  }

  private boolean isSuccessConditionTrue(LinkedList basis, LinkedList tokens){
    return basis.size() == 1 && basis.peek().equals(precedenceRelationController.getAxiom()) &&
            stack.size() == 1 && tokens.size() == 1;
  }

  private RelationType getRelationType(LanguageSymbol first, LanguageSymbol second){
    return precedenceRelationController.getRelationType(first, second);
  }
}
