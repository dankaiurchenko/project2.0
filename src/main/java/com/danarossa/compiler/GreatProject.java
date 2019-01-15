package com.danarossa.compiler;

import com.danarossa.compiler.controllers.BIOException;
import com.danarossa.compiler.controllers.ViewController;
import com.danarossa.compiler.models.Program;
import com.danarossa.compiler.models.TokensReader;
import com.danarossa.compiler.models.analyzers.CompilerException;
import com.danarossa.compiler.models.analyzers.lexical.LexicalAnalyzer;
import com.danarossa.compiler.models.analyzers.syntactical.SyntaxAnalyzer;
import com.danarossa.compiler.models.analyzers.syntactical2.StatesController;
import com.danarossa.compiler.models.analyzers.syntactical2.SyntaxAnalyzer2;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.*;
import java.util.Arrays;
import java.util.Scanner;

public class GreatProject extends Application {

  private Stage primaryStage;

  @Override
  public void start(Stage primaryStage) {
    try {
      this.primaryStage = primaryStage;
      this.primaryStage.setTitle("BGDN programming language compiler");
      FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/RootPane.fxml"));//, resources);
      Parent rootLayout = fxmlLoader.load();

      ViewController controller = fxmlLoader.getController();
      controller.setGp(this);

      try {
        this.primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("images/binary-code.png")));
      } catch (Exception e) {
        System.out.println("icon not found");
      }
      this.primaryStage.setScene(new Scene(rootLayout));
      this.primaryStage.show();

    } catch (IOException | NullPointerException e) {
      e.printStackTrace();
    }
  }

  public Stage getPrimaryStage() {
    return primaryStage;
  }

  public static void main(String[] args) {
    Application.launch(args);
//    testthetokensreader();
//    Program program = testthelexicalAnalyzer();
//    testSyntscticalAnalyzer(program);
//    testSyntsctical2Analyzer(program);
  }

  private static void testSyntsctical2Analyzer(Program program) {
    File MPAFile = new File("D:\\Projects\\3course\\compilers\\MPA.xlsx");
    try {
      StatesController statesController = new StatesController(MPAFile);
      SyntaxAnalyzer2 analyzer = new SyntaxAnalyzer2(program, statesController);
      if(analyzer.analyze()){
        System.out.println("Syntactical2 analysis done successfully");
        System.out.println(program.getTransitionTable());
      }else{
        ObservableList<CompilerException> exceptions = analyzer.getExceptions();
        System.out.println(Arrays.toString(exceptions.toArray()));
        System.out.println(program.getTransitionTable());
      }
    } catch (BIOException e ) {
      System.out.println("Invalid file    Try another file, please");
    }


  }

  private static void testSyntscticalAnalyzer(Program program) {
    SyntaxAnalyzer analyzer = new SyntaxAnalyzer(program);
    if(analyzer.analyze()){
      System.out.println("Syntactical analysis done successfully");
    }else{
      ObservableList<CompilerException> exceptions = analyzer.getExceptions();
      System.out.println(Arrays.toString(exceptions.toArray()));
    }
  }

  private static Program testthelexicalAnalyzer() {
    File tokensFile = new File("C:\\Users\\Bogdana Iurchienko\\Desktop\\tokens.txt");
    File programFile = new File("C:\\Users\\Bogdana Iurchienko\\Desktop\\programTest.txt");
    TokensReader tokensReader;

    try (Scanner s = new Scanner(tokensFile)) {
      tokensReader = new TokensReader(s);
      Program program = new Program();
      program.setTableOfTokens(tokensReader.getMapOfTokens());
      Reader reader = new StringReader(openSourceCode(programFile));
      LexicalAnalyzer analyzer = new LexicalAnalyzer(reader, program);
      if(analyzer.analyze()){
        System.out.println("Lexical analysis done successfully");
        System.out.println(program.getTokens());
        System.out.println(program.getIdentifiers());
        System.out.println(program.getConstants());
      } else {
        ObservableList<CompilerException> exceptions = analyzer.getExceptions();
        System.out.println(Arrays.toString(exceptions.toArray()));
      }
      return program;
    } catch (FileNotFoundException ex) {
      System.out.println("File not found, Try another file, please");
    }
    return null;
  }

  private static void testthetokensreader() {
    File file = new File("C:\\Users\\Bogdana Iurchienko\\Desktop\\tokensTest.txt");
    try (Scanner s = new Scanner(file)) {
      TokensReader tokensReader = new TokensReader(s);
      System.out.println(tokensReader.getTokensText());
      System.out.println(tokensReader);
    } catch (FileNotFoundException ex) {
      System.out.println("File not found, Try another file, please");
    }
  }

  private static String openSourceCode(File programFile){
    StringBuilder builder = new StringBuilder();
      try(Scanner s = new Scanner(programFile)) {
        while (s.hasNextLine()) {
          String newline = s.nextLine().replace((char)-1, ' ');
          builder.append(newline).append(System.getProperty("line.separator"));
        }
      } catch (FileNotFoundException ex) {
        System.out.println("File not found , Try another file, please");
      }
//      System.out.println(builder.toString());
    return builder.toString();
  }


}
