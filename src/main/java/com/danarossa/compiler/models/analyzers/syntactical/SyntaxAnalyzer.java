package com.danarossa.compiler.models.analyzers.syntactical;

import com.danarossa.compiler.models.Program;
import com.danarossa.compiler.models.analyzers.AbstractAnalyzer;


public class SyntaxAnalyzer extends AbstractAnalyzer {
  private int currentToken = 0;
  private int number = 0;

  public SyntaxAnalyzer(Program program) {
    super(program);
  }

  public boolean analyze() {
    if (nextTokenEqualTo("Program")) {
      if (nextTokenEqualTo("Ident")) {
        if (analyzeDeclarationList()) {
          if (nextTokenEqualTo("{")) {
            if (analyzeOperatorsList()) {
              if (nextTokenEqualTo("}")) {
                //System.out.println("current token " + currentToken + " size  " + program.getTokens().size());
                if (currentToken == program.getTokens().size()) {
                  return true;
                } else addException("End of program expected");
              } else addException("The ending curve brace is missing");
            } else addException("There is the problem in operators list");
          } else addException("The beginning curve brace is missing");
        } else addException("There is the problem in declaration list");
      } else addException("The name of the program missing");
    } else addException("A 'Program' at the beginning missing");
    return false;
  }

  private boolean analyzeDeclarationList() {
    if (analyzeDeclaration()) {
      while (nextTokenEqualTo(";")) {
        if (!analyzeDeclaration()) {
          addException("Last declaration is missing");
          return false;
        }
      }
      return true;
    } else addException("Variables declaration is missing");
    return false;
  }

  private boolean analyzeDeclaration() {
    if (analyzeType()) {
      if (analyzeIdentList()) {
        return true;
      } else addException("There is problem in identifier list");
    } else addException("There is problem with variable type");
    return false;
  }

  private boolean analyzeIdentList() {
    if (nextTokenEqualTo("Ident")) {
      while (nextTokenEqualTo(",")) {
        if (!nextTokenEqualTo("Ident")) {
          addException("Last identifier is missing");
          return false;
        }
      }
      return true;
    } else addException("An identifier is missing");
    return false;
  }

  private boolean analyzeType() {
    if (nextTokenEqualTo("integer")) {
      return true;
    } else if (nextTokenEqualTo("short")) {
      return true;
    } else return nextTokenEqualTo("label");
  }

  private boolean analyzeOperatorsList() {
    if (analyzeOperator()) {
      while (nextTokenEqualTo(";")) {
        if (!analyzeOperator()) {
          addException("Last operator is missing");
          return false;
        }
      }
      return true;
    } else addException("An operator is missing");
    return false;
  }

  private boolean analyzeOperator() {
    if (analyzeLoop()) {
      return true;
    } else if (analyzeCondition()) {
      return true;
    } else if (analyzeAssignmentOrTagging()) {
      return true;
    } else if (analyzeInput()) {
      return true;
    } else if (analyzeTagging()) {
      return true;
    } else if (analyzeUnconditionalTransition()) {
      return true;
    } else if (analyzeRange()) {
      return true;
    } else if (analyzeOutput()) {
      return true;
    } else addException("There is a problem with operator");
    return false;
  }

  private boolean analyzeRange() {
    if(analyzeExp()){
      if(nextTokenEqualTo("..")){
        if(analyzeExp()){
          return true;
        }else addException("An expression problem");
      }else addException("An '..' is missing");
    }
    return false;
  }

  private boolean analyzeUnconditionalTransition() {
    if(nextTokenEqualTo("goto")){
      if(nextTokenEqualTo("Ident")){
        return true;
      } else addException("An label identifier is missing");
    }
    return false;
  }

  private boolean analyzeTagging() {
    if(nextTokenEqualTo("Ident")){
      if(nextTokenEqualTo(":")){
        return true;
      }else addException("An ':' is missing");
    }
    return false;
  }

  private boolean analyzeOutput() {
    if (nextTokenEqualTo("writeLine")) {
      if (nextTokenEqualTo("(")) {
        if (analyzeIdentList()) {
          if (nextTokenEqualTo(")")) {
            return true;
          } else addException("The ending brace is missing");
        } else addException("There is a problem in identifier list");
      } else addException("The beginning brace is missing");
    }
    return false;
  }

  private boolean analyzeInput() {
    if (nextTokenEqualTo("readLine")) {
      if (nextTokenEqualTo("(")) {
        if (analyzeIdentList()) {
          if (nextTokenEqualTo(")")) {
            return true;
          } else addException("The ending brace is missing");
        } else addException("There is problem in identifier list ");
      } else addException("The beginning curve brace is missing");
    }
    return false;
  }


  private boolean analyzeAssignmentOrTagging() {
    //TODO навзви ексепшенів
    if (nextTokenEqualTo("Ident")) {
      if (nextTokenEqualTo("=")) {
        if (analyzeExp()) {
          return true;
        } else addException("Expression problem");
      } else if(nextTokenEqualTo(":")) {
        return true;
      } else addException("An ':' or '=' is missing");
    }
    return false;
  }

  private boolean analyzeExp() {
    boolean minus = nextTokenEqualTo("-");
    if (analyzeTerm()) {
      while (nextTokenEqualTo("+") || nextTokenEqualTo("-")) {
        if (!analyzeTerm()) {
          addException("There is an expression problem");
          return false;
        }
      }
      return true;
    }
    if (minus) addException("minus is not needed");
    return false;
  }

  private boolean analyzeTerm() {
    if (analyzeMulti()) {
      while (nextTokenEqualTo("*") || nextTokenEqualTo("/")) {
        if (!analyzeMulti()) {
          addException("There is an expression (Multi) problem");
          return false;
        }
      }
      return true;
    }
    return false;
  }

  private boolean analyzeMulti() {
    if (analyzePrimaryExp()) {
      while (nextTokenEqualTo("**") ) {
        if (!analyzePrimaryExp()) {
          addException("There is an PrimaryExp problem");
          return false;
        }
      }
      return true;
    }
    return false;
  }


  private boolean analyzePrimaryExp() {
    if (nextTokenEqualTo("Ident")) {
      return true;
    } else if (nextTokenEqualTo("Const")) {
      return true;
    } else if (nextTokenEqualTo("(")) {
      if (analyzeExp()) {
        if (nextTokenEqualTo(")")) {
          return true;
        } else addException("An ending brace is missing");
      } else addException("There is an expression problem");
    }
    return false;
  }


  private boolean analyzeCondition() {
    //TODO назви ексепшенів
    if (nextTokenEqualTo("if")) {
      if (analyzeAttitude()) {
        if (nextTokenEqualTo("then")) {
          if (analyzeUnconditionalTransition()) {
              return true;
          } else addException("Problem with unconditional transition");
        } else addException("An 'then' in fork operator is missing");
      } else addException("There is an attitude problem");
    }
    return false;
  }

  private boolean analyzeLogExp() {
    //TODO навзви ексепшенів
    if (analyzeLogTerm()) {
      while (nextTokenEqualTo("or")) {
        if (!analyzeLogTerm()) {
          addException("Last 'or' part is missing");
          return false;
        }
      }
      return true;
    }
    return false;
  }

  private boolean analyzeLogTerm() {
    //TODO навзви ексепшенів
    if (analyzeLogMulti()) {
      while (nextTokenEqualTo("and")) {
        if (!analyzeLogMulti()) {
          addException("Last 'and' part is missing");
          return false;
        }
      }
      return true;
    }
    return false;
  }

  private boolean analyzeLogMulti() {
    if (nextTokenEqualTo("not")) {
      return analyzeLogMulti();
    } else if (nextTokenEqualTo("[")) {
      if (analyzeLogExp()) {
        return nextTokenEqualTo("]");
      }
    } else return analyzeAttitude();
    return false;
  }

  private boolean analyzeAttitude() {
    // TODO
    if (analyzeExp()) {
      if (analyzeExpSign()) {
        if (analyzeExp()) {
          return true;
        } else addException("There is an expression problem in logical relation");
      } else addException("There is a relation type problem");
    }
    return false;
  }

  private boolean analyzeExpSign() {
    if (nextTokenEqualTo("<")) {
      return true;
    } else if (nextTokenEqualTo(">")) {
      return true;
    } else if (nextTokenEqualTo(">=")) {
      return true;
    } else if (nextTokenEqualTo("<=")) {
      return true;
    } else if (nextTokenEqualTo("==")) {
      return true;
    } else return nextTokenEqualTo("!=");
  }

  private boolean analyzeLoop() {
    //TODO назви ексепшенів
    if (nextTokenEqualTo("do")) {
      if (analyzeOperatorsList()) {
        if (nextTokenEqualTo("while")) {
          if (analyzeLogExp()) {
            return true;
          } else addException("There is an logical expression problem");
        } else addException("An 'while' is missing");
      } else addException("Problem with operators list");
    }
    return false;
  }


  private void addException(String message) {
//    System.out.println(number);
    exceptions.add(new SAException(program.getToken(currentToken), message, number++));
  }

  private boolean nextTokenEqualTo(String token) {
    if (currentToken == program.getTokens().size()) return false;
    if (program.getToken(currentToken).getCode() == program.getCode(token)) {
      currentToken++;
      System.out.println(token);
      return true;
    }
    return false;
  }
}
