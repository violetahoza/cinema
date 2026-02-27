package models;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Cinema {
    private int cinemaID;
    private String cinemaName;
    private String cinemaLocation;
    private int nrScreens;
    private String city;
    private int cityID;

    public Cinema(int cinemaID, String cinemaName, String cinemaLocation, int nrScreens, String city) {
        this.cinemaID = cinemaID;
        this.cinemaName = cinemaName;
        this.cinemaLocation = cinemaLocation;
        this.nrScreens = nrScreens;
        this.city = city;
    }
    public Cinema(String cinemaName, String cinemaLocation, int nrScreens, int cityID) {
       // this.cinemaID = cinemaID;
        this.cinemaName = cinemaName;
        this.cinemaLocation = cinemaLocation;
        this.nrScreens = nrScreens;
        this.cityID = cityID;
    }

    public Cinema(ResultSet resultSet) throws SQLException {
        this.cinemaID = resultSet.getInt("cinemaid");
        this.cinemaName = resultSet.getString("cinemaname");
        this.cinemaLocation = resultSet.getString("location");
        this.nrScreens = resultSet.getInt("nrscreens");
        this.cityID = resultSet.getInt("cityid");
    }

    public int getCityID(){
        return cityID;
    }
    public int getNrScreens() {
        return nrScreens;
    }

    public void setNrScreens(int nrScreens) {
        this.nrScreens = nrScreens;
    }

    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public int getCinemaID() {
        return cinemaID;
    }
    public String getCinemaLocation() {
        return cinemaLocation;
    }
    public String getCinemaName() {
        return cinemaName;
    }
    public void setCinemaName(String cinemaName) {
        this.cinemaName = cinemaName;
    }
    public void setCinemaID(int cinemaID) {
        this.cinemaID = cinemaID;
    }
    public void setCinemaLocation(String cinemaLocation) {
        this.cinemaLocation = cinemaLocation;
    }
}
