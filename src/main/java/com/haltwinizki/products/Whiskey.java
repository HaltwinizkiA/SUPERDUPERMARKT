package com.haltwinizki.products;

import java.util.Date;

public class Whiskey extends Product {
    private final int maxQuality = 200;
    private final int qualityChangeRate = 30;

    public Whiskey() {
    }

    public Whiskey(long id, String name, double price, int quality, Date expirationDate, int dayCounter) {
        super(id, name, price, quality, expirationDate, dayCounter);
    }

    public Whiskey(String name, double price, int quality, Date expirationDate) {
        super(name, price, quality, expirationDate);
    }

    @Override
    public void changeQuality() {
        if (getQuality().get() < maxQuality && this.getDayCounter().incrementAndGet() >= qualityChangeRate) {
            this.getQuality().getAndIncrement();
            this.getDayCounter().set(0);
        }
    }

    public double getDailyPrice() {
        int multiplier = this.getQuality().get() / 25;
        if (multiplier > 0) {
            return getPrice() + 0.10 * (25 * multiplier);//-Preiserhöhungen alle 25 Tage -Grundpreis+ 0,10€ * Qualität/25
        }
        return getPrice();
    }

    @Override
    public boolean isSpoiled() {
        return false;
    }

    @Override
    public boolean isFresh() {
        return true;
    }
}
