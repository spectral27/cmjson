package org.example;

import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class FileReadWrite {

    private static final Path PATH = Path.of("distributions.json");

    public String readFromFile() throws IOException {
        return Files.readString(PATH);
    }

    public void writeToFile(List<Distribution> distributions) {
        DefaultPrettyPrinter.Indenter indenter = new DefaultIndenter("  ", "\n");

        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        printer.indentArraysWith(indenter);
        printer.indentObjectsWith(indenter);

        ObjectMapper jackson = new ObjectMapper();

        String distributionsJson = null;

        try {
            distributionsJson = jackson.writer(printer).writeValueAsString(distributions);
        } catch (IOException e) {
            System.out.println("Error mapping list to json: " + e.getMessage());
            System.exit(1);
        }

        try {
            Files.writeString(PATH, distributionsJson + "\n");
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
            System.exit(1);
        }
    }

}
