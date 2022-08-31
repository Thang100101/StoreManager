package com.example.yourshopapplication.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.yourshopapplication.Model.Product;

import java.util.List;

@Dao
public interface ProductDAO {
    @Query("select * from Product")
    public List<Product> getAll();
    @Query("select * from Product where Product_id = :id")
    public Product get(int id);
    @Query("select * from Product where code = :code")
    public Product getByCode(String code);

    @Insert
    public long add(Product product);
    @Delete
    public void delete(Product product);
    @Update
    public int update(Product product);
}
