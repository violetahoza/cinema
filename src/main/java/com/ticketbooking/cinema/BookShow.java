package com.ticketbooking.cinema;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import util.AlertMaker;
import util.BaseController;
import util.DatabaseHandler;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class BookShow extends BaseController implements Initializable {
    private String movie;
    private String cinema;
    private String screen;
    private String location;
    private String showtime;
    private String date;
    private int availablePremium, availableRegular;
    @FXML
    private TextField ticket;
    @FXML
    private ToggleGroup seat;
    @FXML
    private ToggleGroup paymentMethod;
    @FXML
    private RadioButton regularradiobutton;
    @FXML
    private RadioButton Cash;
    @FXML
    private RadioButton Card;
    @FXML
    private RadioButton PayPal;
    @FXML
    private RadioButton premiumradiobutton;
    @FXML
    private Label moviename;
    @FXML
    private Label theatrename;

    @FXML
    private Label premium_seat;
    @FXML
    private Label regular_seat;
    @FXML
    private Label time;
    @FXML
    private Label hall;
    @FXML
    private Button purchase;
    @FXML
    private Button back;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        back.setOnAction(event -> handleBackButtonAction());
        purchase.setOnAction(event -> handlePurchaseButtonAction());

        movie = MoviesCustomer.movie;
        cinema = MoviesCustomer.cinema;
        screen = MoviesCustomer.screen;
        date = MoviesCustomer.date;
        availablePremium = MoviesCustomer.premiumSeats;
        availableRegular = MoviesCustomer.regularSeats;

        moviename.setText(movie);
        theatrename.setText(cinema);
        hall.setText(screen);
        time.setText(date);
        premium_seat.setText(String.valueOf(availablePremium));
        regular_seat.setText(String.valueOf(availableRegular));

        seat = new ToggleGroup();
        regularradiobutton.setToggleGroup(seat);
        premiumradiobutton.setToggleGroup(seat);

        paymentMethod = new ToggleGroup();
        Cash.setToggleGroup(paymentMethod);
        Card.setToggleGroup(paymentMethod);
        PayPal.setToggleGroup(paymentMethod);
    }

    private void handlePurchaseButtonAction() {

        //retrieve the inputs
        int nrTickets = 0;
        int paymentId = -1;
        try{
            nrTickets = Integer.parseInt(ticket.getText());
        } catch (NumberFormatException exception){
            System.out.println("Please enter a valid number!");
            AlertMaker.display("Warning!", "Please enter a valid number!");
        }

        RadioButton selectedSeat = (RadioButton) seat.getSelectedToggle();
        RadioButton selectedPaymentMethod = (RadioButton) paymentMethod.getSelectedToggle();

        if(selectedSeat == null){
            System.out.println("Please select a seat type");
            AlertMaker.display("Warning!", "Please select a seat type");
        }

        int availableSeats = (selectedSeat.getText().equals("premium")) ? availablePremium : availableRegular;
        if (nrTickets > availableSeats) {
            System.out.println("Not enough available seats. Please select a smaller number of tickets.");
            AlertMaker.display("Warning!", "Not enough available seats. Please select a smaller number of tickets or another type of seat.");
            return;
        }

        double total = calculateAmount(nrTickets, selectedSeat);

        if (selectedPaymentMethod == null) {
            System.out.println("Please select a payment method");
            AlertMaker.display("Warning!", "Please select a payment method");
            // Handle the case where no payment method is selected (return, display an error message, etc.)
        } else {
            String paymentMethodText = selectedPaymentMethod.getText();
            paymentId = insertPayment(total, paymentMethodText);
        }

        if(paymentId != -1){
            int reservationId = bookTickets(nrTickets, paymentId);

            if(reservationId != -1){
            if(selectedSeat.getText().equals("premium")){
                availablePremium -= nrTickets;
                premium_seat.setText(String.valueOf(availablePremium));

            }else {
                availableRegular -= nrTickets;
                regular_seat.setText(String.valueOf(availableRegular));
            }

            updateSeats(MoviesCustomer.showID, availableRegular, availablePremium);

            AlertMaker.display("Success!", "Booking and payment successful!");
            System.out.println("Booking successful!");
            /*Stage stage = (Stage) purchase.getScene().getWindow();
            stage.close();
            loadPage("moviesCustomer.fxml");*/
            } else {
                System.out.println("Booking successful, payment failed");
            }
        } else{
            System.out.println("Booking failed. Please try again.");
            AlertMaker.display("Error", "Booking failed. Please try again.");
        }
    }

    private void updateSeats(int showId, int regularSeats, int premiumSeats){
        try{
            Connection connection = DatabaseHandler.connect();
            String sql = "UPDATE showtimes SET regularavailable = ?, premiumavailable = ? WHERE showtimeid =?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, regularSeats);
            preparedStatement.setInt(2, premiumSeats);
            preparedStatement.setInt(3, showId);
            preparedStatement.executeUpdate();
        } catch (SQLException ex){
            throw new RuntimeException(ex);
        }
        DatabaseHandler.disconnect();
    }
    private int bookTickets(int nrTickets, int paymentId){
        try {
            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/cinema", "postgres", "password");
            String sql = "INSERT INTO reservation (userid, nrtickets, paymentid, showid, reservationdate) VALUES (?, ?, ?, ?, CURRENT_DATE)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

            int userId = DatabaseHandler.getUserID(CustomerHomeController.username);
            int showId = MoviesCustomer.showID;

            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, nrTickets);
            preparedStatement.setInt(3, paymentId);
            preparedStatement.setInt(4, showId);

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows > 0) {
                // Get the generated reservation ID
                ResultSet resultSet = preparedStatement.getGeneratedKeys();
                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return -1;
    }

    private int insertPayment(double total, String paymentMethod){

        int paymentID;

        try {
            Connection connection = DatabaseHandler.connect();
            String sql = "INSERT INTO payments (paymentamount, paymentmethod, status, paymentdate) VALUES (?, ?, ?, CURRENT_DATE)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

            preparedStatement.setDouble(1, total);
            preparedStatement.setString(2, paymentMethod);
            preparedStatement.setString(3, "pending");

            int rowsAffected = preparedStatement.executeUpdate();
            if(rowsAffected > 0){
                ResultSet resultSet = preparedStatement.getGeneratedKeys();
                if (resultSet.next())
                    return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return -1;
    }

    private double calculateAmount(int nrTickets, RadioButton selectedSeat) {
        // Assuming the prices for regular and premium seats are 15 and 30 respectively
        double seatPrice = (selectedSeat.getId().equals("premium")) ? 30.0 : 15.0;

        return nrTickets * seatPrice;
    }
    private void handleBackButtonAction() {
        System.out.println("You have been redirected to the Movies page. You can book tickets for a movie.");
        Stage stage1 = (Stage) back.getScene().getWindow();
        stage1.close();
        loadPage("moviesCustomer.fxml");
    }
}
