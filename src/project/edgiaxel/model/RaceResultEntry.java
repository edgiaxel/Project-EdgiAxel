package project.edgiaxel.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class RaceResultEntry {

    private final IntegerProperty position;
    private final StringProperty carNumber;
    private final StringProperty teamName;
    private final StringProperty carModel;
    private final StringProperty bestTimeOrLaps; 
    private final StringProperty category;

    public RaceResultEntry(int position, String carNumber, String teamName, String carModel, String bestTimeOrLaps, String category) {
        this.position = new SimpleIntegerProperty(position);
        this.carNumber = new SimpleStringProperty(carNumber);
        this.teamName = new SimpleStringProperty(teamName);
        this.carModel = new SimpleStringProperty(carModel);
        this.bestTimeOrLaps = new SimpleStringProperty(bestTimeOrLaps);
        this.category = new SimpleStringProperty(category);
    }

    public IntegerProperty positionProperty() {
        return position;
    }

    public StringProperty carNumberProperty() {
        return carNumber;
    }

    public StringProperty teamNameProperty() {
        return teamName;
    }

    public StringProperty carModelProperty() {
        return carModel;
    }

    public StringProperty bestTimeOrLapsProperty() {
        return bestTimeOrLaps;
    }

    public StringProperty categoryProperty() {
        return category;
    }

    public int getPosition() {
        return position.get();
    }

    public String getCarNumber() {
        return carNumber.get();
    }

    public String getTeamName() {
        return teamName.get();
    }

    public String getCarModel() {
        return carModel.get();
    }

    public String getBestTimeOrLaps() {
        return bestTimeOrLaps.get();
    }

    public String getCategory() {
        return category.get();
    }

    public void setPosition(int position) {
        this.position.set(position);
    }

    public void setCarNumber(String carNumber) {
        this.carNumber.set(carNumber);
    }

    public void setTeamName(String teamName) {
        this.teamName.set(teamName);
    }

    public void setCarModel(String carModel) {
        this.carModel.set(carModel);
    }

    public void setBestTimeOrLaps(String bestTimeOrLaps) {
        this.bestTimeOrLaps.set(bestTimeOrLaps);
    }

    public void setCategory(String category) {
        this.category.set(category);
    }

    @Override
    public String toString() {
        return String.format(
                "Pos: %d | #%s | %s | %s | %s | %s",
                getPosition(), getCarNumber(), getTeamName(), getCarModel(), getBestTimeOrLaps(), getCategory()
        );
    }
}
