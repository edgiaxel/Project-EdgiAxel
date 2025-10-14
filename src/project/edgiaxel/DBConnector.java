package project.edgiaxel;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnector {

    // You left the server details blank, so I'm using the standard Laragon defaults.
    // IF THIS DOESN'T WORK, CHECK YOUR LARAGON SETTINGS, YOU IDIOT.
    private static final String URL = "jdbc:mysql://localhost:3306/Project-EdgiAxel";
    private static final String USER = "root";
    private static final String PASSWORD = "4090"; // Laragon's default is often blank

    /**
     * Attempts to establish a connection to the MySQL database.
     *
     * @return A valid Connection object, or null if connection fails.
     */
    public static Connection getConnection() {
        Connection connection = null;
        try {
            // 1. Load the JDBC Driver (Required for older Java versions, good habit)
            Class.forName("com.mysql.cj.jdbc.Driver");

            // 2. Establish the connection
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("DBConnector: Successfully connected to Project_EdgiAxel!");
            return connection;

        } catch (ClassNotFoundException e) {
            System.err.println("DBConnector ERROR: MySQL JDBC Driver not found.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("DBConnector ERROR: Failed to connect to database. Check URL, username, or if Laragon is running!");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Safely closes the database connection.
     *
     * @param connection The Connection object to close.
     */
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
