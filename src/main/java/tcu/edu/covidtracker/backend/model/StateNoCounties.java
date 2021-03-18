package tcu.edu.covidtracker.backend.model;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;

@Data
public class StateNoCounties {
    @Id
    private String id;
    private String name;
    private ArrayList<Statistics> stats;

    public StateNoCounties(String name, ArrayList stats) {
        this.name = name;
        this.stats = stats;
        id = new ObjectId().toString();
    }

}
