package util;

import javafx.animation.ParallelTransition;
import javafx.animation.RotateTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class BaseController {

  protected void rotateGears(ImageView gear1, ImageView gear2, ImageView gear3)
  {
      RotateTransition rgear1 = new RotateTransition(Duration.seconds(5), gear1);
      rgear1.setFromAngle(0);
      rgear1.setToAngle(360);
      RotateTransition rgear2 = new RotateTransition(Duration.seconds(5), gear2);
      rgear2.setFromAngle(360);
      rgear2.setToAngle(0);
      RotateTransition rgear3 = new RotateTransition(Duration.seconds(5), gear3);
      rgear3.setFromAngle(0);
      rgear3.setToAngle(360);
      ParallelTransition parallelTransition = new ParallelTransition(rgear1, rgear2, rgear3);
      parallelTransition.setCycleCount(ParallelTransition.INDEFINITE);
      parallelTransition.play();
  }
    protected void loadPage(String resource) {
       // Stage currentStage = (Stage) homeButton.getScene().getWindow();
       // currentStage.close();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(resource));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Movie Ticket Booking System");
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
