package org.example;

import org.example.products.Käse;
import org.example.products.Product;
import org.example.products.Wein;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class MarktConsoleRenderer {
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
    Scanner scanner;

    public MarktConsoleRenderer(Scanner scanner) {
        this.scanner = scanner;
    }

    public Product createProduct() {

        System.out.println("Wählen Sie bitte Product Art aus ");
        while (true) {
            String art = scanner.next();
            if (art.equalsIgnoreCase("wein")) {

                return new Wein(nameInput(), priceInput(), qualityInput(), null);
            }
            if (art.equalsIgnoreCase("käse")) {
                return new Käse(nameInput(), priceInput(), qualityInput(), expirationDateInput());

            } else System.out.println("Sie haben den falschen Art eingegeben");
        }

    }


    public Product updateProductMenu(Product product) {
        boolean loop = true;
        while (loop) {
            System.out.println("1. Produktname");
            System.out.println("2. Produktpreis");
            System.out.println("3. Produktqualität'");
            System.out.println("4. Verfallsdatum");
            System.out.println("5. Beenden");
            System.out.print("Wählen Sie aus, was Sie ändern möchten ");
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    product.setName(nameInput());
                    break;
                case 2:
                    product.setPrice(priceInput());
                    break;
                case 3:
                    product.setQuality(qualityInput());
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

    private String nameInput() {
        System.out.println("Geben Sie bitte name und drücken Sie ENTER");
        return scanner.next();
    }

    private double priceInput() {
        System.out.println("Geben Sie bitte price in format 12,99 und drücken Sie ENTER");
        while (true) {
            try {

                return scanner.nextDouble();


            } catch (InputMismatchException e) {
                scanner.nextLine();
                System.out.println("Sie haben das Datenformat nicht befolgt " + "\n Bitte versuchen Sie es erneut");

            }
        }
    }

    private Date expirationDateInput() {

        Date expirationDate;
        System.out.println("Geben Sie bitte Verfallsdatum in format (dd.MM.yyyy) und drücken Sie ENTER");
        while (true) {
            try {

                String line = scanner.nextLine();
                expirationDate = dateFormat.parse(line);

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
                scanner.nextLine();
                System.out.println("Sie haben das Datenformat nicht befolgt " + "\n Bitte versuchen Sie es erneut");

            }
        }

    }

}
