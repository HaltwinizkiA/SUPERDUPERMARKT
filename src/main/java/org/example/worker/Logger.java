package org.example.worker;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

public class Logger {
    private static final String FILENAME = "src/main/resources/logQualityChange.csv";

    public static void log(Exception ex) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILENAME))) {
            bw.write(new Date()+" - "+ex);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void log(String message) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILENAME))) {
            bw.write(new Date()+" - "+message);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
