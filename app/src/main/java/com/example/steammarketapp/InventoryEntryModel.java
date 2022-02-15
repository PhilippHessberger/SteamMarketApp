package com.example.steammarketapp;

import java.util.ArrayList;

public class InventoryEntryModel {

    private InvMetaDataModel metadata;
    private ArrayList<ItemModel> itemModelArrayList;
    private ArrayList<DescriptionModel> descriptionModelArrayList;

    public InventoryEntryModel() {

    }

    public void setMetadata(InvMetaDataModel metadata) {
        this.metadata = metadata;
    }

    public ArrayList<ItemModel> getItemModelArrayList() {
        return itemModelArrayList;
    }

    public InvMetaDataModel getMetadata() {
        return metadata;
    }
}
