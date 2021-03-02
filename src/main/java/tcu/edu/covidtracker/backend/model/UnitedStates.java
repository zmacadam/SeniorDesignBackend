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
public class UnitedStates {

    @Id
    private String date;
    private List<Statistics> stats = new ArrayList<>();
    public void addStats(Statistics statistics) {
        this.stats.add(statistics);
    }
}
