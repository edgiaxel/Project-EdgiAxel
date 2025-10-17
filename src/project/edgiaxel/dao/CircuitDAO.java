package project.edgiaxel.dao;

import project.edgiaxel.DBConnector;
import project.edgiaxel.model.Circuit;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;

public class CircuitDAO {

    private static CircuitDAO instance;

    private CircuitDAO() {
    }

    public static CircuitDAO getInstance() {
        if (instance == null) {
            instance = new CircuitDAO();
        }
        return instance;
    }

    /**
     * Fetches all circuits in the database.
     */
    public ObservableList<Circuit> getAllCircuits() {
        ObservableList<Circuit> circuits = FXCollections.observableArrayList();
        String sql = "SELECT * FROM circuit ORDER BY name";

        try ( Connection conn = DBConnector.getConnection();  Statement stmt = conn.createStatement();  ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                circuits.add(new Circuit(
                        rs.getInt("circuit_id"),
                        rs.getString("name"),
                        rs.getString("location"),
                        rs.getString("country"),
                        rs.getDouble("length_km"),
                        rs.getString("race_type")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching all circuits: " + e.getMessage());
        }
        return circuits;
    }

    /**
     * Inserts a new circuit.
     */
    public boolean insertCircuit(Circuit circuit) {
        String sql = "INSERT INTO circuit (name, location, country, length_km, race_type) VALUES (?, ?, ?, ?, ?)";
        Connection conn = DBConnector.getConnection();
        PreparedStatement pstmt = null;

        try {
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, circuit.getName());
            pstmt.setString(2, circuit.getLocation());
            pstmt.setString(3, circuit.getCountry());
            pstmt.setDouble(4, circuit.getLengthKm());
            pstmt.setString(5, circuit.getRaceType());
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    circuit.setCircuitId(generatedKeys.getInt(1)); // Update object ID
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error inserting circuit: " + e.getMessage());
            return false;
        } finally {
            DBConnector.closeConnection(conn);
        }
        return false;
    }

    /**
     * Updates an existing circuit.
     */
    public boolean updateCircuit(Circuit circuit) {
        String sql = "UPDATE circuit SET name = ?, location = ?, country = ?, length_km = ?, race_type = ? WHERE circuit_id = ?";
        Connection conn = DBConnector.getConnection();
        PreparedStatement pstmt = null;

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, circuit.getName());
            pstmt.setString(2, circuit.getLocation());
            pstmt.setString(3, circuit.getCountry());
            pstmt.setDouble(4, circuit.getLengthKm());
            pstmt.setString(5, circuit.getRaceType());
            pstmt.setInt(6, circuit.getCircuitId());
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error updating circuit: " + e.getMessage());
            return false;
        } finally {
            DBConnector.closeConnection(conn);
        }
    }

    /**
     * Deletes a circuit.
     */
    public boolean deleteCircuit(Circuit circuit) {
        String sql = "DELETE FROM circuit WHERE circuit_id = ?";
        Connection conn = DBConnector.getConnection();
        PreparedStatement pstmt = null;

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, circuit.getCircuitId());
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            // Check if it's currently used in a championship (Foreign Key constraint check needed in a real app)
            System.err.println("Error deleting circuit: " + e.getMessage());
            return false;
        } finally {
            DBConnector.closeConnection(conn);
        }
    }
}
