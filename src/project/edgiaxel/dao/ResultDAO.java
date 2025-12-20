package project.edgiaxel.dao;

import project.edgiaxel.DBConnector;
import project.edgiaxel.model.RaceResultEntry;
import java.sql.*;
import java.util.*;

public class ResultDAO {

    private static ResultDAO instance;

    public static ResultDAO getInstance() {
        if (instance == null) {
            instance = new ResultDAO();
        }
        return instance;
    }

    public void saveSessionResult(int seasonId, int circuitId, List<RaceResultEntry> results, String sessionType) {
        // Yuura: "Directly using team_id now! No more subquery guessing games!"
        String sql = "INSERT INTO race_results (season_id, circuit_id, team_id, position, category, is_dnf, result_value, session_type) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnector.getConnection()) {
            // Good practice to turn off auto-commit for batch
            conn.setAutoCommit(false);

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                for (RaceResultEntry res : results) {
                    ps.setInt(1, seasonId);
                    ps.setInt(2, circuitId);
                    ps.setInt(3, res.getTeamId()); // <--- CRITICAL FIX
                    ps.setInt(4, res.getPosition());
                    ps.setString(5, res.getCategory());
                    ps.setBoolean(6, res.getBestTimeOrLaps().equals("DNF"));
                    ps.setString(7, res.getBestTimeOrLaps());
                    ps.setString(8, sessionType);
                    ps.addBatch();
                }
                ps.executeBatch();
                conn.commit(); // Save it!
                System.out.println("Yuura: Session " + sessionType + " saved successfully!");
            } catch (SQLException ex) {
                conn.rollback();
                ex.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void applyRacePoints(int seasonId, int circuitId, String raceDuration, List<RaceResultEntry> results) {
        int[] scale = getPointScale(raceDuration);
        int hypercarRank = 1;
        int gt3Rank = 1;
        Set<Integer> scoredManufacturers = new HashSet<>();

        try (Connection conn = DBConnector.getConnection()) {
            conn.setAutoCommit(false);
            for (RaceResultEntry res : results) {
                if (res.getBestTimeOrLaps().equals("DNF")) {
                    continue;
                }

                int classRank = 0;
                if (res.getCategory().equalsIgnoreCase("Hypercar")) {
                    classRank = hypercarRank++;
                } else if (res.getCategory().equalsIgnoreCase("LMGT3")) {
                    classRank = gt3Rank++;
                }

                if (classRank > 10) {
                    continue;
                }

                int pts = scale[classRank - 1];

                // You can now use res.getTeamId() directly instead of looking it up!
                int teamId = res.getTeamId();
                int manuId = getManufacturerIdByTeam(teamId);

                updateTablePoints(conn, "team_standings", "team_id", teamId, seasonId, pts);
                updateDriverPoints(conn, teamId, seasonId, pts);

                if (!scoredManufacturers.contains(manuId)) {
                    updateTablePoints(conn, "manufacturer_standings", "manufacturer_id", manuId, seasonId, pts);
                    scoredManufacturers.add(manuId);
                }
            }
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateTablePoints(Connection conn, String table, String idCol, int id, int seasonId, int pts) throws SQLException {
        String sql = "INSERT INTO " + table + " (season_id, " + idCol + ", points) VALUES (?, ?, ?) "
                + "ON DUPLICATE KEY UPDATE points = points + ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, seasonId);
            ps.setInt(2, id);
            ps.setInt(3, pts);
            ps.setInt(4, pts);
            ps.executeUpdate();
        }
    }

    private void updateDriverPoints(Connection conn, int teamId, int seasonId, int pts) throws SQLException {
        String sql = "INSERT INTO driver_standings (season_id, driver_id, points) "
                + "SELECT ?, driver_id, ? FROM team_driver WHERE team_id = ? "
                + "ON DUPLICATE KEY UPDATE points = points + ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, seasonId);
            ps.setInt(2, pts);
            ps.setInt(3, teamId);
            ps.setInt(4, pts);
            ps.executeUpdate();
        }
    }

    private int getTeamIdByCarNum(String carNum) {
        String sql = "SELECT team_id FROM team WHERE car_number = ?";
        try (Connection conn = DBConnector.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, carNum);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("team_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private int getManufacturerIdByTeam(int teamId) {
        String sql = "SELECT manufacturer_id FROM team WHERE team_id = ?";
        try (Connection conn = DBConnector.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, teamId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("manufacturer_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public void applyBonusPoint(int seasonId, String carNumber, String bonusType) {
        // You might want to update this to take teamId, but for now, the existing string lookup is okay for bonus points
        // if you fix the simulateSession logic.
        // ... implementation same as before ...
        // But wait! We need the helper getTeamIdByCarNum since we used it.
        String sql = "SELECT team_id FROM team WHERE car_number = ?";
        try (Connection conn = DBConnector.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, carNumber);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int teamId = rs.getInt("team_id");
                updateTablePoints(conn, "team_standings", "team_id", teamId, seasonId, 1);
                updateDriverPoints(conn, teamId, seasonId, 1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private int[] getPointScale(String raceDuration) {
        if (raceDuration.contains("24")) {
            return new int[]{50, 36, 30, 24, 20, 16, 12, 8, 4, 2};
        } else if (raceDuration.contains("8–10")) {
            return new int[]{38, 27, 23, 18, 15, 12, 9, 6, 3, 2};
        } else {
            return new int[]{25, 18, 15, 12, 10, 8, 6, 4, 2, 1};
        }
    }
}
