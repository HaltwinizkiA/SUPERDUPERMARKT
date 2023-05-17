package com.haltwinizki.worker;


import com.haltwinizki.annotation.CsvProperty;
import com.haltwinizki.products.Product;
import com.haltwinizki.products.Whiskey;
import liquibase.util.csv.CSVReader;
import liquibase.util.csv.CSVWriter;
import com.haltwinizki.products.Käse;
import com.haltwinizki.products.Wein;
import org.apache.log4j.Logger;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class FileWorker {
    private static final Logger log = Logger.getLogger(FileWorker.class);
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
    private final String head = "ART,ID,NAME,PRICE,QUALITY,EXPIRATION DATE(dd.MM.yyyy),DAY COUNTER";

    private Class getTypeBySimpleName(String s) {
        switch (s) {
            case "Wein":
                return Wein.class;
            case "Käse":
                return Käse.class;
            case "Whiskey":
                return Whiskey.class;
            default:
                log.error("Invalid product type: " + s);
                return null;//todo throw castom exception
        }

    }

    public SimpleDateFormat getDateFormat() {
        return dateFormat;
    }

    public List<Product> readProductsAusCSVReflection(String fileName) throws Exception {
        List<Product> productList = new ArrayList<>();
        try (CSVReader csvReader = new CSVReader(new FileReader(fileName))) {
            List<String[]> rows = csvReader.readAll();
            for (int i = 1; i < rows.size(); i++) {
                if (rows.get(i).length < 7) {
                    continue;
                }
                Product product = (Product) getTypeBySimpleName(rows.get(i)[0]).newInstance();
                Field[] fields = product.getClass().getSuperclass().getDeclaredFields();
                setProductFields(product, fields, rows.get(i));
                productList.add(product);
            }
        }
        return productList;
    }

    private void setProductFields(Product product, Field[] fields, String[] row) throws ParseException, IllegalAccessException {
        for (Field field : fields) {
            if (!field.isAnnotationPresent(CsvProperty.class)) {
                continue;
            }
            CsvProperty annotation = field.getAnnotation(CsvProperty.class);
            int columnNumber = annotation.columnNumber();
            field.setAccessible(true);
            String value = row[columnNumber];
            Object fieldValue = parseFieldValue(field.getType(), value);
            field.set(product, fieldValue);
        }
    }

    private String[] createRow(Field[] fields, Product product) throws IllegalAccessException {
        String[] row = new String[fields.length + 1];
        for (Field field : fields) {
            field.setAccessible(true);
            row[0] = product.getClass().getSimpleName();
            if (field.isAnnotationPresent(CsvProperty.class)) {
                CsvProperty annotation = field.getAnnotation(CsvProperty.class);
                int columnNumber = annotation.columnNumber();
                row[columnNumber] = parseFieldValue(field.get(product));
            }
        }
        return row;
    }

    public void writeProductsInCSVReflection(String fileName, List<Product> productList) throws IllegalAccessException {
        if (productList.size() == 0) {
            return;
        }
        List<String[]> rows = new ArrayList<>();
        String[] heads = head.split(",");
        rows.add(heads);
        for (Product product : productList) {
            Field[] fields = product.getClass().getSuperclass().getDeclaredFields();
            String[] row = createRow(fields, product);
            rows.add(row);
        }
        try (CSVWriter cw = new CSVWriter(new FileWriter(fileName))) {
            cw.writeAll(rows);
        } catch (IOException e) {
            log.error(e);
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

    public boolean qualityChangeLog(String fileName) {
        List<String[]> rows = getQualityChangeLogs(fileName);
        try (CSVWriter cw = new CSVWriter(new FileWriter(fileName))) {
            rows.add(new String[]{dateFormat.format(new Date())});
            cw.writeAll(rows);
            return true;
        } catch (IOException e) {
            log.error(e);
            return false;
        }

    }

    public List<String[]> getQualityChangeLogs(String fileName) {
        try (CSVReader csvR = new CSVReader(new FileReader(fileName))) {
            return csvR.readAll();
        } catch (IOException ex) {
            log.error("mit Quality Logging sind Probleme aufgetreten " + ex);
            return null;//todo
        }

    }

    public Date getLastLog(String fileName) {
        try (CSVReader br = new CSVReader(new FileReader(fileName))) {
            List<String[]> rows = br.readAll();
            String lastLog = rows.get(rows.size() - 1)[0];
            return dateFormat.parse(lastLog);

        } catch (ParseException | IOException ex) {
            log.error("mit Quality Logging sind Probleme aufgetreten " + ex);
            return null;//todo
        }
    }

}
