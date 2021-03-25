package tcu.edu.covidtracker.backend.automation;


import com.opencsv.exceptions.CsvValidationException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import tcu.edu.covidtracker.backend.automation.utilities.CSVParser;
import tcu.edu.covidtracker.backend.automation.utilities.MongoParser;

import java.io.IOException;

@Configuration
@EnableScheduling
public class Scheduler {

    @Autowired
    private CSVParser csvParser;

    @Autowired
    private MongoParser mongoParser;

    @Scheduled(cron = "0 0 2 * * *", zone = "CST")
    public void downloadFiles() throws IOException, CsvValidationException {
        cumulativeCountyTest();
        cumulativeStateTest();
        vaccineTest();
        csvParser.nytimesCounty();
        csvParser.nytimesState();
        csvParser.vaccinationState();
        mongoParser.dailyState();
    }

    @Test
    public void cumulativeCountyTest() throws IOException {
        CurlExecutor curlExecutor = new CurlExecutor();
        String url = "https://raw.githubusercontent.com/nytimes/covid-19-data/master/us-counties.csv";
        curlExecutor.executeCurl(url, "nytimes-county");
    }

    @Test
    public void cumulativeStateTest() throws IOException {
        CurlExecutor curlExecutor = new CurlExecutor();
        String url = "https://raw.githubusercontent.com/nytimes/covid-19-data/master/us-states.csv";
        curlExecutor.executeCurl(url, "nytimes-state");
    }

    @Test
    public void vaccineTest() throws IOException {
        CurlExecutor curlExecutor = new CurlExecutor();
        String url = "https://raw.githubusercontent.com/owid/covid-19-data/master/public/data/vaccinations/us_state_vaccinations.csv";
        curlExecutor.executeCurl(url, "vaccination-data");
    }

}
