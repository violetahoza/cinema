package models;

import util.DatabaseHandler;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Screen{
    private int screenID;
    private int cinemaID;
    private Cinema cinema;
    private String screenName;
    private int premiumseats;
    private int regularseats;
    public Screen(int screenId, int cinemaID, String screenName, int regularseats, int premiumseats)
    {
        this.screenID = screenId;
        this.cinema = DatabaseHandler.getCinemaByID(cinemaID);
        this.cinemaID = cinemaID;
        this.screenName = screenName;
        this.regularseats = regularseats;
        this.premiumseats = premiumseats;
        this.cinema = DatabaseHandler.getCinemaByID(cinemaID);
    }
    public Screen(int cinemaID, String screenName, int regularseats, int premiumseats)
    {
       // this.screenID = screenId;
        this.cinema = DatabaseHandler.getCinemaByID(cinemaID);
        this.cinemaID = cinemaID;
        this.screenName = screenName;
        this.regularseats = regularseats;
        this.premiumseats = premiumseats;
        this.cinema = DatabaseHandler.getCinemaByID(cinemaID);
    }

    public Screen(ResultSet resultSet) throws SQLException {
        this.screenID = resultSet.getInt("screenid");
        this.cinemaID = resultSet.getInt("cinemaid");
        this.cinema = DatabaseHandler.getCinemaByID(resultSet.getInt("cinemaid"));
        this.regularseats = resultSet.getInt("regularseats");
        this.premiumseats = resultSet.getInt("premiumseats");
        this.screenName = resultSet.getString("screenname");
        this.cinema = DatabaseHandler.getCinemaByID(cinemaID); // Initialize Cinema object
    }

    public String getScreenName() {
        return screenName;
    }
    public int getCinemaID() {
        return cinemaID;
    }
    public int getScreenID() {
        return screenID;
    }
    public int getPremiumseats() {
        return premiumseats;
    }

    public int getRegularseats() {
        return regularseats;
    }

    public Cinema getCinema() {
        return cinema;
    }

    public void setCinema(Cinema cinema) {
        this.cinema = cinema;
    }
}
