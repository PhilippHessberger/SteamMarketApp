package com.example.steammarketapp;

public class InventoryFileModel {

    private String steamID, filename;

    public InventoryFileModel(String steamID, String filename) {
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
