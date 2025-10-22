package project.edgiaxel.model;

import javafx.beans.property.*;

public class Circuit {

    private final IntegerProperty circuitId;
    private final StringProperty name;
    private final StringProperty location;
    private final StringProperty country;
    private final SimpleDoubleProperty lengthKm;
    private final StringProperty raceType;
    private final StringProperty mapPath;

    public Circuit(int circuitId, String name, String location, String country, double lengthKm, String raceType) {
        this.circuitId = new SimpleIntegerProperty(circuitId);
        this.name = new SimpleStringProperty(name);
        this.location = new SimpleStringProperty(location);
        this.country = new SimpleStringProperty(country);
        this.lengthKm = new SimpleDoubleProperty(lengthKm);
        this.raceType = new SimpleStringProperty(raceType);
        this.mapPath = new SimpleStringProperty("/images/circuit_" + name.toLowerCase().split(" ")[0] + ".jpg");
    }

    public StringProperty nameProperty() {
        return name;
    }

    public StringProperty locationProperty() {
        return location;
    }

    public StringProperty countryProperty() {
        return country;
    }

    public SimpleDoubleProperty lengthKmProperty() {
        return lengthKm;
    }

    public StringProperty raceTypeProperty() {
        return raceType;
    }

    public int getCircuitId() {
        return circuitId.get();
    }

    public String getName() {
        return name.get();
    }

    public String getLocation() {
        return location.get();
    }

    public String getCountry() {
        return country.get();
    }

    public double getLengthKm() {
        return lengthKm.get();
    }

    public String getRaceType() {
        return raceType.get();
    }

    public String getMapPath() {
        return mapPath.get();
    }

    public void setCircuitId(int circuitId) {
        this.circuitId.set(circuitId);
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public void setLocation(String location) {
        this.location.set(location);
    }

    public void setCountry(String country) {
        this.country.set(country);
    }

    public void setLengthKm(double lengthKm) {
        this.lengthKm.set(lengthKm);
    }

    public void setRaceType(String raceType) {
        this.raceType.set(raceType);
    }

    public void setMapPath(String mapPath) {
        this.mapPath.set(mapPath);
    }

    @Override
    public String toString() {
        return name.get() + " (" + raceType.get() + ")";
    }
}
