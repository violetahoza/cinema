package models;

import java.sql.Date;

public class Reservation {
    private int reservationID, userID, paymentID, nrtickets;
    private double price;
    private String cinemaName, cinemaLocation, status, movieTitle, screenName, paymentMethod;
    private Date reservationDate;

    public Reservation(int reservationID, int userID, int paymentID, int nrtickets, double price, String status, Date reservationDate, String cinemaLocation, String cinemaName, String movieTitle, String screenName, String paymentMethod) {
        this.reservationID =reservationID;
        this.nrtickets = nrtickets;
        this.reservationDate = reservationDate;
        this.userID = userID;
        this.paymentID = paymentID;
        this.price = price;
        this.cinemaLocation = cinemaLocation;
        this.cinemaName = cinemaName;
        this.movieTitle = movieTitle;
        this.paymentMethod = paymentMethod;
        this.screenName = screenName;
        this.status = status;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

}
