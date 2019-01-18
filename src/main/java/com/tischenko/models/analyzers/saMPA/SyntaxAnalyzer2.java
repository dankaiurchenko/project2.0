package com.tischenko.models.analyzers.saMPA;

import com.tischenko.models.Program;
import com.tischenko.models.analyzers.AbstractAnalyzer;
import com.tischenko.models.analyzers.SAException;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.Stack;

public class SyntaxAnalyzer2 extends AbstractAnalyzer {

  private int number = 0;
  private int currentToken = 0;
  private int currentState = 1;
  private final Stack<Integer> stack = new Stack<>();
  private final StatesController statesController;

  public SyntaxAnalyzer2(Program program, StatesController statesController) {
    super(program);
    this.statesController = statesController;
  }

  public boolean analyze(){
    loop:while(currentToken < program.getTokens().size()){
      program.addDumpState(new dumpState(++number));
      StatesController.Transition currentTransition = statesController.getTransition(currentState,
                      program.getTableOfTokens().get(String.valueOf(program.getToken(currentToken).getCode())));
      if(currentTransition!=null){
        switch (currentTransition.getBeta()) {
          case "error":
            addException(currentTransition.getErrorMessage());
            break loop;
          case "exit":
            if (currentToken == program.getTokens().size() - 1 && stack.empty()) {
              return true;
            } else if(!stack.empty()){
              currentState = stack.pop();
            } else {
              addException("End of program expected");
              break loop;
            }
            break;
          default:
            currentState = Integer.parseInt(currentTransition.getBeta());
            if(currentTransition.getStackMark() != 0){
              stack.push(currentTransition.getStackMark());
            }
            break;
        }
        if(!currentTransition.getMark().equals("void")){
          currentToken++;
           if(currentToken == program.getTokens().size() - 1) {
            addException("End of program expected");
          }
        }
      } else {
        addException("Cannot resolve symbol");
        break;
      }
    }
    return false;
  }

  private void addException(String message){
    exceptions.add(new SAException(program.getToken(currentToken), message, 1));
  }

  @SuppressWarnings("unused")
  public class dumpState{
    IntegerProperty number;
    IntegerProperty state;
    StringProperty token;
    StringProperty stackValue;

    dumpState(int number) {
      this.number = new SimpleIntegerProperty(number);
      this.state = new SimpleIntegerProperty(currentState);
      this.token = new SimpleStringProperty(program.getToken(currentToken).getToken());
      this.stackValue = new SimpleStringProperty(stack.toString());
    }

    public int getNumber() {
      return number.get();
    }

    public IntegerProperty numberProperty() {
      return number;
    }

    public int getState() {
      return state.get();
    }

    public IntegerProperty stateProperty() {
      return state;
    }

    public String getToken() {
      return token.get();
    }

    public StringProperty tokenProperty() {
      return token;
    }

    public String getStackValue() {
      return stackValue.get();
    }

    public StringProperty stackValueProperty() {
      return stackValue;
    }

    @Override
    public String toString() {
      return number.getValue() +
              "  " + state.getValue() +
              "  " + token.getValue() +
              "  " + stackValue.getValue() +
              '\n';
    }
  }
}
