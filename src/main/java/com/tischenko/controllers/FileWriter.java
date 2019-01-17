package com.tischenko.controllers;

import com.tischenko.models.Program;
import com.tischenko.models.analyzers.CompilerException;
import com.tischenko.models.analyzers.syntactical2.SyntaxAnalyzer2;
import javafx.collections.ObservableList;

import java.io.*;
import java.util.*;

class FileWriter {
  private final Program program;
  private ObservableList<CompilerException> exceptions;
  private boolean written = true;

  FileWriter(Program program, ObservableList<CompilerException> exceptions) {
    this.program = program;
    this.exceptions = exceptions;
  }

  void writeSourceCodeIntoFile(File file, String sourceCode) throws BIOException {
    PrintWriter writer;
    try {
      writer = new PrintWriter(file);
      writer.println(sourceCode);
      writer.close();
    } catch (FileNotFoundException e) {
      throw new BIOException(e);
    }
  }

  void writeIDsFile() throws BIOException{
    String fileName = addFileNameSpecification(program.getProgramFile().getPath(), "Identifiers");
    try(Formatter formatter = new Formatter(new FileOutputStream(fileName))){
      formatter.format("%3s %10s %10s" + System.getProperty("line.separator"), "N", "Name", "Type");
      for(Program.Identifier identifier : this.program.getIdentifiers()) {
        formatter.format("%3s %10s %10s" + System.getProperty("line.separator"), identifier.getNumber(), identifier.getToken(), identifier.getTypeName() );
      }
    }catch (FileNotFoundException e){
      written = false;
//      System.out.println(e.getMessage());
      throw new BIOException(e);
    }
  }

  void writeExceptionsFile()throws BIOException{
    String fileName = addFileNameSpecification(program.getProgramFile().getPath(), "Exceptions");
    try(Formatter formatter = new Formatter(new FileOutputStream(fileName))){
      formatter.format("%3s %50s %5s %10s %20s" + System.getProperty("line.separator"),
              "N", "Message", "Line", "Token", "Analyzer");
      for(CompilerException exception : exceptions) {
        formatter.format("%3s %50s %5s %10s %20s" + System.getProperty("line.separator"),
                exception.getNumber(), exception.getMessage(), exception.getLine(), exception.getToken(), exception.getAnalyzer());
      }
    }catch (FileNotFoundException e){
      written = false;
//      System.out.println(e.getMessage());
      throw new BIOException(e);
    }
  }

  void writeConstantsFile() throws BIOException{
    String fileName = addFileNameSpecification(program.getProgramFile().getPath(), "Constants");
    try(Formatter formatter = new Formatter(new FileOutputStream(fileName))){
      formatter.format("%3s %10s" + System.getProperty("line.separator"), "N", "Value");
      for(Program.Const id : this.program.getConstants()) {
        formatter.format("%3s %10s" + System.getProperty("line.separator"), id.getNumber(), id.getToken());
      }
    }catch (IOException e){
      written = false;
//      System.out.println(e.getMessage());
      throw  new BIOException(e);
    }
  }

  void writeTokenFile() throws BIOException {
    String fileName = addFileNameSpecification(program.getProgramFile().getPath(), "Tokens");
    try(Formatter formatter = new Formatter(new FileOutputStream(fileName))){
      formatter.format("%3s %5s %10s %5s %6s %6s" + System.getProperty("line.separator"),
              "N", "Line", "Token", "Code", "Identifier N", "Const N");
      for(Program.Token token : this.program.getTokens()) {
        formatter.format("%3s %5s %10s %5s %6s %6s"  + System.getProperty("line.separator"),
                token.getNumber(), token.getLine(), token.getToken(), token.getCode(), token.getIdCode(), token.getConstantCode());
      }
    }catch (IOException e){
      written = false;
//      System.out.println(e.getMessage());
      throw new BIOException(e);
    }
  }

  void writeTransitionsFile() throws BIOException {
    String fileName = addFileNameSpecification(program.getProgramFile().getPath(), "Transitions");
    try(Formatter formatter = new Formatter(new FileOutputStream(fileName))){
      formatter.format("%3s %10s %10s %30s" + System.getProperty("line.separator"),
              "N", "Input token", "State", "Stack");
      for(SyntaxAnalyzer2.dumpState transition : this.program.getTransitionTable()) {
        formatter.format("%3s %10s %10s %30s"  + System.getProperty("line.separator"),
                transition.getNumber(), transition.getToken(), transition.getState(), transition.getStackValue());
      }
    }catch (IOException e){
      written = false;
//      System.out.println(e.getMessage());
      throw new BIOException(e);
    }
  }

  boolean isWritten() {
    return written;
  }

  static private String addFileNameSpecification(String name, String toAdd){
    final int lastPeriodPos = name.lastIndexOf('.');
    if (lastPeriodPos <= 0) {
      return name+toAdd + ".txt";
    } else {
      return name.substring(0, lastPeriodPos) + toAdd + ".txt";
    }
  }
}
