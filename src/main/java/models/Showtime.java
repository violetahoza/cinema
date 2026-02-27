package models;

import util.DatabaseHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class Showtime {
    private java.sql.Timestamp start;
    private int showtimeID;
    private int movieId;
    private int screenID;
    private int regularAvailable, premiumAvailable;
    private Timestamp startTime;
    private String movieTitle, cinemaName, cinemaLocation, screenName;
    private Movie movie;
    private Screen screen;
    private Cinema cinema;

    public Showtime(int showtimeID, int movieId, int screenID, Timestamp startTime, int regularAvailable, int premiumAvailable) {
        this.showtimeID = showtimeID;
        this.movieId = movieId;
        this.screenID = screenID;
        this.premiumAvailable = premiumAvailable;
        this.regularAvailable = regularAvailable;
        this.startTime = startTime;
        this.movie = DatabaseHandler.getMovieByID(movieId);
        this.screen = DatabaseHandler.getScreenByID(screenID);
        if (screen != null) {
            this.cinema = screen.getCinema();
            this.cinemaName = cinema != null ? cinema.getCinemaName() : "";
            this.cinemaLocation = cinema != null ? cinema.getCinemaLocation() : "";
            this.screenName = screen.getScreenName();
        }
        this.movieTitle = movie != null ? movie.getTitle() : "";
    }

    public Showtime(int movieId, int screenID, Timestamp startTime, int regularAvailable, int premiumAvailable) {
        //this.showtimeID = showtimeID;
        this.movieId = movieId;
        this.regularAvailable = regularAvailable;
        this.premiumAvailable = premiumAvailable;
        this.screenID = screenID;
        this.startTime = startTime;
        this.movie = DatabaseHandler.getMovieByID(movieId);
        this.screen = DatabaseHandler.getScreenByID(screenID);
        if (screen != null) {
            this.cinema = screen.getCinema();
            this.cinemaName = cinema != null ? cinema.getCinemaName() : "";
            this.cinemaLocation = cinema != null ? cinema.getCinemaLocation() : "";
            this.screenName = screen.getScreenName();
        }
        this.movieTitle = movie != null ? movie.getTitle() : "";
    }

    public Showtime(ResultSet resultSet) throws SQLException {
        this.showtimeID = resultSet.getInt("showtimeid");
        this.movieId = resultSet.getInt("movieid");
        this.screenID = resultSet.getInt("screenid");
        this.startTime = resultSet.getTimestamp("starttime");
        this.premiumAvailable = resultSet.getInt("premiumavailable");
        this.regularAvailable = resultSet.getInt("regularavailable");
    }

    public int getPremiumAvailable() {
        return premiumAvailable;
    }

    public int getRegularAvailable() {
        return regularAvailable;
    }
    public int getMovieId() {
        return movieId;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public int getShowtimeID() {
        return showtimeID;
    }

    public int getScreenID() {
        return screenID;
    }
    public String getMovieTitle() {
        return movieTitle;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getCinemaName() {
        return cinemaName;
    }

    public String getCinemaLocation() {
        return cinemaLocation;
    }

}
