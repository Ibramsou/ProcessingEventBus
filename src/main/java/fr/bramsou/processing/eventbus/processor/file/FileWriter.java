package fr.bramsou.processing.eventbus.processor.file;

import java.io.IOException;
import java.io.PrintWriter;

@FunctionalInterface
public interface FileWriter {

    void accept(PrintWriter writer) throws IOException;
}
