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
    if (analyzeVariableType()) {
      if (analyzeIdentifiersList()) {
        return true;
      } else addException("There is problem in identifier list");
    } else addException("There is problem with variable type");
    return false;
  }

  private boolean analyzeIdentifiersList() {
    if (nextTokenEqualTo("identifier")) {
      while (nextTokenEqualTo(",")) {
        if (!nextTokenEqualTo("identifier")) {
          addException("Last identifier is missing");
          return false;
        }
      }
      return true;
    } else addException("An identifier is missing");
    return false;
  }

  private boolean analyzeVariableType() {
    if (nextTokenEqualTo("int")) {
      return true;
    } else return nextTokenEqualTo("double");
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
    if (analyzeCircle()) {
      return true;
    } else if (analyzeFork()) {
      return true;
    } else if (analyzeAssignment()) {
      return true;
    } else if (analyzeInput()) {
      return true;
    } else if (analyzeOutput()) {
      return true;
    } else addException("There is a problem with operator");
    return false;
  }

  private boolean analyzeOutput() {
    if (nextTokenEqualTo("write")) {
      if (nextTokenEqualTo("(")) {
        if (analyzeIdentifiersList()) {
          if (nextTokenEqualTo(")")) {
            return true;
          } else addException("The ending brace is missing");
        } else addException("There is a problem in identifier list");
      } else addException("The beginning brace is missing");
    }
    return false;
  }

  private boolean analyzeInput() {
    if (nextTokenEqualTo("read")) {
      if (nextTokenEqualTo("(")) {
        if (analyzeIdentifiersList()) {
          if (nextTokenEqualTo(")")) {
            return true;
          } else addException("The ending brace is missing");
        } else addException("There is problem in identifier list ");
      } else addException("The beginning curve brace is missing");
    }
    return false;
  }


  private boolean analyzeAssignment() {
    //TODO навзви ексепшенів
    if (nextTokenEqualTo("identifier")) {
      if (nextTokenEqualTo("=")) {
        if (analyzeSecondPartOfAssignment()) {
          return true;
        } else addException("Second part assignment problem");
      } else addException("An '=' is missing");
    }
    return false;
  }

  private Boolean analyzeSecondPartOfAssignment() {
    //TODO навзви ексепшенів
    if (analyzeExpression()) {
      return true;
    } else if (nextTokenEqualTo("?")) {
      if (analyzeLogicalExpression()) {
        if (nextTokenEqualTo("?")) {
          if (analyzeExpression()) {
            if (nextTokenEqualTo(":")) {
              return analyzeExpression();
            } else addException("An \":\" is missing");
          } else addException("There is an expression problem");
        } else addException("An inner \"?\" is missing");
      } else addException("There is an logical expression problem");
    }
    return false;
  }


  private boolean analyzeExpression() {
    boolean minus = nextTokenEqualTo("-");
    if (analyzeT()) {
      while (nextTokenEqualTo("+") || nextTokenEqualTo("-")) {
        if (!analyzeT()) {
          addException("There is an expression problem");
          return false;
        }
      }
      return true;
    }
    if(minus) addException("minus is not needed");
    return false;
  }

  private boolean analyzeT() {
    if (analyzeF()) {
      while (nextTokenEqualTo("*") || nextTokenEqualTo("/")) {
        if (!analyzeF()) {
          addException("There is an expression (F) problem");
          return false;
        }
      }
      return true;
    }
    return false;
  }

  private boolean analyzeF() {
    if (nextTokenEqualTo("identifier")) {
      return true;
    } else if (nextTokenEqualTo("constant")) {
      return true;
    } else if (nextTokenEqualTo("(")) {
      if (analyzeExpression()) {
        if (nextTokenEqualTo(")")) {
          return true;
        } else addException("An ending brace is missing");
      } else addException("There is an expression problem");
    }
    return false;
  }


  private boolean analyzeFork() {
    //TODO назви ексепшенів
    if (nextTokenEqualTo("if")) {
      if (analyzeLogicalExpression()) {
        if (nextTokenEqualTo("then")) {
          if (analyzeOperatorsList()) {
            if (nextTokenEqualTo("fi")) {
              return true;
            } else addException("An 'fi' at the end of a fork operator is missing");
          } else addException("The operator is missing");
        } else addException("An 'then' in fork operator is missing");
      } else addException("There is a logical expression problem");
    }
    return false;
  }

  private boolean analyzeLogicalExpression() {
    //TODO навзви ексепшенів
    if (analyzeLT()) {
      while (nextTokenEqualTo("OR")) {
        if (!analyzeLT()) {
          addException("Last 'OR' part is missing");
          return false;
        }
      }
      return true;
    }
    return false;
  }

  private boolean analyzeLT() {
    //TODO навзви ексепшенів
    if (analyzeLM()) {
      while (nextTokenEqualTo("AND")) {
        if (!analyzeLM()) {
          addException("Last 'AND' part is missing");
          return false;
        }
      }
      return true;
    }
    return false;
  }

  private boolean analyzeLM() {
    if (nextTokenEqualTo("NOT")) {
      return analyzeLM();
    } else if (nextTokenEqualTo("[")) {
      if (analyzeLogicalExpression()) {
        return nextTokenEqualTo("]");
      }
    } else return analyzeLogicalRelation();
    return false;
  }

  private boolean analyzeLogicalRelation() {
    // TODO
    if (analyzeExpression()) {
      if (analyzeLogicalRelationType()) {
        if (analyzeExpression()) {
          return true;
        } else addException("There is an expression problem in logical relation");
      } else addException("There is a relation type problem");
    }
    return false;
  }

  private boolean analyzeLogicalRelationType() {
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

  private boolean analyzeCircle() {
    //TODO назви ексепшенів
    if (nextTokenEqualTo("do")) {
      if (nextTokenEqualTo("while")) {
        if (nextTokenEqualTo("(")) {
          if (analyzeLogicalExpression()) {
            if (nextTokenEqualTo(")")) {
              if (analyzeOperatorsList()) {
                if (nextTokenEqualTo("enddo")) {
                  return true;
                } else addException("An 'enddo' in the end of circle operator is missing");
              } else addException("An operator is missing");
            } else addException("An ending brace is missing");
          } else addException("There is an logical expression problem");
        } else addException("An opening brace is missing");
      } else addException("An 'while' is missing");
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
