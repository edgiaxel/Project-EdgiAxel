package project.edgiaxel.dao;

import project.edgiaxel.DBConnector;
import project.edgiaxel.model.Driver;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;

public class DriverDAO {

    private static DriverDAO instance;

    private DriverDAO() {
    }

    public static DriverDAO getInstance() {
        if (instance == null) {
            instance = new DriverDAO();
        }
        return instance;
    }

    /**
     * Fetches all drivers in the database.
     */
    public ObservableList<Driver> getAllDrivers() {
        ObservableList<Driver> drivers = FXCollections.observableArrayList();
        String sql = "SELECT * FROM driver ORDER BY last_name, first_name";

        try ( Connection conn = DBConnector.getConnection();  Statement stmt = conn.createStatement();  ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                drivers.add(new Driver(
                        rs.getInt("driver_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("nationality")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching all drivers: " + e.getMessage());
        }
        return drivers;
    }

    /**
     * Inserts a new driver into the global pool.
     */
    public boolean insertDriver(Driver driver) {
        String sql = "INSERT INTO driver (first_name, last_name, nationality) VALUES (?, ?, ?)";
        Connection conn = DBConnector.getConnection();
        PreparedStatement pstmt = null;

        try {
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, driver.getFirstName());
            pstmt.setString(2, driver.getLastName());
            pstmt.setString(3, driver.getNationality());
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    driver.setDriverId(generatedKeys.getInt(1)); // Update object ID
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error inserting driver: " + e.getMessage());
            return false;
        } finally {
            DBConnector.closeConnection(conn);
        }
        return false;
    }

    /**
     * Deletes a driver from the global pool.
     */
    public boolean deleteDriver(Driver driver) {
        String sql = "DELETE FROM driver WHERE driver_id = ?";
        Connection conn = DBConnector.getConnection();
        PreparedStatement pstmt = null;

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, driver.getDriverId());
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            if (e.getErrorCode() == 1451) {
                System.err.println("Error: Cannot delete driver due to active Team assignments (FK constraint).");
                return false;
            }
            System.err.println("Error deleting driver: " + e.getMessage());
            return false;
        } finally {
            DBConnector.closeConnection(conn);
        }
    }

    /**
     * Updates an existing driver’s details in the database.
     */
    public boolean updateDriver(Driver driver) {
        String sql = "UPDATE driver SET first_name = ?, last_name = ?, nationality = ? WHERE driver_id = ?";
        Connection conn = DBConnector.getConnection();
        PreparedStatement pstmt = null;

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, driver.getFirstName());
            pstmt.setString(2, driver.getLastName());
            pstmt.setString(3, driver.getNationality());
            pstmt.setInt(4, driver.getDriverId());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error updating driver: " + e.getMessage());
            return false;
        } finally {
            DBConnector.closeConnection(conn);
        }
    }
}
