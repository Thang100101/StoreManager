package com.example.yourshopapplication.Model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Price")
public class Price {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "Price_id")
    private Integer priceId;
    @ColumnInfo(name = "Order_id")
    private int orderId;
    @ColumnInfo(name = "Total Price")
    private double totalPrice;
    @ColumnInfo(name = "Currency")
    private String currency;

    public Integer getPriceId() {
        return priceId;
    }

    public void setPriceId(Integer priceId) {
        this.priceId = priceId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
