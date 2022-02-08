package com.example.steammarketapp;

import java.math.BigInteger;

public class ItemModel {

    private final BigInteger classid, id;
    private DescriptionModel descriptionModel;

    public ItemModel(BigInteger classid, BigInteger id) {
        this.classid = classid;
        this.id = id;
    }

    public void setDescriptionModel(DescriptionModel descriptionModel) {
        this.descriptionModel = descriptionModel;
    }

    public DescriptionModel getDescriptionModel() {
        return descriptionModel;
    }

    public BigInteger getClassid() {
        return classid;
    }

    public BigInteger getId() {
        return id;
    }
}
