package com.example.yourshopapplication.Model;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

public class ProductWithOrder {
    @Embedded
    private Product product;
    @Relation(
            parentColumn = "Product_id",
            entityColumn = "Order_id",
            associateBy = @Junction(OrderDetail.class)
    )
    private List<Order> orders;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
}
