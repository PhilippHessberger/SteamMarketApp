package com.example.steammarketapp;

public class ItemModel {

    private final int classid;
    private final int amount;
    private DescriptionModel descriptionModel;

    public ItemModel(int classid, int amount) {
        this.classid = classid;
        this.amount = amount;
    }

    public void setDescriptionModel(DescriptionModel descriptionModel) {
        this.descriptionModel = descriptionModel;
    }

    public DescriptionModel getDescriptionModel() {
        return descriptionModel;
    }

    public int getClassid() {
        return classid;
    }

    public int getAmount() {
        return amount;
    }
}
