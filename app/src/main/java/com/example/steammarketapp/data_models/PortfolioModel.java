package com.example.steammarketapp.data_models;

import java.util.ArrayList;

public class PortfolioModel {

    private ArrayList<SnapshotModel> inventoryHistory;

    public void addEntry(SnapshotModel inventoryModel) {
        inventoryHistory.add(inventoryModel);
    }
}
