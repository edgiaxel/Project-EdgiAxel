package project.edgiaxel;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnector {
    private static final String URL = "jdbc:mysql://localhost:3306/Project-EdgiAxel";
    private static final String USER = "root";
    private static final String PASSWORD = "4090"; 

    public static Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("DBConnector: Successfully connected!");
            return connection;

        } catch (ClassNotFoundException e) {
            System.err.println("DBConnector ERROR : MySQL JDBC Driver not found.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("DBConnector ERROR : Failed to connect to database.");
            e.printStackTrace();
        }
        return null;
    }

    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("DBConnector: Connection closed.");
            } catch (SQLException e) {
                System.err.println("DBConnector ERROR: Failed to close connection.");
                e.printStackTrace();
            }
        }
    }
}
