package com.example.steammarketapp;

import java.math.BigDecimal;
import java.math.BigInteger;

public class DescriptionModel {

    private BigInteger classid, volume;
    private String itemName;
    private boolean marketable, tradable;
    // Prices are saved in cents because it is easier to calculate stuff that way.
    private BigDecimal lowestPrice, medianPrice;

    public DescriptionModel(BigInteger classid, String itemName, int marketable, int tradable) {
        this.classid = classid;
        this.itemName = itemName;
        this.marketable = (marketable == 1);
        this.tradable = (tradable == 1);
    }

    public DescriptionModel(BigInteger classid, String itemName, int marketable, int tradable, BigDecimal lowestPrice, BigDecimal medianPrice, BigInteger volume) {
        this.classid = classid;
        this.itemName = itemName;
        this.marketable = (marketable == 1);
        this.tradable = (tradable == 1);
        this.lowestPrice = lowestPrice;
        this.medianPrice = medianPrice;
        this.volume = volume;
    }

    public BigInteger getClassid() {
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

    public BigDecimal getLowestPrice() {
        return lowestPrice;
    }

    public void setLowestPrice(BigDecimal lowestPrice) {
        this.lowestPrice = lowestPrice;
    }

    public BigDecimal getMedianPrice() {
        return medianPrice;
    }

    public void setMedianPrice(BigDecimal medianPrice) {
        this.medianPrice = medianPrice;
    }

    public BigInteger getVolume() {
        return volume;
    }

    public void setVolume(BigInteger volume) {
        this.volume = volume;
    }
}
