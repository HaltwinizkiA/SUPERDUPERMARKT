package com.haltwinizki.worker;


import com.haltwinizki.annotation.CsvProperty;
import com.haltwinizki.products.Product;
import liquibase.pro.packaged.F;
import liquibase.pro.packaged.T;
import liquibase.util.csv.CSVReader;
import liquibase.util.csv.CSVWriter;
import com.haltwinizki.products.Käse;
import com.haltwinizki.products.Wein;
import org.apache.log4j.Logger;

import java.io.*;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

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
    private final String head = "ART,ID,NAME,PRICE,QUALITY,EXPIRATION DATE(dd.MM.yyyy)";

    public static Class getTypeBySimpleName(String s) {
        switch (s) {
            case "Wein":
                return Wein.class;
            case "Käse":
                return Käse.class;
            default:
                log.info("Invalid product type: " + s);
                return null;
        }

    }

    public SimpleDateFormat getDateFormat() {
        return dateFormat;
    }

    public List<Product> readProductsAusCSV(String fileName) throws IOException, ParseException {
        //method , der fabrics method für erstellung benutzt
        List<Product> productsList = new ArrayList<>();
        try (CSVReader csvReader = new CSVReader(new FileReader(fileName))) {

            List<String[]> rows = csvReader.readAll();
            for (int i = 1; i < rows.size(); i++) {
                String art = rows.get(i)[ART_NUM];
                long id = Long.parseLong(rows.get(i)[ID_NUM]);
                String name = rows.get(i)[NAME_NUM];
                double price = Double.parseDouble(rows.get(i)[PRICE_NUM]);
                int quality = Integer.parseInt(rows.get(i)[QUALITY_NUM]);
                int dayCounter = Integer.parseInt(rows.get(i)[DAY_COUNTER_NUM]);
                Date date = null;
                if (!rows.get(i)[DATE_NUM].equals("null")) {
                    date = dateFormat.parse(rows.get(i)[DATE_NUM]);
                }
                Product product = Product.create(art, i, name, price, quality, date, dayCounter);
                productsList.add(product);
            }
        }
        return productsList;
    }

    public List<Product> readProductsAusCSVReflection(String fileName) throws Exception {
        List<Product> productList = new ArrayList<>();
        try (CSVReader csvReader = new CSVReader(new FileReader(fileName))) {

            List<String[]> rows = csvReader.readAll();
            for (int i = 1; i < rows.size(); i++) {
                Product product = (Product) getTypeBySimpleName(rows.get(i)[0]).newInstance();
                Field[] fields = product.getClass().getSuperclass().getDeclaredFields();
                for (Field field : fields) {
                    if (field.isAnnotationPresent(CsvProperty.class)) {
                        CsvProperty annotation = field.getAnnotation(CsvProperty.class);
                        int columnNumber = annotation.columnNumber();
                        field.setAccessible(true);

                        String value = rows.get(i)[columnNumber];
                        Object fieldValue = parseFieldValue(field.getType(), value);
                        field.set(product, fieldValue);
                    }

                }
                productList.add(product);
            }
        }
        return productList;
    }

    public void writeProductsInCSVReflection(String fileName, List<Product> productList) throws IllegalAccessException {
        List<String[]> rows = new ArrayList<>();
        String[] heads = head.split(",");
        rows.add(heads);
        for (Product product : productList) {
            Field[] fields = product.getClass().getSuperclass().getDeclaredFields();
            String[] row = new String[fields.length + 2];
            for (Field field : fields) {
                field.setAccessible(true);
                row[0] = product.getClass().getSimpleName();
                if (field.isAnnotationPresent(CsvProperty.class)) {
                    CsvProperty annotation = field.getAnnotation(CsvProperty.class);
                    int columnNumber = annotation.columnNumber();
                    row[columnNumber] = parseFieldValue(field.get(product));
                }
            }
            rows.add(row);
        }
        try (CSVWriter cw = new CSVWriter(new FileWriter(fileName))) {
            cw.writeAll(rows);
        } catch (IOException e) {
            log.info(e);
        }
    }

    private String parseFieldValue(Object object) {
        if (object != null && object.getClass() == Date.class) {
            return dateFormat.format(object);
        }
        return String.valueOf(object);
    }

    private Object parseFieldValue(Class<?> fieldType, String value) throws ParseException {
        if (value.equals("null")) {
            return null;
        } else if (fieldType == int.class || fieldType == Integer.class) {
            return Integer.parseInt(value);
        } else if (fieldType == long.class || fieldType == Long.class) {
            return Long.parseLong(value);
        } else if (fieldType == AtomicInteger.class || fieldType == AtomicInteger.class) {
            return new AtomicInteger(Integer.parseInt(value));
        } else if (fieldType == double.class || fieldType == Double.class) {
            return Double.parseDouble(value);
        } else if (fieldType == String.class) {
            return value;
        } else if (fieldType == Date.class) {
            return dateFormat.parse(value);
        } else {
            throw new IllegalArgumentException("Unsupported field type: " + fieldType.getName());
        }
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
            log.info(e);
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
