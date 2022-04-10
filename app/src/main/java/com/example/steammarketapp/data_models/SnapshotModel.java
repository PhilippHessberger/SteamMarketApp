package com.example.steammarketapp.data_models;

import java.util.ArrayList;

public class SnapshotModel {

    private MetaDataModel metadata;
    private ArrayList<DescriptionModel> descriptions;

    public SnapshotModel(ArrayList<DescriptionModel> descriptions) {
        this.descriptions = descriptions;
    }

    public void setMetadata(MetaDataModel metadata) {
        this.metadata = metadata;
    }

    public ArrayList<DescriptionModel> getDescriptions() {
        return descriptions;
    }

    public MetaDataModel getMetadata() {
        return metadata;
    }
}
