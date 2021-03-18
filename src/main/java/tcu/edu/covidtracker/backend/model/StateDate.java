package tcu.edu.covidtracker.backend.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@Document
public class StateDate {
    @Id
    private String date;
    private State state;

    public void setState(State state) {this.state = state; }

}
