package tcu.edu.covidtracker.backend.automation.utilities;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tcu.edu.covidtracker.backend.model.County;
import tcu.edu.covidtracker.backend.model.State;
import tcu.edu.covidtracker.backend.model.Statistics;
import tcu.edu.covidtracker.backend.model.UnitedStates;
import tcu.edu.covidtracker.backend.repository.StateRepository;
import tcu.edu.covidtracker.backend.repository.USRepository;

import javax.annotation.PostConstruct;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;

@Component
public class MongoParser {

    @Autowired
    private StateRepository stateRepository;

    @Autowired
    private USRepository usRepository;

    @PostConstruct
    public void dailyState() throws IOException, CsvValidationException {
        String stateDir = "src/main/resources/csv/download/nytimes/state/";
        String countyDir = "src/main/resources/csv/download/nytimes/county/";
        String stateDir2 = "src/main/resources/csv/download/covidtracking/state/";
        int size = new File(stateDir).list().length;
        for (int i = 0; i < size-1; i++) {
            LocalDate today = LocalDate.now().minusDays(3 + i);
            LocalDate yesterday = today.minusDays(1);
            Reader todayStateReader = Files.newBufferedReader(Paths.get(stateDir + "\\" + today + ".csv"));
            Reader yesterdayStateReader = Files.newBufferedReader(Paths.get(stateDir + "\\" + yesterday + ".csv"));
            Reader todayStateReader2 = Files.newBufferedReader(Paths.get(stateDir2 + "\\" + today + ".csv"));
            CSVReader todayStateCSVReader = new CSVReader(todayStateReader);
            CSVReader todayStateCSVReader2 = new CSVReader(todayStateReader2);
            CSVReader yesterdayStateCSVReader = new CSVReader(yesterdayStateReader);
            String[] todayStateNext;
            String[] todayStateNext2;
            String[] yesterdayStateNext;
            todayStateCSVReader.readNext();
            todayStateCSVReader2.readNext();
            yesterdayStateCSVReader.readNext();
            UnitedStates unitedStates = new UnitedStates();
            unitedStates.setDate(today.toString());
            Statistics totalStats = new Statistics().initStatistics();
            totalStats.setDate(today.toString());
            while ((todayStateNext = todayStateCSVReader.readNext()) != null && (todayStateNext2 = todayStateCSVReader2.readNext()) != null && (yesterdayStateNext = yesterdayStateCSVReader.readNext()) != null) {

                Reader todayCountyReader = Files.newBufferedReader(Paths.get(countyDir + "\\" + today + ".csv"));
                Reader yesterdayCountyReader = Files.newBufferedReader(Paths.get(countyDir + "\\" + yesterday + ".csv"));
                CSVReader todayCountyCSVReader = new CSVReader(todayCountyReader);
                CSVReader yesterdayCountyCSVReader = new CSVReader(yesterdayCountyReader);

                String tState = todayStateNext[0];
                String tCases = todayStateNext[2];
                String tDeaths = todayStateNext[3];

                String yCases = yesterdayStateNext[2];
                String yDeaths = yesterdayStateNext[3];

                String hospitalized = todayStateNext2[2];
                String hospitalizedCum = todayStateNext2[3];
                String hospitalizedCur = todayStateNext2[4];
                String hospitalizedInc = todayStateNext2[5];


                int nCases = Integer.parseInt(tCases) - Integer.parseInt(yCases);
                int nDeaths = Integer.parseInt(tDeaths) - Integer.parseInt(yDeaths);

                totalStats.setHospitalized(Integer.parseInt(hospitalized));
                totalStats.setHospitalizedCum(Integer.parseInt(hospitalizedCum));
                totalStats.setHospitalizedCur(Integer.parseInt(hospitalizedCur));
                totalStats.setHospitalizedInc(Integer.parseInt(hospitalizedInc));
                totalStats.setCases(totalStats.getCases() + Integer.parseInt(tCases));
                totalStats.setDeaths(totalStats.getDeaths() + Integer.parseInt(tDeaths));
                totalStats.setNewCases(totalStats.getNewCases() + nCases);
                totalStats.setNewDeaths(totalStats.getNewDeaths() + nDeaths);

                if (i == 0) {
                    State state = new State();
                    state.setName(tState);

                    Statistics stats = new Statistics().initStatistics();
                    stats.setCases(Integer.parseInt(tCases));
                    stats.setDeaths(Integer.parseInt(tDeaths));
                    stats.setNewCases(nCases);
                    stats.setNewDeaths(nDeaths);
                    stats.setHospitalized(Integer.parseInt(hospitalized));
                    stats.setHospitalizedCum(Integer.parseInt(hospitalizedCum));
                    stats.setHospitalizedCur(Integer.parseInt(hospitalizedCur));
                    stats.setHospitalizedInc(Integer.parseInt(hospitalizedInc));
                    stats.setDate(today.toString());

                    state.addStats(stats);

                    String[] todayCountyNext;
                    String[] yesterdayCountyNext;
                    todayCountyCSVReader.readNext();
                    yesterdayCountyCSVReader.readNext();

                    while ((todayCountyNext = todayCountyCSVReader.readNext()) != null && (yesterdayCountyNext = yesterdayCountyCSVReader.readNext()) != null) {
                        String tState2 = todayCountyNext[1];
                        if (tState.equals(tState2)) {
                            String tCounty = todayCountyNext[0];
                            String tCases2 = todayCountyNext[3];
                            String tDeaths2 = todayCountyNext[4];

                            String yCases2 = yesterdayCountyNext[3];
                            String yDeaths2 = yesterdayCountyNext[4];

                            int nCases2 = Integer.parseInt(tCases2) - Integer.parseInt(yCases2);
                            int nDeaths2 = Integer.parseInt(tDeaths2) - Integer.parseInt(yDeaths2);

                            County county = new County();
                            county.setName(tCounty);

                            Statistics countyStats = new Statistics();
                            countyStats.setCases(Integer.parseInt(tCases2));
                            countyStats.setDeaths(Integer.parseInt(tDeaths2));
                            countyStats.setNewCases(nCases2);
                            countyStats.setNewDeaths(nDeaths2);
                            countyStats.setDate(today.toString());

                            county.addStats(countyStats);
                            state.addCounty(county);
                        }
                    }
                    stateRepository.save(state);
                }
            }
            if (i == 0) {
                unitedStates.addStats(totalStats);
                usRepository.save(unitedStates);
            }
        }
    }
}
