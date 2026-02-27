package com.ticketbooking.cinema;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import util.AlertMaker;
import util.BaseController;

import java.net.URL;
import java.util.ResourceBundle;

public class CustomerHomeController extends BaseController implements Initializable {
    public static String username;
    @FXML
    private ImageView gear1;
    @FXML
    private ImageView gear2;
    @FXML
    private ImageView gear3;
    @FXML
    private Button homeButton;
    @FXML
    private Button moviesButton;
    @FXML
    private Button profileButton;
    @FXML
    private Button settingsButton;
    @FXML
    private Button logoutButton;
    @FXML
    private Button historyButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        rotateGears(gear1, gear2, gear3);
        homeButton.setOnAction(event -> handleButtonAction(event));
        logoutButton.setOnAction(event -> handleButtonAction(event));
        moviesButton.setOnAction(event -> handleButtonAction(event));
        settingsButton.setOnAction(event -> handleButtonAction(event));
        profileButton.setOnAction(event -> handleButtonAction(event));
        historyButton.setOnAction(event -> handleButtonAction(event));
    }

    @FXML
    private void handleButtonAction(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        String buttonText = clickedButton.getText();
        if(buttonText.equals("Logout"))
        {
            boolean confirmExit = AlertMaker.showConfirmationDialog(
                    "Confirmation",
                    "Exit",
                    "Are you sure you want to log out?");
            if(confirmExit) {
                System.out.println("You have been redirected to the login page.");
                Stage stage1 = (Stage) homeButton.getScene().getWindow();
                stage1.close();
                loadPage("logIn.fxml");
            }
        }
        else {
            Stage stage1 = (Stage) clickedButton.getScene().getWindow();
            stage1.close();
        }

        switch (buttonText) {
            case "Home":
                System.out.println("You have been redirected to the Home page.");
                loadPage("customer.fxml");
                break;
            case "Movies":
                System.out.println("You have been redirected to the Movies page. You can book movies.");
                loadPage("moviesCustomer.fxml");
                break;
            case "History":
                System.out.println("You have been redirected to the History page. You can view your reservations.");
                loadPage("history.fxml");
                break;
            case "Settings":
                System.out.println("You have been redirected to the Settings page. You can change your password.");
                loadPage("settingsCustomer.fxml");
                break;
            case "Profile":
                System.out.println("You have been redirected to the Profile page. You can update your details from here.");
                loadPage("profile.fxml");
                break;
            default:
                System.out.println("Invalid button: " + buttonText);
                break;
        }
    }
}
