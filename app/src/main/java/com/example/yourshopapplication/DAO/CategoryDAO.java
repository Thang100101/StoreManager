package com.example.yourshopapplication.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.yourshopapplication.Model.Category;

import java.util.List;

@Dao
public interface CategoryDAO {
    @Query("Select * from Category")
    public List<Category> getAll();
    @Query("Select * from Category where Category_id = :id")
    public Category get(int id);
    @Query("Select * from category where Type = :type")
    public List<Category> getUnDefault(Category.CategoryType type);

    @Insert
    public long add(Category category);
    @Delete
    public void delete(Category category);
    @Update
    public void update(Category category);
}
