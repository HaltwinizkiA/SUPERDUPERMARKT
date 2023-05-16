package org.example;

import org.example.products.Product;
import org.example.service.ProductService;
import org.example.worker.FileWorker;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class DailyProductQualityChangeScheduler extends Thread {
    private final String fileName = "src/main/resources/products.csv";
    private FileWorker fileWorker=new FileWorker();
    public void schedulerStart(ProductService service) {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                service.qualityChange();
                System.out.println("Qualität wurde geändert");
            }
        };

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {

                    Date date=new Date();
                    Date lastLogDate=fileWorker.getLastLog(fileName);

                    if (fileWorker.getDateFormat().format(date).equals(fileWorker.getDateFormat().format(lastLogDate))){
                        task.run();
                        fileWorker.log(fileName);

                    }
                    try {
                        Thread.sleep(86400000); // eine Tag in Milisekunden
                    } catch (InterruptedException e) {

                    }
                }
            }
        });


        thread.start();
    }


}
