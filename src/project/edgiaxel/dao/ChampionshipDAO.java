package project.edgiaxel.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import project.edgiaxel.DBConnector;
import project.edgiaxel.SessionManager;
import project.edgiaxel.model.ChampionshipSeason;
import project.edgiaxel.model.Circuit;

public class ChampionshipDAO {

    private static ChampionshipDAO instance;

    public static ChampionshipDAO getInstance() {
        if (instance == null) {
            instance = new ChampionshipDAO();
        }
        return instance;
    }

    public boolean doesSeasonExist(int year) {
        String sql = "SELECT COUNT(*) FROM championship_season WHERE year = ? AND user_id = ?";
        try (Connection conn = DBConnector.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, year);
            ps.setInt(2, SessionManager.getCurrentUserId());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public int createNewSeason(int year, ObservableList<Circuit> selectedCircuits) {
        String sqlSeason = "INSERT INTO championship_season (year, status, user_id) VALUES (?, 'Ongoing', ?)";
        String sqlCircuit = "INSERT INTO season_circuit (season_id, circuit_id, race_index) VALUES (?, ?, ?)";

        try (Connection conn = DBConnector.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sqlSeason, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, year);
                ps.setInt(2, SessionManager.getCurrentUserId()); 
                ps.executeUpdate();

                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    int seasonId = rs.getInt(1);
                    try (PreparedStatement psC = conn.prepareStatement(sqlCircuit)) {
                        for (int i = 0; i < selectedCircuits.size(); i++) {
                            psC.setInt(1, seasonId);
                            psC.setInt(2, selectedCircuits.get(i).getCircuitId());
                            psC.setInt(3, i + 1);
                            psC.addBatch();
                        }
                        psC.executeBatch();
                    }
                    conn.commit();
                    return seasonId;
                }
            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public void updateSeasonStatus(int seasonId, String status) {
        String sql = "UPDATE championship_season SET status = ? WHERE season_id = ?";
        try (Connection conn = DBConnector.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, seasonId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ChampionshipSeason getLatestOngoingSeason() {
        String sql = "SELECT * FROM championship_season WHERE status = 'Ongoing' ORDER BY created_at DESC LIMIT 1";
        try (Connection conn = DBConnector.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return new ChampionshipSeason(rs.getInt("season_id"), rs.getInt("year"), rs.getString("status"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ObservableList<ChampionshipSeason> getOngoingSeasons() {
        ObservableList<ChampionshipSeason> seasons = FXCollections.observableArrayList();
        String sql = "SELECT * FROM championship_season WHERE status = 'Ongoing' AND user_id = ? ORDER BY year DESC";

        try (Connection conn = DBConnector.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, SessionManager.getCurrentUserId());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                seasons.add(new ChampionshipSeason(rs.getInt("season_id"), rs.getInt("year"), rs.getString("status")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return seasons;
    }

}
