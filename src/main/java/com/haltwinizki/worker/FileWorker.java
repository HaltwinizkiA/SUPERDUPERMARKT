package com.haltwinizki.worker;


import com.haltwinizki.products.Product;
import liquibase.util.csv.CSVReader;
import liquibase.util.csv.CSVWriter;
import com.haltwinizki.products.Käse;
import com.haltwinizki.products.Wein;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FileWorker {
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

    public List<Product> readProductsAusCSV(String fileName) {
        List<Product> productsList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            boolean headerSkipped = false;

            while ((line = br.readLine()) != null) {
                if (!headerSkipped) {
                    // Überspringe die Kopfzeile
                    headerSkipped = true;
                    continue;
                }

                String[] data = line.split(",");

                String art = data[ART_NUM];
                int id = Integer.parseInt(data[ID_NUM]);
                String name = data[NAME_NUM];
                double price = Double.parseDouble(data[PRICE_NUM]);
                int quality = Integer.parseInt(data[QUALITY_NUM]);
                int dayCounter = Integer.parseInt(data[DAY_COUNTER_NUM]);

                Product product;

                if (art.equals("Käse")) {
                    Date expirationDate = dateFormat.parse(data[DATE_NUM]);
                    product = new Käse(id, name, price, quality, expirationDate, dayCounter);
                    productsList.add(product);
                }
                if (art.equals("Wein")) {
                    product = new Wein(id, name, price, quality, null, dayCounter);
                    productsList.add(product);
                }


            }
        } catch (IOException | ParseException e) {
            System.out.println("Produktdatei nicht gefunden");
            Logger.log(e);
        }
        return productsList;
    }

    public void writeProductsInCSV(String fileName, List<Product> productsList) {

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
            // Schreibe die Kopfzeile der CSV-Datei
            bw.write("ART,ID,NAME,PRICE,QUALITY,EXPIRATION DATE(dd.MM.yyyy)");
            bw.newLine();

            for (Product product : productsList) {
                String line="";

                if (product.getClass().getSimpleName().equals("Käse")) {
                    line = product.getClass().getSimpleName() + "," + product.getId() + "," + product.getName() + ","
                            + product.getPrice() + "," + product.getQuality() + "," + dateFormat.format(product.getExpirationDate()) + "," + product.getDayCounter();
                }if (product.getClass().getSimpleName().equals("Wein")) {
                    line = product.getClass().getSimpleName() + "," + product.getId() + "," + product.getName() + ","
                            + product.getPrice() + "," + product.getQuality() + "," + null + "," + product.getDayCounter();
                }


                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            Logger.log(e);
        }
    }

    public boolean qualityChangeLog(String fileName) {
        List<String[]> rows = getQualityChangeLogs(fileName);
        try (CSVWriter cw = new CSVWriter(new FileWriter(fileName))) {
            rows.add(new String[]{dateFormat.format(new Date())});
            cw.writeAll(rows);
            return true;
        } catch (IOException e) {
            Logger.log(e);
            return false;
        }

    }

    public List<String[]> getQualityChangeLogs(String fileName) {
        try (CSVReader csvR = new CSVReader(new FileReader(fileName))) {
            return csvR.readAll();
        } catch (IOException ex) {
            Logger.log("mit Quality Logging sind Probleme aufgetreten " + ex);
            return null;//todo
        }

    }


    public Date getLastLog(String fileName) {
        try (CSVReader br = new CSVReader(new FileReader(fileName))) {
            List<String[]> rows = br.readAll();
            String lastLog = rows.get(rows.size() - 1)[0];
            return dateFormat.parse(lastLog);

        } catch (ParseException | IOException ex) {
            Logger.log("mit Quality Logging sind Probleme aufgetreten " + ex);
            return null;//todo
        }
    }

}
