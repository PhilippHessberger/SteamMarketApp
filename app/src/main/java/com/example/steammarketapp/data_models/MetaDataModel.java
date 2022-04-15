package com.example.steammarketapp.data_models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class MetaDataModel {

    private BigDecimal snapshotValue = BigDecimal.ZERO;
    private final LocalDateTime dateOfEntry;
    private final int index;

    public MetaDataModel(LocalDateTime currentDate, int index) {
        this.dateOfEntry = currentDate;
        this.index = index;
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

    public int getSnapshotIndex() {
        return index;
    }
}
