import fr.bramsou.processing.eventbus.processor.EventGenerator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Test {

    public static void main(String[] args) {
        String generateId = UUID.randomUUID().toString().replace("-", "_");
        List<String> templateLines = new ArrayList<>();
        int bodyIndex = 0;
        try (InputStream stream = EventGenerator.class.getClassLoader().getResourceAsStream("registry-template.txt")) {
            if (stream == null) throw new IllegalArgumentException("Template file was not found !");
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
                String line = null;
                int count = 0;
                while ((line = reader.readLine()) != null) {
                    if (line.contains("%initialize_body%")) {
                        bodyIndex = count;
                       templateLines.add("");
                    } else {
                        templateLines.add(line.replace("%generated_id%", generateId));
                    }
                    count++;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        templateLines.set(bodyIndex, "Hi");

        for (String templateLine : templateLines) {
            System.out.println(templateLine);
        }
    }
}
