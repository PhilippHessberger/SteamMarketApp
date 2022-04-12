package com.example.steammarketapp.data_models;

import java.math.BigDecimal;
import java.math.BigInteger;

public class DescriptionModel implements Comparable<DescriptionModel> {

    private final BigInteger classid;
    private BigInteger volume;
    private BigInteger amount = new BigInteger(String.valueOf(0));
    private final String itemName;
    private final boolean marketable, tradable;
    // Prices are saved in cents because it is easier to calculate stuff that way.
    private BigDecimal lowestPrice, medianPrice;

    public DescriptionModel(BigInteger classid, String itemName, int marketable, int tradable) {
        this.classid = classid;
        this.itemName = itemName;
        this.marketable = (marketable == 1);
        this.tradable = (tradable == 1);
    }

    public DescriptionModel(BigInteger classid, String itemName, int marketable, int tradable, BigDecimal lowestPrice, BigDecimal medianPrice, BigInteger volume, BigInteger amount) {
        this.classid = classid;
        this.itemName = itemName;
        this.marketable = (marketable == 1);
        this.tradable = (tradable == 1);
        this.lowestPrice = lowestPrice;
        this.medianPrice = medianPrice;
        this.volume = volume;
        this.amount = amount;
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

    public BigInteger getAmount() {
        return amount;
    }

    public void setAmount(BigInteger amount) {
        this.amount = amount;
    }

    public void addOne() {
        this.amount = amount.add(BigInteger.valueOf(1));
    }

    @Override
    public int compareTo(DescriptionModel description) {
        BigDecimal val1 = this.lowestPrice.multiply(new BigDecimal(this.amount));
        BigDecimal val2 = description.lowestPrice.multiply(new BigDecimal(description.amount));

        return val2.compareTo(val1);
    }
}
