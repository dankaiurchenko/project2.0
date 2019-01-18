package com.tischenko.models.analyzers.la;

import com.tischenko.models.Const;
import com.tischenko.models.Ident;
import com.tischenko.models.Program;
import com.tischenko.models.analyzers.AbstractAnalyzer;
import com.tischenko.models.analyzers.la.Exceptions.DuplicatedVariableException;
import com.tischenko.models.analyzers.la.Exceptions.EndOfFileException;
import com.tischenko.models.analyzers.la.Exceptions.ScannerException;
import com.tischenko.models.analyzers.la.Exceptions.UndeclaredVariableException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

import static java.lang.Integer.*;

public class LexicalAnalyzer extends AbstractAnalyzer {
  private final BufferedReader nextCharReader;
  private final LAStatesController LAStatesController = new LAStatesController();
  private final int LINE_FEED = 10;
  private Character currentChar;
  private int currentLine = 1;
  private int currentState = 1;
  private int currentTokenNumber = 0;
  private boolean isFileOver = false;
  private boolean hasToReadNextChar = true;
  private IdentifierTypes currentIdentifierType = null;
  private int number = 0;
  private int constNumber = 1;
  private int idNumber = 1;
  private StringBuilder currentToken = new StringBuilder();


  public LexicalAnalyzer(Reader reader, Program program) {
    super(program);
    nextCharReader = new BufferedReader(reader);
  }

  public boolean analyze() {
    try {
      readNextCharIfNeeded();
      loop:
      while (true) {
        LAStatesController.Transition currentTransition =
                LAStatesController.getTransition(currentState, CharClasses.defineCharClass(currentChar));
        if (currentTransition != null) {
          if (currentTransition.getMark().equals("void")) hasToReadNextChar = false;
          else currentToken.append(currentChar);
          switch (currentTransition.getBeta()) {
            case "error":
              addException();
              break loop;
            case "token":
            case "Ident":
              addIdent();
              goToFirstState();
              break;
            case "Const":
              addConstant();
              goToFirstState();
              break;
            default:
              currentState = parseInt(currentTransition.getBeta());
          }
        } else {
          addException();
          break;
        }
        readNextCharIfNeeded();
      }
    } catch (EndOfFileException e) {
      if (exceptions.isEmpty())
        return true;
    }
    return false;
  }

  private void addToken(int constant, int id, int tokenCode) {
    int currentTokenCode = (tokenCode != 0) ? tokenCode :
            decode(program.getTableOfTokens().get(currentToken.toString()));
    setConstType(currentTokenCode);
    program.addToken(currentToken.toString(), currentTokenCode,
            id, constant, currentTokenNumber, currentLine);
    currentTokenNumber++;
  }

  private void addIdent() {
    if (program.getTableOfTokens().containsKey(currentToken.toString())) {
      addToken(0, 0, parseInt(program.getTableOfTokens().get(currentToken.toString())));
      return;
    }
    Ident ident = new Ident(currentToken.toString());
    if (program.hasIdent(ident)) {
      addExistingIdent(ident);
    } else {
      createNewIdent();
    }
  }

  private void createNewIdent() {
    if (currentIdentifierType == null) {
      exceptions.add(new UndeclaredVariableException(number++, currentToken.toString(), currentLine));
      return;
    }
    program.addIdent(new Ident(idNumber++, currentToken.toString(), currentIdentifierType));
    addToken(0, program.getIdents().size(), program.getCode("Ident"));
  }

  private void addExistingIdent(Ident ident) {
    if (currentIdentifierType != null) {
      exceptions.add(new DuplicatedVariableException(number++, currentToken.toString(), currentLine));
      return;
    }
    addToken(0, program.getIdents().indexOf(ident) + 1, program.getCode("Ident"));
  }

  private void addConstant() {
    Const aConst = new Const(Double.parseDouble(currentToken.toString()));
    if (program.hasConsts(aConst)) {
      addToken(0, program.getConsts().indexOf(aConst) + 1, program.getCode("Const"));
    } else {
      program.addConst(new Const(constNumber++, Double.parseDouble(currentToken.toString())));
      addToken(program.getConsts().size(), 0, program.getCode("Const"));
    }
  }

  private void setConstType(int currentTokenCode) {
    if (currentIdentifierType != null &&
            currentTokenCode != program.getCode(",") &&
            currentTokenCode != program.getCode("Ident") &&
            currentTokenCode != program.getCode(":") &&
            IdentifierTypes.getType(currentToken.toString()) == null) {
      currentIdentifierType = null;
    } else {
      IdentifierTypes c = IdentifierTypes.getType(currentToken.toString());
      if (c != null) currentIdentifierType = c;
    }
  }

  private void goToFirstState() throws EndOfFileException {
    currentState = 1;
    currentToken = new StringBuilder();
    readNextCharIfNeeded();
    readWhiteSeparators();
    hasToReadNextChar = false;
  }

  private char readNextChar() throws EndOfFileException {
    int intChar;
    if (isFileOver) {
      throw new EndOfFileException();
    }
    try {
      intChar = nextCharReader.read();
      int END_OF_FILE = -1;
      if (intChar == END_OF_FILE) {
        isFileOver = true;
        return (char) LINE_FEED;
      } else if (intChar == LINE_FEED) {
        currentLine++;
      }
    } catch (IOException o) {
      isFileOver = true;
      return (char) LINE_FEED;
    }
    return (char) intChar;
  }

  private void readNextCharIfNeeded() throws EndOfFileException {
    if (isFileOver) {
      throw new EndOfFileException();
    }
    if (hasToReadNextChar) {
      currentChar = readNextChar();
    }
    hasToReadNextChar = true;
  }

  private void addException() {
    hasToReadNextChar = false;
    exceptions.add(new ScannerException(number++, currentToken.toString(), currentLine));
  }

  private void readWhiteSeparators() throws EndOfFileException {
    while (CharClasses.WHITE.contains(currentChar) && !isFileOver) {
      if (CharClasses.COMMENT.contains(currentChar)) {
        readComment();
      }
      currentChar = readNextChar();
    }
  }

  private void readComment() throws EndOfFileException {
    while (currentChar != LINE_FEED && !isFileOver &&
            !String.valueOf(currentChar).equals(System.getProperty("line.separator"))) {
      currentChar = readNextChar();
    }
  }
}
