package project.edgiaxel.util;

import project.edgiaxel.model.Manufacturer;
import project.edgiaxel.model.Circuit; // Don't forget this import, dimwit.
import project.edgiaxel.DBConnector;
import java.sql.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import project.edgiaxel.model.CarModel;
import project.edgiaxel.model.Team;
import project.edgiaxel.model.Driver;
import project.edgiaxel.model.Standing;

public class DBUtil {

    // --- MANUFACTURER METHODS (ALREADY PROVIDED) ---
    public static ObservableList<Manufacturer> getAllManufacturers() {
        String sql = "SELECT manufacturer_id, name, country, category FROM Manufacturer";
        ObservableList<Manufacturer> manufacturers = FXCollections.observableArrayList();

        try ( Connection conn = DBConnector.getConnection();  Statement stmt = conn.createStatement();  ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                manufacturers.add(new Manufacturer(
                        rs.getInt("manufacturer_id"),
                        rs.getString("name"),
                        rs.getString("country"),
                        rs.getString("category")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Database query failed! (Manufacturers) Did you forget to start Laragon?!");
            e.printStackTrace();
        }
        return manufacturers;
    }

    // --- CIRCUIT METHODS (NEW) ---
    public static ObservableList<Circuit> getAllCircuits() {
        String sql = "SELECT circuit_id, name, location, country, length_km, race_type FROM Circuit ORDER BY race_type DESC, length_km DESC";
        ObservableList<Circuit> circuits = FXCollections.observableArrayList();

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
            System.err.println("Database query failed! (Circuits) Did you forget to start Laragon?!");
            e.printStackTrace();
        }
        return circuits;
    }
// --- TEAM METHODS (CORRECTED) ---

    public static ObservableList<Team> getAllTeams() {
        String sql = "SELECT t.team_id, t.car_number, t.team_name, t.car_model_id, t.nationality, t.category "
                + "FROM Team t "
                + // We don't actually need the JOIN here since Team has car_model_id, 
                // and we get the Car Model name separately in the controller anyway.
                // But if you INSIST on joining:
                "ORDER BY t.category, t.car_number";

        // Reverting to the simpler, non-joined query because the data model (Team.java) 
        // doesn't store the model_name, and the controller calls another DAO method to get it.
        // That means the JOIN in your original query was pointless and just caused trouble!
        String simplerSql = "SELECT team_id, car_number, team_name, car_model_id, nationality, category "
                + "FROM Team "
                + "ORDER BY category, car_number";

        ObservableList<Team> teams = FXCollections.observableArrayList();

        try ( Connection conn = DBConnector.getConnection();  Statement stmt = conn.createStatement(); // Using the simpler query since you don't use 'm.model_name' in the Java logic anyway!
                  ResultSet rs = stmt.executeQuery(simplerSql)) {

            while (rs.next()) {
                teams.add(new Team(
                        rs.getInt("team_id"),
                        rs.getString("car_number"),
                        rs.getString("team_name"),
                        rs.getInt("car_model_id"),
                        rs.getString("nationality"),
                        rs.getString("category")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Database query failed! (Teams) Check your connection settings!");
            e.printStackTrace();
        }
        return teams;
    }

// --- DRIVER METHODS (NEW - FETCH DRIVERS FOR A SPECIFIC TEAM) ---
    public static ObservableList<Driver> getDriversByTeam(int teamId) {
        String sql = "SELECT d.driver_id, d.first_name, d.last_name, d.nationality "
                + "FROM Driver d "
                + "JOIN Team_Driver td ON d.driver_id = td.driver_id "
                + "WHERE td.team_id = ?"; // Use PreparedStatement for safety, you know better.

        ObservableList<Driver> drivers = FXCollections.observableArrayList();

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
            System.err.println("Database query failed! (Drivers by Team) The link is broken.");
            e.printStackTrace();
        }
        return drivers;
    }

// --- CAR MODEL METHOD (NEW - Get model name for display) ---
    public static CarModel getCarModelById(int carModelId) {
        String sql = "SELECT * FROM Car_Model WHERE car_model_id = ?";

        try ( Connection conn = DBConnector.getConnection();  PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, carModelId);

            try ( ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new CarModel(
                            rs.getInt("car_model_id"),
                            rs.getInt("manufacturer_id"),
                            rs.getString("model_name"),
                            rs.getInt("base_rating")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error retrieving Car Model!");
            e.printStackTrace();
        }
        return null; // Should not happen if data integrity is right
    }
    // You'll need more methods here for INSERT, UPDATE, DELETE later. DON'T FORGET!

    // --- STANDING METHODS (NEW) ---
    public static ObservableList<Standing> getManufacturerStandings(int year) {
        // 1. Get the season ID for the year
        int seasonId = getSeasonIdByYear(year);
        if (seasonId == -1) {
            return FXCollections.emptyObservableList();
        }

        // 2. Query standings and join with the Manufacturer table to get the name
        String sql = "SELECT cs.position, m.name, cs.total_points "
                + "FROM Championship_Standings cs "
                + "JOIN Manufacturer m ON cs.reference_id = m.manufacturer_id "
                + "WHERE cs.season_id = ? AND cs.championship_type = 'Manufacturer_Hyper' "
                + "ORDER BY cs.position ASC";

        ObservableList<Standing> standings = FXCollections.observableArrayList();

        try ( Connection conn = DBConnector.getConnection();  PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, seasonId);

            try ( ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    standings.add(new Standing(
                            rs.getInt("position"),
                            rs.getString("name"),
                            "", // Car Model not needed for Manufacturer standings
                            rs.getInt("total_points")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Database query failed! (Manufacturer Standings) Did you break the standings table, Axel?!");
            e.printStackTrace();
        }
        return standings;
    }

// Helper method needed to fetch season ID
    private static int getSeasonIdByYear(int year) {
        String sql = "SELECT season_id FROM Championship_Season WHERE year = ?";
        try ( Connection conn = DBConnector.getConnection();  PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, year);
            try ( ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("season_id");
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error: Cannot find Season ID for year " + year);
        }
        return -1;
    }

// --- TEAM GT3 STANDING PLACEHOLDER ---
// You need more complex logic and joins to get team standings, but let's stick to Manuf. for now.\
    // --- MANUFACTURER CRUD METHODS (NEW) ---
// CREATE
    public static void insertManufacturer(Manufacturer manufacturer) throws SQLException {
        String sql = "INSERT INTO Manufacturer (name, country, category) VALUES (?, ?, ?)";

        // Use try-with-resources to ensure connection is closed, don't be a caveman.
        try ( Connection conn = DBConnector.getConnection();  PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, manufacturer.getName());
            pstmt.setString(2, manufacturer.getCountry());
            pstmt.setString(3, manufacturer.getCategory());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Manufacturer creation failed, no rows affected.");
            }
            // Get the generated ID back to update the Java object (Good practice!)
            try ( ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    // Since the Manufacturer object is local, you'd usually update the ObservableList later.
                }
            }
        }
    }

// UPDATE
    public static void updateManufacturer(Manufacturer manufacturer) throws SQLException {
        String sql = "UPDATE Manufacturer SET name = ?, country = ?, category = ? WHERE manufacturer_id = ?";

        try ( Connection conn = DBConnector.getConnection();  PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, manufacturer.getName());
            pstmt.setString(2, manufacturer.getCountry());
            pstmt.setString(3, manufacturer.getCategory());
            pstmt.setInt(4, manufacturer.getManufacturerId());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Manufacturer update failed. Did you select the right ID?");
            }
        }
    }

// DELETE (WARNING: This will cascade and delete all associated cars and teams!)
    public static void deleteManufacturer(int manufacturerId) throws SQLException {
        String sql = "DELETE FROM Manufacturer WHERE manufacturer_id = ?";

        try ( Connection conn = DBConnector.getConnection();  PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, manufacturerId);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Manufacturer deletion failed. The ID probably doesn't exist.");
            }
        }
    }

    // CREATE
    public static void insertCircuit(Circuit circuit) throws SQLException {
        String sql = "INSERT INTO Circuit (name, location, country, length_km, race_type) VALUES (?, ?, ?, ?, ?)";

        try ( Connection conn = DBConnector.getConnection();  PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, circuit.getName());
            pstmt.setString(2, circuit.getLocation());
            pstmt.setString(3, circuit.getCountry());
            pstmt.setDouble(4, circuit.getLengthKm());
            pstmt.setString(5, circuit.getRaceType());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Circuit insertion failed, no rows affected.");
            }
        }
    }

// UPDATE
    public static void updateCircuit(Circuit circuit) throws SQLException {
        String sql = "UPDATE Circuit SET name = ?, location = ?, country = ?, length_km = ?, race_type = ? WHERE circuit_id = ?";

        try ( Connection conn = DBConnector.getConnection();  PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, circuit.getName());
            pstmt.setString(2, circuit.getLocation());
            pstmt.setString(3, circuit.getCountry());
            pstmt.setDouble(4, circuit.getLengthKm());
            pstmt.setString(5, circuit.getRaceType());
            pstmt.setInt(6, circuit.getCircuitId());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Circuit update failed. Did you select the right ID?");
            }
        }
    }

// DELETE
    public static void deleteCircuit(int circuitId) throws SQLException {
        // NOTE: This should probably fail if the circuit is part of an ongoing season!
        // But since you haven't implemented that check yet, we'll just delete it.
        String sql = "DELETE FROM Circuit WHERE circuit_id = ?";

        try ( Connection conn = DBConnector.getConnection();  PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, circuitId);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Circuit deletion failed. The ID probably doesn't exist.");
            }
        }
    }

    // --- CAR MODEL METHODS ---
    public static ObservableList<CarModel> getAllCarModels() {
        String sql = "SELECT car_model_id, manufacturer_id, model_name, base_rating FROM Car_Model";
        ObservableList<CarModel> models = FXCollections.observableArrayList();

        try ( Connection conn = DBConnector.getConnection();  Statement stmt = conn.createStatement();  ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                models.add(new CarModel(
                        rs.getInt("car_model_id"),
                        rs.getInt("manufacturer_id"),
                        rs.getString("model_name"),
                        rs.getInt("base_rating")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Database query failed! (Car Models)");
            e.printStackTrace();
        }
        return models;
    }

// --- TEAM CRUD METHODS (NEW) ---
// CREATE
    public static void insertTeam(Team team) throws SQLException {
        String sql = "INSERT INTO Team (car_number, team_name, manufacturer_id, car_model_id, nationality, category) "
                + "VALUES (?, ?, (SELECT manufacturer_id FROM Car_Model WHERE car_model_id = ?), ?, ?, ?)";

        try ( Connection conn = DBConnector.getConnection();  PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, team.getCarNumber());
            pstmt.setString(2, team.getTeamName());
            pstmt.setInt(3, team.carModelIdProperty().get()); // Manufacturer ID derived from Car Model
            pstmt.setInt(4, team.carModelIdProperty().get());
            pstmt.setString(5, team.getNationality());
            pstmt.setString(6, team.categoryProperty().get());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Team entry failed. Maybe the car number already exists?");
            }
            // Get the generated team ID back (good for future Driver assignments)
            try ( ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    team.teamIdProperty().set(generatedKeys.getInt(1));
                }
            }
        }
    }

// UPDATE
    public static void updateTeam(Team team) throws SQLException {
        String sql = "UPDATE Team SET car_number = ?, team_name = ?, car_model_id = ?, nationality = ? WHERE team_id = ?";

        try ( Connection conn = DBConnector.getConnection();  PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, team.getCarNumber());
            pstmt.setString(2, team.getTeamName());
            pstmt.setInt(3, team.carModelIdProperty().get());
            pstmt.setString(4, team.getNationality());
            pstmt.setInt(5, team.getTeamId());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Team update failed. Check if team ID exists.");
            }
        }
    }

// DELETE (Will cascade to remove all drivers from Team_Driver)
    public static void deleteTeam(int teamId) throws SQLException {
        String sql = "DELETE FROM Team WHERE team_id = ?";

        try ( Connection conn = DBConnector.getConnection();  PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, teamId);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Team deletion failed. Did it exist?");
            }
        }
    }

    // --- DRIVER CRUD METHODS (NEW) ---
// READ: Get All Drivers (Global List)
    public static ObservableList<Driver> getAllDrivers() {
        String sql = "SELECT driver_id, first_name, last_name, nationality FROM Driver ORDER BY last_name";
        ObservableList<Driver> drivers = FXCollections.observableArrayList();

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
            System.err.println("Database query failed! (All Drivers)");
            e.printStackTrace();
        }
        return drivers;
    }

// CREATE: Insert New Driver (Global List)
    public static void insertDriver(Driver driver) throws SQLException {
        String sql = "INSERT INTO Driver (first_name, last_name, nationality) VALUES (?, ?, ?)";

        try ( Connection conn = DBConnector.getConnection();  PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, driver.getFirstName());
            pstmt.setString(2, driver.getLastName());
            pstmt.setString(3, driver.getNationality());

            pstmt.executeUpdate();

            // Retrieve and set the new ID back to the object
            try ( ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    driver.driverIdProperty().set(generatedKeys.getInt(1));
                }
            }
        }
    }

// DELETE: Remove ALL drivers from a Team's roster, then re-add the new list.
// NOTE: This is the safest way to handle team roster updates.
    public static void updateTeamRoster(int teamId, ObservableList<Driver> newRoster) throws SQLException {
        // 1. DELETE existing roster (The "R" in CRUD for Team_Driver)
        String deleteSql = "DELETE FROM Team_Driver WHERE team_id = ?";
        try ( Connection conn = DBConnector.getConnection();  PreparedStatement pstmtDelete = conn.prepareStatement(deleteSql)) {
            pstmtDelete.setInt(1, teamId);
            pstmtDelete.executeUpdate();
        }

        // 2. INSERT new roster (The "C" in CRUD for Team_Driver)
        String insertSql = "INSERT INTO Team_Driver (team_id, driver_id) VALUES (?, ?)";
        try ( Connection conn = DBConnector.getConnection();  PreparedStatement pstmtInsert = conn.prepareStatement(insertSql)) {

            for (Driver driver : newRoster) {
                pstmtInsert.setInt(1, teamId);
                pstmtInsert.setInt(2, driver.getDriverId());
                pstmtInsert.addBatch(); // Batch insert for speed, idiot.
            }
            pstmtInsert.executeBatch();
        }
    }
}
