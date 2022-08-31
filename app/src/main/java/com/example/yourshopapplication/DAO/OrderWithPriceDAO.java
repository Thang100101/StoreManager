package com.example.yourshopapplication.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.yourshopapplication.Model.OrderWithPrice;

import java.util.List;

@Dao
public interface OrderWithPriceDAO {
    @Transaction
    @Query("select * from `Order`")
    public List<OrderWithPrice> getAll();

    @Transaction
    @Query("select * from `Order` where Order_id = :id")
    public OrderWithPrice get(int id);

}
