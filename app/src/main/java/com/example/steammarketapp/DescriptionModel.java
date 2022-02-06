package com.example.steammarketapp;

public class DescriptionModel {

    private int classid, volume = 0;
    private String itemName;
    private boolean marketable, tradable;
    private float lowestPrice, medianPrice;

    public DescriptionModel(int classid, String itemName, int marketable, int tradable) {
        this.classid = classid;
        this.itemName = itemName;
        this.marketable = (marketable == 1);
        this.tradable = (tradable == 1);
    }

    public DescriptionModel(int classid, String itemName, int marketable, int tradable, float lowestPrice, float medianPrice, int volume) {
        this.classid = classid;
        this.itemName = itemName;
        this.marketable = (marketable == 1);
        this.tradable = (tradable == 1);
        this.lowestPrice = lowestPrice;
        this.medianPrice = medianPrice;
        this.volume = volume;
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

    public float getLowestPrice() {
        return lowestPrice;
    }

    public void setLowestPrice(float lowestPrice) {
        this.lowestPrice = lowestPrice;
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
