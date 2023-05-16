package org.example.products;

import java.util.Date;

public class Käse extends Product {


    public Käse(long id, String name, double price, int quality, Date expirationDate) {
        super(id, name, price, quality, expirationDate);
    }
    public Käse( String name, double price, int quality, Date expirationDate) {
        super( name, price, quality, expirationDate);
    }

    @Override
    public void qualityChange() {

        setQuality(getQuality()-1);


    }

    @Override
    public double getTäglichePreis() {
        return getPrice() + 0.10 * getQuality();
    }

    @Override
    public boolean zuEntsorgung() {
        if (getQuality()<30){
            return false;
        }
        return true;
    }
}
