package models;

import enums.UserType;

public class Admin extends User {

    public Admin() {
    }

    public Admin(int userID, String username, String password) {
        super(userID, username, UserType.admin, password);
    }
}
