package com.example.yourshopapplication.Activity.Main.Fragments.DetailProduct;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;

import com.example.yourshopapplication.Activity.GrantPermission;
import com.example.yourshopapplication.Activity.Payment.Cart;
import com.example.yourshopapplication.DAO.ProductDAO;
import com.example.yourshopapplication.Database.StoreManagerDatabase;
import com.example.yourshopapplication.Model.OrderDetail;
import com.example.yourshopapplication.Model.Product;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.List;

public class DetailProductPresenter {
    private DetailProductView view;
    private Context context;
    private StoreManagerDatabase db;

    public DetailProductPresenter(DetailProductView view, Context context){
        this.view = view;
        this.context = context;
        this.db = StoreManagerDatabase.getInstance(context);
    }

    public void addProductToCart(Product product){
        Cart.getInstance().addProductToCart(product, context);
        List<OrderDetail> orderDetailList = Cart.getInstance().getOrderDetailList();
        for(OrderDetail od : orderDetailList){
            if(od.getProduct_id() == product.getId()){
                view.addProductToCartSuccess(product);
            }
        }
    }

    public void getImageFromGallery(ActivityResultLauncher<Intent> activityResultLauncher, Context context){
        GrantPermission.openGallery(activityResultLauncher, context);
    }

    public void scanQRCode(ActivityResultLauncher<ScanOptions> launcher, ScanOptions options, Context context){
        GrantPermission.scanQRCode(launcher, options, context);
    }

    public void updateProduct(Product product){
        ProductDAO productDAO = db.getProductDAO();
        if(checkExistsProductName(product)){
            view.existsProductName(product);
            return;
        }
        if(productDAO.update(product)>=1){
            view.updateProductSuccess(product);
        }else{
            view.updateProductFail();
        }
    }

    public void updateExistsProductName(Product product){
        ProductDAO productDAO = db.getProductDAO();
        if(productDAO.update(product)>=1){
            view.updateProductSuccess(product);
        }else{
            view.updateProductFail();
        }
    }

    private boolean checkExistsProductName(Product product){
        List<Product> products = db.getProductDAO().getAll();
        for(Product p : products){
            if(product.getName().replace(" ","").equals(p.getName().replace(" ",""))
                && product.getId() != p.getId()){
                return true;
            }
        }
        return false;
    }
}
