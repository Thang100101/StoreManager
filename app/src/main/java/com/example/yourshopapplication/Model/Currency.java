package com.example.yourshopapplication.Model;

import android.content.Context;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.yourshopapplication.Adapter.CurrencyAdapterSpinner;
import com.example.yourshopapplication.DAO.CurrencyDAO;
import com.example.yourshopapplication.Database.StoreManagerDatabase;

import java.util.List;

@Entity(tableName = "Currency")
public class Currency {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "Currency_id")
    private Integer id;
    @ColumnInfo(name = "Name")
    private String name;

    public static void createDefaultCurrency(Context context){
        StoreManagerDatabase db = StoreManagerDatabase.getInstance(context);
        CurrencyDAO currencyDAO = db.getCurrencyDAO();
        List<Currency> currencies = currencyDAO.getAll();
        if(currencies != null && currencies.size() != 0)
            return;
        currencyDAO.add(new Currency("USD"));
        currencyDAO.add(new Currency("VND"));
        currencyDAO.add(new Currency("WON"));
        currencyDAO.add(new Currency("YEN"));
        currencyDAO.add(new Currency("NDT"));
    }

    public static List<Currency> getDefaultCurrency(Context context){
        StoreManagerDatabase db = StoreManagerDatabase.getInstance(context);
        CurrencyDAO currencyDAO = db.getCurrencyDAO();
        return currencyDAO.getAll();
    }

    public Currency(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
