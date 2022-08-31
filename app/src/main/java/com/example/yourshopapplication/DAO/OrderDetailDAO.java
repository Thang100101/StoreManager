package com.example.yourshopapplication.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.yourshopapplication.Model.OrderDetail;
import com.example.yourshopapplication.Model.OrderWithProduct;
import com.example.yourshopapplication.Model.ProductWithOrder;

import java.util.List;

@Dao
public interface OrderDetailDAO {
    @Transaction
    @Query("select * from `Order`")
    public List<OrderWithProduct> getAllOrderWithProduct();

    @Transaction
    @Query("select * from `Order` where Order_id = :id")
    public OrderWithProduct getOrderWithProductById(int id);

    @Transaction
    @Query("select * from `Product`")
    public List<ProductWithOrder> getAllProductWithOrder();

    @Transaction
    @Query("select * from `Product` where Product_id = :id")
    public ProductWithOrder getProductWithOrderById(int id);

    @Insert
    public long add(OrderDetail orderDetail);
}
