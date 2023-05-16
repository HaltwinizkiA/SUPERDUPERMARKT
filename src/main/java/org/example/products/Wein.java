package org.example.products;

import java.util.Date;

public class Wein extends Product {
    public Wein(long id, String name, double price, int quality, Date expirationDate,int dayCounter) {
        super(id, name, price, quality, expirationDate,dayCounter);
    }

    public Wein(String name, double price, int quality, Date expirationDate) {
        super(name, price, quality, expirationDate);
    }

    @Override
    public void qualityChange() {

        if (getQuality() < 50 && this.getDayCounter() == 10) {
           this.setQuality(getQuality() + 1);
            this.setDayCounter(0);
        } else this.setDayCounter(this.getDayCounter() + 1);

    }

    @Override
    public double getTäglichePreis() {
        return getPrice();
    }

    @Override
    public boolean zuEntsorgung() {
        return getQuality() > 0;
    }
}
