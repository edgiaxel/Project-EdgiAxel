package project.edgiaxel.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import project.edgiaxel.DBConnector;
import project.edgiaxel.model.Circuit;
import project.edgiaxel.model.RaceResultEntry;
import project.edgiaxel.model.StandingEntry;

public class StandingsDAO {

    private static StandingsDAO instance;

    private StandingsDAO() {
    }

    public static StandingsDAO getInstance() {
        if (instance == null) {
            instance = new StandingsDAO();
        }
        return instance;
    }

    public ObservableList<Integer> getAvailableYears() {
        ObservableList<Integer> years = FXCollections.observableArrayList();
        String sql = "SELECT DISTINCT year FROM championship_season ORDER BY year DESC";
        try (Connection conn = DBConnector.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                years.add(rs.getInt("year"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return years;
    }

    public ObservableList<StandingEntry> getDriverStandings(int year) {
        ObservableList<StandingEntry> list = FXCollections.observableArrayList();
        String sql = "SELECT d.first_name, d.last_name, t.team_name, ds.points FROM driver_standings ds "
                + "JOIN driver d ON ds.driver_id = d.driver_id "
                + "JOIN team_driver td ON d.driver_id = td.driver_id "
                + "JOIN team t ON td.team_id = t.team_id "
                + "JOIN championship_season s ON ds.season_id = s.season_id "
                + "WHERE s.year = ? ORDER BY ds.points DESC";
        try (Connection conn = DBConnector.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, year);
            ResultSet rs = ps.executeQuery();
            int pos = 1;
            while (rs.next()) {
                String fullName = rs.getString("first_name") + " " + rs.getString("last_name");
                list.add(new StandingEntry(pos++, fullName, rs.getInt("points"), "", rs.getString("team_name")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public ObservableList<StandingEntry> getTeamStandings(int year, String category) {
        ObservableList<StandingEntry> list = FXCollections.observableArrayList();
        String sql;

        if (category.equalsIgnoreCase("Overall")) {
            sql = "SELECT t.team_name, t.car_number, ts.points FROM team_standings ts "
                    + "JOIN team t ON ts.team_id = t.team_id "
                    + "JOIN championship_season s ON ts.season_id = s.season_id "
                    + "WHERE s.year = ? ORDER BY ts.points DESC";
        } else {
            sql = "SELECT t.team_name, t.car_number, ts.points FROM team_standings ts "
                    + "JOIN team t ON ts.team_id = t.team_id "
                    + "JOIN championship_season s ON ts.season_id = s.season_id "
                    + "WHERE s.year = ? AND t.category = ? ORDER BY ts.points DESC";
        }

        try (Connection conn = DBConnector.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, year);
            if (!category.equalsIgnoreCase("Overall")) {
                ps.setString(2, category);
            }

            ResultSet rs = ps.executeQuery();
            int pos = 1;
            while (rs.next()) {
                list.add(new StandingEntry(pos++, rs.getString("team_name"), rs.getInt("points"), category, rs.getString("car_number")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public ObservableList<StandingEntry> getManufacturerStandings(int year) {
        ObservableList<StandingEntry> list = FXCollections.observableArrayList();
        String sql = "SELECT m.name, ms.points FROM manufacturer_standings ms "
                + "JOIN manufacturer m ON ms.manufacturer_id = m.manufacturer_id "
                + "JOIN championship_season s ON ms.season_id = s.season_id "
                + "WHERE s.year = ? ORDER BY ms.points DESC";
        try (Connection conn = DBConnector.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, year);
            ResultSet rs = ps.executeQuery();
            int pos = 1;
            while (rs.next()) {
                list.add(new StandingEntry(pos++, rs.getString("name"), rs.getInt("points"), "", ""));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean resetSeasonData(int year) {
        String[] queries = {
            "DELETE FROM race_results WHERE season_id = (SELECT season_id FROM championship_season WHERE year = ?)",
            "DELETE FROM driver_standings WHERE season_id = (SELECT season_id FROM championship_season WHERE year = ?)",
            "DELETE FROM team_standings WHERE season_id = (SELECT season_id FROM championship_season WHERE year = ?)",
            "DELETE FROM manufacturer_standings WHERE season_id = (SELECT season_id FROM championship_season WHERE year = ?)",
            "DELETE FROM season_circuit WHERE season_id = (SELECT season_id FROM championship_season WHERE year = ?)",
            "DELETE FROM championship_season WHERE year = ?"
        };

        try (Connection conn = DBConnector.getConnection()) {
            conn.setAutoCommit(false);
            for (String sql : queries) {
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setInt(1, year);
                    ps.executeUpdate();
                }
            }
            conn.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ObservableList<RaceResultEntry> getSessionResults(int year, int circuitId, String sessionType) {
        ObservableList<RaceResultEntry> results = FXCollections.observableArrayList();
        String sql = "SELECT rr.position, t.team_id, t.car_number, t.team_name, cm.model_name, rr.result_value, t.category "
                + "FROM race_results rr "
                + "JOIN team t ON rr.team_id = t.team_id "
                + "JOIN car_model cm ON t.car_model_id = cm.car_model_id "
                + "JOIN championship_season s ON rr.season_id = s.season_id "
                + "WHERE s.year = ? AND rr.circuit_id = ? AND rr.session_type LIKE ? "
                + "ORDER BY rr.position ASC";

        try (Connection conn = DBConnector.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, year);
            ps.setInt(2, circuitId);
            ps.setString(3, sessionType);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                results.add(new RaceResultEntry(
                        rs.getInt("position"),
                        rs.getString("car_number"),
                        rs.getString("team_name"),
                        rs.getString("model_name"),
                        rs.getString("result_value"),
                        rs.getString("category"),
                        rs.getInt("team_id") 
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    public ObservableList<Circuit> getCircuitsForYear(int year) {
        ObservableList<Circuit> circuits = FXCollections.observableArrayList();
        String sql = "SELECT c.* FROM circuit c "
                + "JOIN season_circuit sc ON c.circuit_id = sc.circuit_id "
                + "JOIN championship_season s ON sc.season_id = s.season_id "
                + "WHERE s.year = ? ORDER BY sc.race_index ASC";
        try (Connection conn = DBConnector.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, year);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                circuits.add(new Circuit(
                        rs.getInt("circuit_id"), rs.getString("name"),
                        rs.getString("location"), rs.getString("country"),
                        rs.getDouble("length_km"), rs.getString("race_type")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return circuits;
    }
}
