package com.example.steammarketapp;

public class InventoryModel {

    private String steamID, filename;

    public InventoryModel(String steamID, String filename) {
        this.steamID = steamID;
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }

    public String getSteamID() {
        return steamID;
    }
}
