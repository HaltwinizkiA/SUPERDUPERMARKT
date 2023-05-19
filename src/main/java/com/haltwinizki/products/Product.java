package com.haltwinizki.products;

import com.haltwinizki.annotation.CsvProperty;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class Product implements Cloneable {

    @CsvProperty(columnNumber = 4)
    private AtomicInteger quality;
    @CsvProperty(columnNumber = 1)
    private long id;
    @CsvProperty(columnNumber = 2)
    private String name;
    @CsvProperty(columnNumber = 3)
    private double price;
    @CsvProperty(columnNumber = 5)
    private Date expirationDate;
    @CsvProperty(columnNumber = 6)
    private AtomicInteger dayCounter;// Ich habe diese Eigenschaft hinzugefügt, um die Qualitätsänderung zu verfolgen

    public Product() {
        this.quality = new AtomicInteger();
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;

        Product product = (Product) o;

        if (getDayCounter().get() != product.getDayCounter().get()) return false;
        if (getId() != product.getId()) return false;
        if (Double.compare(product.getPrice(), getPrice()) != 0) return false;
        if (getQuality().get() != product.getQuality().get()) return false;
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

    public abstract boolean isFresh();

    public String toString(SimpleDateFormat dateFormat) {
        String condition = "";
        if (!this.isFresh()) {
            condition = "abgelaufen ";
        } else {
            condition = "gut";
        }
        String expirationDate;
        if (this.getExpirationDate() == null) {
            expirationDate = "";
        } else expirationDate = dateFormat.format(this.getExpirationDate());
        return String.format("%10.8s | %8s | %25.26s |%14s | %10.4s | %10s | %13s | %21s |\n", this.getClass().getSimpleName(), this.getId(), this.getName(), this.getPrice(), this.getDailyPrice(), this.getQuality(), expirationDate, condition);
    }

    @Override
    public Product clone() {
        try {
            return (Product) super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }
}
