package com.example.yourshopapplication.Model;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

public class OrderWithProduct {
    @Embedded
    private Order order;
    @Relation(
            parentColumn = "Order_id",
            entityColumn = "Product_id",
            associateBy = @Junction(OrderDetail.class)
    )
    private List<Product> products;

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
