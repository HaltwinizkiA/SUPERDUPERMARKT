package com.haltwinizki.products;

import java.util.Date;

public class Käse extends Product {


    public Käse(long id, String name, double price, int quality, Date expirationDate, int dayCounter) {
        super(id, name, price, quality, expirationDate, dayCounter);
    }

    public Käse(String name, double price, int quality, Date expirationDate) {
        super(name, price, quality, expirationDate);
    }

    @Override
    public void qualityChange() {
        this.setQuality(getQuality() - 1);
    }

    @Override
    public double getDailyPrice() {
        return getPrice() + 0.10 * getQuality();
    }

    @Override
    public boolean isSpoiled() {
        return getQuality() < 30;
    }

    @Override
    public boolean checkExpiration() {
        return new Date().before(this.getExpirationDate()) && !this.isSpoiled();
    }
}
