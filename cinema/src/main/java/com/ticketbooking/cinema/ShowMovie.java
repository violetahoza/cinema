package com.ticketbooking.cinema;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import util.BaseController;
import util.DatabaseHandler;

import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ShowMovie extends BaseController implements Initializable {
    public static int movieid;
    @FXML
    private ImageView poster;
    @FXML
    private Label name;
    @FXML
    private Label rating;
    @FXML
    private TextArea details;
    @FXML
    private TextArea date;
    @FXML
    private TextArea cast;
    @FXML
    private TextArea director;
    @FXML
    private TextArea genre;
    @FXML
    private Button back;

    public void setMovieid(int movieid) {
        this.movieid = movieid;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        back.setOnAction(event -> handleBackButtonAction());
        try{
            initializeMovie();
        } catch (Exception e){
            System.out.println("The movie details cannot be displayed.");
        }
    }
    public void initializeMovie() {
        try (Connection connection = DatabaseHandler.connect();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM movies WHERE movieid = ?");
        ) {
            preparedStatement.setInt(1, movieid);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    name.setText(resultSet.getString("title"));
                    int rate = (int) (resultSet.getDouble("rating") * 10);
                    rating.setText(String.valueOf(rate) + "%");
                    details.setText(resultSet.getString("details"));
                    cast.setText(resultSet.getString("casting"));
                    date.setText(String.valueOf(resultSet.getDate("releasedate")));

                    String gen = DatabaseHandler.getGenre(resultSet.getInt("genreid"));
                    genre.setText(gen);
                    String direct = DatabaseHandler.getDirector(resultSet.getInt("directorid"));
                    director.setText(direct);

                    InputStream inputStream = resultSet.getBinaryStream("poster");
                    Image image = new Image(inputStream);
                    poster.setImage(image);
                    DatabaseHandler.disconnect();
                } else {
                    System.out.println("No movie found with the specified ID.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error retrieving movie details: " + e.getMessage());
        }
    }

    private void handleBackButtonAction() {
        System.out.println("You have been redirected to the Movies page. You can book tickets for a movie.");
        Stage stage1 = (Stage) back.getScene().getWindow();
        stage1.close();
        //loadPage("moviesCustomer.fxml");
    }

}
