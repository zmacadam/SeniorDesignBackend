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
public class StatesDate {
    @Id
    private String date;
    private List<StateNoCounties> states = new ArrayList<>();

    public void addState(State state) {
        StateNoCounties stateNoCounties = new StateNoCounties(state.getName(), state.getStats());
        this.states.add(stateNoCounties);
    }

}
