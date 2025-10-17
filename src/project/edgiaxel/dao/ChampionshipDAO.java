package project.edgiaxel.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import project.edgiaxel.DBConnector;
import project.edgiaxel.model.Circuit;
import project.edgiaxel.model.ChampionshipSeason;
import java.sql.*;

public class ChampionshipDAO {
    
    private static ChampionshipDAO instance;

    private ChampionshipDAO() {}

    public static ChampionshipDAO getInstance() {
        if (instance == null) {
            instance = new ChampionshipDAO();
        }
        return instance;
    }
    
    /**
     * Fetches all Championship Seasons available for viewing.
     */
    public ObservableList<ChampionshipSeason> getAllSeasons() {
        ObservableList<ChampionshipSeason> seasons = FXCollections.observableArrayList();
        String sql = "SELECT * FROM championship_season ORDER BY year DESC";

        try (Connection conn = DBConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                seasons.add(new ChampionshipSeason(
                        rs.getInt("season_id"),
                        rs.getInt("year"),
                        rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching all seasons: " + e.getMessage());
        }
        return seasons;
    }
    
    /**
     * Checks if a season for a given year already exists.
     */
    public boolean doesSeasonExist(int year) {
        String sql = "SELECT COUNT(*) FROM championship_season WHERE year = ?";
        try (Connection conn = DBConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, year);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error checking season existence: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Creates a new Championship Season and its race calendar (Transactional).
     * @return The generated season ID or -1 on failure.
     */
    public int createNewSeason(int year, ObservableList<Circuit> selectedCircuits) {
        // Two tables are involved: championship_season and season_circuit (Needs to be created).
        String seasonSql = "INSERT INTO championship_season (year, status) VALUES (?, 'Created')";
        String circuitSql = "INSERT INTO season_circuit (season_id, circuit_id, race_index) VALUES (?, ?, ?)";
        Connection conn = DBConnector.getConnection();

        if (conn == null) return -1;
        
        int newSeasonId = -1;

        try {
            conn.setAutoCommit(false); // Start transaction

            // 1. Insert Championship Season
            try (PreparedStatement seasonStmt = conn.prepareStatement(seasonSql, Statement.RETURN_GENERATED_KEYS)) {
                seasonStmt.setInt(1, year);
                if (seasonStmt.executeUpdate() > 0) {
                    ResultSet generatedKeys = seasonStmt.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        newSeasonId = generatedKeys.getInt(1);
                    }
                } else {
                    conn.rollback();
                    return -1;
                }
            }
            
            // 2. Insert Circuits for the Season
            try (PreparedStatement circuitStmt = conn.prepareStatement(circuitSql)) {
                int raceIndex = 1;
                for (Circuit circuit : selectedCircuits) {
                    circuitStmt.setInt(1, newSeasonId);
                    circuitStmt.setInt(2, circuit.getCircuitId());
                    circuitStmt.setInt(3, raceIndex++);
                    circuitStmt.addBatch();
                }
                circuitStmt.executeBatch();
            }

            conn.commit();
            return newSeasonId;

        } catch (SQLException e) {
            System.err.println("Transaction failed: Failed to create new season: " + e.getMessage());
            try {
                conn.rollback();
            } catch (SQLException ex) {
                System.err.println("Rollback failed: " + ex.getMessage());
            }
            return -1;
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    DBConnector.closeConnection(conn);
                }
            } catch (SQLException e) {
                System.err.println("Error resetting auto-commit: " + e.getMessage());
            }
        }
    }
    
    /**
     * Updates the status of an existing Championship Season.
     */
    public boolean updateSeasonStatus(int seasonId, String newStatus) {
        String sql = "UPDATE championship_season SET status = ? WHERE season_id = ?";
        Connection conn = DBConnector.getConnection();
        PreparedStatement pstmt = null;

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, newStatus);
            pstmt.setInt(2, seasonId);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error updating season status: " + e.getMessage());
            return false;
        } finally {
            DBConnector.closeConnection(conn);
        }
    }
}