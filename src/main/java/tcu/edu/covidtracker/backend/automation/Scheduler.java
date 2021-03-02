package tcu.edu.covidtracker.backend.automation;


import org.checkerframework.checker.units.qual.C;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;

@Configuration
@EnableScheduling
public class Scheduler {

//    Disabled for testing
//    @Autowired
//    private CurlExecutor curlExecutor;


    @Test
    public void cumulativeCountyTest() throws IOException {
//        Enabled for testing
        CurlExecutor curlExecutor = new CurlExecutor();
        String url = "https://raw.githubusercontent.com/nytimes/covid-19-data/master/us-counties.csv";
        curlExecutor.executeCurl(url, "nytimes-county");
    }


    @Test
    public void cumulativeStateTest() throws IOException {
        //        Enabled for testing
        CurlExecutor curlExecutor = new CurlExecutor();
        String url = "https://raw.githubusercontent.com/nytimes/covid-19-data/master/us-states.csv";
        curlExecutor.executeCurl(url, "nytimes-state");
    }

    @Test
    public void vaccineTest() throws IOException {
        //        Enabled for testing
        CurlExecutor curlExecutor = new CurlExecutor();
        String url = "https://raw.githubusercontent.com/owid/covid-19-data/master/public/data/vaccinations/us_state_vaccinations.csv";
        curlExecutor.executeCurl(url, "vaccine/");
    }

    @Test
    public void covidTrackingUS() throws IOException {
        CurlExecutor curlExecutor = new CurlExecutor();
        String url = "https://covidtracking.com/data/download/national-history.csv";
        curlExecutor.executeCurl(url, "covidtracking-us");
    }

    @Test
    public void covidTrackingState() throws IOException {
        CurlExecutor curlExecutor = new CurlExecutor();
        String url = "https://covidtracking.com/data/download/all-states-history.csv";
        curlExecutor.executeCurl(url, "covidtracking-state");
    }

}
