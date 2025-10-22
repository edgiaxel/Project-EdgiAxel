package project.edgiaxel.dao;

import project.edgiaxel.DBConnector;
import project.edgiaxel.model.CarModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;

public class CarModelDAO {

    private static CarModelDAO instance;

    private CarModelDAO() {
    }

    public static CarModelDAO getInstance() {
        if (instance == null) {
            instance = new CarModelDAO();
        }
        return instance;
    }

    public CarModel getCarModelById(int carModelId) {
        String sql = "SELECT * FROM car_model WHERE car_model_id = ?";
        CarModel model = null;

        try ( Connection conn = DBConnector.getConnection();  PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, carModelId);
            try ( ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    model = new CarModel(
                            rs.getInt("car_model_id"),
                            rs.getInt("manufacturer_id"),
                            rs.getString("model_name"),
                            rs.getInt("base_rating")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching Car Model by ID: " + e.getMessage());
        }
        return model;
    }

    public ObservableList<CarModel> getCarModelsByManufacturerId(int manufacturerId) {
        ObservableList<CarModel> models = FXCollections.observableArrayList();
        String sql = "SELECT * FROM car_model WHERE manufacturer_id = ?";

        try ( Connection conn = DBConnector.getConnection();  PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, manufacturerId);
            try ( ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    models.add(new CarModel(
                            rs.getInt("car_model_id"),
                            rs.getInt("manufacturer_id"),
                            rs.getString("model_name"),
                            rs.getInt("base_rating")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching Car Models by Manufacturer ID: " + e.getMessage());
        }
        return models;
    }

    public boolean updateCarModel(CarModel model) {
        String sql = "UPDATE car_model SET model_name = ?, base_rating = ? WHERE car_model_id = ?";
        Connection conn = DBConnector.getConnection();
        PreparedStatement pstmt = null;

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, model.getModelName());
            pstmt.setInt(2, model.getBaseRating());
            pstmt.setInt(3, model.getCarModelId());
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error updating car model: " + e.getMessage());
            return false;
        } finally {
            DBConnector.closeConnection(conn);
        }
    }

    public boolean insertCarModel(CarModel model) {
        String sql = "INSERT INTO car_model (manufacturer_id, model_name, base_rating) VALUES (?, ?, ?)";
        Connection conn = DBConnector.getConnection();
        PreparedStatement pstmt = null;

        try {
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, model.getManufacturerId());
            pstmt.setString(2, model.getModelName());
            pstmt.setInt(3, model.getBaseRating());
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    model.setCarModelId(generatedKeys.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error inserting car model: " + e.getMessage());
            return false;
        } finally {
            DBConnector.closeConnection(conn);
        }
        return false;
    }

    public boolean deleteCarModel(CarModel model) {
        String sql = "DELETE FROM car_model WHERE car_model_id = ?";
        Connection conn = DBConnector.getConnection();
        PreparedStatement pstmt = null;

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, model.getCarModelId());
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            if (e.getErrorCode() == 1451) {
                System.err.println("Error: Cannot delete car model due to associated Teams existing (FK constraint).");
                return false;
            }
            System.err.println("Error deleting car model: " + e.getMessage());
            return false;
        } finally {
            DBConnector.closeConnection(conn);
        }
    }
}
