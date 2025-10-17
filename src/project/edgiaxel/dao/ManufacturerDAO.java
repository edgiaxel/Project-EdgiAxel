package project.edgiaxel.dao;

import project.edgiaxel.DBConnector;
import project.edgiaxel.model.Manufacturer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;

public class ManufacturerDAO {

    // --- Singleton Pattern (Optional but good practice) ---
    private static ManufacturerDAO instance;

    private ManufacturerDAO() {}

    public static ManufacturerDAO getInstance() {
        if (instance == null) {
            instance = new ManufacturerDAO();
        }
        return instance;
    }
    // --------------------------------------------------------

    // 1. Fetch all Manufacturers
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

    // 2. Insert new Manufacturer (Needed for MasterDataView)
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
                    manufacturer.setManufacturerId(generatedKeys.getInt(1)); // Update object ID
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

    // 3. Update Manufacturer (Needed for MasterDataView)
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
    
    // 4. Delete Manufacturer (Needed for MasterDataView)
    public boolean deleteManufacturer(Manufacturer manufacturer) {
        // NOTE: Deleting a manufacturer will likely require deleting associated teams and car models first (due to FK constraints).
        // This method should be called only after those checks/deletions.
        String sql = "DELETE FROM manufacturer WHERE manufacturer_id = ?";
        Connection conn = DBConnector.getConnection();
        PreparedStatement pstmt = null;

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, manufacturer.getManufacturerId());
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            // Check for foreign key violation
            if (e.getErrorCode() == 1451) { 
                // MySQL Error 1451: Cannot delete or update a parent row: a foreign key constraint fails
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