package com.ticketbooking.cinema;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import models.Showtime;
import util.AlertMaker;
import util.BaseController;
import util.DatabaseHandler;

import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class MoviesCustomer extends BaseController implements Initializable {
    public static String movie;
    public static String cinema;
    public static int regularSeats, premiumSeats, showID;
    public static String location;
    public static String screen;
    public static  String date;
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
    private TableView<Showtime> table = new TableView<Showtime>();
    @FXML
    private TableColumn<Showtime, String> locationCol = new TableColumn<Showtime, String>("Location");
    @FXML
    private TableColumn<Showtime, String> cinemaCol = new TableColumn<Showtime, String>("Cinema");
    @FXML
    private TableColumn<Showtime, String> movieCol = new TableColumn<Showtime, String>("Movie");
    @FXML
    private TableColumn<Showtime, String> hallCol = new TableColumn<Showtime, String>("Hall");
    @FXML
    private TableColumn<Showtime, Timestamp> dateCol = new TableColumn<>("Date");
    @FXML
    private Button bookButton;
    @FXML
    private Button refreshButton;
    @FXML
    private Button detailsButton;
    @FXML
    private DatePicker datePicker;
    @FXML
    private ComboBox genreComboBox;
    ObservableList<Showtime> list = FXCollections.observableArrayList();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        homeButton.setOnAction(event -> handleHomeButtonAction());
        logoutButton.setOnAction(event -> handleLogoutButtonAction());
        moviesButton.setOnAction(event -> handleMoviesButtonAction());
        settingsButton.setOnAction(event -> handleSettingsButtonAction());
        profileButton.setOnAction(event -> handleProfileButtonAction());
        historyButton.setOnAction(event -> handleHistoryButtonAction());
        detailsButton.setOnAction(event -> handleDetailsButtonAction());
        bookButton.setOnAction(event -> handleBookButtonAction());
        datePicker.setOnAction(event -> handleFilterByDateButtonAction());
        genreComboBox.setOnAction(event -> handleFilterByGenreButtonAction());
        refreshButton.setOnAction(event -> refresh());

        table.setPlaceholder(
                new Label("No rows to display"));
        updateTable();
        table.setEditable(true);
        table.setVisible(true);

        List<String> genres = DatabaseHandler.getAllGenres();
        genreComboBox.setItems(FXCollections.observableArrayList(genres));
    }

    @FXML
    private void handleFilterByGenreButtonAction() {
        String selectedGenre = (String) genreComboBox.getValue();

        if (selectedGenre != null) {
            ObservableList<Showtime> filteredList = DatabaseHandler.getShowsByGenre(selectedGenre);
            table.getItems().clear();
            table.setItems(filteredList);
        } else {
            AlertMaker.display("Filter Error", "Please select a genre to filter.");
        }
    }
    @FXML
    private void handleFilterByDateButtonAction() {
        LocalDate selectedDate = datePicker.getValue();
        Timestamp timestamp = Timestamp.valueOf(selectedDate.atStartOfDay());

        if (selectedDate != null) {
            // Perform filtering based on selectedDate
            ObservableList<Showtime> filteredList = DatabaseHandler.getShowsByDate(timestamp);
            table.getItems().clear();
            table.setItems(filteredList);
        } else {
            // Handle case where the date is not selected
            AlertMaker.display("Filter Error", "Please select a date to filter.");
        }
    }
    private void handleBookButtonAction() {
        Showtime selectedShowtime = table.getSelectionModel().getSelectedItem();
        if(selectedShowtime != null) {
            movie = selectedShowtime.getMovieTitle();
            cinema = selectedShowtime.getCinemaName();
            date = String.valueOf(selectedShowtime.getStartTime());
            location = selectedShowtime.getCinemaLocation();
            screen = selectedShowtime.getScreenName();
            premiumSeats = selectedShowtime.getPremiumAvailable();
            regularSeats = selectedShowtime.getRegularAvailable();
            showID = selectedShowtime.getShowtimeID();

            System.out.println("You have been redirected to the booking page.");
            Stage stage1 = (Stage) moviesButton.getScene().getWindow();
            stage1.close();
            loadPage("bookShow.fxml");
        }
        else {
            System.out.println("Please select a show");
        }
    }

    private void handleDetailsButtonAction() {
        Showtime selectedShowtime = table.getSelectionModel().getSelectedItem();

        if (selectedShowtime != null) {
            int selectedMovieId = selectedShowtime.getMovieId();
            System.out.println("Selected Movie ID: " + selectedMovieId);

            // Pass the selected movie ID to the next page (showMovie.fxml)
            ShowMovie.movieid = selectedMovieId;

            // Redirect to the movie's page
           // Stage stage1 = (Stage) moviesButton.getScene().getWindow();
            //stage1.close();
            loadPage("showMovie.fxml");
        } else {
            // Inform the user that no movie is selected
            AlertMaker.display("No Movie Selected", "Please select a movie before viewing details.");
        }
    }

    public void refresh(){
        table.getItems().clear();
        table.setItems(DatabaseHandler.getShow());
        handleMoviesButtonAction();
    }

    void updateTable(){
        movieCol.setCellValueFactory(new PropertyValueFactory<>("movieTitle"));
        cinemaCol.setCellValueFactory(new PropertyValueFactory<>("cinemaName"));
        locationCol.setCellValueFactory(new PropertyValueFactory<>("cinemaLocation"));
        hallCol.setCellValueFactory(new PropertyValueFactory<>("screenName"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("startTime"));

     /*   System.out.println("Number of items in the list: " + list.size());
        list.forEach(showtime -> System.out.println("Showtime: " + showtime));*/

        list = DatabaseHandler.getShow();
        table.setItems(list);
        table.refresh();
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
