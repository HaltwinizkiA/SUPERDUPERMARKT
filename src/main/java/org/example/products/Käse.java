package org.example.products;

import java.util.Date;

public class Käse extends Product {


    public Käse(long id, String name, double price, int quality, Date expirationDate,int dayCounter) {
        super(id, name, price, quality, expirationDate,dayCounter);
    }
    public Käse( String name, double price, int quality, Date expirationDate) {
        super( name, price, quality, expirationDate);
    }

    @Override
    public void qualityChange() {
        this.setQuality(getQuality()-1);
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
