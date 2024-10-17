package com.ticketbooking.cinema;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import models.Cinema;
import models.Movie;
import models.Screen;
import models.Showtime;
import util.AlertMaker;
import util.BaseController;
import util.DatabaseHandler;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class ShowController extends BaseController implements Initializable {
    private static int showID;
    @FXML
    private TableView<Showtime> table = new TableView<Showtime>();
   @FXML
   private TableColumn<Showtime, String> cinemaCol = new TableColumn<Showtime, String>("Cinema");
    @FXML
    private TableColumn<Showtime, String> movieCol = new TableColumn<Showtime, String>("Movie");
    @FXML
    private TableColumn<Showtime, String> screenCol = new TableColumn<Showtime, String>("Screen");
    @FXML
    private TableColumn<Showtime, String> locationCol = new TableColumn<Showtime, String>("Location");
    @FXML
    private TableColumn<Showtime, Timestamp> startCol = new TableColumn<>("Start");
    @FXML
    private TextField movietf;
    @FXML
    private TextField locationtf;
    @FXML
    private TextField screentf;
    @FXML
    private TextField cinematf;
    @FXML
    private TextField datetf;
    @FXML
    private Button addButton;
    @FXML
    private Button removeButton;
    @FXML
    private Button updateButton;
    @FXML
    private Button refreshButton;
    @FXML
    private Button homeButton;
    @FXML
    private Button moviesButton;
    @FXML
    private Button showButton;
    @FXML
    private Button settingsButton;
    @FXML
    private Button logoutButton;
    @FXML
    private Button cinemaButton;
    int index = -1;
    ObservableList<Showtime> list = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        homeButton.setOnAction(event -> handleHomeButtonAction());
        logoutButton.setOnAction(event -> handleLogoutButtonAction());
        moviesButton.setOnAction(event -> handleMoviesButtonAction());
        showButton.setOnAction(event -> handleShowButtonAction());
        cinemaButton.setOnAction(event -> handleCinemaButtonAction());
        settingsButton.setOnAction(event -> handleSettingsButtonAction());
        addButton.setOnAction(event -> handleAddButtonAction());
        updateButton.setOnAction(event -> handleUpdateButtonAction());
        removeButton.setOnAction(event -> handleRemoveButtonAction());
        //refreshButton.setOnAction(event -> handleRefreshButtonAction());
        //  init();
        table.setPlaceholder(
                new Label("No rows to display"));
        updateTable();
        table.setEditable(true);
        table.setVisible(true);
        //showLoadingIndicator();
    }

    @FXML
    private void handleUpdateButtonAction() {
        edit();
        //updateTable();
    }

    void addShow(){
        try {
            String movieTitle = movietf.getText();
            String cinemaName = cinematf.getText();
            String location = locationtf.getText();
            String screenName = screentf.getText();
            Timestamp startTime = Timestamp.valueOf(datetf.getText());
            //int screenID = table.getSelectionModel().getSelectedItem().getScreenID();

            Movie movie = DatabaseHandler.getMovieByTitle(movieTitle);
            Cinema cinema = DatabaseHandler.getCinemaByNameAndLocation(cinemaName, location);
            Screen screen;

            if (movie != null && cinema != null) {
                screen = DatabaseHandler.getScreenByNameAndCinema(screenName, cinema.getCinemaID());

                if (screen != null) {
                    Showtime showtime = new Showtime(movie.getMovieID(), screen.getScreenID(), startTime, screen.getRegularseats(), screen.getPremiumseats());
                    int rowsAffected = DatabaseHandler.insertShow(showtime);

                    if (rowsAffected > 0) {
                        AlertMaker.display("Success", "Show added successfully.");
                        refreshTable();
                    } else {
                        AlertMaker.display("Error", "Failed to add show.");
                    }
            } else {
                AlertMaker.display("Error", "Movie, cinema, or screen not found.");
            }
        }
        } catch (Exception e) {
            AlertMaker.display("Error", "Invalid input or date format.");
            e.printStackTrace();
        }
    }

    void delete(){
        Connection connection = null;
        try {
            connection = DatabaseHandler.connect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        int showID = table.getSelectionModel().getSelectedItem().getShowtimeID();
        String sql = "DELETE FROM showtimes WHERE showtimeid = ?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, showID);

            int rowsAffected = preparedStatement.executeUpdate();
            if(rowsAffected > 0) {
                AlertMaker.display("Success", "The show was deleted.");
                updateTable();
                DatabaseHandler.disconnect();
            }
            else AlertMaker.display("Warning", "No show found with the specified id.");

        } catch (Exception ex){
            AlertMaker.display("ERROR", "The show couldn't be deleted");
            ex.printStackTrace();
        }
    }

    void edit(){
        if (index >= 0) {
            try {
                String sql = "UPDATE showtimes SET movieid = ?, screenid = ?, starttime = ? where showtimeid = ?";
                try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/cinema", "postgres", "password");
                     PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

                    if (connection.isClosed()) {
                        System.out.println("Connection is closed");
                        Connection connection1 = DatabaseHandler.connect();
                        PreparedStatement preparedStatement1 = connection.prepareStatement(sql);
                    }

                    String title = movietf.getText();
                    Movie movie = DatabaseHandler.getMovieByTitle(title);
                    int movieId = movie.getMovieID();
                    Cinema cinema = DatabaseHandler.getCinemaByNameAndLocation(cinematf.getText(), locationtf.getText());
                    String screenName = screentf.getText();
                    Screen screen = DatabaseHandler.getScreenByNameAndCinema(screenName, cinema.getCinemaID());

                    preparedStatement.setInt(1, movieId);
                    preparedStatement.setInt(2, screen.getScreenID());
                    preparedStatement.setTimestamp(3, Timestamp.valueOf(datetf.getText()));
                    preparedStatement.setInt(4, showID);

                    int rowsAffected = preparedStatement.executeUpdate();
                    if (rowsAffected > 0) {
                        AlertMaker.display("Success", "Show updated successfully.");
                        updateTable();
                    } else {
                        AlertMaker.display("Warning", "The show was not found.");
                    }
                }
            } catch (Exception ex) {
                AlertMaker.display("ERROR", "Something went wrong during the update.");
                ex.printStackTrace();
            }
        } else {
            AlertMaker.display("Error", "Please select a show to update.");
        }
    }

    void updateTable(){
        movieCol.setCellValueFactory(new PropertyValueFactory<>("movieTitle"));
        cinemaCol.setCellValueFactory(new PropertyValueFactory<>("cinemaName"));
        locationCol.setCellValueFactory(new PropertyValueFactory<>("cinemaLocation"));
        screenCol.setCellValueFactory(new PropertyValueFactory<>("screenName"));
        startCol.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        list = DatabaseHandler.getShow();
        table.setItems(list);
    }


    public void getSelected(javafx.scene.input.MouseEvent mouseEvent) {
        index = table.getSelectionModel().getSelectedIndex();
        if (index <= -1){
            System.out.println("index is negative");
            return;
        }

        Showtime selectedShowtime = list.get(index);
        showID = selectedShowtime.getShowtimeID();

        movietf.setText(selectedShowtime.getMovieTitle());
        locationtf.setText(selectedShowtime.getCinemaLocation());
        screentf.setText(selectedShowtime.getScreenName());
        cinematf.setText(selectedShowtime.getCinemaName());
        datetf.setText(String.valueOf(selectedShowtime.getStartTime()));
    }
    @FXML
    private void handleRefreshButtonAction() {
        refreshTable();
    }

    @FXML
    private void refreshTable(){
        table.setItems(DatabaseHandler.getShow());
        handleShowButtonAction();
    }

    @FXML private void handleAddButtonAction(){
        // loadWindow("addMovie.fxml", "Add movie");
        addShow();
    }
    @FXML private void handleRemoveButtonAction(){
        boolean confirmExit = AlertMaker.showConfirmationDialog(
                "Confirmation",
                "Exit",
                "Are you sure you want to delete the show?");
        if(confirmExit) {
            delete();
        }
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
        loadPage("admin.fxml");
    }
    @FXML
    private void handleSettingsButtonAction() {
        System.out.println("You have been redirected to the Settings page. You can change your password here.");
        Stage stage1 = (Stage) homeButton.getScene().getWindow();
        stage1.close();
        loadPage("settingsAdmin.fxml");
    }

    private void handleCinemaButtonAction() {
        System.out.println("You have been redirected to the Cinema page. You can modify the list of cinemas.");
        Stage stage1 = (Stage) homeButton.getScene().getWindow();
        stage1.close();
        loadPage("cinema.fxml");
    }
    @FXML
    private void handleShowButtonAction() {
        System.out.println("You have been redirected to the Shows page. You can add new show and remove old ones.");
        Stage stage1 = (Stage) homeButton.getScene().getWindow();
        stage1.close();
        loadPage("show.fxml");
    }
    @FXML
    private void handleMoviesButtonAction() {
        System.out.println("You have been redirected to the Movies page. You can update the list of movies.");
        Stage stage1 = (Stage) homeButton.getScene().getWindow();
        stage1.close();
        loadPage("moviesAdmin.fxml");
    }
}
