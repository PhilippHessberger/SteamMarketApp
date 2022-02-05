package com.example.steammarketapp;

public class DescriptionModel {

    private int classid, volume;
    private String itemName;
    private boolean marketable, tradable;
    private float price, medianPrice;

    public DescriptionModel(int classid, String itemName, int marketable, int tradable) {
        this.classid = classid;
        this.itemName = itemName;
        this.marketable = (marketable == 1);
        this.tradable = (tradable == 1);
    }

    public int getClassid() {
        return classid;
    }

    public String getItemName() {
        return itemName;
    }

    public boolean isMarketable() {
        return marketable;
    }

    public boolean isTradable() {
        return tradable;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getMedianPrice() {
        return medianPrice;
    }

    public void setMedianPrice(float medianPrice) {
        this.medianPrice = medianPrice;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }
}
