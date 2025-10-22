package project.edgiaxel.dao;

import project.edgiaxel.DBConnector;
import project.edgiaxel.model.Manufacturer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;

public class ManufacturerDAO {

    private static ManufacturerDAO instance;

    private ManufacturerDAO() {}

    public static ManufacturerDAO getInstance() {
        if (instance == null) {
            instance = new ManufacturerDAO();
        }
        return instance;
    }
    
    public ObservableList<Manufacturer> getAllManufacturers() {
        ObservableList<Manufacturer> manufacturers = FXCollections.observableArrayList();
        String sql = "SELECT * FROM manufacturer";

        try (Connection conn = DBConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                manufacturers.add(new Manufacturer(
                        rs.getInt("manufacturer_id"),
                        rs.getString("name"),
                        rs.getString("country"),
                        rs.getString("category")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching all manufacturers: " + e.getMessage());
        }
        return manufacturers;
    }

    public boolean insertManufacturer(Manufacturer manufacturer) {
        String sql = "INSERT INTO manufacturer (name, country, category) VALUES (?, ?, ?)";
        Connection conn = DBConnector.getConnection();
        PreparedStatement pstmt = null;

        try {
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, manufacturer.getName());
            pstmt.setString(2, manufacturer.getCountry());
            pstmt.setString(3, manufacturer.getCategory());
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    manufacturer.setManufacturerId(generatedKeys.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error inserting manufacturer: " + e.getMessage());
            return false;
        } finally {
            DBConnector.closeConnection(conn);
        }
        return false;
    }

    public boolean updateManufacturer(Manufacturer manufacturer) {
        String sql = "UPDATE manufacturer SET name = ?, country = ?, category = ? WHERE manufacturer_id = ?";
        Connection conn = DBConnector.getConnection();
        PreparedStatement pstmt = null;

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, manufacturer.getName());
            pstmt.setString(2, manufacturer.getCountry());
            pstmt.setString(3, manufacturer.getCategory());
            pstmt.setInt(4, manufacturer.getManufacturerId());
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error updating manufacturer: " + e.getMessage());
            return false;
        } finally {
            DBConnector.closeConnection(conn);
        }
    }
    
    public boolean deleteManufacturer(Manufacturer manufacturer) {
        String sql = "DELETE FROM manufacturer WHERE manufacturer_id = ?";
        Connection conn = DBConnector.getConnection();
        PreparedStatement pstmt = null;

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, manufacturer.getManufacturerId());
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            if (e.getErrorCode() == 1451) { 
                System.err.println("Error: Cannot delete manufacturer due to associated Teams/Car Models existing.");
                return false;
            }
            System.err.println("Error deleting manufacturer: " + e.getMessage());
            return false;
        } finally {
            DBConnector.closeConnection(conn);
        }
    }
}