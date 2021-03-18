package tcu.edu.covidtracker.backend.model;


import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;

@Data
public class State {
    @Id
    private String id;
    private String name;
    private ArrayList<Statistics> stats = new ArrayList<>();
    private ArrayList<County> counties = new ArrayList<>();

    public void addCounty(County county) {
        this.counties.add(county);
    }

    public void addStats(Statistics statistics) {
        this.stats.add(statistics);
    }

    public State() {
        id = new ObjectId().toString();
    }

}
