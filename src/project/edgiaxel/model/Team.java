package project.edgiaxel.model;

import javafx.beans.property.*;
import javafx.collections.ObservableList;

public class Team {

    private final IntegerProperty teamId;
    private final StringProperty carNumber;
    private final StringProperty teamName;
    private final IntegerProperty manufacturerId;
    private final IntegerProperty carModelId;
    private final StringProperty nationality;
    private final StringProperty category;
    private final StringProperty liveryPath;

    private Manufacturer manufacturer;
    private CarModel carModel;
    private ObservableList<Driver> drivers;

    public Team(int teamId, String carNumber, String teamName, int manufacturerId, int carModelId, String nationality, String category) {
        this.teamId = new SimpleIntegerProperty(teamId);
        this.carNumber = new SimpleStringProperty(carNumber);
        this.teamName = new SimpleStringProperty(teamName);
        this.manufacturerId = new SimpleIntegerProperty(manufacturerId);
        this.carModelId = new SimpleIntegerProperty(carModelId);
        this.nationality = new SimpleStringProperty(nationality);
        this.category = new SimpleStringProperty(category);
        String shortManufacturerName = teamName.split(" ")[0].toLowerCase();

        this.liveryPath = new SimpleStringProperty("/images/team_" + shortManufacturerName + "_" + carNumber + ".jpg");
    }

    public IntegerProperty teamIdProperty() {
        return teamId;
    }

    public StringProperty carNumberProperty() {
        return carNumber;
    }

    public StringProperty teamNameProperty() {
        return teamName;
    }

    public StringProperty nationalityProperty() {
        return nationality;
    }

    public StringProperty categoryProperty() {
        return category;
    }

    public StringProperty liveryPathProperty() {
        return liveryPath;
    }

    public StringProperty carModelNameProperty() {
        return new SimpleStringProperty(carModel != null ? carModel.getModelName() : "Loading...");
    }

    public int getTeamId() {
        return teamId.get();
    }

    public String getCarNumber() {
        return carNumber.get();
    }

    public String getTeamName() {
        return teamName.get();
    }

    public int getManufacturerId() {
        return manufacturerId.get();
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

    public String getLiveryPath() {
        return liveryPath.get();
    }

    public Manufacturer getManufacturer() {
        return manufacturer;
    }

    public CarModel getCarModel() {
        return carModel;
    }

    public ObservableList<Driver> getDrivers() {
        return drivers;
    }

    public void setTeamId(int teamId) {
        this.teamId.set(teamId);
    }

    public void setCarNumber(String carNumber) {
        this.carNumber.set(carNumber);
    }

    public void setTeamName(String teamName) {
        this.teamName.set(teamName);
    }

    public void setManufacturerId(int manufacturerId) {
        this.manufacturerId.set(manufacturerId);
    }

    public void setCarModelId(int carModelId) {
        this.carModelId.set(carModelId);
    }

    public void setNationality(String nationality) {
        this.nationality.set(nationality);
    }

    public void setCategory(String category) {
        this.category.set(category);
    }

    public void setLiveryPath(String liveryPath) {
        this.liveryPath.set(liveryPath);
    }

    public void setManufacturer(Manufacturer manufacturer) {
        this.manufacturer = manufacturer;
    }

    public void setCarModel(CarModel carModel) {
        this.carModel = carModel;
    }

    public void setDrivers(ObservableList<Driver> drivers) {
        this.drivers = drivers;
    }

    @Override
    public String toString() {
        return "#" + carNumber.get() + " " + teamName.get() + " (" + category.get() + ")";
    }
}
