package com.haltwinizki.products;

import java.util.Date;

public class Wein extends Product {
    private final int maxQuality = 50;
    private final int qualityChangeRate = 10;

    public Wein() {
    }

    public Wein(long id, String name, double price, int quality, Date expirationDate, int dayCounter) {
        super(id, name, price, quality, expirationDate, dayCounter);
    }

    public Wein(String name, double price, int quality, Date expirationDate) {
        super(name, price, quality, expirationDate);
    }

    @Override
    public Wein clone() throws CloneNotSupportedException {
        return (Wein) super.clone();
    }

    @Override
    public void changeQuality() {
        if (getQuality().get() < maxQuality && this.getDayCounter().incrementAndGet() >= qualityChangeRate) {
            this.getQuality().getAndIncrement();
            this.getDayCounter().set(0);
        }

    }

    @Override
    public double getDailyPrice() {
        return getPrice();
    }

    @Override
    public boolean isSpoiled() {
        return false;
    }

    @Override
    public boolean checkExpiration() {
        return true;
    }
}
