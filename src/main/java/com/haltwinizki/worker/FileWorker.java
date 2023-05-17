package com.haltwinizki.worker;


import com.haltwinizki.products.Product;
import liquibase.util.csv.CSVReader;
import liquibase.util.csv.CSVWriter;
import com.haltwinizki.products.Käse;
import com.haltwinizki.products.Wein;
import org.apache.log4j.Logger;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FileWorker {
    private static final Logger log = Logger.getLogger(FileWorker.class);
    private static final int ART_NUM = 0;
    private static final int ID_NUM = 1;
    private static final int NAME_NUM = 2;
    private static final int PRICE_NUM = 3;
    private static final int QUALITY_NUM = 4;
    private static final int DATE_NUM = 5;
    private static final int DAY_COUNTER_NUM = 6;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

    public SimpleDateFormat getDateFormat() {
        return dateFormat;
    }

    public List<Product> readProductsAusCSV(String fileName) throws IOException, ParseException {
        List<Product> productsList = new ArrayList<>();
        try (CSVReader csvReader = new CSVReader(new FileReader(fileName))) {

           List<String[]> rows=csvReader.readAll();



            for (int i=1;i<rows.size();i++){

                String art = rows.get(i)[ART_NUM];
                long id = Long.parseLong(rows.get(i)[ID_NUM]);
                String name = rows.get(i)[NAME_NUM];
                double price = Double.parseDouble(rows.get(i)[PRICE_NUM]);
                int quality = Integer.parseInt(rows.get(i)[QUALITY_NUM]);
                int dayCounter = Integer.parseInt(rows.get(i)[DAY_COUNTER_NUM]);

                Product product;

                if ("Käse".equals(art)) {
                    Date expirationDate = dateFormat.parse(rows.get(i)[DATE_NUM]);
                    product = new Käse(id, name, price, quality, expirationDate, dayCounter);
                    productsList.add(product);
                }
                if ("Wein".equals(art)) {
                    product = new Wein(id, name, price, quality, null, dayCounter);
                    productsList.add(product);
                }


            }
        }
        return productsList;
    }

    public void writeProductsInCSV(String fileName, List<Product> productsList) throws IOException {

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
            // Schreibe die Kopfzeile der CSV-Datei
            bw.write("ART,ID,NAME,PRICE,QUALITY,EXPIRATION DATE(dd.MM.yyyy)");
            bw.newLine();

            for (Product product : productsList) {
                String line = "";

                if ("Käse".equals(product.getClass().getSimpleName())) {
                    line = product.getClass().getSimpleName() + "," + product.getId() + "," + product.getName() + "," + product.getPrice() + "," + product.getQuality().get() + "," + dateFormat.format(product.getExpirationDate()) + "," + product.getDayCounter();
                }
                if ("Wein".equals(product.getClass().getSimpleName())) {
                    line = product.getClass().getSimpleName() + "," + product.getId() + "," + product.getName() + "," + product.getPrice() + "," + product.getQuality().get() + "," + null + "," + product.getDayCounter();
                }


                bw.write(line);
                bw.newLine();
            }
        }
    }

    public boolean qualityChangeLog(String fileName) {
        List<String[]> rows = getQualityChangeLogs(fileName);
        try (CSVWriter cw = new CSVWriter(new FileWriter(fileName))) {
            rows.add(new String[]{dateFormat.format(new Date())});
            cw.writeAll(rows);
            return true;
        } catch (IOException e) {
            log.info(e);;
            return false;
        }

    }

    public List<String[]> getQualityChangeLogs(String fileName) {
        try (CSVReader csvR = new CSVReader(new FileReader(fileName))) {
            return csvR.readAll();
        } catch (IOException ex) {
            log.info("mit Quality Logging sind Probleme aufgetreten " + ex);
            return null;//todo
        }

    }


    public Date getLastLog(String fileName) {
        try (CSVReader br = new CSVReader(new FileReader(fileName))) {
            List<String[]> rows = br.readAll();
            String lastLog = rows.get(rows.size() - 1)[0];
            return dateFormat.parse(lastLog);

        } catch (ParseException | IOException ex) {
            log.info("mit Quality Logging sind Probleme aufgetreten " + ex);
            return null;//todo
        }
    }

}
