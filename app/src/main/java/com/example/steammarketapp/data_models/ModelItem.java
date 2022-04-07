package com.example.steammarketapp.data_models;

import java.math.BigInteger;

public class ModelItem {

    private final BigInteger classid, id;
    private ModelDescription modelDescription;

    public ModelItem(BigInteger classid, BigInteger id) {
        this.classid = classid;
        this.id = id;
    }

    public void setDescriptionModel(ModelDescription modelDescription) {
        this.modelDescription = modelDescription;
    }

    public ModelDescription getDescriptionModel() {
        return modelDescription;
    }

    public BigInteger getClassid() {
        return classid;
    }

    public BigInteger getId() {
        return id;
    }
}
