package project.edgiaxel.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;

public class StandingEntry {

    private final IntegerProperty position;
    private final StringProperty name;
    private final IntegerProperty points;
    private final StringProperty category;
    private final StringProperty carNumber; 

    public StandingEntry(int position, String name, int points, String category, String carNumber) {
        this.position = new SimpleIntegerProperty(position);
        this.name = new SimpleStringProperty(name);
        this.points = new SimpleIntegerProperty(points);
        this.category = new SimpleStringProperty(category);
        this.carNumber = new SimpleStringProperty(carNumber);
    }

    public IntegerProperty positionProperty() {
        return position;
    }

    public StringProperty nameProperty() {
        return name;
    }

    public IntegerProperty pointsProperty() {
        return points;
    }

    public StringProperty categoryProperty() {
        return category;
    }

    public StringProperty carNumberProperty() {
        return carNumber;
    }

    public int getPosition() {
        return position.get();
    }

    public String getName() {
        return name.get();
    }

    public int getPoints() {
        return points.get();
    }

    public String getCategory() {
        return category.get();
    }

    public String getCarNumber() {
        return carNumber.get();
    }

    public void setPosition(int position) {
        this.position.set(position);
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public void setPoints(int points) {
        this.points.set(points);
    }

    public void setCategory(String category) {
        this.category.set(category);
    }

    public void setCarNumber(String carNumber) {
        this.carNumber.set(carNumber);
    }

    @Override
    public String toString() {
        return String.format(
            "Position: %d | Name: %s | Points: %d | Category: %s | Car #: %s",
            getPosition(), getName(), getPoints(), getCategory(), getCarNumber()
        );
    }
}
