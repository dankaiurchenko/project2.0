package com.tischenko.models.analyzers.poliz;

import com.tischenko.models.Ident;
import com.tischenko.models.analyzers.sa.LanguageSymbol;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Stack;

public class AdvancedPoliz extends AbstractPoliz {
  private ArrayList<String> operators = new ArrayList<>();
  private HashMap<String, function> operations = new HashMap<>();
  protected Stack<Valuable> stack = new Stack<>();
  private LinkedList<ProgramPart> arithmeticalPart = new LinkedList<>();

  {
    operators.add("+");
    operators.add("-");
    operators.add("/");
    operators.add("*");
    operations.put("+", (a, b) -> a.getValue() + b.getValue());
    operations.put("-", (a, b) -> a.getValue() - b.getValue());
    operations.put("*", (a, b) -> a.getValue() * b.getValue());
    operations.put("/", (a, b) -> a.getValue() / b.getValue());
  }

  public AdvancedPoliz(ObservableList<Ident> ids) {
    super(ids);
  }

  public void addToPoliz(LinkedList<LanguageSymbol> basis) {
    if (basis.size() == 1 && basis.peek().getStringRepresentation().equals("Const")) {
      arithmeticalPart.add(new Const(Double.parseDouble(basis.peek().getToken().getToken())));
    } else if (basis.size() == 1 && basis.peek().getStringRepresentation().equals("Ident")) {
      arithmeticalPart.add(ids.get(basis.peek().getToken().getToken()));
    } else if (basis.size() == 3) {
      LanguageSymbol a = basis.pop();
      LanguageSymbol b = basis.pop();
      if (b.getStringRepresentation().equals("=")){
        poliz.add(ids.get(a.getToken().getToken()));
        while(arithmeticalPart.size() > 0){
          poliz.add(arithmeticalPart.removeFirst());
        }
        poliz.add(new Operator(b.getStringRepresentation()));
      }else {
        if (operators.contains(b.getStringRepresentation())) {
          arithmeticalPart.add(new Operator(b.getToken().getToken()));
        }
      }
    }
  }

  @Override
  public void execute() {
    int i = 0;
    while(poliz.size() > 0){
      ProgramPart token = poliz.removeFirst();
      polizStateDumps.add(new PolizStateDump(++i, stack, this.toString()));
      if (!(token instanceof Operator)) {
        stack.push((Valuable)token);
      } else if(((Operator) token).operator.equals("=")){
        Valuable b = stack.pop();
        Valuable a = stack.pop();
        ids.get(((Id)a).getName()).setValue(b.getValue());
//        ((Id)a).setValue(b.getValue());
      } else{
        Valuable b = stack.pop();
        Valuable a = stack.pop();
        stack.push(new Const(operations.get(((Operator) token).getOperator()).doIt(a, b)));
      }
    }
    for(HashMap.Entry<String, Id> id: ids.entrySet()){
      System.out.println(id.getKey() + " "  + id.getValue().getValue());
    }

//    result = stack.peek().getValue();
//    polizStateDumps.add(new PolizStateDump(++i, String.valueOf(result)));
  }


  @Override
  public double getResult() {
    return 0;
  }


}

