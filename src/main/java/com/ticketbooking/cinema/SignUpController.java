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
import models.Customer;
import org.controlsfx.validation.Severity;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
import util.AlertMaker;
import util.DatabaseHandler;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class SignUpController implements Initializable {
    private static final String emailPattern;
    private static final String phonePattern;

    static {
        emailPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        phonePattern = "^\\d{10}$";
    }

    @FXML
    private TextField usernametf;
    @FXML
    private TextField nametf;
    @FXML
    private TextField emailtf;
    @FXML
    private TextField phonetf;
    @FXML
    private PasswordField passwordtf;
    @FXML
    private Button loginButton;
    @FXML
    private Button signupButton;
    @FXML
    private Button cancelButton;

    public void validation(){
        ValidationSupport validationSupport = new ValidationSupport();
        validationSupport.registerValidator(usernametf, Validator.createEmptyValidator("Input required", Severity.ERROR));
        ValidationSupport validationSupport2 = new ValidationSupport();
        validationSupport2.registerValidator(nametf, Validator.createEmptyValidator("Input required", Severity.ERROR));
        ValidationSupport validationSupport3 = new ValidationSupport();
        validationSupport3.registerValidator(phonetf, Validator.createRegexValidator("The number must contain 10 digits", phonePattern, Severity.ERROR));
        ValidationSupport validationSupport4 = new ValidationSupport();
        validationSupport4.registerValidator(emailtf, Validator.createRegexValidator("Provide a correct email address.", emailPattern, Severity.ERROR));
        ValidationSupport validationSupport5 = new ValidationSupport();
        validationSupport5.registerValidator(passwordtf, Validator.createEmptyValidator("Introduce the password", Severity.ERROR));
    }

    public void initialize(URL location, ResourceBundle resources){
        validation();

        loginButton.setOnAction(event -> handleLoginButtonAction());
        signupButton.setOnAction(event -> handleSignupButtonAction());
        cancelButton.setOnAction(event -> handleCancelButtonAction());
    }

    private Customer getData(){
        Customer customer = new Customer();
        customer.setUsername(usernametf.getText());
        customer.setFullName(nametf.getText());
        customer.setEmail(emailtf.getText());
        customer.setUserType(UserType.customer);
        customer.setPhone(phonetf.getText());
        customer.setPassword(passwordtf.getText());
        return customer;
    }

    private boolean validationCheck(){
        if("".equals(usernametf.getText()))
        {
            System.out.println("Enter a username.");
            return false;
        }
        if("".equals(nametf.getText()))
        {
            System.out.println("Enter your name.");
            return false;
        }
        if("".equals(passwordtf.getText())) return false;
        if(!Pattern.matches(emailPattern, emailtf.getText()) || !Pattern.matches(phonePattern, phonetf.getText()) /*|| !Pattern.matches(passwordPattern, passwordtf.getText())*/) {
            if (!Pattern.matches(emailPattern, emailtf.getText())) {
                System.out.println("Introduce a valid email address.");
                //return false;
            }
            if (!Pattern.matches(phonePattern, phonetf.getText())) {
                System.out.println("Introduce a valid phone number.");
                //return false;
            }
        }
        return true;
    }


    @FXML
    private void handleSignupButtonAction() {
        if(validationCheck())
        {
            Customer customer = getData();
            String username = customer.getUsername();
            String name = customer.getFullName();
            String email = customer.getEmail();
            String phone = customer.getPhone();
            String password = customer.getPassword();
            //String password = PasswordHashing.hashPassword(customer.getPassword());

            int result = DatabaseHandler.insertUser(customer);

            if(result > 0) {
                System.out.println("You are now registered. Please login from the main page");
                AlertMaker.display("Success!", "Login from the main page.");
                Stage stage = (Stage) signupButton.getScene().getWindow();
                stage.close();
                openLoginWindow();
            }
            else{
                AlertMaker.display("Warning!", "The user already exists!");
                System.out.println("The user already exists. Go to the main page and login.");
                Stage stage = (Stage) signupButton.getScene().getWindow();
                stage.close();
                openLoginWindow();
            }
        }
        else System.out.println("Data is incorrect. Please enter all relevant data in proper manner.");
    }

    @FXML
    private void handleLoginButtonAction() {
        System.out.println("You have been redirected to the login page.");
        Stage stage = (Stage) signupButton.getScene().getWindow();
        stage.close();
        openLoginWindow();
    }

    private void openLoginWindow() {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("logIn.fxml"));
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
}
