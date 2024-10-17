package com.ticketbooking.cinema;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
      try {
          FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("logIn.fxml"));
          Scene scene;
          scene = new Scene(fxmlLoader.load(), 608, 400);
          stage.setTitle("Movie Ticket Booking System");
          stage.setResizable(false);
          stage.setScene(scene);
          stage.show();
      } catch (IOException e){
          e.printStackTrace();
      }
    }

    public static void main(String[] args) {
        launch();
    }
}