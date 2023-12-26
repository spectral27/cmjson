package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("No arguments provided");
            System.exit(1);
        }

        Selector selector = new Selector();

        switch (args[0]) {
            case "-s", "--select" -> {
                if (args.length == 1) {
                    selector.selectAll();
                } else {
                    selector.select(args[1]);
                }
            }
            case "-i", "--insert" -> {
                if (args.length > 1) {
                    String input = null;

                    try {
                        input = Files.readString(Path.of(args[1]));
                    } catch (IOException e) {
                        System.out.println(e.getClass().getSimpleName() + " " + e.getMessage());
                        System.exit(1);
                    }

                    selector.insert(input);
                } else {
                    System.out.println("Provide a path with json input file.");
                    System.exit(1);
                }
            }
            case "-u", "--update" -> {
                if (args.length > 1) {
                    String input = null;

                    try {
                        input = Files.readString(Path.of(args[1]));
                    } catch (IOException e) {
                        System.out.println(e.getClass().getSimpleName() + " " + e.getMessage());
                        System.exit(1);
                    }

                    selector.update(input);
                } else {
                    System.out.println("Provide a path with json input file.");
                    System.exit(1);
                }
            }
            case "-d", "--delete" -> {
                if (args.length > 1) {
                    selector.delete(args[1]);
                } else {
                    System.out.println("Distribution to delete id needed.");
                }
            }
            default -> {
                System.out.println("Unrecognized option: " + args[0]);
                System.exit(1);
            }
        }
    }

}
