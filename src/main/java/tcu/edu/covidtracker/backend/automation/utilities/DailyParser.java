package tcu.edu.covidtracker.backend.automation.utilities;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import net.minidev.json.JSONObject;
import org.junit.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;

public class DailyParser {

    @Test
    public void dailyState() throws IOException, CsvValidationException {
        String stateDir = "C:\\Users\\Zach\\eclipse-workspace\\backend\\src\\main\\resources\\csv\\download\\state";
        int size = new File(stateDir).list().length;
        for (int i = 1; i < size - 1; i++) {
            LocalDate today = LocalDate.now().minusDays(i);
            LocalDate yesterday = today.minusDays(1);
            Reader todayReader = Files.newBufferedReader(Paths.get(stateDir + "\\" + today + ".csv"));
            Reader yesterdayReader = Files.newBufferedReader(Paths.get(stateDir + "\\" + yesterday + ".csv"));
            CSVReader todayCSVReader = new CSVReader(todayReader);
            CSVReader yesterdayCSVReader = new CSVReader(yesterdayReader);
            String[] todayNext;
            String[] yesterdayNext;
            todayCSVReader.readNext();
            yesterdayCSVReader.readNext();
            JSONObject usJSON = new JSONObject();
            JSONObject usHeader = new JSONObject();
            usHeader.put("cases", 0);
            usHeader.put("deaths", 0);
            usHeader.put("newCases", 0);
            usHeader.put("newDeaths", 0);
            while ((todayNext = todayCSVReader.readNext()) != null && (yesterdayNext = yesterdayCSVReader.readNext()) != null) {

                String tState = todayNext[0];
                String tFips = todayNext[1];
                String tCases = todayNext[2];
                String tDeaths = todayNext[3];

                String yState = yesterdayNext[0];
                String yFips = yesterdayNext[1];
                String yCases = yesterdayNext[2];
                String yDeaths = yesterdayNext[3];

                int nCases = Integer.parseInt(tCases) - Integer.parseInt(yCases);
                int nDeaths = Integer.parseInt(tDeaths) - Integer.parseInt(yDeaths);

                JSONObject stateJSON = new JSONObject();
                stateJSON.put("name", tState);
                stateJSON.put("fips", tFips);
                stateJSON.put("cases", tCases);
                stateJSON.put("deaths", tDeaths);
                stateJSON.put("newCases", nCases);
                stateJSON.put("newDeaths", nDeaths);

                //Write to file
                FileWriter fw = new FileWriter("C:\\Users\\Zach\\eclipse-workspace\\backend\\src\\main\\resources\\json\\us\\state\\" + tState + "\\" + today + ".json");
                fw.write(stateJSON.toJSONString());
                fw.flush();
                fw.close();

                stateJSON.remove("name");

                usJSON.put(tState, stateJSON);
                usHeader.put("cases", (int) usHeader.get("cases") + Integer.parseInt(tCases));
                usHeader.put("deaths", (int) usHeader.get("deaths") + Integer.parseInt(tDeaths));
                usHeader.put("newCases", (int) usHeader.get("newCases") + nCases);
                usHeader.put("newDeaths", (int) usHeader.get("newDeaths") + nDeaths);
            }
            usJSON.put("United States", usHeader);
            FileWriter fw = new FileWriter("C:\\Users\\Zach\\eclipse-workspace\\backend\\src\\main\\resources\\json\\us\\" + today + ".json");
            fw.write(usJSON.toJSONString());
            fw.flush();
            fw.close();
        }
    }

    @Test
    public void dailyCounty() {

    }
}
