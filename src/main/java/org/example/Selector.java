package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class Selector {

    private final FileReadWrite readWrite = new FileReadWrite();
    private final ObjectMapper jackson = new ObjectMapper();
    private final IdGenerator idGenerator = new IdGenerator();

    public void selectAll() {
        String distributionsJson = null;

        try {
            distributionsJson = readWrite.readFromFile();
        } catch (IOException e) {
            System.out.println("Error reading from file: " + e.getMessage());
            System.exit(1);
        }

        System.out.println(distributionsJson);
    }

    public void select(String arg0) {
        int id = 0;

        try {
            id = Integer.parseInt(arg0);
        } catch (NumberFormatException e) {
            System.out.println(e.getClass().getSimpleName() + ": " + e.getMessage());
            System.exit(1);
        }

        String distributionsJson = null;

        try {
            distributionsJson = readWrite.readFromFile();
        } catch (IOException e) {
            System.out.println("Error reading from file: " + e.getMessage());
            System.exit(1);
        }

        List<Distribution> distributions = null;

        try {
            distributions = jackson.readValue(distributionsJson, new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            System.out.println("Error mapping json to array: " + e.getMessage());
            System.exit(1);
        }

        int idToFind = id;

        Optional<Distribution> distribution = distributions.stream()
                .filter(d -> d.getId() == idToFind)
                .findFirst();

        if (distribution.isPresent()) {
            String distributionJson = null;

            try {
                distributionJson = jackson.writerWithDefaultPrettyPrinter().writeValueAsString(distribution.get());
            } catch (JsonProcessingException e) {
                System.out.println("Error mapping object to json: " + e.getMessage());
                System.exit(1);
            }

            System.out.println(distributionJson);
        } else {
            System.out.println("Distribution with id " + idToFind + " not found.");
        }
    }

    public void insert(String input) {
        String distributionsJson = null;

        try {
            distributionsJson = readWrite.readFromFile();
        } catch (IOException e) {
            System.out.println("Error reading from file: " + e.getMessage());
            System.exit(1);
        }

        List<Distribution> distributions = null;

        try {
            distributions = jackson.readValue(distributionsJson, new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            System.out.println("Error mapping json to array: " + e.getMessage());
            System.exit(1);
        }

        if (input.startsWith("{")) {
            Distribution newDistribution = null;

            try {
                newDistribution = jackson.readValue(input, Distribution.class);
            } catch (JsonProcessingException e) {
                System.out.println("Error mapping input json to object: " + e.getMessage());
                System.exit(1);
            }

            newDistribution.setId(idGenerator.getNewId(distributions));

            distributions.add(newDistribution);

            readWrite.writeToFile(distributions);

            System.out.println("Insert successful.");
        }
    }

    public void update(String input) {
        String distributionsJson = null;

        try {
            distributionsJson = readWrite.readFromFile();
        } catch (IOException e) {
            System.out.println("Error reading from file: " + e.getMessage());
            System.exit(1);
        }

        List<Distribution> distributions = null;

        try {
            distributions = jackson.readValue(distributionsJson, new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            System.out.println("Error mapping json to array: " + e.getMessage());
            System.exit(1);
        }

        if (input.startsWith("{")) {
            Distribution updated = null;

            try {
                updated = jackson.readValue(input, Distribution.class);
            } catch (JsonProcessingException e) {
                System.out.println("Error mapping input json to object: " + e.getMessage());
                System.exit(1);
            }

            int idToFind = updated.getId();

            Optional<Distribution> toUpdate = distributions.stream()
                    .filter(d -> d.getId() == idToFind)
                    .findFirst();

            if (toUpdate.isPresent()) {
                if (!updated.getName().equals(toUpdate.get().getName())) {
                    toUpdate.get().setName(updated.getName());

                    readWrite.writeToFile(distributions);

                    System.out.println("Update successful.");
                }
            } else {
                System.out.println("Distribution with id " + idToFind + " not found.");
            }
        }
    }

    public void delete(String arg0) {
        int id = 0;

        try {
            id = Integer.parseInt(arg0);
        } catch (NumberFormatException e) {
            System.out.println(e.getClass().getSimpleName() + ": " + e.getMessage());
            System.exit(1);
        }

        String distributionsJson = null;

        try {
            distributionsJson = readWrite.readFromFile();
        } catch (IOException e) {
            System.out.println("Error reading from file: " + e.getMessage());
            System.exit(1);
        }

        List<Distribution> distributions = null;

        try {
            distributions = jackson.readValue(distributionsJson, new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            System.out.println("Error mapping json to array: " + e.getMessage());
            System.exit(1);
        }

        boolean found = false;

        Iterator<Distribution> iterator = distributions.iterator();

        while (iterator.hasNext()) {
            Distribution current = iterator.next();

            if (current.getId() == id) {
                iterator.remove();
                found = true;
                break;
            }
        }

        if (found) {
            readWrite.writeToFile(distributions);
            System.out.println("Delete successful.");
        } else {
            System.out.println("Distribution with id " + id + " not found.");
        }
    }

}
