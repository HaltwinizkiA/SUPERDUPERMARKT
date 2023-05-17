package com.haltwinizki;

import com.haltwinizki.products.Product;
import com.haltwinizki.service.ProductService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class MarketConsoleRenderer {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");
    private final ProductService productService;
    private final Scanner scanner;

    public MarketConsoleRenderer(ProductService productService) {
        scanner = new Scanner(System.in);
        this.productService = productService;
    }

    public boolean render() {
        System.out.println("1. Produkt hinzufügen");
        System.out.println("2. Produkt aktualisieren");
        System.out.println("3. Produkt entfernen");
        System.out.println("4. Taglicheübersicht");
        System.out.println("5. ausrangierte Produkte");
        System.out.println("6. Beenden");
        System.out.print("Wählen Sie eine Option: ");
        int choice = scanner.nextInt();
        switch (choice) {
            case 1:
                addProduct();
                break;
            case 2:
                updateProduct();
                break;
            case 3:
                removeProduct();
                break;
            case 4:
                dailyOverview();
                break;
            case 5:
                discardedProducts();
                break;
            case 6:
                return false;
            default:
                System.out.println("Ungültige Option. Bitte wählen Sie erneut.");

        }
        return true;
    }

    private void discardedProducts() {
        printProducts(productService.getDiscardedProducts());
    }

    private void updateProduct() {
        System.out.println("Geben Sie bitte die Produkt-ID ein");
        long id = scanner.nextLong();
        Product product = productService.get(id);
        if (product == null) {
            System.out.println("Produkt mit dieser ID existiert nicht");
            return;
        }
        productService.update(updateProductMenu(product));
    }

    private void removeProduct() {
        System.out.println("Geben Sie bitte die Produkt-ID ein");
        long id = scanner.nextLong();
        if (productService.remove(id) == null) {
            System.out.println("Produkt mit dieser ID existiert nicht");
        }

    }

    private void addProduct() {
        productService.create(createProduct());
    }

    private void dailyOverview() {
        printProducts(productService.getAllProducts());
    }

    private Product createProduct() {

        System.out.println("Wählen Sie bitte Product Art aus ");
        while (true) {
            String art = scanner.next();
            art=art.toUpperCase();
            try {
                return Product.create(art, nameInput(), inputPrice(), qualityInput(), expirationDateInput());
            }catch (Exception e){
            System.out.println("Sie haben den falschen Art eingegeben");
        }

        }

    }

    private Product updateProductMenu(Product product) {
        boolean loop = true;
        while (loop) {
            System.out.println("1. Produktname");
            System.out.println("2. Produktpreis");
            System.out.println("3. Produktqualität");
            System.out.println("4. Verfallsdatum");
            System.out.println("5. Beenden");
            System.out.print("Wählen Sie Option aus ");
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    product.setName(nameInput());
                    break;
                case 2:
                    product.setPrice(inputPrice());
                    break;
                case 3:
                    product.getQuality().set((qualityInput()));
                    break;
                case 4:
                    product.setExpirationDate(expirationDateInput());
                    break;
                case 5:
                    loop = false;
                    break;
                default:
                    System.out.println("Ungültige Option. Bitte wählen Sie erneut.");

            }
        }
        return product;
    }

    private String nameInput() {//todo
        System.out.println("Geben Sie bitte name und drücken Sie ENTER");
        return scanner.next();
    }

    private double inputPrice() {//todo
        System.out.println("Geben Sie bitte price in format 12,99 und drücken Sie ENTER");
        while (true) {
            try {
                double price=scanner.nextDouble();
                if (price<0){
                    System.out.println("Preis muss mehr als 0 sein");
                    continue;
                }
                return price;
            } catch (InputMismatchException e) {
//                scanner.nextLine();
                System.out.println("Sie haben das Datenformat nicht befolgt " + "\n Bitte versuchen Sie es erneut");

            }
        }

    }

    private Date expirationDateInput() {//todo
        Date expirationDate;
        System.out.println("Geben Sie bitte Verfallsdatum in format (dd.MM.yyyy) und drücken Sie ENTER");
        while (true) {
            try {
                String line = scanner.nextLine();
                expirationDate = DATE_FORMAT.parse(line);
                break;
            } catch (ParseException e) {
                System.out.println("Sie haben das Datenformat nicht befolgt " + "\n Bitte versuchen Sie es erneut");
            }
        }
        return expirationDate;
    }

    private int qualityInput() {
        System.out.println("Geben Sie bitte qualität und drücken Sie ENTER");
        while (true) {
            try {
                return scanner.nextInt();
            } catch (InputMismatchException e) {
                scanner.nextLine();//todo
                System.out.println("Sie haben das Datenformat nicht befolgt " + "\n Bitte versuchen Sie es erneut");
            }
        }
    }

    private void printProducts(List<Product> productList) {
        System.out.format("%12s %10s %27s %15s %12s %12s %12s %23s\n", "ART |", "ID |", "NAME |", "NORMALE PRICE |", "PRICE |", "QUALITY |", "Verfallsdatum |", "ZUSTAND |");
        for (Product product : productList) {
            String condition = "";
            System.out.format("%12s %4$6s %2$25s %3$12s %1$12s %1$12s %5$12s %6$12s \n", "-----------|", "--------------------------|", "--------------|", "---------|", "--------------|", "----------------------|");
            if (!product.isFresh()) {
                condition = "abgelaufen ";
            } else {
                condition = "gut";
            }
            String expirationDate;
            if (product.getExpirationDate() == null) {
                expirationDate = "";
            } else expirationDate = DATE_FORMAT.format(product.getExpirationDate());
            System.out.format("%10.8s | %8s | %25.26s |%14s | %10.4s | %10s | %13s | %21s |\n", product.getClass().getSimpleName(), product.getId(), product.getName(), product.getPrice(), product.getDailyPrice(), product.getQuality(), expirationDate, condition);
        }

    }
}
