package org.example;


import org.example.products.Product;
import org.example.products.Wein;
import org.example.service.ProductService;
import org.example.service.impl.ProductServiceImpl;

import java.text.SimpleDateFormat;
import java.util.Optional;
import java.util.Scanner;

import static java.util.Optional.ofNullable;

public class Manager {

    Scanner scanner = new Scanner(System.in);
    MarktConsoleRenderer marktConsoleRenderer = new MarktConsoleRenderer(scanner);
    private final ProductService productService = new ProductServiceImpl();

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");


    public void start() {

        boolean loop = true;
        while (loop) {
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
                    täglicheÜBersicht();
                    break;
                case 5:
                    discardedProducts();
                    break;
                case 6:
                    loop = false;
                    break;
                default:
                    System.out.println("Ungültige Option. Bitte wählen Sie erneut.");
            }
        }

    }

    private void discardedProducts() {
        System.out.println("ART, ID, NAME, NORMALE PRICE, PRICE, QUALITY");
        for (Product product : productService.getDiscardedProducts()) {

            System.out.println(product.getClass().getSimpleName() + ", " + product.getId() + ", " +
                    product.getName() + ", " + product.getPrice() + ", " + product.getTäglichePreis() + ", "
                    + product.getQuality());

        }
    }

    private void updateProduct() {
        System.out.println("Geben Sie bitte die Produkt-ID ein");
        long id = scanner.nextLong();
        Product product = productService.get(id);
        if (product == null) {
            System.out.println("Produkt mit dieser ID existiert nicht");
            return;
        }
        productService.update(marktConsoleRenderer.updateProductMenu(product));
    }

    private void removeProduct() {
        System.out.println("Geben Sie bitte die Produkt-ID ein");
        long id = scanner.nextLong();
        if (productService.remove(id) == null) {
            System.out.println("Produkt mit dieser ID existiert nicht");
        }

    }

    private void addProduct() {
        productService.create(marktConsoleRenderer.createProduct());
    }

    private void täglicheÜBersicht() {
        System.out.println("ART, ID, NAME, NORMALE PRICE, PRICE, QUALITY, Verfallsdatum, ZUSTAND");
        for (Product product : productService.getAllProducts()) {
            String ablaufDatum = "";
            if (productService.checkExpirationDate(product)) {
                ablaufDatum = "noch nicht abgelaufen";
            }if (product.getClass()== Wein.class){
                ablaufDatum="";
            }
            else {
                ablaufDatum = "- abgelaufen - sollten entfernt werden ";
            }
            String expirationDate;//todo think abou it!
            if (product.getExpirationDate()==null){
                expirationDate="";
            }else expirationDate=dateFormat.format(product.getExpirationDate());

            System.out.println(product.getClass().getSimpleName() + ", " + product.getId() + ", " +
                     product.getName() + ", " + product.getPrice() + ", "
                    + product.getTäglichePreis() + ", " + product.getQuality() + ", "
                    + expirationDate + " " + ablaufDatum);

        }
    }
}
