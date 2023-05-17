package com.haltwinizki.products;

import com.haltwinizki.annotation.CsvProperty;

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class Product implements Cloneable {
    @CsvProperty(columnNumber = 6)
    private AtomicInteger dayCounter;// Ich habe diese Eigenschaft hinzugefügt, um die Qualitätsänderung zu verfolgen
    @CsvProperty(columnNumber = 1)
    private long id;
    @CsvProperty(columnNumber = 3)
    private double price;
    @CsvProperty(columnNumber = 2)
    private String name;
    @CsvProperty(columnNumber = 4)
    private AtomicInteger quality;
    @CsvProperty(columnNumber = 5)
    private Date expirationDate;

    public Product() {
    }

    public Product(long id, String name, double price, int quality, Date expirationDate, int dayCounter) {
        this.id = id;
        this.price = price;
        this.name = name;
        this.quality = new AtomicInteger(quality);
        this.expirationDate = expirationDate;
        this.dayCounter = new AtomicInteger(dayCounter);
    }

    public Product(String name, double price, int quality, Date expirationDate) {
        this.price = price;
        this.name = name;
        this.quality = new AtomicInteger(quality);
        this.expirationDate = expirationDate;
    }

    public static Product create(String type, long id, String name, double price, int quality, Date expirationDate, int dayCounter) {
        switch (type) {
            case "Wein":
                return new Wein(id, name, price, quality, expirationDate, dayCounter);
            case "Käse":
                return new Käse(id, name, price, quality, expirationDate, dayCounter);
            default:
                throw new IllegalArgumentException("Invalid product type: " + type);
        }
    }
    public static Product create(String type, String name, double price, int quality, Date expirationDate) {

        switch (type) {
            case "WEIN":
                return new Wein( name, price, quality, expirationDate);
            case "KÄSE":
                return new Käse( name, price, quality, expirationDate);
            default:
                throw new IllegalArgumentException("Invalid product type: " + type);
        }
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
        result = getDayCounter().get();
        result = 31 * result + (int) (getId() ^ (getId() >>> 32));
        temp = Double.doubleToLongBits(getPrice());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        result = 31 * result + getQuality().get();
        result = 31 * result + (getExpirationDate() != null ? getExpirationDate().hashCode() : 0);
        return result;
    }

    public AtomicInteger getDayCounter() {
        return dayCounter;
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

    public AtomicInteger getQuality() {
        return quality;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public abstract void changeQuality();

    public double getDailyPrice() {
        return getPrice() + 0.10 * getQuality().get();
    }

    public abstract boolean isSpoiled();

    public abstract boolean checkExpiration();

    @Override
    public Product clone() throws CloneNotSupportedException {
        return (Product) super.clone();
    }
}
