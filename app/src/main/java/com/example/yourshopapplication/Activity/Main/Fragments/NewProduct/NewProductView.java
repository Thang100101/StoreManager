package com.example.yourshopapplication.Activity.Main.Fragments.NewProduct;

import com.example.yourshopapplication.Model.Category;
import com.example.yourshopapplication.Model.Currency;
import com.example.yourshopapplication.Model.Product;

import java.util.List;

public interface NewProductView {
    public void setCurrencySelected(Currency currency);
    public void addCategoryToSpinner(Category category);
    public void existsProductName(Product product, List<Category> categories);
    public void addProductSuccess();
    public void addProductFail();
    public void existsProductCode();
}
