package com.example.yourshopapplication.Model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(tableName = "Order Detail", primaryKeys = {"Product_id", "Order_id"})
public class OrderDetail {
    private int Order_id;
    private int Product_id;
    @ColumnInfo(name = "Quantity")
    private double quantity;

    public OrderDetail() {
    }

    public OrderDetail(int order_id, int product_id) {
        Order_id = order_id;
        Product_id = product_id;
    }

    public int getOrder_id() {
        return Order_id;
    }

    public void setOrder_id(int order_id) {
        Order_id = order_id;
    }

    public int getProduct_id() {
        return Product_id;
    }

    public void setProduct_id(int product_id) {
        Product_id = product_id;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }
}
