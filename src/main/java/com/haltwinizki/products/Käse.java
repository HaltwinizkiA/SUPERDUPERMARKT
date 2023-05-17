package com.haltwinizki.products;

import java.util.Date;

public class Käse extends Product {
    private final int minQuality = 30;

    public Käse() {
    }

    public Käse(long id, String name, double price, int quality, Date expirationDate, int dayCounter) {
        super(id, name, price, quality, expirationDate, dayCounter);
    }

    public Käse(String name, double price, int quality, Date expirationDate) {
        super(name, price, quality, expirationDate);
    }

    @Override
    public Käse clone() throws CloneNotSupportedException {
        return (Käse) super.clone();
    }

    @Override
    public void changeQuality() {
        this.getQuality().decrementAndGet();
    }

    @Override
    public boolean isSpoiled() {
        return getQuality().get() < minQuality;
    }

    @Override
    public boolean isFresh() {
        return new Date().before(this.getExpirationDate()) && !this.isSpoiled();
    }
}
