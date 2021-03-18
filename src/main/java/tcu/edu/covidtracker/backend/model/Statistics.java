package tcu.edu.covidtracker.backend.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@Document
public class Statistics {

    private int cases;
    private int deaths;
    private int hospitalized;
    private int hospitalizedCur;
    private int hospitalizedCum;
    private int hospitalizedInc;
    private int newCases;
    private int newDeaths;

    public Statistics initStatistics() {
        this.cases = 0;
        this.deaths = 0;
        this.hospitalized = 0;
        this.hospitalizedCur = 0;
        this.hospitalizedCum = 0;
        this.hospitalizedInc = 0;
        this.newCases = 0;
        this.newDeaths = 0;
        return this;
    }
}
