package com.example.steammarketapp.data_models;

import java.util.ArrayList;

public class ModelInventory {

    private ArrayList<ModelSnapshot> inventoryHistory;

    public void addEntry(ModelSnapshot inventoryModel) {
        inventoryHistory.add(inventoryModel);
    }
}
