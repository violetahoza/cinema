package com.ticketbooking.cinema;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import models.Reservation;
import models.Review;
import util.AlertMaker;
import util.BaseController;
import util.DatabaseHandler;

import java.net.URL;
import java.sql.Timestamp;
import java.util.List;
import java.util.ResourceBundle;

public class HistoryController extends BaseController implements Initializable {
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
    private TableView<Reservation> table = new TableView<Reservation>();
    @FXML
    private TableColumn<Reservation, String> locationCol = new TableColumn<Reservation, String>("Location");
    @FXML
    private TableColumn<Reservation, String> cinemaCol = new TableColumn<Reservation, String>("Cinema");
    @FXML
    private TableColumn<Reservation, String> movieCol = new TableColumn<Reservation, String>("Movie");
    @FXML
    private TableColumn<Reservation, String> hallCol = new TableColumn<Reservation, String>("Hall");
    @FXML
    private TableColumn<Reservation, Integer> ticketsCol = new TableColumn<Reservation, Integer>("Tickets");
    @FXML
    private TableColumn<Reservation, Double> priceCol = new TableColumn<Reservation, Double>("Price");
    @FXML
    private TableColumn<Reservation, Double> paymentCol = new TableColumn<Reservation, Double>("Payment");

    @FXML
    private TableColumn<Reservation, Timestamp> dateCol = new TableColumn<>("Date");
    @FXML
    private Button reviewButton;
    @FXML
    private ComboBox<Integer> starsComboBox;
    @FXML
    private TextArea comment;
    ObservableList<Reservation> list = FXCollections.observableArrayList();
    int userId = DatabaseHandler.getUserID(CustomerHomeController.username);

    void updateTable(){
        movieCol.setCellValueFactory(new PropertyValueFactory<>("movieTitle"));
        cinemaCol.setCellValueFactory(new PropertyValueFactory<>("cinemaName"));
        locationCol.setCellValueFactory(new PropertyValueFactory<>("cinemaLocation"));
        hallCol.setCellValueFactory(new PropertyValueFactory<>("screenName"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("reservationDate"));
        ticketsCol.setCellValueFactory(new PropertyValueFactory<>("nrtickets"));
        paymentCol.setCellValueFactory(new PropertyValueFactory<>("paymentMethod"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        list = DatabaseHandler.getReservation(userId);
        table.setItems(list);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        homeButton.setOnAction(event -> handleHomeButtonAction());
        logoutButton.setOnAction(event -> handleLogoutButtonAction());
        moviesButton.setOnAction(event -> handleMoviesButtonAction());
        settingsButton.setOnAction(event -> handleSettingsButtonAction());
        profileButton.setOnAction(event -> handleProfileButtonAction());
        historyButton.setOnAction(event -> handleHistoryButtonAction());
        reviewButton.setOnAction(event -> handleReviewButtonAction());
        starsComboBox.getItems().addAll(1, 2, 3, 4, 5);

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                handleMovieSelection();
            }
        });

        updateTable();
    }
    private int getSelectedMovieId() {
        Reservation selectedReservation = table.getSelectionModel().getSelectedItem();

        if (selectedReservation != null) {
            return DatabaseHandler.getMovieByTitle(selectedReservation.getMovieTitle()).getMovieID(); // Assuming there's a method to get the movie ID
        } else {
            return -1; // Indicating that no movie is selected
        }
    }
    @FXML
    private void handleReviewButtonAction() {
        int selectedMovieId = getSelectedMovieId();
        if (selectedMovieId != -1) {
            int rating = (int) starsComboBox.getValue();
            String comm = comment.getText();

            DatabaseHandler.insertReview(selectedMovieId, userId, rating, comm);
            AlertMaker.display("Review Submitted", "Thank you for your review!");
            clearReview();// Clear the review area
        } else {
            AlertMaker.display("Error", "Please select a movie to review.");
        }
    }
    private void handleMovieSelection() {
        // Only show the review for the initial movie and clear the text area for others
        int selectedMovieId = getSelectedMovieId();
        if (selectedMovieId != -1) {
            // Show the review for the initial movie
            List<Review> movieReviews = DatabaseHandler.getReviewsForMovie(selectedMovieId);
            if (!movieReviews.isEmpty()) {
                Review latestReview = movieReviews.get(movieReviews.size() - 1);
                comment.setText(latestReview.getReviewText());
                starsComboBox.setValue(latestReview.getRating());
            } else {
                clearReview();
            }
        } else {
            // Clear the text area for other movies
            clearReview();
        }
    }
    private void clearReview() {
        comment.clear();
        starsComboBox.setValue(null);
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
