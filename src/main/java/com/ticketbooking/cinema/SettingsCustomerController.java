package com.ticketbooking.cinema;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import util.AlertMaker;
import util.BaseController;
import util.DatabaseHandler;

import java.net.URL;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class SettingsCustomerController extends BaseController implements Initializable {
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
    @FXML
    private Button updateButton;
    @FXML
    private PasswordField oldpassword;
    @FXML
    private PasswordField newpassword;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        rotateGears(gear1, gear2, gear3);
        homeButton.setOnAction(event -> handleHomeButtonAction());
        logoutButton.setOnAction(event -> handleLogoutButtonAction());
        moviesButton.setOnAction(event -> handleMoviesButtonAction());
        settingsButton.setOnAction(event -> handleSettingsButtonAction());
        profileButton.setOnAction(event -> handleProfileButtonAction());
        historyButton.setOnAction(event -> handleHistoryButtonAction());
        updateButton.setOnAction(event -> handleUpdateButtonAction());
    }
    @FXML
    private void handleUpdateButtonAction() {
        String password = null;
        try{
            String sql = "SELECT passwordp FROM Users WHERE username = '" + CustomerHomeController.username + "'";
            ResultSet resultSet = DatabaseHandler.executeQuery(sql);
            resultSet.next();
            password = resultSet.getString("passwordp");
           // System.out.println(password);
            resultSet.close();
            DatabaseHandler.disconnect();
        } catch (Exception exception){
            System.out.println(exception.getMessage());
        }

        String oldpass = oldpassword.getText();
        String newpass = newpassword.getText();
        if(oldpass.equals(password))
        {
            if("".equals(newpass))
            {
                System.out.println("The new password cannot be empty!");
            }
            else if(newpass.equals(oldpass)){
                System.out.println("The new password should be different from the current password!");
            }
            else {
                int ok = DatabaseHandler.updatePassword(CustomerHomeController.username, newpass);
                if(ok > 0)
                {
                    AlertMaker.display("Success!", "The password has been updated.");
                    System.out.println("You have been redirected to the Settings page. You can change your password here.");
                    Stage stage1 = (Stage) homeButton.getScene().getWindow();
                    stage1.close();
                    loadPage("settingsCustomer.fxml");
                }
            }
        }
        else {
            System.out.println("Please check your current password!");
        }
    }

    @FXML
    private void handleHistoryButtonAction() {
        System.out.println("You have been redirected to the History page.");
        Stage stage1 = (Stage) homeButton.getScene().getWindow();
        stage1.close();
        loadPage("history.fxml");
    }

    @FXML
    private void handleProfileButtonAction() {
        System.out.println("You have been redirected to the Profile page. You can see your profile's information.");
        Stage stage1 = (Stage) homeButton.getScene().getWindow();
        stage1.close();
        loadPage("profile.fxml");
    }

    @FXML
    private void handleLogoutButtonAction() {
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

    @FXML
    private void handleHomeButtonAction() {
        System.out.println("You have been redirected to the home page.");
        Stage stage1 = (Stage) homeButton.getScene().getWindow();
        stage1.close();
        loadPage("customer.fxml");
    }

    @FXML
    private void handleSettingsButtonAction() {
        System.out.println("You have been redirected to the Settings page. You can change your password here.");
        Stage stage1 = (Stage) homeButton.getScene().getWindow();
        stage1.close();
        loadPage("settingsCustomer.fxml");
    }
    @FXML
    private void handleMoviesButtonAction() {
        System.out.println("You have been redirected to the Movies page. You can book tickets for a movie.");
        Stage stage1 = (Stage) homeButton.getScene().getWindow();
        stage1.close();
        loadPage("moviesCustomer.fxml");
    }
}
