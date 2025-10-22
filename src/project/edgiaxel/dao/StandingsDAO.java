package project.edgiaxel.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import project.edgiaxel.model.StandingEntry;
import project.edgiaxel.model.RaceResultEntry;

public class StandingsDAO {

    private static StandingsDAO instance;

    private StandingsDAO() {
    }

    public static StandingsDAO getInstance() {
        if (instance == null) {
            instance = new StandingsDAO();
        }
        return instance;
    }

    public ObservableList<StandingEntry> getChampionshipStandings(int seasonId, String type) {
        ObservableList<StandingEntry> standings = FXCollections.observableArrayList();

        if (seasonId == 2025) {
            ObservableList<StandingEntry> hypercarTeams = FXCollections.observableArrayList(
                    new StandingEntry(1, "Toyota Gazoo Racing (#7)", 165, "Hypercar", "7"),
                    new StandingEntry(2, "Porsche Penske Motorsport (#6)", 158, "Hypercar", "6"),
                    new StandingEntry(3, "Ferrari AF Corse (#50)", 140, "Hypercar", "50"),
                    new StandingEntry(4, "Toyota Gazoo Racing (#8)", 135, "Hypercar", "8"),
                    new StandingEntry(5, "Peugeot TotalEnergies (#93)", 110, "Hypercar", "93"),
                    new StandingEntry(6, "BMW M Team WRT (#20)", 105, "Hypercar", "20"),
                    new StandingEntry(7, "Cadillac Hertz Team Jota (#12)", 90, "Hypercar", "12"),
                    new StandingEntry(8, "Alpine Endurance Team (#35)", 88, "Hypercar", "35"),
                    new StandingEntry(9, "Proton Competition (#99)", 65, "Hypercar", "99"),
                    new StandingEntry(10, "BMW M Team WRT (#15)", 62, "Hypercar", "15"),
                    new StandingEntry(11, "Ferrari AF Corse (#51)", 60, "Hypercar", "51"),
                    new StandingEntry(12, "Alpine Endurance Team (#36)", 58, "Hypercar", "36"),
                    new StandingEntry(13, "Peugeot TotalEnergies (#94)", 56, "Hypercar", "94"),
                    new StandingEntry(14, "Porsche Penske Motorsport (#5)", 55, "Hypercar", "5"),
                    new StandingEntry(15, "AF Corse (#83)", 52, "Hypercar", "83"),
                    new StandingEntry(16, "Cadillac Hertz Team Jota (#38)", 51, "Hypercar", "38"),
                    new StandingEntry(17, "Aston Martin THOR Team (#007)", 50, "Hypercar", "007"),
                    new StandingEntry(18, "Aston Martin THOR Team (#009)", 48, "Hypercar", "009")
            );

            ObservableList<StandingEntry> lmgt3Teams = FXCollections.observableArrayList(
                    new StandingEntry(1, "Team WRT (BMW M4 GT3 Evo)", 175, "LMGT3", "46"),
                    new StandingEntry(2, "Iron Dames (Porsche 911 GT3 R)", 160, "LMGT3", "85"),
                    new StandingEntry(3, "Heart of Racing Team (Aston Martin Vantage)", 140, "LMGT3", "27"),
                    new StandingEntry(4, "AF Corse (Ferrari 296 GT3)", 125, "LMGT3", "83"), 
                    new StandingEntry(5, "Manthey 1st Phorm (Porsche 911 GT3 R)", 115, "LMGT3", "92"),
                    new StandingEntry(6, "United Autosports (McLaren 720S GT3 Evo)", 95, "LMGT3", "59"),
                    new StandingEntry(7, "Iron Lynx (Mercedes-AMG GT3 Evo)", 80, "LMGT3", "60"),
                    new StandingEntry(8, "Akkodis ASP Team (Lexus RC F GT3)", 75, "LMGT3", "78"),
                    new StandingEntry(9, "Proton Competition (Ford Mustang GT3)", 60, "LMGT3", "77"),
                    new StandingEntry(10, "The Bend Team WRT (BMW M4 GT3 Evo)", 58, "LMGT3", "31"),
                    new StandingEntry(11, "Vista AF Corse (Ferrari 296 GT3) (#21)", 56, "LMGT3", "21"),
                    new StandingEntry(12, "TF Sport (Corvette Z06 GT3.R) (#33)", 55, "LMGT3", "33"),
                    new StandingEntry(13, "United Autosports (McLaren 720S GT3 Evo) (#95)", 53, "LMGT3", "95"),
                    new StandingEntry(14, "Iron Lynx (Mercedes-AMG GT3 Evo) (#61)", 52, "LMGT3", "61"),
                    new StandingEntry(15, "Vista AF Corse (Ferrari 296 GT3) (#54)", 51, "LMGT3", "54"),
                    new StandingEntry(16, "Akkodis ASP Team (Lexus RC F GT3) (#87)", 50, "LMGT3", "87"),
                    new StandingEntry(17, "Proton Competition (Ford Mustang GT3) (#88)", 49, "LMGT3", "88"), 
                    new StandingEntry(18, "TF Sport (Corvette Z06 GT3.R) (#81)", 48, "LMGT3", "81"), 
                    new StandingEntry(19, "Racing Spirit of Léman (Aston Martin Vantage) (#10)", 47, "LMGT3", "10")
            );

            switch (type) {
                case "MANUFACTURER":
                    standings.addAll(
                            new StandingEntry(1, "Toyota", 175, "Hypercar", ""),
                            new StandingEntry(2, "Porsche", 170, "Hypercar", ""), 
                            new StandingEntry(3, "BMW", 160, "Hypercar", ""),
                            new StandingEntry(4, "Ferrari", 150, "Hypercar", ""),
                            new StandingEntry(5, "Cadillac", 100, "Hypercar", ""),
                            new StandingEntry(6, "Peugeot", 95, "Hypercar", ""),
                            new StandingEntry(7, "Aston Martin", 80, "LMGT3", ""),
                            new StandingEntry(8, "Alpine", 75, "Hypercar", ""),
                            new StandingEntry(9, "Chevrolet", 70, "LMGT3", ""),
                            new StandingEntry(10, "McLaren", 65, "LMGT3", "")
                    );
                    break;

                case "HYPERCAR_TEAM":
                    standings.addAll(hypercarTeams);
                    break;

                case "LMGT3_TEAM":
                    standings.addAll(lmgt3Teams);
                    break;

                case "OVERALL":
                    ObservableList<StandingEntry> overall = FXCollections.observableArrayList(hypercarTeams);
                    overall.addAll(lmgt3Teams);

                    for (int i = 0; i < overall.size(); i++) {
                        overall.get(i).setPosition(i + 1);
                    }
                    standings.addAll(overall);
                    break;
            }
        }

        return standings;
    }

    public ObservableList<RaceResultEntry> getRaceResults(int seasonId, int circuitId, String sessionType) {
        ObservableList<RaceResultEntry> results = FXCollections.observableArrayList();

        if (seasonId != 2025) {
            return results;
        }
        
        switch (circuitId) {
            case 1:
                results.addAll(
                        new RaceResultEntry(1, "7", "Toyota Gazoo Racing", "GR010 Hybrid", "200 Laps", "Hypercar"),
                        new RaceResultEntry(2, "6", "Porsche Penske Motorsport", "963", "199 Laps", "Hypercar"),
                        new RaceResultEntry(3, "50", "Ferrari AF Corse", "499P", "198 Laps", "Hypercar"),
                        new RaceResultEntry(4, "20", "BMW M Team WRT", "M Hybrid V8", "197 Laps", "Hypercar"),
                        new RaceResultEntry(5, "8", "Toyota Gazoo Racing", "GR010 Hybrid", "196 Laps", "Hypercar"),
                        new RaceResultEntry(6, "12", "Cadillac Hertz Team Jota", "V-Series.R", "195 Laps", "Hypercar"),
                        new RaceResultEntry(7, "5", "Porsche Penske Motorsport", "963", "194 Laps", "Hypercar"),
                        new RaceResultEntry(8, "51", "Ferrari AF Corse", "499P", "193 Laps", "Hypercar"),
                        new RaceResultEntry(9, "35", "Alpine Endurance Team", "A424", "192 Laps", "Hypercar"),
                        new RaceResultEntry(10, "15", "BMW M Team WRT", "M Hybrid V8", "191 Laps", "Hypercar"),
                        new RaceResultEntry(11, "93", "Peugeot TotalEnergies", "9X8", "190 Laps", "Hypercar"),
                        new RaceResultEntry(12, "83", "AF Corse", "499P", "189 Laps", "Hypercar"),
                        new RaceResultEntry(13, "38", "Cadillac Hertz Team Jota", "V-Series.R", "188 Laps", "Hypercar"),
                        new RaceResultEntry(14, "99", "Proton Competition", "963", "187 Laps", "Hypercar"),
                        new RaceResultEntry(15, "36", "Alpine Endurance Team", "A424", "186 Laps", "Hypercar"),
                        new RaceResultEntry(16, "94", "Peugeot TotalEnergies", "9X8", "185 Laps", "Hypercar"),
                        new RaceResultEntry(17, "007", "Aston Martin THOR Team", "Valkyrie", "170 Laps (DNF)", "Hypercar"),
                        new RaceResultEntry(18, "009", "Aston Martin THOR Team", "Valkyrie", "165 Laps (DNF)", "Hypercar"),
                        new RaceResultEntry(10, "46", "Team WRT", "BMW M4 GT3 Evo", "180 Laps", "LMGT3"),
                        new RaceResultEntry(11, "85", "Iron Dames", "Porsche 911 GT3 R", "179 Laps", "LMGT3"),
                        new RaceResultEntry(12, "27", "Heart of Racing Team", "Vantage AMR GT3 Evo", "178 Laps", "LMGT3"),
                        new RaceResultEntry(13, "21", "Vista AF Corse", "Ferrari 296 GT3", "177 Laps", "LMGT3"),
                        new RaceResultEntry(14, "92", "Manthey 1st Phorm", "Porsche 911 GT3 R", "176 Laps", "LMGT3"),
                        new RaceResultEntry(15, "59", "United Autosports", "McLaren 720S GT3 Evo", "175 Laps", "LMGT3"),
                        new RaceResultEntry(16, "60", "Iron Lynx", "Mercedes-AMG GT3 Evo", "174 Laps", "LMGT3"),
                        new RaceResultEntry(17, "78", "Akkodis ASP Team", "Lexus RC F GT3", "173 Laps", "LMGT3"),
                        new RaceResultEntry(18, "77", "Proton Competition", "Ford Mustang GT3", "165 Laps (DNF)", "LMGT3"),
                        new RaceResultEntry(19, "31", "The Bend Team WRT", "BMW M4 GT3 Evo", "172 Laps", "LMGT3"),
                        new RaceResultEntry(20, "54", "Vista AF Corse", "Ferrari 296 GT3", "171 Laps", "LMGT3"),
                        new RaceResultEntry(21, "33", "TF Sport", "Corvette Z06 GT3.R", "170 Laps", "LMGT3"),
                        new RaceResultEntry(22, "81", "TF Sport", "Corvette Z06 GT3.R", "169 Laps", "LMGT3"),
                        new RaceResultEntry(23, "95", "United Autosports", "McLaren 720S GT3 Evo", "168 Laps", "LMGT3"),
                        new RaceResultEntry(24, "61", "Iron Lynx", "Mercedes-AMG GT3 Evo", "167 Laps", "LMGT3"),
                        new RaceResultEntry(25, "87", "Akkodis ASP Team", "Lexus RC F GT3", "166 Laps", "LMGT3"),
                        new RaceResultEntry(26, "88", "Proton Competition", "Ford Mustang GT3", "160 Laps (DNF)", "LMGT3"),
                        new RaceResultEntry(27, "10", "Racing Spirit of Léman", "Vantage AMR GT3 Evo", "155 Laps (DNF)", "LMGT3")
                );
                break;

            case 2:
                results.addAll(
                        new RaceResultEntry(1, "50", "Ferrari AF Corse", "499P", "150 Laps", "Hypercar"),
                        new RaceResultEntry(2, "7", "Toyota Gazoo Racing", "GR010 Hybrid", "150 Laps", "Hypercar"),
                        new RaceResultEntry(3, "5", "Porsche Penske Motorsport", "963", "149 Laps", "Hypercar"),
                        new RaceResultEntry(4, "93", "Peugeot TotalEnergies", "9X8", "148 Laps", "Hypercar"),
                        new RaceResultEntry(5, "6", "Porsche Penske Motorsport", "963", "147 Laps", "Hypercar"),
                        new RaceResultEntry(6, "8", "Toyota Gazoo Racing", "GR010 Hybrid", "146 Laps", "Hypercar"),
                        new RaceResultEntry(7, "51", "Ferrari AF Corse", "499P", "145 Laps", "Hypercar"),
                        new RaceResultEntry(8, "12", "Cadillac Hertz Team Jota", "V-Series.R", "144 Laps", "Hypercar"),
                        new RaceResultEntry(9, "20", "BMW M Team WRT", "M Hybrid V8", "143 Laps", "Hypercar"),
                        new RaceResultEntry(10, "83", "AF Corse", "499P", "142 Laps", "Hypercar"),
                        new RaceResultEntry(11, "35", "Alpine Endurance Team", "A424", "141 Laps", "Hypercar"),
                        new RaceResultEntry(12, "15", "BMW M Team WRT", "M Hybrid V8", "140 Laps", "Hypercar"),
                        new RaceResultEntry(13, "36", "Alpine Endurance Team", "A424", "138 Laps", "Hypercar"),
                        new RaceResultEntry(14, "94", "Peugeot TotalEnergies", "9X8", "135 Laps", "Hypercar"),
                        new RaceResultEntry(15, "99", "Proton Competition", "963", "130 Laps", "Hypercar"),
                        new RaceResultEntry(16, "38", "Cadillac Hertz Team Jota", "V-Series.R", "125 Laps (DNF)", "Hypercar"),
                        new RaceResultEntry(17, "007", "Aston Martin THOR Team", "Valkyrie", "110 Laps (DNF)", "Hypercar"),
                        new RaceResultEntry(18, "009", "Aston Martin THOR Team", "Valkyrie", "95 Laps (DNF)", "Hypercar"),
                        new RaceResultEntry(10, "85", "Iron Dames", "Porsche 911 GT3 R", "130 Laps", "LMGT3"),
                        new RaceResultEntry(11, "46", "Team WRT", "BMW M4 GT3 Evo", "129 Laps", "LMGT3"),
                        new RaceResultEntry(12, "54", "Vista AF Corse", "Ferrari 296 GT3", "128 Laps", "LMGT3"),
                        new RaceResultEntry(13, "92", "Manthey 1st Phorm", "Porsche 911 GT3 R", "127 Laps", "LMGT3"),
                        new RaceResultEntry(14, "59", "United Autosports", "McLaren 720S GT3 Evo", "126 Laps", "LMGT3"),
                        new RaceResultEntry(15, "60", "Iron Lynx", "Mercedes-AMG GT3 Evo", "125 Laps", "LMGT3"),
                        new RaceResultEntry(16, "78", "Akkodis ASP Team", "Lexus RC F GT3", "124 Laps", "LMGT3"),
                        new RaceResultEntry(17, "77", "Proton Competition", "Ford Mustang GT3", "123 Laps", "LMGT3"),
                        new RaceResultEntry(18, "27", "Heart of Racing Team", "Vantage AMR GT3 Evo", "122 Laps", "LMGT3"),
                        new RaceResultEntry(19, "31", "The Bend Team WRT", "BMW M4 GT3 Evo", "121 Laps", "LMGT3"),
                        new RaceResultEntry(20, "21", "Vista AF Corse", "Ferrari 296 GT3", "120 Laps", "LMGT3"),
                        new RaceResultEntry(21, "33", "TF Sport", "Corvette Z06 GT3.R", "119 Laps", "LMGT3"),
                        new RaceResultEntry(22, "81", "TF Sport", "Corvette Z06 GT3.R", "118 Laps", "LMGT3"),
                        new RaceResultEntry(23, "95", "United Autosports", "McLaren 720S GT3 Evo", "117 Laps", "LMGT3"),
                        new RaceResultEntry(24, "61", "Iron Lynx", "Mercedes-AMG GT3 Evo", "116 Laps", "LMGT3"),
                        new RaceResultEntry(25, "87", "Akkodis ASP Team", "Lexus RC F GT3", "115 Laps", "LMGT3"),
                        new RaceResultEntry(26, "88", "Proton Competition", "Ford Mustang GT3", "110 Laps", "LMGT3"),
                        new RaceResultEntry(27, "10", "Racing Spirit of Léman", "Vantage AMR GT3 Evo", "100 Laps (DNF)", "LMGT3")
                );
                break;

            case 3:
                results.addAll(
                        new RaceResultEntry(1, "6", "Porsche Penske Motorsport", "963", "145 Laps", "Hypercar"),
                        new RaceResultEntry(2, "8", "Toyota Gazoo Racing", "GR010 Hybrid", "145 Laps", "Hypercar"),
                        new RaceResultEntry(3, "51", "Ferrari AF Corse", "499P", "144 Laps", "Hypercar"),
                        new RaceResultEntry(4, "15", "BMW M Team WRT", "M Hybrid V8", "143 Laps", "Hypercar"),
                        new RaceResultEntry(5, "7", "Toyota Gazoo Racing", "GR010 Hybrid", "142 Laps", "Hypercar"),
                        new RaceResultEntry(6, "50", "Ferrari AF Corse", "499P", "141 Laps", "Hypercar"),
                        new RaceResultEntry(7, "5", "Porsche Penske Motorsport", "963", "140 Laps", "Hypercar"),
                        new RaceResultEntry(8, "12", "Cadillac Hertz Team Jota", "V-Series.R", "139 Laps", "Hypercar"),
                        new RaceResultEntry(9, "35", "Alpine Endurance Team", "A424", "138 Laps", "Hypercar"),
                        new RaceResultEntry(10, "20", "BMW M Team WRT", "M Hybrid V8", "137 Laps", "Hypercar"),
                        new RaceResultEntry(11, "93", "Peugeot TotalEnergies", "9X8", "136 Laps", "Hypercar"),
                        new RaceResultEntry(12, "83", "AF Corse", "499P", "135 Laps", "Hypercar"),
                        new RaceResultEntry(13, "38", "Cadillac Hertz Team Jota", "V-Series.R", "134 Laps", "Hypercar"),
                        new RaceResultEntry(14, "99", "Proton Competition", "963", "133 Laps", "Hypercar"),
                        new RaceResultEntry(15, "36", "Alpine Endurance Team", "A424", "132 Laps", "Hypercar"),
                        new RaceResultEntry(16, "94", "Peugeot TotalEnergies", "9X8", "130 Laps", "Hypercar"),
                        new RaceResultEntry(17, "009", "Aston Martin THOR Team", "Valkyrie", "115 Laps (DNF)", "Hypercar"),
                        new RaceResultEntry(18, "007", "Aston Martin THOR Team", "Valkyrie", "100 Laps (DNF)", "Hypercar"),
                        new RaceResultEntry(10, "27", "Heart of Racing Team", "Vantage AMR GT3 Evo", "125 Laps", "LMGT3"),
                        new RaceResultEntry(11, "46", "Team WRT", "BMW M4 GT3 Evo", "124 Laps", "LMGT3"),
                        new RaceResultEntry(12, "92", "Manthey 1st Phorm", "Porsche 911 GT3 R", "123 Laps", "LMGT3"),
                        new RaceResultEntry(13, "85", "Iron Dames", "Porsche 911 GT3 R", "122 Laps", "LMGT3"),
                        new RaceResultEntry(14, "59", "United Autosports", "McLaren 720S GT3 Evo", "121 Laps", "LMGT3"),
                        new RaceResultEntry(15, "60", "Iron Lynx", "Mercedes-AMG GT3 Evo", "120 Laps", "LMGT3"),
                        new RaceResultEntry(16, "78", "Akkodis ASP Team", "Lexus RC F GT3", "119 Laps", "LMGT3"),
                        new RaceResultEntry(17, "77", "Proton Competition", "Ford Mustang GT3", "118 Laps", "LMGT3"),
                        new RaceResultEntry(18, "31", "The Bend Team WRT", "BMW M4 GT3 Evo", "117 Laps", "LMGT3"),
                        new RaceResultEntry(19, "21", "Vista AF Corse", "Ferrari 296 GT3", "116 Laps", "LMGT3"),
                        new RaceResultEntry(20, "54", "Vista AF Corse", "Ferrari 296 GT3", "115 Laps", "LMGT3"),
                        new RaceResultEntry(21, "33", "TF Sport", "Corvette Z06 GT3.R", "114 Laps", "LMGT3"),
                        new RaceResultEntry(22, "81", "TF Sport", "Corvette Z06 GT3.R", "113 Laps", "LMGT3"),
                        new RaceResultEntry(23, "95", "United Autosports", "McLaren 720S GT3 Evo", "112 Laps", "LMGT3"),
                        new RaceResultEntry(24, "61", "Iron Lynx", "Mercedes-AMG GT3 Evo", "110 Laps", "LMGT3"),
                        new RaceResultEntry(25, "87", "Akkodis ASP Team", "Lexus RC F GT3", "105 Laps", "LMGT3"),
                        new RaceResultEntry(26, "88", "Proton Competition", "Ford Mustang GT3", "90 Laps (DNF)", "LMGT3"),
                        new RaceResultEntry(27, "10", "Racing Spirit of Léman", "Vantage AMR GT3 Evo", "85 Laps (DNF)", "LMGT3")
                );
                break;

            case 4:
                results.addAll(
                        new RaceResultEntry(1, "7", "Toyota Gazoo Racing", "GR010 Hybrid", "350 Laps", "Hypercar"),
                        new RaceResultEntry(2, "50", "Ferrari AF Corse", "499P", "349 Laps", "Hypercar"),
                        new RaceResultEntry(3, "6", "Porsche Penske Motorsport", "963", "345 Laps", "Hypercar"),
                        new RaceResultEntry(4, "8", "Toyota Gazoo Racing", "GR010 Hybrid", "344 Laps", "Hypercar"),
                        new RaceResultEntry(5, "94", "Peugeot TotalEnergies", "9X8", "330 Laps", "Hypercar"),
                        new RaceResultEntry(6, "51", "Ferrari AF Corse", "499P", "328 Laps", "Hypercar"),
                        new RaceResultEntry(7, "5", "Porsche Penske Motorsport", "963", "325 Laps", "Hypercar"),
                        new RaceResultEntry(8, "12", "Cadillac Hertz Team Jota", "V-Series.R", "322 Laps", "Hypercar"),
                        new RaceResultEntry(9, "20", "BMW M Team WRT", "M Hybrid V8", "320 Laps", "Hypercar"),
                        new RaceResultEntry(10, "35", "Alpine Endurance Team", "A424", "318 Laps", "Hypercar"),
                        new RaceResultEntry(11, "15", "BMW M Team WRT", "M Hybrid V8", "315 Laps", "Hypercar"),
                        new RaceResultEntry(12, "83", "AF Corse", "499P", "312 Laps", "Hypercar"),
                        new RaceResultEntry(13, "93", "Peugeot TotalEnergies", "9X8", "310 Laps", "Hypercar"),
                        new RaceResultEntry(14, "36", "Alpine Endurance Team", "A424", "305 Laps", "Hypercar"),
                        new RaceResultEntry(15, "99", "Proton Competition", "963", "300 Laps", "Hypercar"),
                        new RaceResultEntry(16, "38", "Cadillac Hertz Team Jota", "V-Series.R", "280 Laps (DNF)", "Hypercar"),
                        new RaceResultEntry(17, "007", "Aston Martin THOR Team", "Valkyrie", "250 Laps (DNF)", "Hypercar"),
                        new RaceResultEntry(18, "009", "Aston Martin THOR Team", "Valkyrie", "210 Laps (DNF)", "Hypercar"),
                        new RaceResultEntry(10, "92", "Manthey 1st Phorm", "Porsche 911 GT3 R", "310 Laps", "LMGT3"),
                        new RaceResultEntry(11, "46", "Team WRT", "BMW M4 GT3 Evo", "309 Laps", "LMGT3"),
                        new RaceResultEntry(12, "59", "United Autosports", "McLaren 720S GT3 Evo", "308 Laps", "LMGT3"),
                        new RaceResultEntry(13, "61", "Iron Lynx", "Mercedes-AMG GT3 Evo", "307 Laps", "LMGT3"),
                        new RaceResultEntry(14, "85", "Iron Dames", "Porsche 911 GT3 R", "306 Laps", "LMGT3"),
                        new RaceResultEntry(15, "27", "Heart of Racing Team", "Vantage AMR GT3 Evo", "305 Laps", "LMGT3"),
                        new RaceResultEntry(16, "78", "Akkodis ASP Team", "Lexus RC F GT3", "304 Laps", "LMGT3"),
                        new RaceResultEntry(17, "77", "Proton Competition", "Ford Mustang GT3", "303 Laps", "LMGT3"),
                        new RaceResultEntry(18, "31", "The Bend Team WRT", "BMW M4 GT3 Evo", "302 Laps", "LMGT3"),
                        new RaceResultEntry(19, "21", "Vista AF Corse", "Ferrari 296 GT3", "301 Laps", "LMGT3"),
                        new RaceResultEntry(20, "54", "Vista AF Corse", "Ferrari 296 GT3", "300 Laps", "LMGT3"),
                        new RaceResultEntry(21, "33", "TF Sport", "Corvette Z06 GT3.R", "298 Laps", "LMGT3"),
                        new RaceResultEntry(22, "81", "TF Sport", "Corvette Z06 GT3.R", "295 Laps", "LMGT3"),
                        new RaceResultEntry(23, "95", "United Autosports", "McLaren 720S GT3 Evo", "290 Laps", "LMGT3"),
                        new RaceResultEntry(24, "60", "Iron Lynx", "Mercedes-AMG GT3 Evo", "285 Laps", "LMGT3"),
                        new RaceResultEntry(25, "87", "Akkodis ASP Team", "Lexus RC F GT3", "270 Laps", "LMGT3"),
                        new RaceResultEntry(26, "88", "Proton Competition", "Ford Mustang GT3", "250 Laps (DNF)", "LMGT3"),
                        new RaceResultEntry(27, "10", "Racing Spirit of Léman", "Vantage AMR GT3 Evo", "220 Laps (DNF)", "LMGT3")
                );
                break;

            case 5:
                results.addAll(
                        new RaceResultEntry(1, "12", "Cadillac Hertz Team Jota", "V-Series.R", "140 Laps", "Hypercar"),
                        new RaceResultEntry(2, "6", "Porsche Penske Motorsport", "963", "139 Laps", "Hypercar"),
                        new RaceResultEntry(3, "93", "Peugeot TotalEnergies", "9X8", "138 Laps", "Hypercar"),
                        new RaceResultEntry(4, "50", "Ferrari AF Corse", "499P", "137 Laps", "Hypercar"),
                        new RaceResultEntry(5, "7", "Toyota Gazoo Racing", "GR010 Hybrid", "136 Laps", "Hypercar"),
                        new RaceResultEntry(6, "8", "Toyota Gazoo Racing", "GR010 Hybrid", "135 Laps", "Hypercar"),
                        new RaceResultEntry(7, "5", "Porsche Penske Motorsport", "963", "134 Laps", "Hypercar"),
                        new RaceResultEntry(8, "51", "Ferrari AF Corse", "499P", "133 Laps", "Hypercar"),
                        new RaceResultEntry(9, "20", "BMW M Team WRT", "M Hybrid V8", "132 Laps", "Hypercar"),
                        new RaceResultEntry(10, "15", "BMW M Team WRT", "M Hybrid V8", "131 Laps", "Hypercar"),
                        new RaceResultEntry(11, "35", "Alpine Endurance Team", "A424", "130 Laps", "Hypercar"),
                        new RaceResultEntry(12, "36", "Alpine Endurance Team", "A424", "129 Laps", "Hypercar"),
                        new RaceResultEntry(13, "83", "AF Corse", "499P", "128 Laps", "Hypercar"),
                        new RaceResultEntry(14, "94", "Peugeot TotalEnergies", "9X8", "127 Laps", "Hypercar"),
                        new RaceResultEntry(15, "99", "Proton Competition", "963", "126 Laps", "Hypercar"),
                        new RaceResultEntry(16, "38", "Cadillac Hertz Team Jota", "V-Series.R", "120 Laps", "Hypercar"),
                        new RaceResultEntry(17, "007", "Aston Martin THOR Team", "Valkyrie", "100 Laps (DNF)", "Hypercar"),
                        new RaceResultEntry(18, "009", "Aston Martin THOR Team", "Valkyrie", "95 Laps (DNF)", "Hypercar"),
                        new RaceResultEntry(10, "31", "The Bend Team WRT", "BMW M4 GT3 Evo", "120 Laps", "LMGT3"),
                        new RaceResultEntry(11, "27", "Heart of Racing Team", "Vantage AMR GT3 Evo", "119 Laps", "LMGT3"),
                        new RaceResultEntry(12, "85", "Iron Dames", "Porsche 911 GT3 R", "118 Laps", "LMGT3"),
                        new RaceResultEntry(13, "46", "Team WRT", "BMW M4 GT3 Evo", "117 Laps", "LMGT3"),
                        new RaceResultEntry(14, "92", "Manthey 1st Phorm", "Porsche 911 GT3 R", "116 Laps", "LMGT3"),
                        new RaceResultEntry(15, "59", "United Autosports", "McLaren 720S GT3 Evo", "115 Laps", "LMGT3"),
                        new RaceResultEntry(16, "60", "Iron Lynx", "Mercedes-AMG GT3 Evo", "114 Laps", "LMGT3"),
                        new RaceResultEntry(17, "78", "Akkodis ASP Team", "Lexus RC F GT3", "113 Laps", "LMGT3"),
                        new RaceResultEntry(18, "77", "Proton Competition", "Ford Mustang GT3", "112 Laps", "LMGT3"),
                        new RaceResultEntry(19, "21", "Vista AF Corse", "Ferrari 296 GT3", "111 Laps", "LMGT3"),
                        new RaceResultEntry(20, "54", "Vista AF Corse", "Ferrari 296 GT3", "110 Laps", "LMGT3"),
                        new RaceResultEntry(21, "33", "TF Sport", "Corvette Z06 GT3.R", "109 Laps", "LMGT3"),
                        new RaceResultEntry(22, "81", "TF Sport", "Corvette Z06 GT3.R", "108 Laps", "LMGT3"),
                        new RaceResultEntry(23, "95", "United Autosports", "McLaren 720S GT3 Evo", "107 Laps", "LMGT3"),
                        new RaceResultEntry(24, "61", "Iron Lynx", "Mercedes-AMG GT3 Evo", "105 Laps", "LMGT3"),
                        new RaceResultEntry(25, "87", "Akkodis ASP Team", "Lexus RC F GT3", "100 Laps", "LMGT3"),
                        new RaceResultEntry(26, "88", "Proton Competition", "Ford Mustang GT3", "90 Laps (DNF)", "LMGT3"),
                        new RaceResultEntry(27, "10", "Racing Spirit of Léman", "Vantage AMR GT3 Evo", "85 Laps (DNF)", "LMGT3")
                );
                break;

            case 6:
                results.addAll(
                        new RaceResultEntry(1, "8", "Toyota Gazoo Racing", "GR010 Hybrid", "145 Laps", "Hypercar"),
                        new RaceResultEntry(2, "7", "Toyota Gazoo Racing", "GR010 Hybrid", "145 Laps", "Hypercar"),
                        new RaceResultEntry(3, "50", "Ferrari AF Corse", "499P", "144 Laps", "Hypercar"),
                        new RaceResultEntry(4, "6", "Porsche Penske Motorsport", "963", "143 Laps", "Hypercar"),
                        new RaceResultEntry(5, "5", "Porsche Penske Motorsport", "963", "142 Laps", "Hypercar"),
                        new RaceResultEntry(6, "51", "Ferrari AF Corse", "499P", "141 Laps", "Hypercar"),
                        new RaceResultEntry(7, "12", "Cadillac Hertz Team Jota", "V-Series.R", "140 Laps", "Hypercar"),
                        new RaceResultEntry(8, "38", "Cadillac Hertz Team Jota", "V-Series.R", "139 Laps", "Hypercar"),
                        new RaceResultEntry(9, "20", "BMW M Team WRT", "M Hybrid V8", "138 Laps", "Hypercar"),
                        new RaceResultEntry(10, "15", "BMW M Team WRT", "M Hybrid V8", "137 Laps", "Hypercar"),
                        new RaceResultEntry(11, "93", "Peugeot TotalEnergies", "9X8", "136 Laps", "Hypercar"),
                        new RaceResultEntry(12, "94", "Peugeot TotalEnergies", "9X8", "135 Laps", "Hypercar"),
                        new RaceResultEntry(13, "35", "Alpine Endurance Team", "A424", "134 Laps", "Hypercar"),
                        new RaceResultEntry(14, "36", "Alpine Endurance Team", "A424", "133 Laps", "Hypercar"),
                        new RaceResultEntry(15, "83", "AF Corse", "499P", "132 Laps", "Hypercar"),
                        new RaceResultEntry(16, "99", "Proton Competition", "963", "130 Laps", "Hypercar"),
                        new RaceResultEntry(17, "007", "Aston Martin THOR Team", "Valkyrie", "110 Laps (DNF)", "Hypercar"),
                        new RaceResultEntry(18, "009", "Aston Martin THOR Team", "Valkyrie", "90 Laps (DNF)", "Hypercar"),
                        new RaceResultEntry(10, "46", "Team WRT", "BMW M4 GT3 Evo", "125 Laps", "LMGT3"),
                        new RaceResultEntry(11, "85", "Iron Dames", "Porsche 911 GT3 R", "124 Laps", "LMGT3"),
                        new RaceResultEntry(12, "92", "Manthey 1st Phorm", "Porsche 911 GT3 R", "123 Laps", "LMGT3"),
                        new RaceResultEntry(13, "27", "Heart of Racing Team", "Vantage AMR GT3 Evo", "122 Laps", "LMGT3"),
                        new RaceResultEntry(14, "59", "United Autosports", "McLaren 720S GT3 Evo", "121 Laps", "LMGT3"),
                        new RaceResultEntry(15, "60", "Iron Lynx", "Mercedes-AMG GT3 Evo", "120 Laps", "LMGT3"),
                        new RaceResultEntry(16, "78", "Akkodis ASP Team", "Lexus RC F GT3", "119 Laps", "LMGT3"),
                        new RaceResultEntry(17, "77", "Proton Competition", "Ford Mustang GT3", "118 Laps", "LMGT3"),
                        new RaceResultEntry(18, "31", "The Bend Team WRT", "BMW M4 GT3 Evo", "117 Laps", "LMGT3"),
                        new RaceResultEntry(19, "21", "Vista AF Corse", "Ferrari 296 GT3", "116 Laps", "LMGT3"),
                        new RaceResultEntry(20, "54", "Vista AF Corse", "Ferrari 296 GT3", "115 Laps", "LMGT3"),
                        new RaceResultEntry(21, "33", "TF Sport", "Corvette Z06 GT3.R", "114 Laps", "LMGT3"),
                        new RaceResultEntry(22, "81", "TF Sport", "Corvette Z06 GT3.R", "113 Laps", "LMGT3"),
                        new RaceResultEntry(23, "95", "United Autosports", "McLaren 720S GT3 Evo", "112 Laps", "LMGT3"),
                        new RaceResultEntry(24, "61", "Iron Lynx", "Mercedes-AMG GT3 Evo", "110 Laps", "LMGT3"),
                        new RaceResultEntry(25, "87", "Akkodis ASP Team", "Lexus RC F GT3", "105 Laps", "LMGT3"),
                        new RaceResultEntry(26, "88", "Proton Competition", "Ford Mustang GT3", "95 Laps (DNF)", "LMGT3"),
                        new RaceResultEntry(27, "10", "Racing Spirit of Léman", "Vantage AMR GT3 Evo", "90 Laps (DNF)", "LMGT3")
                );
                break;

            case 7:
                results.addAll(
                        new RaceResultEntry(1, "7", "Toyota Gazoo Racing", "GR010 Hybrid", "160 Laps", "Hypercar"),
                        new RaceResultEntry(2, "8", "Toyota Gazoo Racing", "GR010 Hybrid", "159 Laps", "Hypercar"),
                        new RaceResultEntry(3, "6", "Porsche Penske Motorsport", "963", "158 Laps", "Hypercar"),
                        new RaceResultEntry(4, "50", "Ferrari AF Corse", "499P", "157 Laps", "Hypercar"),
                        new RaceResultEntry(5, "51", "Ferrari AF Corse", "499P", "156 Laps", "Hypercar"),
                        new RaceResultEntry(6, "5", "Porsche Penske Motorsport", "963", "155 Laps", "Hypercar"),
                        new RaceResultEntry(7, "12", "Cadillac Hertz Team Jota", "V-Series.R", "154 Laps", "Hypercar"),
                        new RaceResultEntry(8, "38", "Cadillac Hertz Team Jota", "V-Series.R", "153 Laps", "Hypercar"),
                        new RaceResultEntry(9, "20", "BMW M Team WRT", "M Hybrid V8", "152 Laps", "Hypercar"),
                        new RaceResultEntry(10, "15", "BMW M Team WRT", "M Hybrid V8", "151 Laps", "Hypercar"),
                        new RaceResultEntry(11, "93", "Peugeot TotalEnergies", "9X8", "150 Laps", "Hypercar"),
                        new RaceResultEntry(12, "94", "Peugeot TotalEnergies", "9X8", "149 Laps", "Hypercar"),
                        new RaceResultEntry(13, "35", "Alpine Endurance Team", "A424", "148 Laps", "Hypercar"),
                        new RaceResultEntry(14, "36", "Alpine Endurance Team", "A424", "147 Laps", "Hypercar"),
                        new RaceResultEntry(15, "83", "AF Corse", "499P", "146 Laps", "Hypercar"),
                        new RaceResultEntry(16, "99", "Proton Competition", "963", "145 Laps", "Hypercar"),
                        new RaceResultEntry(17, "007", "Aston Martin THOR Team", "Valkyrie", "130 Laps (DNF)", "Hypercar"),
                        new RaceResultEntry(18, "009", "Aston Martin THOR Team", "Valkyrie", "115 Laps (DNF)", "Hypercar"),
                        new RaceResultEntry(10, "85", "Iron Dames", "Porsche 911 GT3 R", "140 Laps", "LMGT3"),
                        new RaceResultEntry(11, "92", "Manthey 1st Phorm", "Porsche 911 GT3 R", "139 Laps", "LMGT3"),
                        new RaceResultEntry(12, "46", "Team WRT", "BMW M4 GT3 Evo", "138 Laps", "LMGT3"),
                        new RaceResultEntry(13, "27", "Heart of Racing Team", "Vantage AMR GT3 Evo", "137 Laps", "LMGT3"),
                        new RaceResultEntry(14, "59", "United Autosports", "McLaren 720S GT3 Evo", "136 Laps", "LMGT3"),
                        new RaceResultEntry(15, "60", "Iron Lynx", "Mercedes-AMG GT3 Evo", "135 Laps", "LMGT3"),
                        new RaceResultEntry(16, "78", "Akkodis ASP Team", "Lexus RC F GT3", "134 Laps", "LMGT3"),
                        new RaceResultEntry(17, "77", "Proton Competition", "Ford Mustang GT3", "133 Laps", "LMGT3"),
                        new RaceResultEntry(18, "31", "The Bend Team WRT", "BMW M4 GT3 Evo", "132 Laps", "LMGT3"),
                        new RaceResultEntry(19, "21", "Vista AF Corse", "Ferrari 296 GT3", "131 Laps", "LMGT3"),
                        new RaceResultEntry(20, "54", "Vista AF Corse", "Ferrari 296 GT3", "130 Laps", "LMGT3"),
                        new RaceResultEntry(21, "33", "TF Sport", "Corvette Z06 GT3.R", "129 Laps", "LMGT3"),
                        new RaceResultEntry(22, "81", "TF Sport", "Corvette Z06 GT3.R", "128 Laps", "LMGT3"),
                        new RaceResultEntry(23, "95", "United Autosports", "McLaren 720S GT3 Evo", "127 Laps", "LMGT3"),
                        new RaceResultEntry(24, "61", "Iron Lynx", "Mercedes-AMG GT3 Evo", "125 Laps", "LMGT3"),
                        new RaceResultEntry(25, "87", "Akkodis ASP Team", "Lexus RC F GT3", "120 Laps", "LMGT3"),
                        new RaceResultEntry(26, "88", "Proton Competition", "Ford Mustang GT3", "110 Laps (DNF)", "LMGT3"),
                        new RaceResultEntry(27, "10", "Racing Spirit of Léman", "Vantage AMR GT3 Evo", "100 Laps (DNF)", "LMGT3")
                );
                break;

            case 8:
                results.addAll(
                        new RaceResultEntry(1, "6", "Porsche Penske Motorsport", "963", "50 Laps", "Hypercar"),
                        new RaceResultEntry(2, "50", "Ferrari AF Corse", "499P", "249 Laps", "Hypercar"),
                        new RaceResultEntry(3, "15", "BMW M Team WRT", "M Hybrid V8", "248 Laps", "Hypercar"),
                        new RaceResultEntry(4, "7", "Toyota Gazoo Racing", "GR010 Hybrid", "247 Laps", "Hypercar"),
                        new RaceResultEntry(5, "8", "Toyota Gazoo Racing", "GR010 Hybrid", "246 Laps", "Hypercar"),
                        new RaceResultEntry(6, "5", "Porsche Penske Motorsport", "963", "245 Laps", "Hypercar"),
                        new RaceResultEntry(7, "51", "Ferrari AF Corse", "499P", "244 Laps", "Hypercar"),
                        new RaceResultEntry(8, "12", "Cadillac Hertz Team Jota", "V-Series.R", "243 Laps", "Hypercar"),
                        new RaceResultEntry(9, "38", "Cadillac Hertz Team Jota", "V-Series.R", "242 Laps", "Hypercar"),
                        new RaceResultEntry(10, "20", "BMW M Team WRT", "M Hybrid V8", "241 Laps", "Hypercar"),
                        new RaceResultEntry(11, "93", "Peugeot TotalEnergies", "9X8", "240 Laps", "Hypercar"),
                        new RaceResultEntry(12, "94", "Peugeot TotalEnergies", "9X8", "238 Laps", "Hypercar"),
                        new RaceResultEntry(13, "35", "Alpine Endurance Team", "A424", "235 Laps", "Hypercar"),
                        new RaceResultEntry(14, "36", "Alpine Endurance Team", "A424", "230 Laps", "Hypercar"),
                        new RaceResultEntry(15, "83", "AF Corse", "499P", "225 Laps", "Hypercar"),
                        new RaceResultEntry(16, "99", "Proton Competition", "963", "220 Laps", "Hypercar"),
                        new RaceResultEntry(17, "007", "Aston Martin THOR Team", "Valkyrie", "200 Laps (DNF)", "Hypercar"),
                        new RaceResultEntry(18, "009", "Aston Martin THOR Team", "Valkyrie", "180 Laps (DNF)", "Hypercar"),
                        new RaceResultEntry(10, "46", "Team WRT", "BMW M4 GT3 Evo", "220 Laps", "LMGT3"),
                        new RaceResultEntry(11, "59", "United Autosports", "McLaren 720S GT3 Evo", "219 Laps", "LMGT3"),
                        new RaceResultEntry(12, "85", "Iron Dames", "Porsche 911 GT3 R", "218 Laps", "LMGT3"),
                        new RaceResultEntry(13, "92", "Manthey 1st Phorm", "Porsche 911 GT3 R", "217 Laps", "LMGT3"),
                        new RaceResultEntry(14, "27", "Heart of Racing Team", "Vantage AMR GT3 Evo", "216 Laps", "LMGT3"),
                        new RaceResultEntry(15, "60", "Iron Lynx", "Mercedes-AMG GT3 Evo", "215 Laps", "LMGT3"),
                        new RaceResultEntry(16, "78", "Akkodis ASP Team", "Lexus RC F GT3", "214 Laps", "LMGT3"),
                        new RaceResultEntry(17, "77", "Proton Competition", "Ford Mustang GT3", "213 Laps", "LMGT3"),
                        new RaceResultEntry(18, "31", "The Bend Team WRT", "BMW M4 GT3 Evo", "212 Laps", "LMGT3"),
                        new RaceResultEntry(19, "21", "Vista AF Corse", "Ferrari 296 GT3", "211 Laps", "LMGT3"),
                        new RaceResultEntry(20, "54", "Vista AF Corse", "Ferrari 296 GT3", "210 Laps", "LMGT3"),
                        new RaceResultEntry(21, "33", "TF Sport", "Corvette Z06 GT3.R", "208 Laps", "LMGT3"),
                        new RaceResultEntry(22, "81", "TF Sport", "Corvette Z06 GT3.R", "205 Laps", "LMGT3"),
                        new RaceResultEntry(23, "95", "United Autosports", "McLaren 720S GT3 Evo", "200 Laps", "LMGT3"),
                        new RaceResultEntry(24, "61", "Iron Lynx", "Mercedes-AMG GT3 Evo", "195 Laps", "LMGT3"),
                        new RaceResultEntry(25, "87", "Akkodis ASP Team", "Lexus RC F GT3", "180 Laps", "LMGT3"),
                        new RaceResultEntry(26, "88", "Proton Competition", "Ford Mustang GT3", "160 Laps (DNF)", "LMGT3"),
                        new RaceResultEntry(27, "10", "Racing Spirit of Léman", "Vantage AMR GT3 Evo", "140 Laps (DNF)", "LMGT3")
                );
                break;
        }

        if (sessionType.equals("QUALIFYING")) {
            for (RaceResultEntry entry : results) {
                entry.setBestTimeOrLaps(String.format("1:%02d.%03d", (int) (Math.random() * 59), (int) (Math.random() * 999)));
            }
        }
        if (sessionType.equals("FREE_PRACTICE")) {
            for (RaceResultEntry entry : results) {
                entry.setBestTimeOrLaps(String.format("1:%03d.%03d", (int) (Math.random() * 120), (int) (Math.random() * 999)));
            }
        }

        return results;
    }

}
