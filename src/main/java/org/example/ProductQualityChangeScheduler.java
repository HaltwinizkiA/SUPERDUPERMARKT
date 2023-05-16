package org.example;


import org.example.service.ProductService;
import org.example.worker.FileWorker;


import java.util.Date;


public class ProductQualityChangeScheduler{
    private static final String FILENAME = "src/main/resources/logQualityChange.csv";
    private final FileWorker fileWorker = new FileWorker();
    private Thread thread;

    public void schedulerStart(ProductService service) {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                service.qualityChange();
                System.out.println("\n Qualität wurde geändert");
            }
        };

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {

                    Date date = new Date();
                    Date lastLogDate = fileWorker.getLastLog(FILENAME);
                    if (!fileWorker.getDateFormat().format(date).equals(fileWorker.getDateFormat().format(lastLogDate))) {
                        task.run();
                        fileWorker.log(FILENAME);
                    }
                    try {
                        Thread.sleep(86400000); // eine Tag in Milisekunden
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });


        thread.start();


    }

    public void schedulerStop() {
        thread.stop();
    }


}


