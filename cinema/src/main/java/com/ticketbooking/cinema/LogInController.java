package com.ticketbooking.cinema;

import enums.UserType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import util.AlertMaker;
import util.AuthenticationService;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LogInController implements Initializable {
    @FXML
    private TextField usernametf;
    @FXML
    private PasswordField passwordtf1;
    @FXML
    private PasswordField passwordtf2;
    @FXML
    private Button loginButton;
    @FXML
    private Button signupButton;
    @FXML
    private Button cancelButton;

    // location - The URL used to resolve relative paths for the root object.
    // resources - The ResourceBundle used to localize the root object.

    public void initialize(URL location, ResourceBundle resources){

        usernametf.setPromptText("Enter your username.");
        passwordtf1.setPromptText("Enter your password.");
        passwordtf2.setPromptText("Enter your password.");

        loginButton.setOnAction(event -> handleLoginButtonAction());
        signupButton.setOnAction(event -> handleSignupButtonAction());
        cancelButton.setOnAction(event -> handleCancelButtonAction());
    }

    @FXML
    private void handleCancelButtonAction() {
        System.out.println("Cancel button clicked");
        boolean confirmExit = AlertMaker.showConfirmationDialog(
                "Confirmation",
                "Exit",
                "Are you sure you want to exit?");
        if(confirmExit) {
            Stage stage = (Stage) cancelButton.getScene().getWindow();
            stage.close();
        }
    }

    @FXML
    private void handleSignupButtonAction() {
        System.out.println("You have been redirected to the registration page.");
        Stage stage = (Stage) signupButton.getScene().getWindow();
        stage.close();
        openSignupWindow();
    }

    private void openSignupWindow() {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("signUp.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Movie Ticket Booking System");
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLoginButtonAction(){
        String username = usernametf.getText();
        String password = (passwordtf1 != null) ? passwordtf1.getText() : "";
        String confirmPassword = (passwordtf2 != null) ? passwordtf2.getText() : "";

        if(password.equals(confirmPassword)){
            UserType authenticatedUser = AuthenticationService.authenticateUser(username, password);
            //UserType authenticatedUser = AuthenticationService.authenticateUser(username, PasswordHashing.hashPassword(password));

            if(authenticatedUser != null) {
                System.out.println("Login successfully:)");
                AlertMaker.display("Success!", "Welcome!");
                openDashboard(authenticatedUser);
            }
            else{
                System.out.println("Authentication failed. Please try again.");
                System.out.println("username: " + username);
                System.out.println("password: " + password);
                System.out.println("confirm password: " + confirmPassword);
            }
        } else  {
            System.out.println("The passwords don't match. Please try again.");
            System.out.println("password: " + password);
            System.out.println("confirm password: " + confirmPassword);
        }
    }

    private void openDashboard(UserType userType) {
        Stage stage1 = (Stage) loginButton.getScene().getWindow();
        stage1.close();
        try{
            String dashboardFXML = (userType == UserType.admin) ? "admin.fxml" : "customer.fxml";
            if(userType == UserType.customer)
                CustomerHomeController.username = usernametf.getText();
            else
                AdminHomeController.username = usernametf.getText();
            FXMLLoader loader = new FXMLLoader(getClass().getResource(dashboardFXML));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle((userType == UserType.admin) ? "Welcome Admin!" : "Movie Ticket Booking App");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
