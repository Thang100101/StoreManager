package com.example.yourshopapplication.Model;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Relation;

import java.util.List;

public class OrderWithPrice {
    @Embedded
    private Order order;
    @Relation(
            parentColumn = "Order_id",
            entityColumn = "Order_id"
    )
    private List<Price> prices;

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public List<Price> getPrices() {
        return prices;
    }

    public void setPrices(List<Price> prices) {
        this.prices = prices;
    }
}
