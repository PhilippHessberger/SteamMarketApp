package com.example.steammarketapp.data_models;

public class ModelInventoryFile {

    private String steamID, filename;

    public ModelInventoryFile(String steamID, String filename) {
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
