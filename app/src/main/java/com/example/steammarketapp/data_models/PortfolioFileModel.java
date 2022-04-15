package com.example.steammarketapp.data_models;

public class PortfolioFileModel {

    private final String steamID, filename;

    public PortfolioFileModel(String filename) {
        this.filename = filename;
        // Restoring original format (exceptions are "/", which got replaced with "-" previously):
        this.steamID = filename.replace("portfolio_", "")
                .replace("-", "/")
                .replace(".json", "");
    }

    public String getFilename() {
        return filename;
    }

    public String getSteamID() {
        return steamID;
    }
}
