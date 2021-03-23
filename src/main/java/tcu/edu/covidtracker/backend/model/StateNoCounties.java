package tcu.edu.covidtracker.backend.model;

import lombok.Data;
import org.bson.types.ObjectId;

import java.util.ArrayList;

@Data
public class StateNoCounties {
    private String id;
    private String name;
    private ArrayList<Statistics> stats;

    public StateNoCounties(String name, ArrayList stats) {
        this.name = name;
        this.stats = stats;
        id = new ObjectId().toString();
    }

}
