package com.haltwinizki;

import com.haltwinizki.worker.FileWorker;
import liquibase.pro.packaged.F;

public class Main {
    public static void main(String[] args) throws Exception {
        FileWorker worker=new FileWorker();
        worker.writeProductsInCSVReflection("src/main/resources/products.csv",worker.readProductsAusCSVReflection("src/main/resources/products.csv"));
        Manager manager = new Manager();
        manager.start();
    }
}