package com.tischenko.models.analyzers.lexical;

import com.tischenko.models.Program;
import com.tischenko.models.analyzers.AbstractAnalyzer;
import com.tischenko.models.analyzers.lexical.Exceptions.DuplicatedVariableException;
import com.tischenko.models.analyzers.lexical.Exceptions.EndOfFileException;
import com.tischenko.models.analyzers.lexical.Exceptions.ScannerException;
import com.tischenko.models.analyzers.lexical.Exceptions.UndeclaredVariableException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

public class LexicalAnalyzer extends AbstractAnalyzer {
  //TODO зарефакторить код
  private Character currentChar;

  private int currentLine = 1;
  private int currentState = 1;
  private int currentTokenNumber = 0;
  private boolean hasToReadNextChar = true;
  private constType currentConstType = null;
  private int number = 0;
  private int constNumber = 1;
  private int idNumber = 1;
  private boolean isFileOver = false;
  private final BufferedReader nextCharReader;
  private StringBuilder currentToken = new StringBuilder();
  private StatesController statesController = new StatesController();

  private int LINE_FEED = 10;


  public LexicalAnalyzer(Reader reader, Program program) {
    super(program);
    nextCharReader = new BufferedReader(reader);
  }

  private void addToken(int constant, int id, int tokenCode) {
    int currentTokenCode = (tokenCode != 0) ? tokenCode :
            Integer.decode(program.getTableOfTokens().get(currentToken.toString()));
    setConstType(currentTokenCode);
    program.addToken(currentToken.toString(), currentTokenCode,
            id, constant, currentTokenNumber, currentLine);
    currentTokenNumber++;
  }

  private void addIdentifier() {
    if (program.getTableOfTokens().containsKey(currentToken.toString())) {
      addToken(0, 0, Integer.parseInt(program.getTableOfTokens().get(currentToken.toString())));
      return;
    }
    //додать перевірку, чи іще немає там цієї штуки
    Program.Identifier identifier = new Program.Identifier(currentToken.toString());
    if (program.hasIdentifier(identifier)) {
      if (currentConstType != null) {
        exceptions.add(new DuplicatedVariableException(number++, currentToken.toString(), currentLine));
        return;
      }
      addToken(0, program.getIdentifiers().indexOf(identifier) + 1, program.getCode("Ident"));
    } else {
      if (currentConstType == null) {
        exceptions.add(new UndeclaredVariableException(number++, currentToken.toString(), currentLine));
        return;
      }
      identifier = new Program.Identifier(idNumber++, currentToken.toString(), currentConstType);
      program.addIdentifier(identifier);
      addToken(0, program.getIdentifiers().size(), program.getCode("Ident"));
    }
  }

  private void addConstant() {
    Program.Const constant = new Program.Const(Double.parseDouble(currentToken.toString()));
    if (program.hasConstant(constant)) {
      addToken(0, program.getConstants().indexOf(constant) + 1, program.getCode("Const"));
    } else {
      program.addConstant(new Program.Const(constNumber++, Double.parseDouble(currentToken.toString())));
      addToken(program.getConstants().size(), 0, program.getCode("Const"));
    }
  }

  private void setConstType(int currentTokenCode) {
    if (currentConstType != null && currentTokenCode != program.getCode(",") &&
            currentTokenCode != program.getCode("Ident") &&
            currentTokenCode != program.getCode(":") &&
            constType.getType(currentToken.toString()) == null) {
      currentConstType = null;
    } else{
      constType c = constType.getType(currentToken.toString());
      if(c!=null) currentConstType = c;
    }

  }

  @SuppressWarnings("InfiniteLoopStatement")
  public boolean analyze() {
    try {
      readNextCharIfNeeded();
      loop:
      while (true) {
        StatesController.Transition currentTransition =
                statesController.getTransition(currentState, CharClasses.defineCharClass(currentChar));
        if (currentTransition != null) {
          if (currentTransition.getMark().equals("void")) hasToReadNextChar = false;
          else currentToken.append(currentChar);
          switch (currentTransition.getBeta()) {
            case "error":
              addException();
              break loop;
            case "token":
            case "Ident":
              addIdentifier();
              goToFirstState();
              break;
            case "Const":
              addConstant();
              goToFirstState();
              break;
            default:
              currentState = Integer.parseInt(currentTransition.getBeta());
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

  private void goToFirstState() throws EndOfFileException {
    currentState = 1;
    currentToken = new StringBuilder();
    readNextCharIfNeeded();
    readWhiteSeparatorsAdnComments();
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
        intChar = LINE_FEED;
        isFileOver = true;
      }
      if (intChar == LINE_FEED) {
        currentLine++;
      }
    } catch (IOException o) {
      isFileOver = true;
      intChar = LINE_FEED;
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

  private void readWhiteSeparatorsAdnComments() throws EndOfFileException {
    boolean isItComment = false;
    while (CharClasses.WHITE.contains(currentChar) ||
            CharClasses.COMMENT.contains(currentChar) || isItComment) {
      if (currentChar == LINE_FEED || String.valueOf(currentChar).equals(System.getProperty("line.separator"))) {
        isItComment = false;
        currentLine++;
      } else if (CharClasses.COMMENT.contains(currentChar)) {
        isItComment = true;
      }
      currentChar = readNextChar();
      if (isFileOver) {
        return;
      }
    }
  }

  //regex for identifier /^[a-zA-Z_][a-zA-Z0-9_]*$/
  //regex for constant ^[0-9]*\.?[0-9]*$

}
