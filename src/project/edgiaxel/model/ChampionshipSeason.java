package project.edgiaxel.model;

import javafx.beans.property.*;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

public class ChampionshipSeason {

    private final IntegerProperty seasonId;
    private final IntegerProperty year;
    private final StringProperty status; 

    private ObservableList<Circuit> circuits;

    public ChampionshipSeason(int seasonId, int year, String status) {
        this.seasonId = new SimpleIntegerProperty(seasonId);
        this.year = new SimpleIntegerProperty(year);
        this.status = new SimpleStringProperty(status);
    }

    public IntegerProperty seasonIdProperty() {
        return seasonId;
    }

    public IntegerProperty yearProperty() {
        return year;
    }

    public StringProperty statusProperty() {
        return status;
    }

    public int getSeasonId() {
        return seasonId.get();
    }

    public int getYear() {
        return year.get();
    }

    public String getStatus() {
        return status.get();
    }

    public ObservableList<Circuit> getCircuits() {
        return circuits;
    }

    public void setSeasonId(int seasonId) {
        this.seasonId.set(seasonId);
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    public void setCircuits(ObservableList<Circuit> circuits) {
        this.circuits = circuits;
    }
}
