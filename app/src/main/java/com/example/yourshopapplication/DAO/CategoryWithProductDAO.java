package com.example.yourshopapplication.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.yourshopapplication.Model.CategoryProductCrossRef;
import com.example.yourshopapplication.Model.CategoryWithProduct;

import java.util.List;

@Dao
public interface CategoryWithProductDAO {
    @Transaction
    @Query("select * from Category where Category_id = :id")
    public CategoryWithProduct get(int id);
    @Insert
    public void add(CategoryProductCrossRef cwp);
}
