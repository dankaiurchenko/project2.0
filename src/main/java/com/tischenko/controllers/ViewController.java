package com.tischenko.controllers;

import com.tischenko.Compiler;
import com.tischenko.models.InputTokensReader;
import com.tischenko.models.Program;
import com.tischenko.models.analyzers.AbstractAnalyzer;
import com.tischenko.models.analyzers.CompilerException;
import com.tischenko.models.analyzers.la.LexicalAnalyzer;
import com.tischenko.models.analyzers.poliz.AbstractPoliz;
import com.tischenko.models.analyzers.poliz.AdvancedPoliz;
import com.tischenko.models.analyzers.poliz.PolizStateDump;
import com.tischenko.models.analyzers.sa.ExtendedStateDump;
import com.tischenko.models.analyzers.sa.PrecedenceRelationController;
import com.tischenko.models.analyzers.sa.SyntacticalAnalyzer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class ViewController {
  private Program program;
  private Compiler gp;
  private FileWriter fileWriter;
  private InputTokensReader inputTokensReader;
  private boolean lastLexical;
  private ObservableList<CompilerException> exceptions = FXCollections.observableArrayList();
  private AbstractPoliz poliz;

  // таблиця вхідних токенів
  @FXML
  private TableView<InputTokensReader.Token> inputTokensTable;
  @FXML
  private TableColumn<InputTokensReader.Token, Integer> codeColumn;
  @FXML
  private TableColumn<InputTokensReader.Token, String> tokenColumn;


  // таблиця ходу розбору
  @FXML
  private TableView<PolizStateDump> polizStateDumpTableView;
  @FXML
  private TableColumn<PolizStateDump, String> numberPColumn;
  @FXML
  private TableColumn<PolizStateDump, String> stackPColumn;
  @FXML
  private TableColumn<PolizStateDump, String> polizPColumn;


  // панель статуса
  @FXML
  private Label fileNameLabel;
  @FXML
  private Label statusLabel;

  // для коду програми
  @FXML
  private TextArea sourceCodeArea;
  //menus
  @FXML
  private MenuItem closeProjectMenu;
  @FXML
  private MenuItem saveMenu;
  @FXML
  private MenuItem saveAsSourceCodeMenu;
  @FXML
  private MenuItem runLexicalAnalyzerMenu;
  @FXML
  private MenuItem saveTablesMenu;
  private PrecedenceRelationController precedenceRelationController;

  public ViewController() {
  }

  public void setGp(Compiler gp) {
    this.gp = gp;
  }

  public void newProgram() {
    File file = getFileToSave("TXT files (*.txt)", "*.txt");
    if (file != null) {
      prepareForNewProject(file, "New program is created");
    }
  }

  public void openSourceCode() {
    File file = getFileToRead("*.txt");
    if (file != null) {
      prepareForNewProject(file, "Source code is opened");
      try (Scanner s = new Scanner(file)) {
        while (s.hasNextLine()) {
          String newline = s.nextLine().replace((char) -1, ' ');
//          newline = ((int)newline.charAt(newline.length()-1) == -1) ? newline.substring(0, newline.length() - 1) : newline;
          sourceCodeArea.appendText(newline + System.getProperty("line.separator"));
        }
      } catch (FileNotFoundException ex) {
//        System.err.println(ex.getMessage());
        showErrorDialog("File not found", "Try another file, please");
      }
    }
  }

  public void save() {
    if (program.getProgramName().endsWith(".txt")) {
      try {
        fileWriter.writeSourceCodeIntoFile(updateProgram().getProgramFile(), sourceCodeArea.getText());
      } catch (BIOException e) {
        showErrorDialog("Error occurred", "Please, check out the file and the program source code");
      }
    }
  }

  public void saveTables() {
    try {
      if (!program.getTableOfTokens().isEmpty()) {
        fileWriter.writeTokenFile();
      }
      if (!program.getConsts().isEmpty()) {
        fileWriter.writeConstantsFile();
      }
      if (!program.getIdents().isEmpty()) {
        fileWriter.writeIDsFile();
      }
      if (!exceptions.isEmpty()) {
        fileWriter.writeExceptionsFile();
      }
      if (fileWriter.isWritten()) {
        setStatusLabel("Tables are written successfully");
      }
    } catch (BIOException e) {
      showErrorDialog("File Writing Error occurred", "Please solve your problems");
    }
  }

  public void saveSourceCode() {
    File file = getFileToSave("TXT files (*.txt)", "*.txt");
    if (file != null) {
      try {
        updateProgram();
        fileWriter.writeSourceCodeIntoFile(file, sourceCodeArea.getText());
        setStatusLabel("Source code is saved successfully");
      } catch (BIOException e) {
        showErrorDialog("Error occurred", "Please, check out the file and the program source code");
      }
    }
  }

  public void closeCurrentProject() {
    //усі таблиці очищаються
    program = null;
    saveTablesMenu.setDisable(true);
    sourceCodeArea.setDisable(true);
//    clearTables();
    setMenusProgram(true);
    setMenusInputTokens(true);
  }

  public void runLexicalAnalyzer() {
    try {
      if (inputTokensReader == null || inputTokensReader.getTableOfTokens().isEmpty()) {
        openTokensFile();
      }
      if (sourceCodeArea.getText().isEmpty()) {
        showErrorDialog("Lexical Analyzer Error occurred", "Please, write some code before lexical analysis");
      } else {
        program.cleanup().setTableOfTokens(inputTokensReader.getMapOfTokens());
        executeAnalysis(new LexicalAnalyzer(new StringReader(sourceCodeArea.getText()), program), "Lexical analysis done successfully");
        showTokens();
        showIDs();
        showConstants();
        saveTablesMenu.setDisable(false);
        lastLexical = true;
      }
    } catch (BIOException e) {
      showErrorDialog(e.getMessage(), "Try another file, please");
    }
  }

  private void openTokensFile() throws BIOException {
//    File file = getFileToRead("TXT files (*.txt)", "*.txt");
    //TODO absolute path to tokens file
    File file = new File("D:\\Projects\\3course\\compilers\\настя\\tokens.txt");
    try (Scanner s = new Scanner(file)) {
      inputTokensReader = new InputTokensReader(s);
      inputTokensTable.getItems().clear();
      if (!inputTokensReader.getTableOfTokens().isEmpty()) {
        //заповнюємо таблиці токенів, ід, конст
        inputTokensTable.setItems(inputTokensReader.getTableOfTokens());
        codeColumn.setCellValueFactory(new PropertyValueFactory<>("code"));
        tokenColumn.setCellValueFactory(new PropertyValueFactory<>("token"));
      }
      setStatusLabel("Input tokens file opened");
      setMenusInputTokens(false);
    } catch (FileNotFoundException ex) {
      throw new BIOException("File not found");
    }
//      System.out.println(inputTokensReader.getTokensText());
  }

  private void openGrammarFile() throws IOException, BIOException {
    //TODO absolute path to grammarFile
    File file = new File("D:\\Documents\\GitHub\\project\\src\\main\\resources\\forlab\\grammar.txt");
    try (BufferedReader reader = new BufferedReader(
            new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
      precedenceRelationController = new PrecedenceRelationController(reader);
      setStatusLabel("Grammar file opened");
      setMenusInputTokens(false);
      writeRelationTable(file, precedenceRelationController);
      setStatusLabel("Grammar relation table is written to a file");
    }
  }

  private void writeRelationTable(File file, PrecedenceRelationController pRController) throws BIOException {
    RelationTableWriter relationTableWriter = new RelationTableWriter(
            pRController.getRelationTable(), pRController.getMaxTokenLength());
    relationTableWriter.saveRelationsTable(file);
  }


  public void quit() {
    try {
      gp.getPrimaryStage().close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void showLanguageDoc() {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.getDialogPane().setMinWidth(600);
    setAlertIcon(alert);
    alert.setTitle("Programming language");
    alert.setHeaderText("Language syntax");
    alert.setContentText("<Program>::= Program  Ident  <DeclarationList>  \\{ <OperatorsList> \\}\n" +
            "\n" +
            "<DeclarationList>::=  <Declaration> { ; <Declaration> }\n" +
            "<Declaration>::= <Type> <IdentList>\n" +
            "<Type>::= integer | short | label\n" +
            "<IdentList>::= Ident  { , Ident }\n" +
            "\n" +
            "<OperatorsList>::= <Operator> { ; <Operator> }\n" +
            "<Operator>::= <AssignmentOrTagging> | <Input> | <Output> | <Loop> | <Condition> | <UnconditionalTransition> | <Range>\n" +
            "<AssignmentOrTagging>::= Ident  ( = <Exp>  | : )\n" +
            "<Input>::= readLine(<IdentList>)\n" +
            "<Output>::= writeLine(<IdentList>)\n" +
            "<Loop>::= do <OperatorsList> while <LogicExp> \n" +
            "<Condition>::= if <Attitude> then <UnconditionalTransition>\n" +
            "<UnconditionalTransition> :: = goto Ident\n" +
            "<Range>::=<Exp>..<Exp>\n" +
            "\n" +
            "<LogExp>::=  <LogTerm> { or <LogTerm> }\n" +
            "<LogTerm>::=  <LogMulti> { and <LogMulti>}\n" +
            "<LogMulti::= <Attitude> | [<LogExp>] | not <LogMulti>\n" +
            "<Attitude>::= <Exp> <ExpSign> <Exp>\n" +
            "<ExpSign>:: = > | < | >= | <= | == | !=\n" +
            "\n" +
            "<Exp>::= ( - | ^)<Term>( + <Term>|  - <Term> )\n" +
            "<Term>::= <Multi>(  * <Multi> |  / <Multi> )\n" +
            "<Multi>::=  <PrimaryExp> { ** <PrimaryExp>}\n" +
            "<PrimaryExp>::= (<Exp>) | Ident | Const\n");
    alert.showAndWait();
  }

  private void setAlertIcon(Alert alert) {
    try {
      Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
      stage.getIcons().add(new Image(this.getClass().getResource("../images/binary-code.png").toString()));
    } catch (Exception e) {
      System.out.println("icon not found");
    }
  }

  private void showErrorDialog(String message, String solution) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle("Error!");
    setAlertIcon(alert);
    alert.setHeaderText(message);
    alert.setContentText(solution);
    alert.showAndWait();
  }

  private void showExceptions() {
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(getClass()
              .getResource("/ExceptionsView.fxml"));
      fxmlLoader.setControllerFactory(c -> new ExceptionsController(this.exceptions));
      AnchorPane root = fxmlLoader.load();
      Scene scene = new Scene(root, 900, 300);
      Stage stage = new Stage();
      stage.setTitle("Exceptions table");
      stage.setScene(scene);
      ((ExceptionsController) fxmlLoader.getController()).show();
      stage.show();
    } catch (IOException ioe) {
      ioe.printStackTrace();
      showErrorDialog("Error occurred", "Try later");

    }
  }

  private void showTokens() {
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(getClass()
              .getResource("/TokensView.fxml"));
      fxmlLoader.setControllerFactory(c -> new TokensController(program.getTokens()));
      AnchorPane root = fxmlLoader.load();
      Scene scene = new Scene(root, 600, 400);
      Stage stage = new Stage();
      stage.setTitle("Tokens table");
      stage.setScene(scene);
      ((TokensController) fxmlLoader.getController()).show();
      stage.show();
    } catch (IOException ioe) {
      ioe.printStackTrace();
      showErrorDialog("Error occurred", "Try later");
    }

  }


  private void showConstants() {
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(getClass()
              .getResource("/ConstantsView.fxml"));
      fxmlLoader.setControllerFactory(c -> new ConstantsController(program.getConsts()));
      AnchorPane root = fxmlLoader.load();
      Scene scene = new Scene(root, 300, 200);
      Stage stage = new Stage();
      stage.setTitle("Constants table");
      stage.setScene(scene);
      ((ConstantsController) fxmlLoader.getController()).show();
      stage.show();
    } catch (IOException ioe) {
      ioe.printStackTrace();
      showErrorDialog("Error occurred", "Try later");
    }

  }

  private void showIDs() {
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(getClass()
              .getResource("/IdentifiersView.fxml"));
      fxmlLoader.setControllerFactory(c -> new IdentifiersController(program.getIdents()));
      AnchorPane root = fxmlLoader.load();
      Scene scene = new Scene(root, 300, 200);
      Stage stage = new Stage();
      stage.setTitle("Identifiers table");
      stage.setScene(scene);
      ((IdentifiersController) fxmlLoader.getController()).show();
      stage.show();
    } catch (IOException ioe) {
      ioe.printStackTrace();
      showErrorDialog("Error occurred", "Try later");

    }


  }

  private void showAnalyzeFlow(ObservableList<ExtendedStateDump> stateDumps) {
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(getClass()
              .getResource("/SyntaxTable.fxml"));
      fxmlLoader.setControllerFactory(c -> new SyntaxTabController());
      AnchorPane root = fxmlLoader.load();
      Scene scene = new Scene(root, 900, 600);
      Stage stage = new Stage();
      stage.setTitle("Syntax analyze flow");
      stage.setScene(scene);
      ((SyntaxTabController) fxmlLoader.getController()).show(stateDumps);
      stage.show();
    } catch (IOException ioe) {
      ioe.printStackTrace();
      showErrorDialog("Error occurred", "Try later");
    }
  }

  private void setMenusProgram(boolean value) {
    closeProjectMenu.setDisable(value);
    saveMenu.setDisable(value);
    saveAsSourceCodeMenu.setDisable(value);
    //saveAsProgramMenu.setDisable(value);
  }

  private void setMenusInputTokens(boolean value) {
    if (program != null) {
      runLexicalAnalyzerMenu.setDisable(value);
    }
  }

  private void prepareForNewProject(File file, String message) {
//    clearTables();
    sourceCodeArea.clear();
    program = new Program();
    fileWriter = new FileWriter(program, exceptions);
    program.setProgramFile(file);
    program.setProgramName(file.getName());
    sourceCodeArea.setDisable(false);
    setFileNameLabel(program.getProgramFile().getPath());
    setMenusProgram(false);
    setMenusInputTokens(false);
    saveTablesMenu.setDisable(true);
    setStatusLabel(message);
  }

  private Program updateProgram() {
    if (!sourceCodeArea.getText().isEmpty()) {
      program.setSourceCode(sourceCodeArea.getText());
    }
    if (inputTokensReader != null && inputTokensReader.getTokensText() != null) {
      program.setTableOfTokens(inputTokensReader.getMapOfTokens());
    }
    return program;
  }

  private void executeAnalysis(AbstractAnalyzer analyzer, String successMessage) {
//    exceptionsTable.getItems().clear();
    this.exceptions.clear();
    if (analyzer.analyze()) {
      setStatusLabel(successMessage);
    } else {
      setStatusLabel("Errors found");
      this.exceptions = analyzer.getExceptions();
      showExceptions();
    }
  }

  @SuppressWarnings("SameParameterValue")
  private File getFileToSave(String description, String... extensions) {
    FileChooser fileChooser = new FileChooser();
    FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(description, extensions);
    fileChooser.getExtensionFilters().add(extFilter);
    return fileChooser.showSaveDialog(this.gp.getPrimaryStage());
  }

  private File getFileToRead(String... extensions) {
    FileChooser fileChooser = new FileChooser();
    FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", extensions);
    fileChooser.getExtensionFilters().add(extFilter);
    return fileChooser.showOpenDialog(this.gp.getPrimaryStage());
  }

  private void setFileNameLabel(String name) {
    fileNameLabel.setText(name);
  }

  private void setStatusLabel(String status) {
    statusLabel.setText(status);
  }

  public void runSyntax3Analyzer() {
    try {
      openGrammarFile();
      if (program == null || program.getTokens() == null || program.getTokens().isEmpty()) {
        showErrorDialog("Syntax Analyzer Error occurred", "Please, produce lexical analysis before syntactical one");
      } else if (!exceptions.isEmpty() && lastLexical) {
        showErrorDialog("Syntax Analyzer Error occurred", "Please, produce valid lexical analysis before syntactical one");
      } else if (precedenceRelationController == null || precedenceRelationController.relationsIsEmpty()) {
        showErrorDialog("No grammar specified", "Please, check out your grammar");
      } else {
        poliz = new AdvancedPoliz(program.getIdents());
        SyntacticalAnalyzer analyzer3 = new SyntacticalAnalyzer(program, precedenceRelationController, poliz);
        executeAnalysis(analyzer3, "Relation based syntax analysis done successfully");
        showAnalyzeFlow(analyzer3.getStateDumps());
        lastLexical = false;
      }
    } catch (FileNotFoundException ex) {
      showErrorDialog("File not found (grammar file)", "Try another file, please");
    } catch (BIOException e) {
      showErrorDialog(e.getMessage(), "Valid your grammar!");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }


  public void runPoliz() {
    if (poliz != null && exceptions.isEmpty()) {
      poliz.execute();
      setStatusLabel("Poliz executed");
      polizStateDumpTableView.getItems().clear();
      if (poliz.getPolizStateDumps() != null && !poliz.getPolizStateDumps().isEmpty()) {
        polizStateDumpTableView.setItems(poliz.getPolizStateDumps());
        numberPColumn.setCellValueFactory(new PropertyValueFactory<>("number"));
        stackPColumn.setCellValueFactory(new PropertyValueFactory<>("stack"));
        polizPColumn.setCellValueFactory(new PropertyValueFactory<>("poliz"));
      }
    }
  }
}

