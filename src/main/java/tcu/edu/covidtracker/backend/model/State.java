package tcu.edu.covidtracker.backend.model;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Document
public class State {
    @Id
    private String name;
    private List<Statistics> stats = new ArrayList<>();
    private List<County> counties = new ArrayList<>();

    public void addStats(Statistics statistics) {
        this.stats.add(statistics);
    }

    public void addCounty(County county) {
        this.counties.add(county);
    }
}
