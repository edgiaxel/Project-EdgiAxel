package project.edgiaxel.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Manufacturer {

    private final IntegerProperty manufacturerId;
    private final StringProperty name;
    private final StringProperty country;
    private final StringProperty category; // Hypercar or LMGT3

    public Manufacturer(int manufacturerId, String name, String country, String category) {
        this.manufacturerId = new SimpleIntegerProperty(manufacturerId);
        this.name = new SimpleStringProperty(name);
        this.country = new SimpleStringProperty(country);
        this.category = new SimpleStringProperty(category);
    }

    // Properties
    public IntegerProperty manufacturerIdProperty() {
        return manufacturerId;
    }

    public StringProperty nameProperty() {
        return name;
    }

    public StringProperty countryProperty() {
        return country;
    }

    public StringProperty categoryProperty() {
        return category;
    }

    // Getters
    public int getManufacturerId() {
        return manufacturerId.get();
    }

    public String getName() {
        return name.get();
    }

    public String getCountry() {
        return country.get();
    }

    public String getCategory() {
        return category.get();
    }

    // Setters
    public void setName(String name) {
        this.name.set(name);
    }

    public void setCountry(String country) {
        this.country.set(country);
    }

    public void setCategory(String category) {
        this.category.set(category);
    }

    @Override
    public String toString() {
        return getName();
    }
}
