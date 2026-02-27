package util;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class AlertMaker {

    public static void display(String title, String message) {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(250);

        Label label = new Label();
        label.setText(message);

        Button closeButton = new Button("EXIT");
        closeButton.setOnAction(event -> window.close());

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, closeButton);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: #800000;");
        label.setStyle("-fx-text-fill: white; -fx-font-size: 20px; -fx-font-family: 'Bookman Old Style'; -fx-font-weight: bold; -fx-font-style: italic;");
        closeButton.setStyle("-fx-text-fill: white; -fx-background-color: #4d0000; -fx-font-size: 20px; -fx-font-family: 'Bookman Old Style'; -fx-font-weight: bold; -fx-font-style: italic;");

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }

    public static boolean showConfirmationDialog(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.initStyle(StageStyle.UTILITY);

        // Customizing buttons (Optional)
        ButtonType yesButton = new ButtonType("Yes");
        ButtonType noButton = new ButtonType("No");

        alert.getButtonTypes().setAll(yesButton, noButton);

        // Show and wait for the user's response
        alert.showAndWait();

        // Return true if the user clicks Yes, false otherwise
        return alert.getResult() == yesButton;
    }
}
