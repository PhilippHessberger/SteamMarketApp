package com.example.steammarketapp.data_models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ModelMetaData {

    private BigDecimal portfolioValue = BigDecimal.ZERO;
    private final LocalDateTime dateOfEntry;

    public ModelMetaData(LocalDateTime currentDate) {
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
