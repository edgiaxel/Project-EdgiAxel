package project.edgiaxel.model;

import javafx.beans.property.*;

public class Manufacturer {

    private final IntegerProperty manufacturerId;
    private final StringProperty name;
    private final StringProperty country;
    private final StringProperty category; // Hypercar or LMGT3
    private final StringProperty logoPath; // For MasterDataView image

    // Constructor
    public Manufacturer(int manufacturerId, String name, String country, String category) {
        this.manufacturerId = new SimpleIntegerProperty(manufacturerId);
        this.name = new SimpleStringProperty(name);
        this.country = new SimpleStringProperty(country);
        this.category = new SimpleStringProperty(category);
        this.logoPath = new SimpleStringProperty("/images/manufacturers/" + name.replaceAll("\\s+", "_") + "_logo.png");
    }

    // --- Properties for TableView ---
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

    public StringProperty logoPathProperty() {
        return logoPath;
    }

    // --- Getters ---
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

    public String getLogoPath() {
        return logoPath.get();
    }

    // --- Setters ---
    public void setManufacturerId(int manufacturerId) {
        this.manufacturerId.set(manufacturerId);
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public void setCountry(String country) {
        this.country.set(country);
    }

    public void setCategory(String category) {
        this.category.set(category);
    }

    public void setLogoPath(String logoPath) {
        this.logoPath.set(logoPath);
    }

    // --- ToString() ---
    @Override
    public String toString() {
        return name.get() + " (" + category.get() + ")";
    }
}
