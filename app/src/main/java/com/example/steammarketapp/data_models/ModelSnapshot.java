package com.example.steammarketapp.data_models;

import java.util.ArrayList;

public class ModelSnapshot {

    private ModelInvMetaData metadata;
    private ArrayList<ModelItem> items;

    public ModelSnapshot(ArrayList<ModelItem> items) {
        this.items = items;
    }

    public void setMetadata(ModelInvMetaData metadata) {
        this.metadata = metadata;
    }

    public ArrayList<ModelItem> getItems() {
        return items;
    }

    public ModelInvMetaData getMetadata() {
        return metadata;
    }
}
