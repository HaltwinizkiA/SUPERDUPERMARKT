package org.example.worker;

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
        List<Product> productsList=new ArrayList<>();
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


                Product product;

                if (art.equals("Käse")) {
                    Date expirationDate = dateFormat.parse(data[5]);
                    product = new Käse(id, name, price, quality, expirationDate);
                    productsList.add(product);
                }
                if (art.equals("Wein")) {
                    product = new Wein(id, name, price, quality, null);
                    productsList.add(product);
                }


            }
        } catch (IOException | ParseException e) {
            System.out.println("Produktdatei nicht gefunden");
            e.printStackTrace();
        }
        return productsList;
    }

    private void writeProductsInCSV(String fileName,List<Product> productsList) {

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
            // Schreibe die Kopfzeile der CSV-Datei
            bw.write("ART,ID,NAME,PRICE,QUALITY,EXPIRATION DATE(dd.MM.yyyy)");
            bw.newLine();

            for (Product product : productsList) {
                String line = product.getClass().getSimpleName() + "," + product.getId() + "," + product.getName() + "," + product.getPrice() + "," + product.getQuality() + "," + dateFormat.format(product.getExpirationDate());

                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public boolean log(String fileName) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
            // log
            bw.write(dateFormat.format(new Date()));
            bw.newLine();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }
    public Date getLastLog(String fileName){
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {

            }
            return  dateFormat.parse(line);

        } catch (ParseException | IOException ex) {
            System.out.println("Im Scheduler mit Logging sind Probleme aufgetreten "+ ex);
            return null;//todo
        }

    }
}
