package com.danarossa.compiler.controllers;

import com.danarossa.compiler.GreatProject;
import com.danarossa.compiler.models.Program;
import com.danarossa.compiler.models.TokensReader;
import com.danarossa.compiler.models.analyzers.AbstractAnalyzer;
import com.danarossa.compiler.models.analyzers.CompilerException;
import com.danarossa.compiler.models.analyzers.lexical.LexicalAnalyzer;
import com.danarossa.compiler.models.analyzers.syntactical.SyntaxAnalyzer;
import com.danarossa.compiler.models.analyzers.syntactical2.StatesController;
import com.danarossa.compiler.models.analyzers.syntactical2.SyntaxAnalyzer2;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.Scanner;

public class ViewController {
  private Program program;
  private GreatProject gp;
  private FileWriter fileWriter;
  private StatesController statesController;
  private TokensReader tokensReader;
  private boolean lastLexical;
  private ObservableList<CompilerException> exceptions = FXCollections.observableArrayList();

    //таблиця розпарсених лексем
  @FXML private TableView<Program.Token> tokensTable;
  @FXML private TableColumn<Program.Token, Integer> tokenNumberColumn;
  @FXML private TableColumn<Program.Token, Integer> tokenLineColumn;
  @FXML private TableColumn<Program.Token, String>  tokenTokenColumn;
  @FXML private TableColumn<Program.Token, Integer> tokenCodeColumn;
  @FXML private TableColumn<Program.Token, Integer> tokenConstantCodeColumn;
  @FXML private TableColumn<Program.Token, Integer> tokenIdentifierCodeColumn;

  // таблиця виключень
  @FXML private TableView<CompilerException> exceptionsTable;
  @FXML private TableColumn<CompilerException, Integer> exceptionNumberColumn;
  @FXML private TableColumn<CompilerException, String> exceptionMessageColumn;
  @FXML private TableColumn<CompilerException, Integer> exceptionLineColumn;
  @FXML private TableColumn<CompilerException, String> exceptionTokenColumn;
  @FXML private TableColumn<CompilerException, String> exceptionAnalyzerColumn;

  // таблиця констант
  @FXML private TableView<Program.Const> constantsTable;
  @FXML private TableColumn<Program.Const, Integer> constantNumberColumn;
  @FXML private TableColumn<Program.Const, Number> constantValueColumn;

  // таблиця ідентифікаторів
  @FXML private TableView<Program.Identifier> identifiersTable;
  @FXML private TableColumn<Program.Identifier, Integer> identifierNumberColumn;
  @FXML private TableColumn<Program.Identifier, Integer> identifierNameColumn;
  @FXML private TableColumn<Program.Identifier, String> identifierTypeColumn;

  // таблиця переходів
  @FXML private TableView<SyntaxAnalyzer2.dumpState> transitionTable;
  @FXML private TableColumn<SyntaxAnalyzer2.dumpState, Integer> transitionNumberColumn;
  @FXML private TableColumn<SyntaxAnalyzer2.dumpState, Integer> transitionStateColumn;
  @FXML private TableColumn<SyntaxAnalyzer2.dumpState, String> transitionTokenColumn;
  @FXML private TableColumn<SyntaxAnalyzer2.dumpState, String> transitionStackValueColumn;

  // таблиця конфігурації
  @FXML private TableView<StatesController.Transition> transitionConfigurationsTable;
  @FXML private TableColumn<StatesController.Transition, Integer> alphaColumn;
  @FXML private TableColumn<StatesController.Transition, String> markColumn;
  @FXML private TableColumn<StatesController.Transition, String> betaColumn;
  @FXML private TableColumn<StatesController.Transition, Integer> stackColumn;
  @FXML private TableColumn<StatesController.Transition, String> errorMessageColumn;

  // таблиця вхідних токенів
  @FXML private TableView<TokensReader.Token> inputTokensTable;
  @FXML private TableColumn<TokensReader.Token, Integer> codeColumn;
  @FXML private TableColumn<TokensReader.Token, String> tokenColumn;

  // панель статуса
  @FXML private Label fileNameLabel;
  @FXML private Label statusLabel;

  // для коду програми
  @FXML private TextArea sourceCodeArea;
  //menus
  @FXML private MenuItem closeProjectMenu;
  @FXML private MenuItem saveMenu;
  @FXML private MenuItem saveAsSourceCodeMenu;
  @FXML private MenuItem runLexicalAnalyzerMenu;
  @FXML private MenuItem runSyntaxAnalyzerMenu;
  @FXML private MenuItem runSyntaxAnalyzer2Menu;
  @FXML private MenuItem saveTablesMenu;

  public ViewController() {
    }

  public void setGp(GreatProject gp) {
    this.gp = gp;
  }

  public void newProgram(){
    File file = getFileToSave("TXT files (*.txt)", "*.txt");
    if(file != null){
      prepareForNewProject(file, "New program is created");
    }
  }

  public void openSourceCode(){
    File file = getFileToRead("TXT files (*.txt)","*.txt" );
    if (file != null) {
      prepareForNewProject(file, "Source code is opened");
      try(Scanner s = new Scanner(file)) {
        while (s.hasNextLine()) {
          String newline = s.nextLine().replace((char)-1, ' ');
//          newline = ((int)newline.charAt(newline.length()-1) == -1) ? newline.substring(0, newline.length() - 1) : newline;
          sourceCodeArea.appendText(newline + System.getProperty("line.separator"));
        }
      } catch (FileNotFoundException ex) {
//        System.err.println(ex.getMessage());
        showErrorDialog("File not found", "Try another file, please");
      }
    }
  }

  public void save(){
    if (program.getProgramName().endsWith(".txt")){
      try {
        fileWriter.writeSourceCodeIntoFile(updateProgram().getProgramFile(), sourceCodeArea.getText());
      } catch (BIOException e) {
        showErrorDialog("Error occurred", "Please, check out the file and the program source code");
      }
    }
  }

  public void saveTables(){
    try {
      if(!program.getTableOfTokens().isEmpty()){
        fileWriter.writeTokenFile();
      }
      if(!program.getConstants().isEmpty()){
        fileWriter.writeConstantsFile();
      }
      if(!program.getIdentifiers().isEmpty()){
        fileWriter.writeIDsFile();
      }
      if(!exceptions.isEmpty()){
        fileWriter.writeExceptionsFile();
      }
      if(!program.getTransitionTable().isEmpty()){
        fileWriter.writeTransitionsFile();
      }
      if(fileWriter.isWritten()){
        setStatusLabel("Tables are written successfully");
      }
    } catch (BIOException e) {
      showErrorDialog("File Writing Error occurred", "Please solve your problems");
    }
  }

  public void saveSourceCode(){
    File file = getFileToSave("TXT files (*.txt)","*.txt" );
    try {
      updateProgram();
      fileWriter.writeSourceCodeIntoFile(file,  sourceCodeArea.getText());
      setStatusLabel("Source code is saved successfully");
    } catch (BIOException e) {
      showErrorDialog("Error occurred", "Please, check out the file and the program source code");
    }
  }

  public void closeCurrentProject(){
    //усі таблиці очищаються
    program = null;
    saveTablesMenu.setDisable(true);
    sourceCodeArea.setDisable(true);
    clearTables();
    setMenusProgram(true);
    setMenusInputTokens(true);
    setMenusInputTransitions(true);
  }

  public void runLexicalAnalyzer(){
    if (sourceCodeArea.getText().isEmpty()){
      showErrorDialog("Lexical Analyzer Error occurred", "Please, write some code before lexical analysis");
    } else if(tokensReader == null || tokensReader.getTableOfTokens().isEmpty()){
      showErrorDialog("Lexical Analyzer Error occurred", "Please, check out the input table of tokens");
    }else {
      clearTables();
      program.cleanup().setTableOfTokens(tokensReader.getMapOfTokens());
      executeAnalysis(new LexicalAnalyzer(new StringReader(sourceCodeArea.getText()), program), "Lexical analysis done successfully");
      showTokens();
      showIDs();
      showConstants();
      saveTablesMenu.setDisable(false);
      lastLexical = true;
    }
  }

  public void runSyntaxAnalyzer(){
//    System.out.println(program.getExceptions());
    if(program.getTokens() == null || program.getTokens().isEmpty()){
      showErrorDialog("Syntax Analyzer Error occurred", "Please, produce lexical analysis before syntactical one");
    }else if(!exceptions.isEmpty() && lastLexical){
      showErrorDialog("Syntax Analyzer Error occurred", "Please, produce valid lexical analysis before syntactical one");
    }else if(program.getTableOfTokens() == null || program.getTableOfTokens().isEmpty()){
      showErrorDialog("Syntax Analyzer Error occurred", "Please, check out the input table of tokens");
    }else {
      executeAnalysis(new SyntaxAnalyzer(program), "Syntactical analysis done successfully");
      lastLexical = false;
    }
  }

  public void runSyntax2Analyzer(){
    program.clearTransitions();
    if(program.getTokens() == null || program.getTokens().isEmpty()){
      showErrorDialog("Syntax Analyzer Error occurred", "Please, produce lexical analysis before syntactical one");
    }else if(!exceptions.isEmpty() && lastLexical){
      showErrorDialog("Syntax Analyzer Error occurred", "Please, produce valid lexical analysis before syntactical one");
    }else if(statesController == null || statesController.getTransitionArrayList().isEmpty()){
      showErrorDialog("Syntax Analyzer Error occurred", "Please, specify MPA analyzer transition table");
    }else if(program.getTableOfTokens() == null || program.getTableOfTokens().isEmpty()){
      showErrorDialog("Syntax Analyzer Error occurred", "Please, check out the input table of tokens");
    }else {
      SyntaxAnalyzer2 analyzer2 = new SyntaxAnalyzer2(program, statesController);
      executeAnalysis(analyzer2, "Syntactical(MPA) analysis done successfully");
      showTransitions();
      lastLexical = false;
    }
  }

  public void openTokensFile(){
    File file = getFileToRead("TXT files (*.txt)","*.txt");
    if (file != null) {
      try(Scanner s = new Scanner(file)) {
        tokensReader = new TokensReader(s);
        inputTokensTable.getItems().clear();
        if(!tokensReader.getTableOfTokens().isEmpty()){
          //заповнюємо таблиці токенів, ід, конст
          inputTokensTable.setItems(tokensReader.getTableOfTokens());
          codeColumn.setCellValueFactory(new PropertyValueFactory<>("code"));
          tokenColumn.setCellValueFactory(new PropertyValueFactory<>("token"));
        }
        setStatusLabel("Input tokens file opened");
        setMenusInputTokens(false);
        setMenusInputTransitions(false);
      } catch (FileNotFoundException ex) {
        showErrorDialog("File not found", "Try another file, please");
      }
//      System.out.println(tokensReader.getTokensText());
    }
  }

  public void openAnalyzerConfiguration(){
    File file = getFileToRead("SpreadSheet files (*.xls, *.xlsx)","*.xls", "*.xlsx");
    if (file != null) {
      new XLSReader(file);
    }
  }

  public void quit(){
    try {
      gp.getPrimaryStage().close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void showLanguageDoc(){
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.getDialogPane().setMinWidth(600);
    setAlertIcon(alert);
    alert.setTitle("BGDN programming language");
    alert.setHeaderText("Language syntax");
    alert.setContentText("<program> ::= <type> id {, id} {; <type> <ids list>} { <operator>;{<operator>;}}\n" +
            "<type> ::=  float | int\n" +
            "<operator> ::= <cycle> | <fork> | id = <expression> | <input> | <output>\n" +
            "<cycle> ::=  for <id>=<expression> to <expression> do <operator>;{<operator>;} end\n" +
            "<fork> ::= if <expression> <relation type>  <expression> then <operator> else <operator>\n" +
            "<input> ::= read( <ids list> )\n" +
            "<output> ::= write( <ids list> )\n" +
            "<relation type> ::= < | > | <= | >= | == | !=\n" +
            "<expression> ::= <T>{ + <T> |  - <T> }\n" +
            "<T> ::= <F> {* <F> |  / <F>}\n" +
            "<F> ::= id | const | (<expression>)");
    alert.showAndWait();
  }

  public void showAboutProgram(){
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    setAlertIcon(alert);
    alert.setTitle("BGDN programming language");
    alert.setHeaderText(null);
    alert.setContentText("BGDN programming language compiler.\n" +
            "Version 1.0\n" +
            "Ukraine, Kiev, KPI 2018\n" +
            "Iurchienko Bogdana\n" +
            "danarossa14@gmail.com");
    alert.showAndWait();
  }

  private void setAlertIcon(Alert alert){
    try {
      Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
      stage.getIcons().add(new Image(this.getClass().getResource("../images/binary-code.png").toString()));
    } catch (Exception e) {
      System.out.println("icon not found");
    }  }




  private void showErrorDialog(String message, String solution){
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle("Error!");
    setAlertIcon(alert);
    alert.setHeaderText(message);
    alert.setContentText(solution);
    alert.showAndWait();
  }

  private void showExceptions(){
    exceptionsTable.getItems().clear();

    exceptionsTable.setItems(exceptions);
    exceptionNumberColumn.setCellValueFactory(new PropertyValueFactory<>("number"));
    exceptionMessageColumn.setCellValueFactory(new PropertyValueFactory<>("message"));
    exceptionLineColumn.setCellValueFactory(new PropertyValueFactory<>("line"));
    exceptionAnalyzerColumn.setCellValueFactory(new PropertyValueFactory<>("analyzer"));
    exceptionTokenColumn.setCellValueFactory(new PropertyValueFactory<>("token"));
  }

  private void showTokens(){
    tokensTable.getItems().clear();

    if(!program.getTokens().isEmpty()){
      //заповнюємо таблиці токенів, ід, конст
      tokensTable.setItems(program.getTokens());
      tokenCodeColumn.setCellValueFactory(new PropertyValueFactory<>("code"));
      tokenConstantCodeColumn.setCellValueFactory(new PropertyValueFactory<>("constantCode"));
      tokenIdentifierCodeColumn.setCellValueFactory(new PropertyValueFactory<>("idCode"));
      tokenLineColumn.setCellValueFactory(new PropertyValueFactory<>("line"));
      tokenNumberColumn.setCellValueFactory(new PropertyValueFactory<>("number"));
      tokenTokenColumn.setCellValueFactory(new PropertyValueFactory<>("token"));
    }
  }

  private void showConstants(){
    //System.out.println(program.getConstants());
    constantsTable.getItems().clear();
    if(!program.getConstants().isEmpty()){
      constantsTable.setItems(program.getConstants());
      constantNumberColumn.setCellValueFactory(new PropertyValueFactory<>("number"));
      constantValueColumn.setCellValueFactory(new PropertyValueFactory<>("token"));
    }
  }

  private void showIDs(){
    identifiersTable.getItems().clear();
    if(!program.getIdentifiers().isEmpty()){
      identifiersTable.setItems(program.getIdentifiers());
      identifierNameColumn.setCellValueFactory(new PropertyValueFactory<>("token"));
      identifierNumberColumn.setCellValueFactory(new PropertyValueFactory<>("number"));
      identifierTypeColumn.setCellValueFactory(new PropertyValueFactory<>("typeName"));
    }

  }

  private void showTransitionConfigurations(){
    transitionConfigurationsTable.getItems().clear();

    if(statesController != null && !statesController.getTransitionArrayList().isEmpty()){
      //заповнюємо таблиці токенів, ід, конст
      transitionConfigurationsTable.setItems(statesController.getTransitionArrayList());
      alphaColumn.setCellValueFactory(new PropertyValueFactory<>("alpha"));
      markColumn.setCellValueFactory(new PropertyValueFactory<>("mark"));
      betaColumn.setCellValueFactory(new PropertyValueFactory<>("beta"));
      stackColumn.setCellValueFactory(new PropertyValueFactory<>("stackMark"));
      errorMessageColumn.setCellValueFactory(new PropertyValueFactory<>("errorMessage"));
    }
  }

  private void showTransitions(){
    transitionTable.getItems().clear();

//    System.out.println(program.getTransitionTable());
    if(!program.getTransitionTable().isEmpty()){
      //заповнюємо таблиці токенів, ід, конст
      transitionTable.setItems(program.getTransitionTable());
      transitionNumberColumn.setCellValueFactory(new PropertyValueFactory<>("number"));
      transitionStateColumn.setCellValueFactory(new PropertyValueFactory<>("state"));
      transitionTokenColumn.setCellValueFactory(new PropertyValueFactory<>("token"));
      transitionStackValueColumn.setCellValueFactory(new PropertyValueFactory<>("stackValue"));
    }
  }

  private void clearTables(){
    identifiersTable.getItems().clear();
    constantsTable.getItems().clear();
    tokensTable.getItems().clear();
    transitionTable.getItems().clear();
  }



  private void setMenusProgram(boolean value){
    closeProjectMenu.setDisable(value);
    saveMenu.setDisable(value);
    saveAsSourceCodeMenu.setDisable(value);
    //saveAsProgramMenu.setDisable(value);
  }

  private void setMenusInputTokens(boolean value){
    if(program != null) {
      runLexicalAnalyzerMenu.setDisable(value);
      runSyntaxAnalyzerMenu.setDisable(value);
    }
  }

  private void setMenusInputTransitions(boolean value){
    if(tokensReader != null && program != null && statesController != null)
    runSyntaxAnalyzer2Menu.setDisable(value);
  }



  private void prepareForNewProject(File file, String message){
    clearTables();
    sourceCodeArea.clear();
    program = new Program();
    fileWriter = new FileWriter(program, exceptions);
    program.setProgramFile(file);
    program.setProgramName(file.getName());
    sourceCodeArea.setDisable(false);
    setFileNameLabel(program.getProgramFile().getPath());
    setMenusProgram(false);
    setMenusInputTransitions(false);
    setMenusInputTokens(false);
    saveTablesMenu.setDisable(true);
    setStatusLabel(message);
  }

  private Program updateProgram(){
    if (!sourceCodeArea.getText().isEmpty()){
      program.setSourceCode(sourceCodeArea.getText());
    }
    if(tokensReader != null && tokensReader.getTokensText() != null){
      program.setTableOfTokens(tokensReader.getMapOfTokens());
    }
    return program;
  }

  private void executeAnalysis(AbstractAnalyzer analyzer, String successMessage){
    exceptionsTable.getItems().clear();
    this.exceptions.clear();
    if(analyzer.analyze()){
      setStatusLabel(successMessage);
    } else {
      setStatusLabel("Errors found");
      this.exceptions = analyzer.getExceptions();
      showExceptions();
    }
  }

  @SuppressWarnings("SameParameterValue")
  private File getFileToSave(String description, String ... extensions) {
    FileChooser fileChooser = new FileChooser();
    FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(description, extensions);
    fileChooser.getExtensionFilters().add(extFilter);
    return fileChooser.showSaveDialog(this.gp.getPrimaryStage());
  }

  private File getFileToRead(String description, String ... extensions) {
    FileChooser fileChooser = new FileChooser();
    FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(description, extensions);
    fileChooser.getExtensionFilters().add(extFilter);
    return fileChooser.showOpenDialog(this.gp.getPrimaryStage());
  }

  private void setFileNameLabel(String name){
    fileNameLabel.setText(name);
  }

  private void setStatusLabel(String status){
    statusLabel.setText(status);
  }

  private class XLSReader implements Runnable{
    private Thread thread;
    private File file;

    XLSReader(File file) {
      this.file = file;
      thread = new Thread(this);
      thread.start();
    }

    @Override
    public void run() {
      try {
        statesController = new StatesController(file);
        showTransitionConfigurations();
        setMenusInputTransitions(false);
        Platform.runLater(() -> setStatusLabel("MPA analyzer configuration table opened"));
      } catch (BIOException e ) {
        showErrorDialog("Invalid file", "Try another file, please");
      }
    }
  }
}
