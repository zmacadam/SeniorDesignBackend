package tcu.edu.covidtracker.backend.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@Document
public class Statistics {

    @Id
    private String date;
    private int cases;
    private int deaths;
    private int hospitalized;
    private int newCases;
    private int newDeaths;

    public Statistics initStatistics() {
        this.cases = 0;
        this.deaths = 0;
        this.hospitalized = 0;
        this.newCases = 0;
        this.newDeaths = 0;
        return this;
    }
}
