package project.edgiaxel.dao;

import project.edgiaxel.DBConnector;
import project.edgiaxel.model.Team;
import project.edgiaxel.model.Driver;
import project.edgiaxel.model.CarModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;

public class TeamDAO {

    private static TeamDAO instance;

    private TeamDAO() {
    }

    public static TeamDAO getInstance() {
        if (instance == null) {
            instance = new TeamDAO();
        }
        return instance;
    }

    /**
     * Fetches all Teams associated with a specific Manufacturer ID. Also
     * fetches and sets the related CarModel and Drivers.
     */
    public ObservableList<Team> getTeamsByManufacturerId(int manufacturerId) {
        ObservableList<Team> teams = FXCollections.observableArrayList();
        String sql = "SELECT * FROM team WHERE manufacturer_id = ?";

        try ( Connection conn = DBConnector.getConnection();  PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, manufacturerId);
            try ( ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Team team = new Team(
                            rs.getInt("team_id"),
                            rs.getString("car_number"),
                            rs.getString("team_name"),
                            rs.getInt("manufacturer_id"),
                            rs.getInt("car_model_id"),
                            rs.getString("nationality"),
                            rs.getString("category")
                    );

                    // Fetch related objects
                    CarModel carModel = CarModelDAO.getInstance().getCarModelById(team.getCarModelId());
                    team.setCarModel(carModel);

                    team.setDrivers(getDriversForTeam(team.getTeamId()));

                    teams.add(team);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching Teams by Manufacturer ID: " + e.getMessage());
        }
        return teams;
    }

    /**
     * Fetches all drivers currently assigned to a specific Team.
     */
    public ObservableList<Driver> getDriversForTeam(int teamId) {
        ObservableList<Driver> drivers = FXCollections.observableArrayList();
        String sql = "SELECT d.* FROM driver d JOIN team_driver td ON d.driver_id = td.driver_id WHERE td.team_id = ?";

        try ( Connection conn = DBConnector.getConnection();  PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, teamId);
            try ( ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    drivers.add(new Driver(
                            rs.getInt("driver_id"),
                            rs.getString("first_name"),
                            rs.getString("last_name"),
                            rs.getString("nationality")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching drivers for team ID " + teamId + ": " + e.getMessage());
        }
        return drivers;
    }

    /**
     * Saves the list of drivers assigned to a team (Transactional operation).
     * This is used in the DriverAssignmentDialog.
     */
    public boolean saveTeamDrivers(int teamId, ObservableList<Driver> newDrivers) {
        Connection conn = DBConnector.getConnection();
        if (conn == null) {
            return false;
        }

        try {
            conn.setAutoCommit(false); // Start transaction

            // 1. Delete all current assignments for the team
            String deleteSql = "DELETE FROM team_driver WHERE team_id = ?";
            try ( PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {
                deleteStmt.setInt(1, teamId);
                deleteStmt.executeUpdate();
            }

            // 2. Insert new driver assignments
            String insertSql = "INSERT INTO team_driver (team_id, driver_id) VALUES (?, ?)";
            try ( PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                for (Driver driver : newDrivers) {
                    insertStmt.setInt(1, teamId);
                    insertStmt.setInt(2, driver.getDriverId());
                    insertStmt.addBatch();
                }
                insertStmt.executeBatch();
            }

            conn.commit(); // Commit transaction
            return true;

        } catch (SQLException e) {
            System.err.println("Transaction failed: Failed to save team drivers: " + e.getMessage());
            try {
                conn.rollback(); // Rollback on error
            } catch (SQLException ex) {
                System.err.println("Rollback failed: " + ex.getMessage());
            }
            return false;
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    DBConnector.closeConnection(conn);
                }
            } catch (SQLException e) {
                // Already handled in DBConnector, but added for safety
            }
        }
    }

    public boolean insertTeam(Team team) {
        String sql = "INSERT INTO team (car_number, team_name, manufacturer_id, car_model_id, nationality, category) VALUES (?, ?, ?, ?, ?, ?)";
        Connection conn = DBConnector.getConnection();
        PreparedStatement pstmt = null;

        try {
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, team.getCarNumber());
            pstmt.setString(2, team.getTeamName());
            pstmt.setInt(3, team.getManufacturerId());
            pstmt.setInt(4, team.getCarModelId());
            pstmt.setString(5, team.getNationality());
            pstmt.setString(6, team.getCategory());
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    team.setTeamId(generatedKeys.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error inserting team: " + e.getMessage());
            return false;
        } finally {
            DBConnector.closeConnection(conn);
        }
        return false;
    }

    /**
     * Updates an existing Team entry.
     */
    public boolean updateTeam(Team team) {
        String sql = "UPDATE team SET car_number = ?, team_name = ?, car_model_id = ?, nationality = ?, category = ? WHERE team_id = ?";
        Connection conn = DBConnector.getConnection();
        PreparedStatement pstmt = null;

        try {
            // Note: ManufacturerId is generally fixed and not updated here.
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, team.getCarNumber());
            pstmt.setString(2, team.getTeamName());
            pstmt.setInt(3, team.getCarModelId());
            pstmt.setString(4, team.getNationality());
            pstmt.setString(5, team.getCategory());
            pstmt.setInt(6, team.getTeamId());
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error updating team: " + e.getMessage());
            return false;
        } finally {
            DBConnector.closeConnection(conn);
        }
    }

    /**
     * Deletes a Team entry. NOTE: This will require CASCADE delete on
     * team_driver table or manual deletion first.
     */
    public boolean deleteTeam(Team team) {
        // Need to delete drivers assignment first due to Foreign Key constraint
        if (!saveTeamDrivers(team.getTeamId(), FXCollections.emptyObservableList())) {
            System.err.println("Failed to clear driver assignments for team " + team.getTeamId());
            return false;
        }

        String sql = "DELETE FROM team WHERE team_id = ?";
        Connection conn = DBConnector.getConnection();
        PreparedStatement pstmt = null;

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, team.getTeamId());
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting team: " + e.getMessage());
            return false;
        } finally {
            DBConnector.closeConnection(conn);
        }
    }
}
