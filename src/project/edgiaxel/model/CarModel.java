package project.edgiaxel.model;

import javafx.beans.property.*;

public class CarModel {

    private final IntegerProperty carModelId;
    private final IntegerProperty manufacturerId;
    private final StringProperty modelName;
    private final IntegerProperty baseRating;
    private final StringProperty imagePath;

    public CarModel(int carModelId, int manufacturerId, String modelName, int baseRating) {
        this.carModelId = new SimpleIntegerProperty(carModelId);
        this.manufacturerId = new SimpleIntegerProperty(manufacturerId);
        this.modelName = new SimpleStringProperty(modelName);
        this.baseRating = new SimpleIntegerProperty(baseRating);
        this.imagePath = new SimpleStringProperty("/images/cars/" + modelName.replaceAll("\\s+", "_") + ".png");
    }

    public IntegerProperty carModelIdProperty() {
        return carModelId;
    }

    public StringProperty modelNameProperty() {
        return modelName;
    }

    public IntegerProperty baseRatingProperty() {
        return baseRating;
    }

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

    public String getImagePath() {
        return imagePath.get();
    }

    public void setCarModelId(int carModelId) {
        this.carModelId.set(carModelId);
    }

    public void setManufacturerId(int manufacturerId) {
        this.manufacturerId.set(manufacturerId);
    }

    public void setModelName(String modelName) {
        this.modelName.set(modelName);
    }

    public void setBaseRating(int baseRating) {
        this.baseRating.set(baseRating);
    }

    public void setImagePath(String imagePath) {
        this.imagePath.set(imagePath);
    }

    @Override
    public String toString() {
        return modelName.get() + " (BOP: " + baseRating.get() + ")";
    }
}
