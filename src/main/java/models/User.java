package models;

import enums.UserType;

public class User {

    private int userID;
    private String username;
    private UserType userType;
    private String password;

    public User() {
    }

    public User(int userID, String username, UserType userType, String password) {
        if (username == null || username.isEmpty())
            throw new IllegalArgumentException("Username cannot be null or empty");

        this.userID = userID;
        this.username = username;
        this.userType = userType;
        this.password = password;
    }

    public int getUserID() {
        return userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
