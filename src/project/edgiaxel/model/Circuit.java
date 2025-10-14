package project.edgiaxel.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Circuit {

    private final IntegerProperty circuitId;
    private final StringProperty name;
    private final StringProperty location;
    private final StringProperty country;
    private final DoubleProperty lengthKm; // DECIMAL(5,3) in DB
    private final StringProperty raceType; // 6 Hours, 8–10 Hours, 24 Hours

    public Circuit(int circuitId, String name, String location, String country, double lengthKm, String raceType) {
        this.circuitId = new SimpleIntegerProperty(circuitId);
        this.name = new SimpleStringProperty(name);
        this.location = new SimpleStringProperty(location);
        this.country = new SimpleStringProperty(country);
        this.lengthKm = new SimpleDoubleProperty(lengthKm);
        this.raceType = new SimpleStringProperty(raceType);
    }

    // Properties
    public IntegerProperty circuitIdProperty() {
        return circuitId;
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

    public DoubleProperty lengthKmProperty() {
        return lengthKm;
    }

    public StringProperty raceTypeProperty() {
        return raceType;
    }

    // Getters
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

    // Setters
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

    @Override
    public String toString() {
        return getName() + " (" + getCountry() + ")";
    }
}
