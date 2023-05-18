package com.haltwinizki.factory;

import com.haltwinizki.products.Käse;
import com.haltwinizki.products.Product;
import com.haltwinizki.products.Wein;
import com.haltwinizki.products.Whiskey;

public class ProductFactory {
    public static Product createProduct(String productName){

        switch (productName) {
            case "WEIN":
                return new Wein();
            case "KÄSE":
                return new Käse();
            case "WHISKEY":
                return new Whiskey();
            default:
                throw new IllegalArgumentException();
        }



    }
}
