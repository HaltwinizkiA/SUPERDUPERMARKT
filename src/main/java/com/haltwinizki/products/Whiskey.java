package com.haltwinizki.products;

public class Whiskey extends Product{
    private final int maxQuality = 200;
    private final int qualityChangeRate = 30;
    @Override
    public void changeQuality() {
        if (getQuality().get() < maxQuality && this.getDayCounter().incrementAndGet() >= qualityChangeRate) {
            this.getQuality().getAndIncrement();
            this.getDayCounter().set(0);
        }
    }

    public double getDailyPrice() {
        int multiplier=this.getQuality().get()/25;
        if (multiplier>0){

            return getPrice()+0.10*(25*multiplier);
        }
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
