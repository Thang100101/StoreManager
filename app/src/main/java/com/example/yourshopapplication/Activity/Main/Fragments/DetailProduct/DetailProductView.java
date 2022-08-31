package com.example.yourshopapplication.Activity.Main.Fragments.DetailProduct;

import com.example.yourshopapplication.Model.Product;

public interface DetailProductView {
    public void addProductToCartSuccess(Product product);
    public void updateProductSuccess(Product product);
    public void updateProductFail();
    public void existsProductName(Product product);
}
