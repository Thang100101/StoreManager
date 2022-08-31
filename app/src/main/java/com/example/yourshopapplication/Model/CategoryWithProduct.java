package com.example.yourshopapplication.Model;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

public class CategoryWithProduct {
    @Embedded
    private Category category;
    @Relation(
            parentColumn = "Category_id",
            entityColumn = "Product_id",
            associateBy = @Junction(CategoryProductCrossRef.class)
    )
    private List<Product> products;

    public CategoryWithProduct(Category category, List<Product> products) {
        this.category = category;
        this.products = products;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
