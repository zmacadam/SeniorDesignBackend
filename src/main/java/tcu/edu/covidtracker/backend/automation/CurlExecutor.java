package tcu.edu.covidtracker.backend.automation;

import com.google.common.base.Charsets;
import com.google.common.io.ByteSource;
import org.springframework.stereotype.Component;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Component
public class CurlExecutor {

    public void executeCurl(String url, String fileName) throws IOException {
        String command = "curl " + url;
        ProcessBuilder processBuilder = new ProcessBuilder(command.split(" " ));
        Process process = processBuilder.start();

        InputStream inputStream = process.getInputStream();
        ByteSource byteSource = new ByteSource() {
            @Override
            public InputStream openStream() {
                return inputStream;
            }
        };

        String text = byteSource.asCharSource(Charsets.UTF_8).read();

//        Date today = Calendar.getInstance().getTime();
//        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        String fileName = dateFormat.format(today);
        File county = new File("src/main/resources/csv/master/" + fileName + ".csv");
        BufferedWriter writer = new BufferedWriter(new FileWriter(county));

        writer.write(text);
        writer.flush();
        writer.close();

        process.destroy();
    }

}
