package project.edgiaxel.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class CarModel {

    private final IntegerProperty carModelId;
    private final IntegerProperty manufacturerId;
    private final StringProperty modelName;
    private final IntegerProperty baseRating;

    public CarModel(int carModelId, int manufacturerId, String modelName, int baseRating) {
        this.carModelId = new SimpleIntegerProperty(carModelId);
        this.manufacturerId = new SimpleIntegerProperty(manufacturerId);
        this.modelName = new SimpleStringProperty(modelName);
        this.baseRating = new SimpleIntegerProperty(baseRating);
    }

    // -------------------- Properties --------------------
    public IntegerProperty carModelIdProperty() {
        return carModelId;
    }

    public IntegerProperty manufacturerIdProperty() {
        return manufacturerId;
    }

    public StringProperty modelNameProperty() {
        return modelName;
    }

    public IntegerProperty baseRatingProperty() {
        return baseRating;
    }

    // -------------------- Getters --------------------
    public int getCarModelId() {
        return carModelId.get();
    }

    public int getManufacturerId() {
        return manufacturerId.get();
    }

    public String getModelName() {
        return modelName.get();
    }

    public int getBaseRating() {
        return baseRating.get();
    }

    // -------------------- Setters --------------------
    public void setCarModelId(int value) {
        this.carModelId.set(value);
    }

    public void setManufacturerId(int value) {
        this.manufacturerId.set(value);
    }

    public void setModelName(String value) {
        this.modelName.set(value);
    }

    public void setBaseRating(int value) {
        this.baseRating.set(value);
    }

    @Override
    public String toString() {
        return getModelName();
    }
}
