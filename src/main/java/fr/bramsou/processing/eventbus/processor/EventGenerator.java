package fr.bramsou.processing.eventbus.processor;

import fr.bramsou.processing.eventbus.processor.file.FileType;
import fr.bramsou.processing.eventbus.processor.file.FileWriter;

import javax.annotation.processing.Filer;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;
import javax.tools.StandardLocation;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class EventGenerator extends BodyGenerator {

    public static final String GENERATED_PACKAGE = "fr.bramsou.processing.eventbus.generated";
    public static final String GENERATED_CLASS = "GeneratedRegistry_%s";

    private final String generateId;
    private final Filer filer;
    private final List<String> template = new ArrayList<>();
    private int bodyIndex = 0;
    private String bodySpacing = "";
    private boolean generated = false;

    public EventGenerator(Filer filer) {
        this.filer = filer;
        this.generateId = UUID.randomUUID().toString().replace("-", "_");
        this.write("META-INF/processing-event-bus.txt", FileType.RESOURCE, writer -> writer.println(this.generateId));
        this.readTemplate();
    }

    private void readTemplate() {
        String generatedClassName = String.format(GENERATED_CLASS, generateId);
        try (InputStream stream = EventGenerator.class.getClassLoader().getResourceAsStream("registry-template.txt")) {
            if (stream == null) throw new IllegalArgumentException("Template file was not found !");
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
                String line = null;
                int count = 0;
                while ((line = reader.readLine()) != null) {
                    if (line.contains("%initialize_body%")) {
                        this.bodyIndex = count;
                        long spaceCount = line.chars().filter(value -> value == ' ').count();
                        StringBuilder builder = new StringBuilder();
                        for (long i = 0; i < spaceCount; i++) {
                            builder.append(' ');
                        }
                        this.bodySpacing = builder.toString();
                        template.add("");
                    } else {
                        template.add(line
                                .replace("%generated_class%", generatedClassName)
                                .replace("%generated_package%", GENERATED_PACKAGE)
                        );
                    }
                    count++;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void generate(Set<? extends TypeElement> annotations, RoundEnvironment environment) {
        if (this.generated) return;
        List<String> bodyLines = new ArrayList<>();
        this.generateBody(annotations, environment, bodyLines);
        this.write(GENERATED_PACKAGE + "." + String.format(GENERATED_CLASS, this.generateId), FileType.JAVA_SOURCE, writer -> {
            int count = 0;
            for (String line : this.template) {
                if (count == this.bodyIndex) {
                    for (String body : bodyLines) {
                        writer.println(this.bodySpacing + body);
                    }
                } else {
                    writer.println(line);
                }
                count++;
            }
        });

        this.generated = true;
    }

    protected void write(String path, FileType type, FileWriter fileWriter) {
        try (PrintWriter writer = new PrintWriter((type == FileType.JAVA_SOURCE ? this.filer.createSourceFile(path) : this.filer.createResource(StandardLocation.CLASS_OUTPUT, "", path)).openWriter())) {
            fileWriter.accept(writer);
        } catch (IOException e) {
            throw new RuntimeException("Cannot write file at path: " + path, e);
        }
    }
}
