package com.tischenko;

import com.tischenko.controllers.ViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class Compiler extends Application {

  private Stage primaryStage;

  @Override
  public void start(Stage primaryStage) {
    try {
      this.primaryStage = primaryStage;
      this.primaryStage.setTitle("Compiler");
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
  }

}
