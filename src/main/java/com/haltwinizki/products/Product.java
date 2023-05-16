package com.haltwinizki.products;

import java.util.Date;

public abstract class Product {
    private int dayCounter;// Ich habe diese Eigenschaft hinzugefügt, um die Qualitätsänderung zu verfolgen
    private long id;
    private double price;
    private String name;
    private int quality;
    private Date expirationDate;

    public Product(long id, String name, double price, int quality, Date expirationDate, int dayCounter) {
        this.id = id;
        this.price = price;
        this.name = name;
        this.quality = quality;
        this.expirationDate = expirationDate;
        this.dayCounter = dayCounter;
    }

    public Product(String name, double price, int quality, Date expirationDate) {
        this.price = price;
        this.name = name;
        this.quality = quality;
        this.expirationDate = expirationDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;

        Product product = (Product) o;

        if (getDayCounter() != product.getDayCounter()) return false;
        if (getId() != product.getId()) return false;
        if (Double.compare(product.getPrice(), getPrice()) != 0) return false;
        if (getQuality() != product.getQuality()) return false;
        if (getName() != null ? !getName().equals(product.getName()) : product.getName() != null) return false;
        return getExpirationDate() != null ? getExpirationDate().equals(product.getExpirationDate()) : product.getExpirationDate() == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = getDayCounter();
        result = 31 * result + (int) (getId() ^ (getId() >>> 32));
        temp = Double.doubleToLongBits(getPrice());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        result = 31 * result + getQuality();
        result = 31 * result + (getExpirationDate() != null ? getExpirationDate().hashCode() : 0);
        return result;
    }

    public int getDayCounter() {
        return dayCounter;
    }

    public void setDayCounter(int dayCounter) {
        this.dayCounter = dayCounter;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuality() {
        return quality;
    }

    public void setQuality(int quality) {
        this.quality = quality;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public abstract void qualityChange();

    public abstract double getDailyPrice();

    public abstract boolean isSpoiled();

    public abstract boolean checkExpiration();
}
