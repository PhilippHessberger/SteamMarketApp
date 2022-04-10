package com.example.steammarketapp.data_models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class MetaDataModel {

    private BigDecimal snapshotValue = BigDecimal.ZERO;
    private final LocalDateTime dateOfEntry;

    public MetaDataModel(LocalDateTime currentDate) {
        this.dateOfEntry = currentDate;
    }

    public void addSnapshotValue(BigDecimal addedValue) {
        this.snapshotValue = (this.snapshotValue.add(addedValue));
    }

    public BigDecimal getSnapshotValue() {
        return snapshotValue;
    }

    public LocalDateTime getDateOfEntry() {
        return dateOfEntry;
    }

    public void setSnapshotValue(BigDecimal snapshotValue) {
        this.snapshotValue = snapshotValue;
    }
}
