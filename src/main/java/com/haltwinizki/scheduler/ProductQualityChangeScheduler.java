package com.haltwinizki.scheduler;

import com.haltwinizki.service.ProductService;
import com.haltwinizki.worker.FileWorker;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

public class ProductQualityChangeScheduler {
    private static final Logger log = Logger.getLogger(FileWorker.class);
    private static final String FILENAME = "src/main/resources/logQualityChange.csv";
    private static final int TIMEOUT = 86400000;// ein Tag in Milisekunden
    private final FileWorker fileWorker = new FileWorker();
    private Thread thread;

    public void schedulerStart(ProductService service) {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (service == null) {
                        log.error("Der Nulldienst wurde an den Scheduler übergeben");
                        break;
                    }
                    Date date = new Date();
                    Date lastLogDate = null;

                    try {
                        lastLogDate = fileWorker.getLastLog(FILENAME);
                    } catch (ParseException | IOException e) {
                        log.error(e);
                    }

                    if (!fileWorker.getDateFormat().format(date).equals(fileWorker.getDateFormat().format(lastLogDate))) {
                        service.changeQuality();
                        try {
                            fileWorker.changeQualityLog(FILENAME);
                        } catch (IOException e) {
                            log.error(e);
                        }
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


