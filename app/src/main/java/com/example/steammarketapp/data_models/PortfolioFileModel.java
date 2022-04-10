package com.example.steammarketapp.data_models;

public class PortfolioFileModel {

    private String steamID, filename;

    public PortfolioFileModel(String steamID, String filename) {
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
