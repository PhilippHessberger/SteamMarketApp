package com.example.steammarketapp.data_models;

import java.util.ArrayList;

public class SnapshotModel {

    private MetaDataModel metadata;
    private ArrayList<ItemModel> items;

    public SnapshotModel(ArrayList<ItemModel> items) {
        this.items = items;
    }

    public void setMetadata(MetaDataModel metadata) {
        this.metadata = metadata;
    }

    public ArrayList<ItemModel> getItems() {
        return items;
    }

    public MetaDataModel getMetadata() {
        return metadata;
    }
}
