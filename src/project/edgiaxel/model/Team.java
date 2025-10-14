package project.edgiaxel.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Team {

    private final IntegerProperty teamId;
    private final StringProperty carNumber;
    private final StringProperty teamName;
    public final IntegerProperty carModelId;
    private final StringProperty nationality;
    private final StringProperty category;
    private final ObservableList<Driver> drivers;

    public Team(int teamId, String carNumber, String teamName, int carModelId, String nationality, String category) {
        this.teamId = new SimpleIntegerProperty(teamId);
        this.carNumber = new SimpleStringProperty(carNumber);
        this.teamName = new SimpleStringProperty(teamName);
        this.carModelId = new SimpleIntegerProperty(carModelId);
        this.nationality = new SimpleStringProperty(nationality);
        this.category = new SimpleStringProperty(category);
        this.drivers = FXCollections.observableArrayList();
    }

    // -------------------- Properties --------------------
    public IntegerProperty teamIdProperty() {
        return teamId;
    }

    public StringProperty carNumberProperty() {
        return carNumber;
    }

    public StringProperty teamNameProperty() {
        return teamName;
    }

    public IntegerProperty carModelIdProperty() {
        return carModelId;
    }

    public StringProperty nationalityProperty() {
        return nationality;
    }

    public StringProperty categoryProperty() {
        return category;
    }

    // -------------------- Getters --------------------
    public int getTeamId() {
        return teamId.get();
    }

    public String getCarNumber() {
        return carNumber.get();
    }

    public String getTeamName() {
        return teamName.get();
    }

    public int getCarModelId() {
        return carModelId.get();
    }

    public String getNationality() {
        return nationality.get();
    }

    public String getCategory() {
        return category.get();
    }

    public ObservableList<Driver> getDrivers() {
        return drivers;
    }

    // -------------------- Setters --------------------
    public void setTeamId(int value) {
        this.teamId.set(value);
    }

    public void setCarNumber(String value) {
        this.carNumber.set(value);
    }

    public void setTeamName(String value) {
        this.teamName.set(value);
    }

    public void setCarModelId(int value) {
        this.carModelId.set(value);
    }

    public void setNationality(String value) {
        this.nationality.set(value);
    }

    public void setCategory(String value) {
        this.category.set(value);
    }

    public void setDrivers(ObservableList<Driver> drivers) {
        this.drivers.clear();
        this.drivers.addAll(drivers);
    }
    
    @Override
    public String toString() {
        return getTeamName();
    }
}
