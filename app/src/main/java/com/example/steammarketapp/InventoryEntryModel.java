package com.example.steammarketapp;

import java.util.ArrayList;

public class InventoryEntryModel {

    private InvMetaDataModel metadata;
    private ArrayList<ItemModel> items;

    public InventoryEntryModel(ArrayList<ItemModel> items) {
        this.items = items;
    }

    public void setMetadata(InvMetaDataModel metadata) {
        this.metadata = metadata;
    }

    public ArrayList<ItemModel> getItems() {
        return items;
    }

    public InvMetaDataModel getMetadata() {
        return metadata;
    }
}
