package tcu.edu.covidtracker.backend.automation.utilities;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.util.JSON;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.stereotype.Component;
import tcu.edu.covidtracker.backend.model.*;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Component
public class MongoParser {

//    @PostConstruct
    public void dailyState() throws IOException, CsvValidationException {
        MongoClient mongoClient = new MongoClient("localhost", 27017);
        DB db = mongoClient.getDB("CovidTracker");
        String stateDir = "src/main/resources/csv/download/nytimes/state/";
        String countyDir = "src/main/resources/csv/download/nytimes/county/";
        String stateDir2 = "src/main/resources/csv/download/covidtracking/state/";
        LocalDate mostRecent = LocalDate.of(2021, 3, 7);
        LocalDate leastRecent = LocalDate.of(2020, 1, 21);
        long size = ChronoUnit.DAYS.between(leastRecent, mostRecent);
        for (int i = 0; i < size-1; i++) {
            LocalDate firstDay = LocalDate.of(2021, 3, 7).minusDays(i);
            LocalDate secondDay = firstDay.minusDays(1);
            Reader todayStateReader = Files.newBufferedReader(Paths.get(stateDir + "\\" + firstDay + ".csv"));
            Reader yesterdayStateReader = Files.newBufferedReader(Paths.get(stateDir + "\\" + secondDay + ".csv"));
            Reader todayStateReader2 = Files.newBufferedReader(Paths.get(stateDir2 + "\\" + firstDay + ".csv"));
            CSVReader todayStateCSVReader = new CSVReader(todayStateReader);
            CSVReader todayStateCSVReader2 = new CSVReader(todayStateReader2);
            CSVReader yesterdayStateCSVReader = new CSVReader(yesterdayStateReader);
            String[] todayStateNext;
            String[] todayStateNext2;
            String[] yesterdayStateNext;
            todayStateCSVReader.readNext();
            todayStateCSVReader2.readNext();
            yesterdayStateCSVReader.readNext();
            DBCollection usCollection;
            if (!db.collectionExists("United States")) {
                usCollection = db.createCollection("United States", null);
            } else {
                usCollection = db.getCollection("United States");
            }
            UnitedStates unitedStates = new UnitedStates();
            unitedStates.setDate(firstDay.toString());
            Statistics totalStats = new Statistics().initStatistics();
            StatesDate statesDate = new StatesDate();
            statesDate.setDate(firstDay.toString());
            while ((todayStateNext = todayStateCSVReader.readNext()) != null && (todayStateNext2 = todayStateCSVReader2.readNext()) != null && (yesterdayStateNext = yesterdayStateCSVReader.readNext()) != null) {

                StateDate stateDate = new StateDate();
                stateDate.setDate(firstDay.toString());

                Reader todayCountyReader = Files.newBufferedReader(Paths.get(countyDir + "\\" + firstDay + ".csv"));
                Reader yesterdayCountyReader = Files.newBufferedReader(Paths.get(countyDir + "\\" + secondDay + ".csv"));
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

                state.addStats(stats);

                String[] todayCountyNext;
                String[] yesterdayCountyNext;
                todayCountyCSVReader.readNext();
                yesterdayCountyCSVReader.readNext();

                DBCollection stateCollection;
                if (!db.collectionExists(tState)) {
                    stateCollection = db.createCollection(tState, null);
                } else {
                    stateCollection = db.getCollection(tState);
                }

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

                        county.addStats(countyStats);
                        state.addCounty(county);
                    }
                }
                stateDate.setState(state);
                Gson gson = new Gson();
                BasicDBObject obj = (BasicDBObject) JSON.parse(gson.toJson(stateDate));
                stateCollection.insert(obj);
                statesDate.addState(state);
            }
            DBCollection allStateCollection;
            if (!db.collectionExists("All States")) {
                allStateCollection = db.createCollection("All States", null);
            } else {
                allStateCollection = db.getCollection("All States");
            }
            Gson allStateGson = new Gson();
            BasicDBObject allStates = (BasicDBObject) JSON.parse(allStateGson.toJson(statesDate));
            allStateCollection.insert(allStates);
            unitedStates.addStats(totalStats);
            Gson gson = new Gson();
            BasicDBObject obj = (BasicDBObject) JSON.parse(gson.toJson(unitedStates));
            usCollection.insert(obj);
        }
    }

}
