package com.ticketbooking.cinema;

import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import models.Movie;
import util.AlertMaker;
import util.BaseController;
import util.DatabaseHandler;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class MoviesAdmin extends BaseController implements Initializable {
    @FXML
    private Button detailsButton;
    @FXML
    private TableView<Movie> table = new TableView<Movie>();
    //@FXML
    //private TableColumn<Movie, Integer> idCol;
    @FXML
    private TableColumn<Movie, String> nameCol = new TableColumn<>("Movie");
    @FXML
    private TableColumn<Movie, String> genreCol = new TableColumn<>("Genre");
    @FXML
    private TableColumn<Movie, String> castCol = new TableColumn<>("Cast");
    @FXML
    private TableColumn<Movie, String> detailsCol = new TableColumn<>("Details");
    @FXML
    private TableColumn<Movie, String> directorCol = new TableColumn<>("Director");
    @FXML
    private TableColumn<Movie, Integer>  minutesCol = new TableColumn<>("Minutes");
    @FXML
    private TableColumn<Movie, Double> ratingCol = new TableColumn<>("Rating");
    @FXML
    private Button addButton;
    @FXML
    private Button removeButton;
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        homeButton.setOnAction(event -> handleHomeButtonAction());
        logoutButton.setOnAction(event -> handleLogoutButtonAction());
        moviesButton.setOnAction(event -> handleMoviesButtonAction());
        showButton.setOnAction(event -> handleShowButtonAction());
        cinemaButton.setOnAction(event -> handleCinemaButtonAction());
        settingsButton.setOnAction(event -> handleSettingsButtonAction());
        addButton.setOnAction(event -> handleAddButtonAction());
        removeButton.setOnAction(event -> handleRemoveButtonAction());
        detailsButton.setOnAction(event -> handleDetailsButtonAction());
        //refreshButton.setOnAction(event -> handleRefreshButtonAction());

        table.setPlaceholder(
                new Label("No rows to display"));
        updateTable();
        table.setItems(DatabaseHandler.getMovies());
        table.setEditable(true);
        table.setVisible(true);

        table.getSelectionModel().getSelectedItems().addListener((ListChangeListener.Change<? extends Movie> change) -> {
            while (change.next()){
                if(change.wasAdded()){
                    Movie selectedRow = change.getAddedSubList().get(0);
                    String columnName = selectedRow.getDetails();
                    System.out.println(columnName);
                }
            }
        } );
    }
    private void handleDetailsButtonAction() {
        Movie selectedMovie = table.getSelectionModel().getSelectedItem();

        if (selectedMovie != null) {
            int selectedMovieId = selectedMovie.getMovieID();
            System.out.println("Selected Movie ID: " + selectedMovieId);

            // Pass the selected movie ID to the next page (showMovie.fxml)
            ShowMovie.movieid = selectedMovieId;
            loadPage("showMovie.fxml");
        } else {
            // Inform the user that no movie is selected
            AlertMaker.display("No Movie Selected", "Please select a movie before viewing details.");
        }
    }

    @FXML
    private void refreshTable(){
        table.getItems().clear();
        table.setItems(DatabaseHandler.getMovies());
        handleMoviesButtonAction();
    }

    private void updateTable(){
        nameCol.setCellValueFactory(new PropertyValueFactory<Movie, String>("title"));
        minutesCol.setCellValueFactory(new PropertyValueFactory<Movie, Integer>("minutes"));
        castCol.setCellValueFactory(new PropertyValueFactory<Movie, String>("cast"));
        directorCol.setCellValueFactory(new PropertyValueFactory<Movie, String>("director"));
        detailsCol.setCellValueFactory(new PropertyValueFactory<Movie, String>("details"));
        ratingCol.setCellValueFactory(new PropertyValueFactory<Movie, Double>("rating"));
        genreCol.setCellValueFactory(new PropertyValueFactory<Movie, String>("genre"));

    }

    @FXML private void handleAddButtonAction(){
       // loadWindow("addMovie.fxml", "Add movie");
        System.out.println("You have been redirected to the addMovie page.");
        Stage stage1 = (Stage) moviesButton.getScene().getWindow();
        stage1.close();
        loadPage("addMovie.fxml");
    }

    public void delete()
    {
        Connection connection = null;
        try {
            connection = DatabaseHandler.connect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Movie movie = table.getSelectionModel().getSelectedItem();
        String sql = "DELETE FROM Movies WHERE title = ?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, movie.getTitle());

            int rowsAffected = preparedStatement.executeUpdate();
            if(rowsAffected > 0) {
                AlertMaker.display("Success", "The movie was deleted.");
                updateTable();
                table.setItems(DatabaseHandler.getMovies());
                DatabaseHandler.disconnect();
            }
            else AlertMaker.display("Warning", "The movie wasn't deleted.");
        } catch (Exception ex){
            AlertMaker.display("ERROR", "Movie couldn't be deleted");
            ex.printStackTrace();
        }
    }

    @FXML
    private void handleRemoveButtonAction(){
        boolean confirmExit = AlertMaker.showConfirmationDialog(
                "Confirmation",
                "Exit",
                "Are you sure you want to delete the movie?");
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


