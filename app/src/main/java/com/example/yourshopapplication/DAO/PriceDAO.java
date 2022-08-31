package com.example.yourshopapplication.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.yourshopapplication.Model.Price;

@Dao
public interface PriceDAO {
    @Insert
    public long add(Price price);
    @Update
    public void update(Price price);
    @Delete
    public void delete(Price price);
}
