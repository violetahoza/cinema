package com.ticketbooking.cinema;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import util.AlertMaker;
import util.BaseController;
import util.DatabaseHandler;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;

import static util.DatabaseHandler.connect;

public class ProfileController extends BaseController implements Initializable {
    private static String username;
    @FXML
    private TextField usernametf1;
    @FXML
    private TextField nametf1;
    @FXML
    private TextField emailtf1;
    @FXML
    private TextField phonetf1;
    @FXML
    private Label nametf;
    @FXML
    private Label usernametf;
    @FXML
    private Label emailtf;
    @FXML
    private Label phonetf;
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
    private Button update;
    @FXML
    private Button deleteButton;
    private static final String emailPattern;
    private static final String phonePattern;
    static {
     emailPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
     phonePattern = "^\\d{10}$";
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
    private void handleUpdateButtonAction(){
        String newusername = usernametf1.getText();
        String newphone = phonetf1.getText();
        String newemail = emailtf1.getText();
        String newname = nametf1.getText();

        // Construct the UPDATE SQL statement
        String updateSql = "UPDATE Users SET username = ?, fullname = ?, phone = ?, email = ? WHERE username = ?";

        String updateQuery = "UPDATE Users SET ";
        boolean hasUpdates = false;

        if (!newusername.isEmpty()) {
            updateQuery += "username = ?, ";
            hasUpdates = true;
        }
        if (!newname.isEmpty()) {
            updateQuery += "fullname = ?, ";
            hasUpdates = true;
        }
        if (!newphone.isEmpty()) {
            updateQuery += "phone = ?, ";
            hasUpdates = true;
        }
        if (!newemail.isEmpty()) {
            updateQuery += "email = ?, ";
            hasUpdates = true;
        }
        // Remove the last comma and space if there are updates
        if (hasUpdates) {
            updateQuery = updateQuery.substring(0, updateQuery.length() - 2);
        }
        updateQuery += " WHERE username = ?";

        int rowsAffected = -1;
        try (Connection connection1 = connect(); PreparedStatement preparedStatement = connection1.prepareStatement(updateQuery)) {
            int parameterIndex = 1;

            if (!newusername.isEmpty()) {
                preparedStatement.setString(parameterIndex++, newusername);
            }
            if (!newname.isEmpty()) {
                preparedStatement.setString(parameterIndex++, newname);
            }
            if (!newphone.isEmpty()) {
                preparedStatement.setString(parameterIndex++, newphone);
            }
            if (!newemail.isEmpty()) {
                preparedStatement.setString(parameterIndex++, newemail);
            }
            preparedStatement.setString(parameterIndex++, CustomerHomeController.username);

            rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Profile updated successfully.");
                AlertMaker.display("Success", "Profile updated successfully.");

                // Update the labels with the new values
                if (!newname.isEmpty()){
                    nametf.setText(newname);
                }
                if (!newphone.isEmpty())  phonetf.setText(newphone);
                if (!newusername.isEmpty()) {
                    usernametf.setText(newusername);
                }
                if (!newemail.isEmpty()) emailtf.setText(newemail);
                if(!Objects.equals(username, usernametf.getText()))
                    CustomerHomeController.username = newusername;
                handleProfileButtonAction();
            } else {
                System.out.println("User not found or profile not updated.");
                AlertMaker.display("Error", "Failed to update profile.");
            }
            } catch (SQLException e) {
                AlertMaker.display("Error", "Error updating profile.");
                System.out.println("Error updating the profile: " + e.getMessage());
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        homeButton.setOnAction(event -> handleHomeButtonAction());
        logoutButton.setOnAction(event -> handleLogoutButtonAction());
        moviesButton.setOnAction(event -> handleMoviesButtonAction());
        settingsButton.setOnAction(event -> handleSettingsButtonAction());
        profileButton.setOnAction(event -> handleProfileButtonAction());
        historyButton.setOnAction(event -> handleHistoryButtonAction());
        deleteButton.setOnAction(event -> handleDeleteButtonAction());
        update.setOnAction(event -> handleUpdateButtonAction());

        username = CustomerHomeController.username;
       try{
           String sql = "SELECT username, fullname, phone, email FROM Users WHERE username = '" + username + "'";
           ResultSet resultSet = DatabaseHandler.executeQuery(sql);
           if (resultSet.isBeforeFirst()) {
               resultSet.next();
               usernametf.setText(resultSet.getString("username"));
               nametf.setText(resultSet.getString("fullname"));
               phonetf.setText(resultSet.getString("phone"));
               emailtf.setText(resultSet.getString("email"));
           }
           resultSet.close();
       } catch (Exception exception){
           System.out.println(exception.getMessage());
       }
    }

    @FXML
    private void handleDeleteButtonAction() {
        boolean confirmExit = AlertMaker.showConfirmationDialog(
                "Confirmation",
                "Exit",
                "Are you sure you want to delete your account?");
        if(confirmExit) {
            Connection connection = null;
            try {
                connection = DatabaseHandler.connect();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            String customer = CustomerHomeController.username;
            String sql = "DELETE FROM Users WHERE username = ?";
            try{
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, customer);

                int rowsAffected = preparedStatement.executeUpdate();
                if(rowsAffected > 0) {
                    AlertMaker.display("Success", "The account was deleted.");
                    DatabaseHandler.disconnect();
                    System.out.println("You have been redirected to the login page.");
                    Stage stage1 = (Stage) deleteButton.getScene().getWindow();
                    stage1.close();
                    loadPage("logIn.fxml");                }
                else AlertMaker.display("Warning", "The account wasn't deleted.");
            } catch (Exception ex){
                AlertMaker.display("ERROR", "The account couldn't be deleted");
                ex.printStackTrace();
            }
        }
    }

}
