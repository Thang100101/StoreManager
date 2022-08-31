package com.example.yourshopapplication.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.yourshopapplication.Model.Currency;

import java.util.List;

@Dao
public interface CurrencyDAO {
    @Query("select * from Currency")
    public List<Currency> getAll();
    @Query("select * from Currency where Currency_id = :id")
    public Currency get(int id);

    @Insert
    public long add(Currency currency);
}
