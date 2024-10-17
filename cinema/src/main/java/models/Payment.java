package models;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Payment {
    private int paymentID;
    private int reservationID;
    private int customerID;
    private Double paymentAmount;
    private Date paymentDate;
    private String paymentMethod, status;

    public  Payment(int paymentID, int customerID, int reservationID, Double paymentAmount, Date paymentDate, String paymentMethod, String status)
    {
        this.paymentAmount = paymentAmount;
        this.reservationID = reservationID;
        this.paymentDate = paymentDate;
        this.paymentID = paymentID;
        this.paymentMethod = paymentMethod;
        this.status = status;
        this.customerID = customerID;
    }

    public Payment(ResultSet resultSet) throws SQLException {
        this.paymentMethod = resultSet.getString("paymentmethod");
        this.paymentAmount = resultSet.getDouble("paymentamount");
        this.paymentDate = resultSet.getDate("paymentdate");
        this.status = resultSet.getString("status");
    }

    public Double getPaymentAmount() {
        return paymentAmount;
    }
    public String getPaymentMethod() {
        return paymentMethod;
    }
}
