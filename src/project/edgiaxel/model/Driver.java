package project.edgiaxel.model;

import javafx.beans.property.*;

public class Driver {

    private final IntegerProperty driverId;
    private final StringProperty firstName;
    private final StringProperty lastName;
    private final StringProperty nationality;

    // Constructor
    public Driver(int driverId, String firstName, String lastName, String nationality) {
        this.driverId = new SimpleIntegerProperty(driverId);
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.nationality = new SimpleStringProperty(nationality);
    }

    // --- Properties for TableView ---
    public StringProperty firstNameProperty() {
        return firstName;
    }

    public StringProperty lastNameProperty() {
        return lastName;
    }

    public StringProperty nationalityProperty() {
        return nationality;
    }

    // --- Getters ---
    public int getDriverId() {
        return driverId.get();
    }

    public String getFirstName() {
        return firstName.get();
    }

    public String getLastName() {
        return lastName.get();
    }

    public String getNationality() {
        return nationality.get();
    }

    // --- Setters ---
    public void setDriverId(int driverId) {
        this.driverId.set(driverId);
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    public void setNationality(String nationality) {
        this.nationality.set(nationality);
    }

    public String getFullName() {
        return firstName.get() + " " + lastName.get();
    }

    // Override toString
    @Override
    public String toString() {
        return getFullName() + " (" + nationality.get() + ")";
    }
}
