package tcu.edu.covidtracker.backend.automation.utilities;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.junit.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class createBaseline {

    @Test
    public void createBaseline() {
        try {
            Reader reader = Files.newBufferedReader(Paths.get("C:\\Users\\Zach\\eclipse-workspace\\backend\\src\\main\\resources\\csv\\master.csv"));
            CSVReader csvReader = new CSVReader(reader);
            HashMap<String, ArrayList> counties = new HashMap<>();
            String[] nextRecord;
            csvReader.readNext();
            while ((nextRecord = csvReader.readNext()) != null) {
                String county = nextRecord[1];
                String state = nextRecord[2];
                String fips = nextRecord[3];
                ArrayList<String> data = new ArrayList<>();
                data.add(county);
                data.add(state);
                data.add(fips);
                counties.put(fips, data);
            }
            writeBaseline(counties);
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
    }

    public void writeBaseline(HashMap<String, ArrayList> counties) {
        try {
            File csv = new File("C:\\Users\\Zach\\eclipse-workspace\\backend\\src\\main\\resources\\csv\\counties.csv");
            FileWriter fw = new FileWriter(csv);
            BufferedWriter bw = new BufferedWriter(fw);
            for (Map.Entry<String, ArrayList> entry : counties.entrySet()) {
                String key = entry.getKey();
                ArrayList<String> value = entry.getValue();
                for (int i = 0; i < value.size(); i++) {
                    if (i == 0) {
                        bw.write(value.get(i));
                    } else {
                        bw.write("," + value.get(i));
                    }
                }
                bw.write("\n");
            }
            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
