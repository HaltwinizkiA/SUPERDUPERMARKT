package com.haltwinizki;


import com.haltwinizki.products.Product;
import com.haltwinizki.products.Wein;
import com.haltwinizki.service.ProductService;
import com.haltwinizki.service.impl.ProductServiceImpl;

import java.text.SimpleDateFormat;
import java.util.Scanner;

public class Manager {

    private final ProductService productService = new ProductServiceImpl();
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
    private final Scanner scanner = new Scanner(System.in);
    private final MarktConsoleRenderer marktConsoleRenderer = new MarktConsoleRenderer(scanner);

    public void start() {

        ProductQualityChangeScheduler dailyProductQualityChangeScheduler = new ProductQualityChangeScheduler();
        dailyProductQualityChangeScheduler.schedulerStart(productService);
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
                    dailyProductQualityChangeScheduler.schedulerStop();
                    break;
                default:
                    System.out.println("Ungültige Option. Bitte wählen Sie erneut.");

            }
        }

    }

    private void discardedProducts() {
        System.out.println("ART, ID, NAME, NORMALE PRICE, PRICE, QUALITY");
        for (Product product : productService.getDiscardedProducts()) {

            System.out.println(product.getClass().getSimpleName() + ", " + product.getId() + ", " + product.getName() + ", " + product.getPrice() + ", " + product.getDayliPrice() + ", " + product.getQuality());

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
            if (productService.checkExpiration(product)) {
                ablaufDatum = "noch nicht abgelaufen";
            } else {
                ablaufDatum = "- abgelaufen - sollten entfernt werden ";
            }
            if (product.getClass() == Wein.class) {
                ablaufDatum = "";
            }

            String expirationDate;//todo think about it!
            if (product.getExpirationDate() == null) {
                expirationDate = "";
            } else expirationDate = dateFormat.format(product.getExpirationDate());

            System.out.println(product.getClass().getSimpleName() + ", " + product.getId() + ", " + product.getName() + ", " + product.getPrice() + ", " + product.getDayliPrice() + ", " + product.getQuality() + ", " + expirationDate + " " + ablaufDatum);

        }
    }
}
