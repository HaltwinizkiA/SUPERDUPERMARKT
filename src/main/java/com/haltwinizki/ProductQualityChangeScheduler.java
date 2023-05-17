package com.haltwinizki;


import com.haltwinizki.service.ProductService;
import com.haltwinizki.worker.FileWorker;
import org.apache.log4j.Logger;


import java.util.Date;


public class ProductQualityChangeScheduler {
    private static final Logger log = Logger.getLogger(FileWorker.class);
    private static final String FILENAME = "src/main/resources/logQualityChange.csv";
    private static final int TIMEOUT = 86400000;// ein Tag in Milisekunden
    private final FileWorker fileWorker = new FileWorker();
    private Thread thread;

    public void schedulerStart(ProductService service) {
        Runnable task = new Runnable() {//todo etwas andres für
            @Override
            public void run() {
                service.changeQuality();
                System.out.println("\n Qualität wurde geändert");
            }
        };

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    Date date = new Date();
                    Date lastLogDate = fileWorker.getLastLog(FILENAME);
                    if (!fileWorker.getDATE_FORMAT().format(date).equals(fileWorker.getDATE_FORMAT().format(lastLogDate))) {
                        task.run();
                        fileWorker.qualityChangeLog(FILENAME);
                    }
                    try {
                        Thread.sleep(TIMEOUT);
                    } catch (InterruptedException e) {
                        log.error(e);
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


