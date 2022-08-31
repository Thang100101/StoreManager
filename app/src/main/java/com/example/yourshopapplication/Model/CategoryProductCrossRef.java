package com.example.yourshopapplication.Model;

import androidx.room.Entity;

@Entity(tableName = "CategoryProduct", primaryKeys = {"Category_id","Product_id"})
public class CategoryProductCrossRef {
    private int Category_id;
    private int Product_id;

    public CategoryProductCrossRef(int category_id, int product_id) {
        Category_id = category_id;
        Product_id = product_id;
    }

    public CategoryProductCrossRef() {
    }

    public int getCategory_id() {
        return Category_id;
    }

    public void setCategory_id(int category_id) {
        Category_id = category_id;
    }

    public int getProduct_id() {
        return Product_id;
    }

    public void setProduct_id(int product_id) {
        Product_id = product_id;
    }
}
