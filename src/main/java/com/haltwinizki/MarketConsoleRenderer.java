package com.haltwinizki;

import com.haltwinizki.factory.ProductFactory;
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

    public void render() {
        while (true) {
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
                    continue;
                case 2:
                    updateProduct();
                    continue;
                case 3:
                    removeProduct();
                    continue;
                case 4:
                    dailyOverview();
                    continue;
                case 5:
                    discardedProducts();
                    continue;
                case 6:
                    break;
                default:
                    System.out.println("Ungültige Option. Bitte wählen Sie erneut.");
            }
        }
    }

    private void discardedProducts() {
        printProducts(productService.getDiscardedProducts());
    }

    private void updateProduct() {
        System.out.println("Geben Sie bitte die Produkt-ID ein");
        long id = scanner.nextLong();
        Product product = productService.read(id);
        if (product == null) {
            System.out.println("Produkt mit dieser ID existiert nicht");
            return;
        }
        productService.update(updateProductMenu(product));
    }

    private void removeProduct() {
        System.out.println("Geben Sie bitte die Produkt-ID ein");
        long id = scanner.nextLong();
        if (productService.delete(id) == null) {
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
        System.out.println("Wählen Sie bitte Product ART aus ");
        while (true) {
            String art = scanner.next();
            art = art.toUpperCase();
            try {
                Product product = ProductFactory.createProduct(art);
                product.setName(inputName());
                product.setPrice(inputPrice());
                product.setExpirationDate(inputExpirationDate());
                product.getQuality().set(inputQuality());
                return product;
            } catch (IllegalArgumentException e) {
                System.out.println("Ungültiger ART, bitte versuchen Sie es erneut.");
            }
        }
    }

    private Product updateProductMenu(Product product) {
        while (true) {
            System.out.println("1. Produktname");
            System.out.println("2. Produktpreis");
            System.out.println("3. Produktqualität");
            System.out.println("4. Verfallsdatum");
            System.out.println("5. Beenden");
            System.out.print("Wählen Sie Option aus ");
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    product.setName(inputName());
                    break;
                case 2:
                    product.setPrice(inputPrice());
                    break;
                case 3:
                    product.getQuality().set((inputQuality()));
                    break;
                case 4:
                    product.setExpirationDate(inputExpirationDate());
                    break;
                case 5:
                    return product;
                default:
                    System.out.println("Ungültige Option. Bitte wählen Sie erneut.");
            }
        }
    }

    private String inputName() {
        System.out.println("Geben Sie bitte name und drücken Sie ENTER");
        return scanner.next();
    }

    private double inputPrice() {
        System.out.println("Geben Sie bitte price in format 12,99 und drücken Sie ENTER");
        while (true) {
            try {
                double price = scanner.nextDouble();
                if (price <= 0) {
                    System.out.println("Der Preis muss größer als 0 sein");
                    continue;
                }
                return price;
            } catch (InputMismatchException e) {
                scanner.nextLine();// clear buffer
                System.out.println("Sie haben das Datenformat nicht befolgt " + "\n Bitte versuchen Sie es erneut");
            }
        }
    }

    private Date inputExpirationDate() {
        System.out.println("Geben Sie bitte Verfallsdatum in format (dd.MM.yyyy) und drücken Sie ENTER");
        while (true) {
            try {
                return DATE_FORMAT.parse(scanner.nextLine());
            } catch (ParseException e) {
                scanner.nextLine();// clear buffer
                System.out.println("Sie haben das Datenformat nicht befolgt " + "\n Bitte versuchen Sie es erneut");
            }
        }
    }

    private int inputQuality() {
        System.out.println("Geben Sie bitte qualität und drücken Sie ENTER");
        while (true) {
            try {
                return scanner.nextInt();
            } catch (InputMismatchException e) {
                scanner.nextLine();// clear buffer
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
