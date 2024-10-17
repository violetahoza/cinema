package util;

import enums.UserType;

import java.sql.*;

public class AuthenticationService {
    private static final String JDBC_URL = "jdbc:postgresql://localhost:5432/cinema";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "password";

    public static UserType authenticateUser(String username, String password){

        String sql = "SELECT usertype FROM Users WHERE username = ? AND passwordP = ?";

       try(Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
           PreparedStatement preparedStatement = connection.prepareStatement(sql)){

           preparedStatement.setString(1, username);
           preparedStatement.setString(2, password);

           ResultSet resultSet = preparedStatement.executeQuery();

           if(resultSet.next()){
               String userType = resultSet.getString("userType");
               return UserType.valueOf(userType);
           }
       } catch (SQLException e){
           e.printStackTrace();
       }
       return null;
    }
}
