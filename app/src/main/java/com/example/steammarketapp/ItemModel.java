package com.example.steammarketapp;

public class ItemModel {

    private final int classid;
    private final int id;
    private DescriptionModel descriptionModel;

    public ItemModel(int classid, int id) {
        this.classid = classid;
        this.id = id;
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

    public int getId() {
        return id;
    }
}
