package com.example.steammarketapp;

import java.util.ArrayList;

public class InventoryModel {

    private ArrayList<InventoryEntryModel> inventoryHistory;

    public void addEntry(InventoryEntryModel inventoryModel) {
        inventoryHistory.add(inventoryModel);
    }
}
