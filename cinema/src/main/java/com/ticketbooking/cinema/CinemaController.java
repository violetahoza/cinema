package com.ticketbooking.cinema;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import models.Cinema;
import models.Screen;
import util.AlertMaker;
import util.BaseController;
import util.DatabaseHandler;

import java.net.URL;
import java.sql.*;
import java.util.Random;
import java.util.ResourceBundle;

public class CinemaController extends BaseController implements Initializable {
    public static int cinemaid;
    private static int screensCnt;
    @FXML
    private TableView<Cinema> table = new TableView<Cinema>();
    @FXML
    private TableColumn<Cinema, String> nameCol = new TableColumn<>("Name");
    @FXML
    private TableColumn<Cinema, String> nrScreens = new TableColumn<>("Screens");
    @FXML
    private TableColumn<Cinema, String> locationCol = new TableColumn<>("Location");
    @FXML
    private TableColumn<Cinema, String> cityCol = new TableColumn<>("City");
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
    @FXML
    private TextField nametf;
    @FXML
    private TextField locationtf;
    @FXML
    private TextField screenstf;
    @FXML
    private TextField citytf;
    private ObservableList<Cinema> data;
    int index = -1;
    ObservableList<Cinema> list, dataList;

    public void addCinema(){
        if(!nametf.getText().isEmpty() && !locationtf.getText().isEmpty() && !screenstf.getText().isEmpty() && !citytf.getText().isEmpty()) {
            try{
                String name = nametf.getText();
                String location = locationtf.getText();
                int screens = Integer.parseInt(screenstf.getText());
                String city = citytf.getText();

                int cityId = DatabaseHandler.getCityID(city);

                if(cityId == -1)
                    cityId = DatabaseHandler.insertCity(city);

                if(cityId != -1){
                    Cinema cinema = new Cinema(name, location, screens, cityId);
                    DatabaseHandler.insertCinema(cinema);
                    int cinemaid = DatabaseHandler.getCinemaByNameAndLocation(name, location).getCinemaID();
                    System.out.println(cinemaid);
                    AlertMaker.display("Success", "Cinema added successfully.");
                    for (int screenNumber = 1; screenNumber <= screens; screenNumber++) {
                        String screenName = "Hall " + (char) ('A' + screenNumber - 1);
                        Random random = new Random();
                        int regularseats = random.nextInt(21) + 30; // generate a random number between 30 and 50
                        int premiumseats = random.nextInt(21) + 10; // generate a random number between 10 and 30

                        // Create screen
                        Screen screen = new Screen(cinemaid, screenName, regularseats, premiumseats);
                        int screenID = DatabaseHandler.insertScreen(screen);
                    }
                    updateTable();
                    DatabaseHandler.disconnect();
                }else {
                    AlertMaker.display("Error", "City not found.");
                }
            } catch (NumberFormatException e) {
                AlertMaker.display("Error", "Screens should be a number.");
            }
        }else {
            AlertMaker.display("Error", "Please fill in all fields.");
        }
    }

    public void edit() {
        if (index >= 0) {
            try {
                String sql = "UPDATE cinemas SET cinemaname = ?, location = ?, nrscreens = ?, cityid = ? WHERE cinemaid = ?";
                try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/cinema", "postgres", "password");
                     PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

                    if (connection.isClosed()) {
                        // Obtain a new connection
                        System.out.println("Connection is closed");
                        Connection connection1 = DatabaseHandler.connect();
                        // Re-prepare the statement with the new connection
                        PreparedStatement preparedStatement1 = connection.prepareStatement(sql);
                    }
                    int newScreens = Integer.parseInt(screenstf.getText());
                    preparedStatement.setString(1, nametf.getText());
                    preparedStatement.setString(2, locationtf.getText());
                    preparedStatement.setInt(3, newScreens);

                    // Obtain cityid based on the selected city
                    String cityName = citytf.getText();
                    int cityId = DatabaseHandler.getCityID(cityName);
                    preparedStatement.setInt(4, cityId);
                    preparedStatement.setInt(5, cinemaid);

                    int rowsAffected = preparedStatement.executeUpdate();
                    if (rowsAffected > 0) {
                        Cinema updatedCinema = list.get(index);
                        updatedCinema.setCinemaName(nametf.getText());
                        updatedCinema.setCinemaLocation(locationtf.getText());
                        updatedCinema.setNrScreens(newScreens);
                        updatedCinema.setCity(citytf.getText());

                        if(newScreens > screensCnt){
                            for(int i = screensCnt + 1; i <= newScreens; i++){
                                String screenName = "Hall " + (char) ('A' + i - 1);
                                Random random = new Random();
                                int regularSeats = random.nextInt(21) + 30; // generate a random number between 30 and 50
                                int premiumSeats = random.nextInt(21) + 10; // generate a random number between 10 and 30

                                Screen screen = new Screen(cinemaid, screenName, regularSeats, premiumSeats);
                                int screenID = DatabaseHandler.insertScreen(screen);
                            }
                        } else if(newScreens < screensCnt){
                            for(int i = newScreens + 1; i <= screensCnt; i++)
                            {
                                String screenName = "Hall " + (char) ('A' + i - 1);
                                String sql1 = "DELETE FROM screens WHERE cinemaid = ? and screenname = ?";
                                PreparedStatement preparedStatement1 = connection.prepareStatement(sql1);
                                preparedStatement1.setInt(1, cinemaid);
                                preparedStatement1.setString(2, screenName);
                                preparedStatement1.executeUpdate();
                            }
                        }
                        screensCnt = newScreens;
                        AlertMaker.display("Success", "Cinema updated successfully.");
                        updateTable();
                    } else {
                        AlertMaker.display("Warning", "No cinema found with the specified name.");
                    }
                }
            } catch (Exception ex) {
                AlertMaker.display("ERROR", "Something went wrong during the update.");
                ex.printStackTrace();
            }
        } else {
            AlertMaker.display("Error", "Please select a cinema to update.");
        }
    }

    public void updateTable(){
        nameCol.setCellValueFactory(new PropertyValueFactory<Cinema, String>("cinemaName"));
        locationCol.setCellValueFactory(new PropertyValueFactory<Cinema, String>("cinemaLocation"));
        nrScreens.setCellValueFactory(new PropertyValueFactory<Cinema, String>("nrScreens"));
        cityCol.setCellValueFactory(new PropertyValueFactory<Cinema, String>("city"));
        list = DatabaseHandler.getCinemas();
        table.setItems(list);
    }

    public void delete(){
        Connection connection = null;
        try {
            connection = DatabaseHandler.connect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        String sql = "DELETE FROM cinemas WHERE cinemaname = ? and location = ?";
        try{
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, nametf.getText());
                preparedStatement.setString(2, locationtf.getText());

                int rowsAffected = preparedStatement.executeUpdate();
                if(rowsAffected > 0) {
                    AlertMaker.display("Success", "The cinema was deleted.");
                    updateTable();
                }
                else AlertMaker.display("Warning", "No cinema found with the specified name.");

        } catch (Exception ex){
            AlertMaker.display("ERROR", "Cinema couldn't be deleted");
            ex.printStackTrace();
        }
        DatabaseHandler.disconnect();
    }
    ObservableList<Cinema> listView = FXCollections.observableArrayList();

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
        Cinema selectedCinema = table.getSelectionModel().getSelectedItem();
        if(selectedCinema != null){
            cinemaid = selectedCinema.getCinemaID();
            screensCnt = selectedCinema.getNrScreens();
        }
        edit();
        //updateTable();
    }

    @FXML
    private void handleRefreshButtonAction() {
        refreshTable();
    }

    @FXML
    private void refreshTable(){
        table.setItems(DatabaseHandler.getCinemas());
        handleCinemaButtonAction();
    }
    @FXML private void handleAddButtonAction(){
        // loadWindow("addMovie.fxml", "Add movie");
        addCinema();
    }
    @FXML private void handleRemoveButtonAction(){
        boolean confirmExit = AlertMaker.showConfirmationDialog(
                "Confirmation",
                "Exit",
                "Are you sure you want to delete the cinema?");
        if(confirmExit) {
            delete();
        }
    }
    public void getSelected(javafx.scene.input.MouseEvent mouseEvent) {
        index = table.getSelectionModel().getSelectedIndex();
        if (index <= -1){
            return;
        }
        nametf.setText(nameCol.getCellData(index).toString());
        locationtf.setText(locationCol.getCellData(index).toString());
        screenstf.setText(String.valueOf(nrScreens.getCellData(index)));
        citytf.setText(cityCol.getCellData(index).toString());
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


