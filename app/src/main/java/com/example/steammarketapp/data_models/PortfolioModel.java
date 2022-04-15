package com.example.steammarketapp.data_models;

import java.util.ArrayList;

public class PortfolioModel {

    private ArrayList<SnapshotModel> snapshots;

    private final String steamID, filename;

    public PortfolioModel(String steamID) {
        this.filename = "portfolio_" + steamID.replace("/", "-") + ".json";
        this.snapshots = new ArrayList<>();
        this.steamID = steamID;
    }

    public void addSnapshot(SnapshotModel snapshot) {
        snapshots.add(snapshot);
    }

    public String getFilename() {
        return filename;
    }

    public String getSteamID() {
        return steamID;
    }

    public ArrayList<SnapshotModel> getSnapshots() {
        return snapshots;
    }
}
