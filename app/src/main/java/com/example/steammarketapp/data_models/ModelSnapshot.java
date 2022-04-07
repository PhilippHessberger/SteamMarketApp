package com.example.steammarketapp.data_models;

import java.util.ArrayList;

public class ModelSnapshot {

    private ModelMetaData metadata;
    private ArrayList<ModelItem> items;

    public ModelSnapshot(ArrayList<ModelItem> items) {
        this.items = items;
    }

    public void setMetadata(ModelMetaData metadata) {
        this.metadata = metadata;
    }

    public ArrayList<ModelItem> getItems() {
        return items;
    }

    public ModelMetaData getMetadata() {
        return metadata;
    }
}
