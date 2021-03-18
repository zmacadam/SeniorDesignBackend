package tcu.edu.covidtracker.backend.model;


import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;

@Data
@Document
public class County {
    @Id
    private String id;
    private String name;
    private ArrayList<Statistics> stats = new ArrayList<>();

    public void addStats(Statistics statistics) {
        this.stats.add(statistics);
    }

    public County() {
        id = new ObjectId().toString();
    }
}
