package com.example.yourshopapplication.Model;

import android.graphics.Bitmap;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.example.yourshopapplication.Adapter.ConvertAvatar;

import java.io.Serializable;

@Entity(tableName = "Product")
public class Product implements Serializable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "Product_id")
    private Integer id;
    @ColumnInfo(name = "Name")
    private String name;
    @ColumnInfo(name = "Price")
    private double price;
    @ColumnInfo(name = "Type")
    private ProductType type;
    @ColumnInfo(name = "Quantity")
    private double quantity;
    @ColumnInfo(name = "Currency")
    private String currency;
    @ColumnInfo(name = "Image")
    private byte[] image;
    @ColumnInfo(name = "code")
    private String code;

    public enum ProductType {
        Mass, Amount;
    }

    public Product(String name) {
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public ProductType getType() {
        return type;
    }

    public void setType(ProductType type) {
        this.type = type;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public Bitmap getImageBitmap() {
        return ConvertAvatar.getBitMap(image);
    }

    public void setImageByBitmap(Bitmap image) {
        this.image = ConvertAvatar.getByte(image);
    }


}
