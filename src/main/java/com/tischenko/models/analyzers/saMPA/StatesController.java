package com.tischenko.models.analyzers.saMPA;

import com.tischenko.controllers.BIOException;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class StatesController {
  private final transient ObservableList<Transition> transitionArrayList = FXCollections.observableArrayList();

  public StatesController(File file) throws BIOException {

    try (JsonReader jsonReader = Json.createReader(new FileReader(file))) {
      JsonArray array = jsonReader.readArray();
      JsonObject object;
      String alpha;
      String mark;
      String beta;
      String stackMark;
      String errorMessage;
      for (int i = 0; i < array.size(); i++) {
        object = array.getJsonObject(i);
        alpha = object.getString("alpha");
        mark = object.getString("mark");
        beta = object.getString("beta");
        stackMark = object.getString("stackMark");
        errorMessage = object.getString("errorMessage");
        transitionArrayList.add(new Transition(alpha, mark, beta, stackMark, errorMessage));
      }
    } catch (IOException e) {
      e.printStackTrace();
      throw new BIOException(e);
    }
  }

  Transition getTransition(int alpha, String mark) {
    for (Transition transition : transitionArrayList) {
      if (transition.alpha.getValue() == alpha && transition.mark.getValue().equals(mark)) {
        return transition;
      }
    }
    for (Transition transition : transitionArrayList) {
      if (transition.alpha.getValue() == alpha && transition.mark.getValue().equals("void")) {
        return transition;
      }
    }
    return null;
  }

  public ObservableList<Transition> getTransitionArrayList() {
    return transitionArrayList;
  }

  @Override
  public String toString() {
    return "StatesController{" +
            "transitionArrayList=" + transitionArrayList +
            '}';
  }

  @SuppressWarnings({"unused", "WeakerAccess"})
  public class Transition {
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
}
