package com.tischenko;

import com.tischenko.controllers.ViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Compiler extends Application {

  private Stage primaryStage;

  public static void main(String[] args) {
    Application.launch(args);
  }

  @Override
  public void start(Stage primaryStage) {
    try {
      this.primaryStage = primaryStage;
      this.primaryStage.setTitle("Compiler");
      FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/RootPane.fxml"));//, resources);
      Parent rootLayout = fxmlLoader.load();

      ViewController controller = fxmlLoader.getController();
      controller.setGp(this);

      this.primaryStage.setScene(new Scene(rootLayout));
      this.primaryStage.show();

    } catch (IOException | NullPointerException e) {
      e.printStackTrace();
    }
  }

  public Stage getPrimaryStage() {
    return primaryStage;
  }

}
