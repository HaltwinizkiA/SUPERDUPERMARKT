package org.example.worker;


import liquibase.util.csv.CSVReader;
import liquibase.util.csv.CSVWriter;
import org.example.products.Käse;
import org.example.products.Product;
import org.example.products.Wein;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FileWorker {
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

                String art = data[0];
                int id = Integer.parseInt(data[1]);
                String name = data[2];
                double price = Double.parseDouble(data[3]);
                int quality = Integer.parseInt(data[4]);
                int dayCounter = Integer.parseInt(data[6]);

                Product product;

                if (art.equals("Käse")) {
                    Date expirationDate = dateFormat.parse(data[5]);
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
                String line = product.getClass().getSimpleName() + "," + product.getId() + "," + product.getName() + "," + product.getPrice() + "," + product.getQuality() + "," + dateFormat.format(product.getExpirationDate()) + "," + product.getDayCounter();

                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            Logger.log(e);;
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
            System.out.println();
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
