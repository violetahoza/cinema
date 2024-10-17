package models;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Movie {
    private int movieID;
    private byte[] posterByte;
    private String title;
    private String genre;
    private int genreid;
    private  int directorid;
    private Date releaseDate;
    private int minutes;
    private double rating;
    private String director;
    private String cast;
    private String details;

    public Movie(String title, int genreid, int minutes, String details, String cast, int directorid, double rating, byte[] posterByte, Date releaseDate){
        this.cast = cast;
        this.minutes = minutes;
       // this.movieID = movieID;
        this.releaseDate = releaseDate;
        this.rating = rating;
        this.details = details;
        this.directorid = directorid;
        this.genreid = genreid;
        this.posterByte = posterByte;
        this.title = title;
       // this.poster = null;
    }

    public Movie(ResultSet resultSet) throws SQLException {
        this.minutes = resultSet.getInt("minutes");
        this.movieID = resultSet.getInt("movieid");
        this.title = resultSet.getString("title");
        this.details = resultSet.getString("details");
        this.releaseDate = resultSet.getDate("releasedate");
        this.directorid = resultSet.getInt("directorid");
       // this.director = resultSet.getString("director");
        this.cast = resultSet.getString("casting");
        this.rating = resultSet.getDouble("rating");
        this.posterByte = resultSet.getBytes("poster");
    }

    public int getGenreid() {
        return genreid;
    }
    public int getDirectorid(){
        return directorid;
    }

    public byte[] getPoster() {
        return posterByte;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Movie(int movieID, String title, String genre, int minutes,  String description,  String cast, String director, double rating) {
        this.movieID = movieID;
        this.title = title;
        this.genre = genre;
        //this.releaseDate = releaseDate;
        this.minutes = minutes;
        this.rating = rating;
        this.cast = cast;
        this.director = director;
        this.details = description;
    }

    public double getRating() {
        return rating;
    }
    public void setRating(double rating) {
        this.rating = rating;
    }
    public void setDirector(String director) {
        this.director = director;
    }
    public String getDirector() {
        return director;
    }
    public String getCast() {
        return cast;
    }

    public int getMovieID() {
        return movieID;
    }
    public Date getReleaseDate() {
        return releaseDate;
    }
    public int getMinutes() {
        return minutes;
    }
    public String getTitle() {
        return title;
    }
    public String getGenre() {
        return genre;
    }
}
