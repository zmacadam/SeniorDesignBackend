package tcu.edu.covidtracker.backend.automation.utilities;

import com.google.gson.Gson;
import com.mongodb.*;
import com.mongodb.util.JSON;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.stereotype.Component;
import tcu.edu.covidtracker.backend.model.*;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;

@Component
public class MongoParser {

    public void dailyState() throws IOException, CsvValidationException {
        MongoClientURI uri = new MongoClientURI(
                "mongodb://root:4x7NOpp0OFnpl1qD@cluster0-shard-00-00.i4jnr.mongodb.net:27017,cluster0-shard-00-01.i4jnr.mongodb.net:27017,cluster0-shard-00-02.i4jnr.mongodb.net:27017/CovidTracker?ssl=true&replicaSet=atlas-11ucwv-shard-0&authSource=admin&retryWrites=true&w=majority");
        MongoClient mongoClient = new MongoClient(uri);
        DB db = mongoClient.getDB("CovidTracker");
        String nyTimesState = "src/main/resources/csv/download/nytimes/state/";
        String nyTimesCounty = "src/main/resources/csv/download/nytimes/county/";
        String covidTrackingState = "src/main/resources/csv/download/covidtracking/state/";
        String vaccinationState = "src/main/resources/csv/download/vaccination/state/";
        boolean covidTrackingFlag;
        boolean vaccinationFlag;
        LocalDate firstDay = LocalDate.now().minusDays(1);
        LocalDate secondDay = firstDay.minusDays(1);
        File covidTrackerFile = new File(covidTrackingState + firstDay + ".csv");
        covidTrackingFlag = covidTrackerFile.exists();
        File vaccinationFile = new File(vaccinationState + firstDay + ".csv");
        vaccinationFlag = vaccinationFile.exists();
        Reader reader1 = Files.newBufferedReader(Paths.get(nyTimesState + firstDay + ".csv"));
        Reader reader2 = Files.newBufferedReader(Paths.get(nyTimesState + secondDay + ".csv"));
        CSVReader todayStateCSVReader = new CSVReader(reader1);
        CSVReader todayStateCSVReader2 = null;
        CSVReader todayStateCSVReader3 = null;
        CSVReader yesterdayStateCSVReader = new CSVReader(reader2);
        String[] todayStateData;
        String[] todayStateData2;
        String[] todayStateData3;
        String[] yesterdayStateData;
        todayStateCSVReader.readNext();
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
        if (covidTrackingFlag) {
            Reader reader3 = Files.newBufferedReader(Paths.get(covidTrackingState + firstDay + ".csv"));
            todayStateCSVReader2 = new CSVReader(reader3);
            todayStateCSVReader2.readNext();
        }
        if (vaccinationFlag) {
            Reader reader4 = Files.newBufferedReader(Paths.get(vaccinationState + firstDay + ".csv"));
            todayStateCSVReader3 = new CSVReader(reader4);
            todayStateCSVReader3.readNext();
        }
        while ((todayStateData = todayStateCSVReader.readNext()) != null) {
            yesterdayStateData = yesterdayStateCSVReader.readNext();

            StateDate stateDate = new StateDate();
            stateDate.setDate(firstDay.toString());

            Reader todayCountyReader = Files.newBufferedReader(Paths.get(nyTimesCounty + firstDay + ".csv"));
            Reader yesterdayCountyReader = Files.newBufferedReader(Paths.get(nyTimesCounty + secondDay + ".csv"));
            CSVReader todayCountyCSVReader = new CSVReader(todayCountyReader);
            CSVReader yesterdayCountyCSVReader = new CSVReader(yesterdayCountyReader);

            String tState = todayStateData[0];
            String tCases = todayStateData[2];
            String tDeaths = todayStateData[3];

            String yCases = yesterdayStateData[2];
            String yDeaths = yesterdayStateData[3];

            String stateFIPS = todayStateData[1];

            todayStateData2 = new String[]{tState, stateFIPS, "0", "0", "0", "0"};
            if (covidTrackingFlag) {
                todayStateData2 = todayStateCSVReader2.readNext();
            }

            todayStateData3 = new String[]{tState, stateFIPS, "0.0", "0.0", "0.0"};
            if (vaccinationFlag) {
                todayStateData3 = todayStateCSVReader3.readNext();
            }

            String vaccinations = todayStateData3[2].split("\\.")[0];
            String distributed = todayStateData3[3].split("\\.")[0];
            String vaccinated = todayStateData3[4].split("\\.")[0];

            vaccinations = vaccinations.equals("") ? "0" : vaccinations;
            distributed = distributed.equals("") ? "0" : distributed;
            vaccinated = vaccinated.equals("") ? "0" : vaccinated;

            String hospitalized = todayStateData2[2];
            String hospitalizedCum = todayStateData2[3];
            String hospitalizedCur = todayStateData2[4];
            String hospitalizedInc = todayStateData2[5];

            int nCases = Integer.parseInt(tCases) - Integer.parseInt(yCases);
            int nDeaths = Integer.parseInt(tDeaths) - Integer.parseInt(yDeaths);

            //TODO
            //Write reflection method for statistics
            totalStats.setHospitalized(totalStats.getHospitalized() + Integer.parseInt(hospitalized));
            totalStats.setHospitalizedCum(totalStats.getHospitalizedCum() + Integer.parseInt(hospitalizedCum));
            totalStats.setHospitalizedCur(totalStats.getHospitalizedCur() + Integer.parseInt(hospitalizedCur));
            totalStats.setHospitalizedInc(totalStats.getHospitalizedInc() + Integer.parseInt(hospitalizedInc));
            totalStats.setCases(totalStats.getCases() + Integer.parseInt(tCases));
            totalStats.setDeaths(totalStats.getDeaths() + Integer.parseInt(tDeaths));
            totalStats.setNewCases(totalStats.getNewCases() + nCases);
            totalStats.setNewDeaths(totalStats.getNewDeaths() + nDeaths);
            totalStats.setTotalVaccinations(totalStats.getTotalVaccinations() + Integer.parseInt(vaccinations));
            totalStats.setVaccinesDistributed(totalStats.getVaccinesDistributed() + Integer.parseInt(distributed));
            totalStats.setPeopleVaccinated(totalStats.getPeopleVaccinated() + Integer.parseInt(vaccinated));

            State state = new State();
            state.setName(tState);
            state.setFips(stateFIPS);

            Statistics stats = new Statistics().initStatistics();
            stats.setCases(Integer.parseInt(tCases));
            stats.setDeaths(Integer.parseInt(tDeaths));
            stats.setNewCases(nCases);
            stats.setNewDeaths(nDeaths);
            stats.setHospitalized(Integer.parseInt(hospitalized));
            stats.setHospitalizedCum(Integer.parseInt(hospitalizedCum));
            stats.setHospitalizedCur(Integer.parseInt(hospitalizedCur));
            stats.setHospitalizedInc(Integer.parseInt(hospitalizedInc));
            stats.setTotalVaccinations(Integer.parseInt(vaccinations));
            stats.setVaccinesDistributed(Integer.parseInt(distributed));
            stats.setPeopleVaccinated(Integer.parseInt(vaccinated));

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

                    String countyFIPS = todayCountyNext[2];

                    County county = new County();
                    county.setName(tCounty);
                    county.setFips(countyFIPS);

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
