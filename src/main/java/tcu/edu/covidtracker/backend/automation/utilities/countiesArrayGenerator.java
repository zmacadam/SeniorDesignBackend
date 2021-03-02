package tcu.edu.covidtracker.backend.automation.utilities;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.junit.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class countiesArrayGenerator {

    @Test
    public void createArray() throws IOException, CsvValidationException {

        Reader reader = Files.newBufferedReader(Paths.get("C:\\Users\\Zach\\eclipse-workspace\\backend\\src\\main\\resources\\csv\\counties.csv"));
        CSVReader csvReader = new CSVReader(reader);
        File csv = new File("C:\\Users\\Zach\\eclipse-workspace\\backend\\src\\main\\resources\\csv\\counties.txt");
        FileWriter fw = new FileWriter(csv);
        BufferedWriter bw = new BufferedWriter(fw);
        String[] nextRecord;
        csvReader.readNext();
        bw.write("[");
        while ((nextRecord = csvReader.readNext()) != null) {
            String county = nextRecord[0];
            String state = nextRecord[1];
            bw.write("\"" + county + ", " + state + "\" , ");
        }
        bw.flush();
        bw.close();
    }

}
