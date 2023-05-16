package org.example.products;

import java.util.Date;

public abstract class Product {

    private int dayCounter;// Ich habe diese Eigenschaft hinzugefügt, um die Qualitätsänderung zu verfolgen
    private long id;
    private double price;
    private String name;
    private int quality;
    private Date expirationDate;
    public Product(long id, String name, double price, int quality, Date expirationDate,int dayCounter) {
        this.id = id;
        this.price = price;
        this.name = name;
        this.quality = quality;
        this.expirationDate = expirationDate;
        this.dayCounter=dayCounter;
    }
    public Product(String name, double price, int quality, Date expirationDate) {
        this.price = price;
        this.name = name;
        this.quality = quality;
        this.expirationDate = expirationDate;
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

    public abstract double getTäglichePreis();

    public abstract boolean zuEntsorgung();
}
