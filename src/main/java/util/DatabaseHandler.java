package util;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler {
    private static final String JDBC_URL = "jdbc:postgresql://localhost:5432/cinema";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "password";
    static Connection connection = null;
    static Statement statement = null;
    public static Connection connect() throws SQLException{
        if(connection == null || connection.isClosed()) {
            try {
                Class.forName("org.postgresql.Driver");
                connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
                statement = connection.createStatement();
            } catch (ClassNotFoundException | SQLException e) {
                throw new SQLException("Database error. Connection not established.", e);
                //System.out.println("Database error. Connection not established.");
                //AlertMaker.showNotification("Database error", "Connection not established", AlertMaker.image_link);
            }
        }
        return connection;
    }
    public static void disconnect(){
        if(connection != null){
            try {
                if (connection != null)
                    statement.close();
                connection.close();
            } catch (SQLException e){
                System.err.println("Error disconnecting from the database: " + e.getMessage());

                //AlertMaker.showNotification("Database error", "Couldn't disconnect!", AlertMaker.image_cross);
            }
        }
    }
    public static int insertUser(Customer customer){
      /*  String sql = "INSERT INTO Users (username, passwordp, fullname, phone, email) VALUES('" + user.getUsername() + "','" +
                user.getPassword() + "','customer','" + user.getFullName() + "','" +
                user.getPhone() + "','" + user.getEmail() + "')";
        //return insertRecord(sql);*/
        int userId = DatabaseHandler.getUserID(customer.getUsername());
        if(userId != -1)
            return 0;
        int rowsAffected = -1;
        String sql = "INSERT INTO Users (username, passwordp, fullname, phone, email) VALUES(?, ?, ?, ?, ?)";
        try(Connection connection1 = connect(); PreparedStatement preparedStatement = connection1.prepareStatement(sql)) {
            preparedStatement.setString(1, customer.getUsername());
            preparedStatement.setString(2, customer.getPassword());
            preparedStatement.setString(3, customer.getFullName());
            preparedStatement.setString(4, customer.getPhone());
            preparedStatement.setString(5, customer.getEmail());

            rowsAffected = preparedStatement.executeUpdate();
            disconnect();
            //System.out.println(rowsAffected);
            return rowsAffected;
        } catch (SQLException e) {
            System.out.println("Error inserting user: " + e.getMessage());
            return 0;
        }
    }
    public static String getGenre(int id){
        String genre = null;
        try(Connection connection1 = connect(); PreparedStatement preparedStatement = connection1.prepareStatement("SELECT genrename FROM genres WHERE genreid = ?")){
            preparedStatement.setInt( 1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
               genre  = resultSet.getString("genrename");
            }
            disconnect();
        } catch (SQLException e){
            e.printStackTrace();
        }
        return genre;
    }
    public static String getDirector(int id){
        String director = null;
        try(Connection connection1 = connect(); PreparedStatement preparedStatement = connection1.prepareStatement("SELECT name FROM director WHERE directorid = ?")){
            preparedStatement.setInt( 1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                director  = resultSet.getString("name");
            }
            disconnect();
        } catch (SQLException e){
            e.printStackTrace();
        }
        return director;
    }
    public static int getGenreID(String genre){
        int genreid = -1;
        try(Connection connection1 = connect(); PreparedStatement preparedStatement = connection1.prepareStatement("SELECT genreid FROM genres WHERE genrename = ?")){
            preparedStatement.setString(1, genre);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                genreid = resultSet.getInt("genreid");
            }
            disconnect();
        } catch (SQLException e){
            e.printStackTrace();
        }
        return genreid;
    }
    public static int getUserID(String username){
        int userId = -1;
        try(Connection connection1 = connect(); PreparedStatement preparedStatement = connection1.prepareStatement("SELECT userid FROM users WHERE username = ?")){
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                userId = resultSet.getInt("userid");
            }
            disconnect();
        } catch (SQLException e){
            e.printStackTrace();
        }
        return userId;
    }
    public static int getDirectorID(String director){
        int directorid = -1;
        try(Connection connection1 = connect(); PreparedStatement preparedStatement = connection1.prepareStatement("SELECT directorid FROM director WHERE name = ?")){
            preparedStatement.setString(1, director);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                directorid = resultSet.getInt("directorid");
            }
            disconnect();
        } catch (SQLException e){
            e.printStackTrace();
        }
        return directorid;
    }
    public static int getCityID(String name){
        int cityid = -1;
        try(Connection connection1 = connect(); PreparedStatement preparedStatement = connection1.prepareStatement("SELECT cityid FROM city where name = ?")){
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                cityid = resultSet.getInt("cityid");
            }
            disconnect();
        } catch (SQLException ex){
            ex.printStackTrace();
        }
        return cityid;
    }
    public static int insertDirector(String director){
        int generatedGenreId = -1;

        try (Connection connection = connect();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "INSERT INTO director (name) VALUES (?)", Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, director);
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                // Retrieve the generated genreid
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        generatedGenreId = generatedKeys.getInt(1);
                    }
                }
            }
            disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception according to your application's error-handling strategy
        }
        return generatedGenreId;
    }
    public static int insertGenre(String genre){
        int generatedGenreId = -1;

        try (Connection connection = connect();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "INSERT INTO genres (genrename) VALUES (?)", Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, genre);
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                // Retrieve the generated genreid
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        generatedGenreId = generatedKeys.getInt(1);
                    }
                }
            }
            disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception according to your application's error-handling strategy
        }
        return generatedGenreId;
    }
    public static int insertCity(String name){
        int generatedCityId = -1;

        try (Connection connection = connect();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "INSERT INTO city (name) VALUES (?)", Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, name);
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                // Retrieve the generated cityid
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        generatedCityId = generatedKeys.getInt(1);
                    }
                }
            }
            disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception according to your application's error-handling strategy
        }
        return generatedCityId;
    }
    public static int insertMovie(Movie movie){
        Connection connection1 = null;
        try {
            connection1 = connect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        String sql = "INSERT INTO movies (title, minutes, genreid, rating, directorid, casting, details, poster, releasedate) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
        //title, minutes, genreid, rating, director, cast, details
        try(PreparedStatement preparedStatement = connection1.prepareStatement(sql)) {
            preparedStatement.setString(1, movie.getTitle());
            preparedStatement.setInt(2, movie.getMinutes());
            preparedStatement.setInt(3, movie.getGenreid());
            preparedStatement.setDouble(4, movie.getRating());
            preparedStatement.setInt(5, movie.getDirectorid());
            preparedStatement.setString(6, movie.getCast());
            preparedStatement.setString(7, movie.getDetails());
            preparedStatement.setBytes(8, movie.getPoster());
            preparedStatement.setDate(9, movie.getReleaseDate());

            int rowsAffected = preparedStatement.executeUpdate();
            System.out.println("Movie inserted with success!");
            disconnect();
            return rowsAffected;
        } catch (SQLException e) {
            System.out.println("Error inserting movie: " + e.getMessage());
            return 0;
        }
    }
    public static int insertCinema(Cinema cinema){
        Connection connection1 = null;
        try {
            connection1 = connect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        String sql = "INSERT INTO Cinemas (cinemaname, location, nrscreens, cityid) VALUES(?, ?, ?, ?)";

        try(PreparedStatement preparedStatement = connection1.prepareStatement(sql)) {
            preparedStatement.setString(1, cinema.getCinemaName());
            preparedStatement.setString(2, cinema.getCinemaLocation());
            preparedStatement.setInt(3, cinema.getNrScreens());
            preparedStatement.setInt(4, cinema.getCityID());

            int rowsAffected = preparedStatement.executeUpdate();
            if(rowsAffected > 0){
                System.out.println("Cinema inserted with success!");
                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if(generatedKeys.next()) {
                    int cinemaid = generatedKeys.getInt(1);//return the generated cinemaid
                    disconnect();
                    return cinemaid;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error inserting cinema: " + e.getMessage());
            return 0;
        }
        return 0;
    }

    public static int updatePassword(String username, String newpassword){
        String updateQuery = "UPDATE Users SET passwordp = ? WHERE username = ?";
        int rowsAffected = -1;
        try(Connection connection1 = connect(); PreparedStatement preparedStatement = connection1.prepareStatement(updateQuery)) {
            preparedStatement.setString(1, newpassword);
            preparedStatement.setString(2, username);

            rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Password updated successfully.");
            } else {
                System.out.println("User not found or password not updated.");
            }
            disconnect();
            return rowsAffected;
        }catch (SQLException e) {
            System.out.println("Error changing the password: " + e.getMessage());
            return 0;
        }
        }

    public static ResultSet executeQuery(String sql) throws SQLException {
        connect();
        ResultSet resultSet = null;
        try {
            resultSet = statement.executeQuery(sql);
        } catch (SQLException exception){
            System.out.println("Something went wrong. Data cannot be retrieved.");
        }
        return resultSet;
    }

    public static ObservableList<Reservation> getReservation(int userId) {
        ObservableList<Reservation> reservations = FXCollections.observableArrayList();
        Connection connection = null;
        try {
            connection = connect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (connection != null) {
            String query = "SELECT * from reservation WHERE userid = " + userId + " ORDER BY reservationdate ASC";

            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(query)) {

                while (resultSet.next()) {
                    int reservationID = resultSet.getInt("reservationid");
                    int userID = resultSet.getInt("userid");
                    int paymentID = resultSet.getInt("paymentid");
                    int nrtickets = resultSet.getInt("nrtickets");
                   // double price = resultSet.getDouble("paymentamount");
                    int showid = resultSet.getInt("showid");
                    String status = resultSet.getString("status");
                    Date reservationDate = resultSet.getDate("reservationdate");

                    Showtime showtime = DatabaseHandler.getShowByID(showid);
                    int movieid = showtime.getMovieId();
                    int screenid = showtime.getScreenID();
                    Movie movie = getMovieByID(movieid);
                    Screen screen = getScreenByID(screenid);
                    Payment payment = getPaymentByID(paymentID);
                    Cinema cinema = getCinemaByID(screen.getCinemaID());

                    Reservation reservation = new Reservation(reservationID, userID, paymentID, nrtickets, payment.getPaymentAmount(), status, reservationDate, cinema.getCinemaLocation(), cinema.getCinemaName(), movie.getTitle(), screen.getScreenName(), payment.getPaymentMethod());
                    reservations.add(reservation);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        disconnect();
        return reservations;
    }


    public static ObservableList<Showtime> getShow(){
        Connection connection1 = null;
        try {
            connection1 = connect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        ObservableList<Showtime> list = FXCollections.observableArrayList();
        try{
            PreparedStatement preparedStatement = connection1.prepareStatement("SELECT * FROM showtimes ORDER BY starttime DESC");
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                int showID = resultSet.getInt("showtimeid");
                int movieID = resultSet.getInt("movieid");
                int screenID = resultSet.getInt("screenid");
                Timestamp startTime = resultSet.getTimestamp("starttime");
                int regularSeats = resultSet.getInt("regularavailable");
                int premiumSeats = resultSet.getInt("premiumavailable");

                Showtime showtime = new Showtime(showID, movieID, screenID, startTime, regularSeats, premiumSeats);
                list.add(showtime);
            }
        } catch (Exception e){
            throw new RuntimeException(e);
        }
        disconnect();
        System.out.println("Number of shows in the list: " + list.size());
        return list;
    }
    public static ObservableList<Movie> searchMovies(String searchText) {
        Connection connection = null;
        try {
            connection = connect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        ObservableList<Movie> list = FXCollections.observableArrayList();

        String sql = "SELECT m.*, g.genrename, d.name FROM movies m " +
                "JOIN genres g ON m.genreid = g.genreid " +
                "JOIN director d ON m.directorid = d.directorid " +
                "WHERE title LIKE ? ORDER BY title ASC";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, "%" + searchText + "%");
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                list.add(new Movie(
                        resultSet.getInt("movieid"),
                        resultSet.getString("title"),
                        resultSet.getString("genrename"),
                        resultSet.getInt("minutes"),
                        resultSet.getString("details"),
                        resultSet.getString("casting"),
                        resultSet.getString("name"),
                        resultSet.getDouble("rating")
                ));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println("Number of movies in the search result: " + list.size());
        return list;
    }

    public static ObservableList<Movie> getMovies(){
        Connection connection1 = null;
        try {
            connection1 = connect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        ObservableList<Movie> list = FXCollections.observableArrayList();
        try{
            PreparedStatement preparedStatement = connection1.prepareStatement("SELECT m.*, g.genrename, d.name FROM movies m JOIN genres g ON m.genreid = g.genreid JOIN director d ON m.directorid = d.directorid ORDER BY title ASC");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
               // System.out.println(resultSet.getString("title"));
                list.add(new Movie(resultSet.getInt("movieid"), resultSet.getString("title"), resultSet.getString("genrename"), resultSet.getInt("minutes"),
                        resultSet.getString("details"), resultSet.getString("casting"), resultSet.getString("name"), resultSet.getDouble("rating")));
            }
        } catch (Exception e){
            throw new RuntimeException(e);
        }
        disconnect();
        System.out.println("Number of movies in the list: " + list.size());
        return list;
    }
    public static ObservableList<Cinema> getCinemas(){
        Connection connection1 = null;
        try {
            connection1 = connect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        ObservableList<Cinema> list = FXCollections.observableArrayList();
        try{
            PreparedStatement preparedStatement = connection1.prepareStatement("SELECT * FROM cinemas ORDER BY nrscreens DESC");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                System.out.println(resultSet.getString("cinemaname"));
                String sql = "SELECT name FROM city WHERE cityid = " + resultSet.getString("cityid");
                PreparedStatement preparedStatement1 = connection1.prepareStatement(sql);
                ResultSet resultSet1 = preparedStatement1.executeQuery();

                while(resultSet1.next()) {
                    list.add(new Cinema(resultSet.getInt("cinemaid"), resultSet.getString("cinemaname"), resultSet.getString("location"), resultSet.getInt("nrscreens"), resultSet1.getString("name")));
                }
            }
        } catch (Exception e){
            throw new RuntimeException(e);
        }
        disconnect();
        System.out.println("Number of cinemas in the list: " + list.size());
        return list;
    }

    public static Movie getMovieByTitle(String movieTitle) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Movie movie = null;

        try {
            connection = connect();
            String sql = "SELECT * FROM movies WHERE title = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, movieTitle);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                // Assuming your Movie class has a constructor that takes ResultSet
                movie = new Movie(resultSet);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        disconnect();
        return movie;
    }
    public static Cinema getCinemaByNameAndLocation(String cinemaName, String location) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Cinema cinema = null;

        try {
            connection = connect();
            String sql = "SELECT * FROM Cinemas WHERE cinemaname = ? AND location = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, cinemaName);
            preparedStatement.setString(2, location);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                // Assuming your Cinema class has a constructor that takes ResultSet
                cinema = new Cinema(resultSet);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        disconnect();
        return cinema;
    }


    public static Showtime getShowByID(int id) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Showtime showtime = null;

        try {
            connection = connect();
            String sql = "SELECT * FROM Showtimes WHERE showtimeid = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                // Assuming your Screen class has a constructor that takes ResultSet
                showtime = new Showtime(resultSet);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        disconnect();
        return showtime;
    }
    public static Payment getPaymentByID(int id) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Payment payment = null;

        try {
            connection = connect();
            String sql = "SELECT * FROM payments WHERE paymentid = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                // Assuming your Screen class has a constructor that takes ResultSet
                payment = new Payment(resultSet);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        disconnect();
        return payment;
    }
    public static Screen getScreenByID(int id) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Screen screen = null;

        try {
            connection = connect();
            String sql = "SELECT * FROM Screens WHERE screenid = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                // Assuming your Screen class has a constructor that takes ResultSet
                screen = new Screen(resultSet);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        disconnect();
        return screen;
    }

    public static Movie getMovieByID(int id) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Movie movie = null;

        try {
            connection = connect();
            String sql = "SELECT * FROM movies WHERE movieid = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                // Assuming your Screen class has a constructor that takes ResultSet
                movie = new Movie(resultSet);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        disconnect();
        return movie;
    }

    public static Cinema getCinemaByID(int id) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Cinema cinema = null;

        try {
            connection = connect();
            String sql = "SELECT * FROM cinemas WHERE cinemaid = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                // Assuming your Screen class has a constructor that takes ResultSet
                cinema = new Cinema(resultSet);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        disconnect();
        return cinema;
    }
   public static int insertShow(Showtime showtime) {
       Connection connection = null;
       PreparedStatement preparedStatement = null;
       int rowsAffected = 0;

       try {
           connection = connect();  // Implement your connect() method

           String sql = "INSERT INTO showtimes (movieid, screenid, starttime, regularavailable, premiumavailable) VALUES (?, ?, ?, ?, ?)";
           preparedStatement = connection.prepareStatement(sql);

           preparedStatement.setInt(1, showtime.getMovieId());
           preparedStatement.setInt(2, showtime.getScreenID());
           preparedStatement.setTimestamp(3, showtime.getStartTime());
           preparedStatement.setInt(4, showtime.getRegularAvailable());
           preparedStatement.setInt(5, showtime.getPremiumAvailable());

           rowsAffected = preparedStatement.executeUpdate();
       } catch (SQLException e) {
           e.printStackTrace();
       }
       disconnect();
       return rowsAffected;
    }

    public static Screen getScreenByNameAndCinema(String screenName, int cinemaID) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Screen screen = null;

        try {
            connection = connect();  // Assume you have a method to establish a database connection

            String query = "SELECT * FROM Screens WHERE screenname = ? AND cinemaid = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, screenName);
            preparedStatement.setInt(2, cinemaID);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                // Assuming you have a constructor in Screen class to create an object from ResultSet
                screen = new Screen(resultSet);
            }
        }  catch (SQLException e){
            e.printStackTrace();
        }
        disconnect();
        return screen;
    }

    public static int insertScreen(Screen screen) {
        try (Connection connection = connect()) {
            String sql = "INSERT INTO Screens (cinemaid, regularseats, premiumseats, screenname) VALUES (?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setInt(1, screen.getCinemaID());
                preparedStatement.setInt(2, screen.getRegularseats());
                preparedStatement.setInt(3, screen.getPremiumseats());
                preparedStatement.setString(4, screen.getScreenName());

                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1); // Return the generated screenId
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Return -1 if the insertion fails
    }
    public static ObservableList<Showtime> getShowsByDate(Timestamp date) {
        ObservableList<Showtime> list = FXCollections.observableArrayList();

        try (Connection connection = DatabaseHandler.connect()) {
            // Use PreparedStatement to prevent SQL injection
            String query = "SELECT * FROM showtimes WHERE starttime >= ? AND starttime < ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setTimestamp(1, date);
                preparedStatement.setTimestamp(2, new Timestamp(date.getTime() + 24 * 60 * 60 * 1000));// Add one day to the timestamp

                /*System.out.println("Query: " + preparedStatement.toString());
                System.out.println("Timestamp 1: " + date);
                System.out.println("Timestamp 2: " + new Timestamp(date.getTime() + 24 * 60 * 60 * 1000));*/

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        int showID = resultSet.getInt("showtimeid");
                        int movieID = resultSet.getInt("movieid");
                        int screenID = resultSet.getInt("screenid");
                        Timestamp startTime = resultSet.getTimestamp("starttime");
                        int regularSeats = resultSet.getInt("regularavailable");
                        int premiumSeats = resultSet.getInt("premiumavailable");

                        Showtime showtime = new Showtime(showID, movieID, screenID, startTime, regularSeats, premiumSeats);
                        list.add(showtime);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    public static ObservableList<Showtime> getShowsByGenre(String genre) {
        ObservableList<Showtime> list = FXCollections.observableArrayList();

        try (Connection connection = connect()) {
            String query = "SELECT s.*, g.genrename FROM showtimes s " +
                    "JOIN movies m ON s.movieid = m.movieid " +
                    "JOIN genres g ON m.genreid = g.genreid " +
                    "WHERE g.genrename = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, genre);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        int showID = resultSet.getInt("showtimeid");
                        int movieID = resultSet.getInt("movieid");
                        int screenID = resultSet.getInt("screenid");
                        Timestamp startTime = resultSet.getTimestamp("starttime");
                        int regularSeats = resultSet.getInt("regularavailable");
                        int premiumSeats = resultSet.getInt("premiumavailable");

                        Showtime showtime = new Showtime(showID, movieID, screenID, startTime, regularSeats, premiumSeats);
                        list.add(showtime);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    public static ObservableList<Reservation> getReservationByDate(Date date, int userId) {
        ObservableList<Reservation> list = FXCollections.observableArrayList();

        try (Connection connection = DatabaseHandler.connect()) {
            String query = "SELECT * FROM reservation WHERE reservationdate = ? and userid = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setDate(1, date);
                preparedStatement.setInt(2, userId);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        while (resultSet.next()) {
                            int reservationID = resultSet.getInt("reservationid");
                            int userID = resultSet.getInt("userid");
                            int paymentID = resultSet.getInt("paymentid");
                            int nrtickets = resultSet.getInt("nrtickets");
                            int showid = resultSet.getInt("showid");
                            String status = resultSet.getString("status");
                            Date reservationDate = resultSet.getDate("reservationdate");

                            Showtime showtime = DatabaseHandler.getShowByID(showid);
                            int movieid = showtime.getMovieId();
                            int screenid = showtime.getScreenID();
                            Movie movie = getMovieByID(movieid);
                            Screen screen = getScreenByID(screenid);
                            Payment payment = getPaymentByID(paymentID);
                            Cinema cinema = getCinemaByID(screen.getCinemaID());

                            Reservation reservation = new Reservation(reservationID, userID, paymentID, nrtickets, payment.getPaymentAmount(), status, reservationDate, cinema.getCinemaLocation(), cinema.getCinemaName(), movie.getTitle(), screen.getScreenName(), payment.getPaymentMethod());
                            list.add(reservation);
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public static List<String> getAllGenres() {
        List<String> genres = new ArrayList<>();

        try (Connection connection = connect()) {
            String query = "SELECT DISTINCT genrename FROM genres";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {
                    String genre = resultSet.getString("genrename");
                    genres.add(genre);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return genres;
    }
    public static int insertReview(int movieId, int userId, int rating, String comment) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = connect();
            String sql = "INSERT INTO reviews (movieid, userid, rating, review) VALUES (?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, movieId);
            preparedStatement.setInt(2, userId);
            preparedStatement.setInt(3, rating);
            preparedStatement.setString(4, comment);

            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return -1; // Indicates an error
        }
    }
    public static List<Review> getReviewsForMovie(int movieId) {
        List<Review> reviews = new ArrayList<>();

        try (Connection connection = connect();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM reviews WHERE movieid = ?")) {
            preparedStatement.setInt(1, movieId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int reviewId = resultSet.getInt("reviewid");
                    int userId = resultSet.getInt("userid");
                    int rating = resultSet.getInt("rating");
                    String reviewText = resultSet.getString("review");
                    Review review = new Review(reviewId, movieId, userId, rating, reviewText);
                    reviews.add(review);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reviews;
    }
}
