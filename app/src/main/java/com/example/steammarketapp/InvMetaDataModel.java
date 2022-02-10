package com.example.steammarketapp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class InvMetaDataModel {

    private BigDecimal portfolioValue = BigDecimal.ZERO;
    private final LocalDateTime dateOfEntry;

    public InvMetaDataModel(LocalDateTime currentDate) {
        this.dateOfEntry = currentDate;
    }

    public void addPortfolioValue(BigDecimal addedValue) {
        this.portfolioValue = (this.portfolioValue.add(addedValue));
    }

    public BigDecimal getPortfolioValue() {
        return portfolioValue;
    }

    public LocalDateTime getDateOfEntry() {
        return dateOfEntry;
    }
}
