package models;
import enums.UserType;
public class Customer extends User {

    private String fullName;
    private String email;
    private String phone;

    public Customer() {
    }

    public Customer(int userID, String username, String fullName, String email, String phone, String password) {
        super(userID, username, UserType.customer, password);
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
