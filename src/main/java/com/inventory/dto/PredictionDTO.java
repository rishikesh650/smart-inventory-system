package com.inventory.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PredictionDTO {
    private String productName;
    private int currentStock;
    private double dailyUsage;
    private int estimatedDaysLeft;
    private String alertLevel;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getCurrentStock() {
        return currentStock;
    }

    public void setCurrentStock(int currentStock) {
        this.currentStock = currentStock;
    }

    public double getDailyUsage() {
        return dailyUsage;
    }

    public void setDailyUsage(double dailyUsage) {
        this.dailyUsage = dailyUsage;
    }

    public int getEstimatedDaysLeft() {
        return estimatedDaysLeft;
    }

    public void setEstimatedDaysLeft(int estimatedDaysLeft) {
        this.estimatedDaysLeft = estimatedDaysLeft;
    }

    public String getAlertLevel() {
        return alertLevel;
    }

    public void setAlertLevel(String alertLevel) {
        this.alertLevel = alertLevel;
    }
}
