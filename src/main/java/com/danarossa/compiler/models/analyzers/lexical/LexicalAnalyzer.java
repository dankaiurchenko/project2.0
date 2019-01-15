package com.danarossa.compiler.models.analyzers.lexical;

import com.danarossa.compiler.models.Program;
import com.danarossa.compiler.models.analyzers.AbstractAnalyzer;
import com.danarossa.compiler.models.analyzers.lexical.Exceptions.DuplicatedVariableException;
import com.danarossa.compiler.models.analyzers.lexical.Exceptions.EndOfFileException;
import com.danarossa.compiler.models.analyzers.lexical.Exceptions.ScannerException;
import com.danarossa.compiler.models.analyzers.lexical.Exceptions.UndeclaredVariableException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LexicalAnalyzer extends AbstractAnalyzer {
//TODO зарефакторить код
  private Character currentChar;

  private int currentLine = 1;
  private int currentTokenNumber = 0;
  private boolean hasToReadNextChar = true;
  private constType currentConstType = null;
  private int number = 0;
  private int constNumber = 1;
  private int idNumber = 1;
  private boolean isFileOver = false;
  private final BufferedReader nextCharReader;
  private StringBuilder currentToken = new StringBuilder();


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
    //додать перевірку, чи іще немає там цієї штуки
    Program.Identifier identifier = new Program.Identifier(currentToken.toString());
    if (program.hasIdentifier(identifier)) {
      if (currentConstType != null) {
        exceptions.add(new DuplicatedVariableException(number++, currentToken.toString(), currentLine));
        return;
      }
      addToken(0, program.getIdentifiers().indexOf(identifier) + 1, program.getCode("identifier"));
    } else {
      if (currentConstType == null) {
        exceptions.add(new UndeclaredVariableException(number++, currentToken.toString(), currentLine));
        return;
      }
      identifier = new Program.Identifier(idNumber++, currentToken.toString(), currentConstType);
      program.addIdentifier(identifier);
      addToken(0, program.getIdentifiers().size(), program.getCode("identifier"));
    }
  }

  private void addConstant() {
    Program.Const constant = new Program.Const(Double.parseDouble(currentToken.toString()));
    if (program.hasConstant(constant)) {
      addToken(0, program.getConstants().indexOf(constant) + 1, program.getCode("constant"));
    } else {
      program.addConstant(new Program.Const(constNumber++, Double.parseDouble(currentToken.toString())));
      addToken(program.getConstants().size(), 0, program.getCode("constant"));
    }
  }

  private void setConstType(int currentTokenCode) {
    if (currentTokenCode == constType.INT.getCode()) {
      currentConstType = constType.INT;
    } else if (currentTokenCode == constType.DOUBLE.getCode()) {
      currentConstType = constType.DOUBLE;
    } else if (
            currentTokenCode != program.getCode(",") &&
                    currentTokenCode != program.getCode("identifier") &&
                    currentTokenCode != program.getCode(":")) {
//      System.out.println("current token setting null " + currentTokenCode);
      currentConstType = null;
    }
  }

  @SuppressWarnings("InfiniteLoopStatement")
  public boolean analyze() {
    try {
      readNextCharIfNeeded();
      while (true) {
//        System.out.println("1");
//        System.out.println("current char    " + currentChar + "     " + currentToken);

        if ((CharClasses.OP.contains(currentChar) ||
                CharClasses.POSSIBLE_DOUBLE.contains(currentChar) ||
                CharClasses.WHITE.contains(currentChar)
        )
                && !currentToken.toString().equals("")) {
//          System.out.println("2");
          if (!CharClasses.WHITE.contains(currentChar)) hasToReadNextChar = false;
          processAddingToken();
//          readWhiteSeparatorsAndComments();
        } else if (CharClasses.OP.contains(currentChar) && currentToken.toString().equals("")) {
//          System.out.println("3");
          currentToken.append(currentChar);
          processAddingToken();
//          readWhiteSeparatorsAndComments();
        } else if (CharClasses.POSSIBLE_DOUBLE.contains(currentChar)) {
//          System.out.println("4");
          currentToken.append(currentChar);
          analyzeDoubleSeparator();
        } else if (!CharClasses.WHITE.contains(currentChar)) {
//          System.out.println("5   " + currentChar);
          currentToken.append(currentChar);
        }
        readNextCharIfNeeded();
      }
    } catch (EndOfFileException e) {
      if (exceptions.isEmpty())
        return true;
    }
    return false;
  }

  private void analyzeDoubleSeparator() throws EndOfFileException {
    readNextCharIfNeeded();
    if (CharClasses.DOUBLE_ENDING.contains(currentChar)) {
      currentToken.append(currentChar);
    }else {
      hasToReadNextChar = false;
    }
    processAddingToken();
  }

  private void processAddingToken() {
//    System.out.println("Trying to add this token :   " + currentToken);
    if (isConstant()) {
      addConstant();
    } else if (program.getTableOfTokens().containsKey(currentToken.toString())) {
      addToken(0, 0, Integer.parseInt(program.getTableOfTokens().get(currentToken.toString())));
    } else if (isIdentifier()) {
      addIdentifier();
    } else addException();
    currentToken = new StringBuilder();
  }

  private boolean isConstant() {
    Pattern pattern = Pattern.compile("^[0-9]*\\.?[0-9]*$");
    Matcher matcher = pattern.matcher(currentToken);
    return matcher.matches();

  }

  private boolean isIdentifier() {
    Pattern pattern = Pattern.compile("^[a-zA-Z_][a-zA-Z0-9_]*$");
    Matcher matcher = pattern.matcher(currentToken);
    return matcher.matches();
  }

  private char readNextChar() throws EndOfFileException {
    int intChar;
    if (isFileOver) {
      throw new EndOfFileException();
    }
    int LINE_FEED = 10;
    try {
      intChar = nextCharReader.read();
//      System.out.println((char)intChar + "   " + intChar);
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
    if (hasToReadNextChar) {
      currentChar = readNextChar();
    }
    hasToReadNextChar = true;
  }

  private void addException() {
    hasToReadNextChar = false;
    exceptions.add(new ScannerException(number++, currentToken.toString(), currentLine));
  }


  //regex for identifier /^[a-zA-Z_][a-zA-Z0-9_]*$/
  //regex for constant ^[0-9]*\.?[0-9]*$

}
