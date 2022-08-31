package com.example.yourshopapplication.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.yourshopapplication.Model.Order;

import java.util.List;

@Dao
public interface OrderDAO {
    @Query("select * from `Order`")
    public List<Order> getAll();
    @Query("select * from `Order` where Order_id = :id")
    public Order get(int id);

    @Insert
    public long add(Order order);
    @Delete
    public void delete(Order order);
    @Update
    public void update(Order order);
}
