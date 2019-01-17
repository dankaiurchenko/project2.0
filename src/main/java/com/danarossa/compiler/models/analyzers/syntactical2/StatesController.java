package com.danarossa.compiler.models.analyzers.syntactical2;

import com.danarossa.compiler.controllers.BIOException;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

public class StatesController {
  private final transient ObservableList<Transition> transitionArrayList = FXCollections.observableArrayList();

  public StatesController(File file) throws BIOException {
    try (Workbook workbook = WorkbookFactory.create(file)){
      String alpha = "";
      String mark = "";
      String beta = "";
      String stackMark = "";
      String errorMessage = "";
      Sheet sheet = workbook.getSheetAt(0);
      DataFormatter dataFormatter = new DataFormatter();
      Iterator<Row> rowIterator = sheet.rowIterator();
      while (rowIterator.hasNext()) {
        Row row = rowIterator.next();
        Iterator<Cell> cellIterator = row.cellIterator();
        if(cellIterator.hasNext())
        alpha = dataFormatter.formatCellValue(cellIterator.next()).trim();
        if(cellIterator.hasNext())
        mark = dataFormatter.formatCellValue(cellIterator.next()).trim();
        if(cellIterator.hasNext())
        beta = dataFormatter.formatCellValue(cellIterator.next()).trim();
        if(cellIterator.hasNext())
        stackMark = dataFormatter.formatCellValue(cellIterator.next()).trim();
        if(cellIterator.hasNext())
        errorMessage = dataFormatter.formatCellValue(cellIterator.next()).trim();
        if(isNumeric(alpha) && isNumeric(stackMark) && !alpha.equals(""))
          transitionArrayList.add(new Transition(alpha, mark, beta, stackMark, errorMessage));
      }
    } catch (IOException | InvalidFormatException  e) {
      e.printStackTrace();
      throw new BIOException(e);
    }
  }

  private boolean isNumeric(String str)
  {
    return str.matches("^[-+]?\\d*$");
  }

  public Transition getTransition(int alpha, String mark){
     //System.out.println(alpha + "   " + mark  );
    for(Transition transition: transitionArrayList){
      if(transition.alpha.getValue() == alpha && transition.mark.getValue().equals(mark)){
        return transition;
      }
    }
    for(Transition transition: transitionArrayList){
      if(transition.alpha.getValue() == alpha && transition.mark.getValue().equals("void")){
        return transition;
      }
    }
    return null;
  }

  public ObservableList<Transition> getTransitionArrayList() {
    return transitionArrayList;
  }

  @SuppressWarnings("unused")
  public class Transition{
    private IntegerProperty alpha;
    private StringProperty mark;
    private StringProperty beta;
    private IntegerProperty stackMark;
    private StringProperty errorMessage;

    Transition(String alpha, String mark, String beta, String stackMark, String errorMessage) {
      this.alpha = new SimpleIntegerProperty(Integer.parseInt(alpha));
      this.mark = new SimpleStringProperty(mark);
      this.beta = new SimpleStringProperty(beta);
      this.stackMark = new SimpleIntegerProperty(Integer.parseInt((stackMark.equals("")) ? "0" : stackMark));
      this.errorMessage = new SimpleStringProperty(errorMessage);
    }

    public int getAlpha() {
      return alpha.get();
    }

    public IntegerProperty alphaProperty() {
      return alpha;
    }

    public String getMark() {
      return mark.get();
    }

    public StringProperty markProperty() {
      return mark;
    }

    public String getBeta() {
      return beta.get();
    }

    public StringProperty betaProperty() {
      return beta;
    }

    public int getStackMark() {
      return stackMark.get();
    }

    public IntegerProperty stackMarkProperty() {
      return stackMark;
    }

    public String getErrorMessage() {
      return errorMessage.get();
    }

    public StringProperty errorMessageProperty() {
      return errorMessage;
    }

    @Override
    public String toString() {
      return "Transition{" +
              "alpha=" + alpha +
              ", mark='" + mark + '\'' +
              ", beta='" + beta + '\'' +
              ", stackMark=" + stackMark +
              ", errorMessage='" + errorMessage + '\'' +
              "}\n";
    }
  }

  @Override
  public String toString() {
    return "StatesController{" +
            "transitionArrayList=" + transitionArrayList +
            '}';
  }
}
