package com.ticketbooking.cinema;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.Movie;
import util.AlertMaker;
import util.BaseController;
import util.DatabaseHandler;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.sql.Date;
import java.util.ResourceBundle;

public class AddMovie extends BaseController implements Initializable {
    @FXML
    private Button submit;
    @FXML
    private Button getimg;
    @FXML
    private Button back;
    @FXML
    private TextField name;
    @FXML
    private TextField director;
    @FXML
    private TextField cast;
    @FXML
    private TextField date;
    @FXML
    private TextField genre;
    @FXML
    private TextField minutes;
    @FXML
    private TextField rating;
    @FXML
    private TextArea details;
    @FXML
    private ImageView poster;
    private File file;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        submit.setOnAction(event -> addMovie());
        back.setOnAction(event -> handleBackButtonAction());
        getimg.setOnAction(event -> getPoster());
    }

    @FXML
    private void getPoster(){
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extensionFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.JPG)", "*.JPG");
        FileChooser.ExtensionFilter extensionFilterjpg = new FileChooser.ExtensionFilter("jpg files (*.jpg)", "*.jpg");
        FileChooser.ExtensionFilter extensionFilterpng = new FileChooser.ExtensionFilter("png files (*.png)", "*.png");
        FileChooser.ExtensionFilter extensionFilterjpeg = new FileChooser.ExtensionFilter("jpeg files (*.jpeg)", "*.jpeg");
        FileChooser.ExtensionFilter extensionFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.PNG)", "*.PNG");
        fileChooser.getExtensionFilters().addAll(extensionFilterJPG, extensionFilterjpeg, extensionFilterPNG, extensionFilterjpg, extensionFilterpng);

        try {
            file = fileChooser.showOpenDialog((Stage) name.getScene().getWindow());
            Image image = new Image(file.toURI().toString());
            poster.setImage(image);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    
    @FXML
    private void addMovie(){
        if(!name.getText().isEmpty() && !cast.getText().isEmpty() && !details.getText().isEmpty() && !director.getText().isEmpty() && !rating.getText().isEmpty()
        && !minutes.getText().isEmpty() && !genre.getText().isEmpty() && !date.getText().isEmpty()){
            try{
                String title = name.getText();
                String castt = cast.getText();
                String description = details.getText();
                String direct = director.getText();
                String gen = genre.getText();
                int min = Integer.parseInt(minutes.getText());
                double rate = Double.parseDouble(rating.getText());
                Date releaseDate = Date.valueOf(date.getText());

                byte[] imageData = new byte[0];
                if(file != null)
                     imageData = Files.readAllBytes(file.toPath());

                int genreId = DatabaseHandler.getGenreID(gen);
                int directorid = DatabaseHandler.getDirectorID(direct);

                if(genreId == -1)
                    genreId = DatabaseHandler.insertGenre(gen);
                if(directorid == -1)
                    directorid = DatabaseHandler.insertDirector(direct);

                if(genreId != -1 && directorid != -1){
                    Movie movie = new Movie(title, genreId, min, description, castt, directorid, rate, imageData, releaseDate);
                    //movie.setPoster(imageData);
                    int rowsAffected =   DatabaseHandler.insertMovie(movie);
                    if(rowsAffected > 0)
                        AlertMaker.display("Success", "Movie added successfully.");
                    else
                        AlertMaker.display("Error", "Movie couldn't be added:(");
                }else {
                    AlertMaker.display("Error", "Genre not found.");
            }
        } catch (NumberFormatException | IOException exception){
                AlertMaker.display("Error", "Minutes should be a number.");
                AlertMaker.display("Error", "Rating should be a number.");
                AlertMaker.display("Error", "Release date should have the structure: YYYY-MM-DD.");
            }
        }else {
            AlertMaker.display("Error", "Please fill in all fields.");
        }
    }

    @FXML
    private void handleBackButtonAction() {
        boolean confirmExit = AlertMaker.showConfirmationDialog(
                "Confirmation",
                "Exit",
                "Are you sure you want to go back?");
        if(confirmExit) {
            System.out.println("You have been redirected to the movies page.");
            Stage stage1 = (Stage) back.getScene().getWindow();
            stage1.close();
            loadPage("moviesAdmin.fxml");
        }
    }

}


