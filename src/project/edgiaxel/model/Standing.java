package project.edgiaxel.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Standing {

    private final IntegerProperty position;
    private final StringProperty name;      // Manufacturer, Team, or Driver
    private final StringProperty carModel;  // Only used for Team standings
    private final IntegerProperty totalPoints;

    public Standing(int position, String name, String carModel, int totalPoints) {
        this.position = new SimpleIntegerProperty(position);
        this.name = new SimpleStringProperty(name);
        this.carModel = new SimpleStringProperty(carModel);
        this.totalPoints = new SimpleIntegerProperty(totalPoints);
    }

    // Properties
    public IntegerProperty positionProperty() {
        return position;
    }

    public StringProperty nameProperty() {
        return name;
    }

    public StringProperty carModelProperty() {
        return carModel;
    }

    public IntegerProperty totalPointsProperty() {
        return totalPoints;
    }

    // Getters
    public int getPosition() {
        return position.get();
    }

    public String getName() {
        return name.get();
    }

    public String getCarModel() {
        return carModel.get();
    }

    public int getTotalPoints() {
        return totalPoints.get();
    }

    // Setters
    public void setPosition(int position) {
        this.position.set(position);
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public void setCarModel(String carModel) {
        this.carModel.set(carModel);
    }

    public void setTotalPoints(int totalPoints) {
        this.totalPoints.set(totalPoints);
    }

    @Override
    public String toString() {
        return "#" + getPosition() + " - " + getName() + " (" + getTotalPoints() + " pts)";
    }
}
