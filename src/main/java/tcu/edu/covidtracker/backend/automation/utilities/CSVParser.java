package tcu.edu.covidtracker.backend.automation.utilities;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.junit.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class CSVParser {

    @Test
    public void parseState() {
        try {
            LinkedHashMap<String, String> baseline = stateBaseLine();
            Reader reader = Files.newBufferedReader(Paths.get("src/main/resources/csv/master/nytimes-state.csv"));
            CSVReader csvReader = new CSVReader(reader);
            LinkedHashMap<String, ArrayList> dates = new LinkedHashMap<>();
            String[] nextRecord;
            csvReader.readNext();
            while ((nextRecord = csvReader.readNext()) != null) {
                String date = nextRecord[0];
                String data = nextRecord[1] + "," + nextRecord[2] + "," + nextRecord[3] + "," + nextRecord[4];
                createMap(dates, date, data);
            }
            stateCumulative(dates, baseline);
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void parseCounty() {
        try {
            LinkedHashMap<String, String> baseline = countyBaseLine();
            Reader reader = Files.newBufferedReader(Paths.get("src/main/resources/csv/master/nytimes-county.csv"));
            CSVReader csvReader = new CSVReader(reader);
            LinkedHashMap<String, ArrayList> dates = new LinkedHashMap<>();
            String[] nextRecord;
            csvReader.readNext();
            while ((nextRecord = csvReader.readNext()) != null) {
                String date = nextRecord[0];
                String data = nextRecord[1] + "," + nextRecord[2] + "," + nextRecord[3] + "," + nextRecord[4] + "," + nextRecord[5];
                createMap(dates, date, data);
            }
            countyCumulative(dates, baseline);
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
    }

    private LinkedHashMap<String, String> countyBaseLine() throws IOException, CsvValidationException {
        Reader reader = Files.newBufferedReader(Paths.get("src/main/resources/csv/master/counties.csv"));
        CSVReader csvReader = new CSVReader(reader);
        LinkedHashMap<String, String> keys = new LinkedHashMap<>();
        String[] nextRecord;
        while ((nextRecord = csvReader.readNext()) != null) {
            String fips = nextRecord[2];
            String state = nextRecord[1];
            String county = nextRecord[0];
            String data = county + "," + state + "," + fips + ",0,0";
            if (!fips.equals("")) {
                if (Integer.parseInt(fips) < 54000) {
                    keys.put(fips, data);
                }
            }
        }
        return keys;
    }

    private LinkedHashMap<String, String> stateBaseLine() throws IOException, CsvValidationException {
        Reader reader = Files.newBufferedReader(Paths.get("src/main/resources/csv/master/states.csv"));
        CSVReader csvReader = new CSVReader(reader);
        LinkedHashMap<String, String> keys = new LinkedHashMap<>();
        String[] nextRecord;
        while ((nextRecord = csvReader.readNext()) != null) {
            String fips = nextRecord[1];
            String state = nextRecord[0];
            String data = state + "," + fips + ",0,0";
            if (!fips.equals("")) {
                if (Integer.parseInt(fips) < 57) {
                    keys.put(fips, data);
                }
            }
        }
        return keys;
    }

    public void createMap(LinkedHashMap<String, ArrayList> dates, String key, String data) {
        if (dates.containsKey(key)) {
            ArrayList<String> cur = dates.get(key);
            cur.add(data);
            dates.put(key, cur);
        } else {
            ArrayList<String> newData = new ArrayList<>();
            newData.add(data);
            dates.put(key, newData);
        }
    }

    public void countyCumulative(LinkedHashMap<String, ArrayList> dates, LinkedHashMap<String, String> baseline) {
        try {
            for (Map.Entry<String, ArrayList> entry : dates.entrySet()) {
                String key = entry.getKey();
                File csv = new File("src/main/resources/csv/download/nytimes/county/" + key + ".csv");
                FileWriter fw = new FileWriter(csv);
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write("county,state,fips,cases,deaths\n");
                ArrayList<String> value = entry.getValue();
                for (int i = 0; i < value.size(); i++) {
                    String cur = value.get(i);
                    String fips = cur.split(",")[2];
                    if (!fips.equals("")) {
                        if (Integer.parseInt(fips) < 54000) {
                            baseline.put(fips, cur);
                        }
                    }
                }
                for (Map.Entry<String, String> fips : baseline.entrySet()) {
                    String val = fips.getValue();
                    bw.write(val+"\n");
                }
                bw.flush();
                bw.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stateCumulative(LinkedHashMap<String, ArrayList> dates, LinkedHashMap<String, String> baseline) {
        try {
            for (Map.Entry<String, ArrayList> entry : dates.entrySet()) {
                String key = entry.getKey();
                File csv = new File("src/main/resources/csv/download/nytimes/state/" + key + ".csv");
                FileWriter fw = new FileWriter(csv);
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write("state,fips,cases,deaths\n");
                ArrayList<String> value = entry.getValue();
                for (int i = 0; i < value.size(); i++) {
                    String cur = value.get(i);
                    String fips = cur.split(",")[1];
                    if (!fips.equals("")) {
                        if (Integer.parseInt(fips) < 57) {
                            baseline.put(fips, cur);
                        }
                    }
                }
                for (Map.Entry<String, String> fips : baseline.entrySet()) {
                    String val = fips.getValue();
                    bw.write(val+"\n");
                }
                bw.flush();
                bw.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
