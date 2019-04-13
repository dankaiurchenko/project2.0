package com.tischenko.models.analyzers.poliz;

import com.tischenko.controllers.BIOException;
import com.tischenko.models.Ident;
import com.tischenko.models.analyzers.sa.LanguageSymbol;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Stack;

public class AdvancedPoliz extends AbstractPoliz {
  protected Stack<Valuable> stack = new Stack<>();
  private ArrayList<String> operators = new ArrayList<>();
  private HashMap<String, function> operations = new HashMap<>();

  {
    operators.add("+");
    operators.add("-");
    operators.add("/");
    operators.add("*");
    operators.add("**");
    operations.put("+", (a, b) -> a.getValue() + b.getValue());
    operations.put("-", (a, b) -> a.getValue() - b.getValue());
    operations.put("*", (a, b) -> a.getValue() * b.getValue());
    operations.put("/", (a, b) -> a.getValue() / b.getValue());
    operations.put("**", (a, b) -> Math.pow(a.getValue(), b.getValue()));
  }

  public AdvancedPoliz(ObservableList<Ident> ids) {
    super(ids);
  }

  public void addToPoliz(LinkedList<LanguageSymbol> basis) {
    if (basis.size() == 1 && basis.peek().getStringRepresentation().equals("Const")) {
      poliz.add(new Const(Double.parseDouble(basis.peek().getToken().getToken())));
    } else if (basis.size() == 1 && basis.peek().getStringRepresentation().equals("Ident")) {
      poliz.add(ids.get(basis.peek().getToken().getToken()));
    } else if (basis.size() == 2 && basis.peekFirst().getStringRepresentation().equals("-")) {
      poliz.add(new Operator("@"));
    } else if (basis.size() == 3) {
      LanguageSymbol a = basis.pop();
      LanguageSymbol b = basis.pop();
      if (b.getStringRepresentation().equals("=")) {
        poliz.add(ids.get(a.getToken().getToken()));
        poliz.add(new Operator(b.getStringRepresentation()));
      } else {
        if (operators.contains(b.getStringRepresentation())) {
          poliz.add(new Operator(b.getToken().getToken()));
        }
      }
    }
  }

  @Override
  public void execute() throws BIOException {
    try {
      int i = 0;
      while (poliz.size() > 0) {
        polizStateDumps.add(new PolizStateDump(++i, stack, this.toString()));
        ProgramPart token = poliz.removeFirst();
        if (!(token instanceof Operator)) {
          stack.push((Valuable) token);
        } else if (((Operator) token).operator.equals("=")) {
          Valuable a = stack.pop();
          Valuable b = stack.pop();
          ids.get(((Id) a).getName()).setValue(b.getValue());
        } else if(((Operator) token).operator.equals("@")){
          stack.push(getInvertedValuable(stack.pop()));
        }else  {
          Valuable b = stack.pop();
          Valuable a = stack.pop();
          stack.push(new Const(operations.get(((Operator) token).getOperator()).doIt(a, b)));
        }
      }
      for (HashMap.Entry<String, Id> id : ids.entrySet()) {
        System.out.println(id.getKey() + " " + (int) id.getValue().getValue());
      }
    } catch (Exception e) {
      throw new BIOException("Error while executing poliz");
    }
  }

  private Valuable getInvertedValuable( Valuable a ) {
    if(a instanceof Id){
      ((Id) a).setValue(- a.getValue());
      return a;
    }else{
      return new Const(- a.getValue());
    }
  }


}

