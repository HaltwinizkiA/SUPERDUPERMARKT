package org.example;



import org.example.worker.FileWorker;

import java.text.ParseException;


public class Main {
    public static void main(String[] args) throws ParseException {

        Manager manager =new Manager();
        manager.start();

    }
}