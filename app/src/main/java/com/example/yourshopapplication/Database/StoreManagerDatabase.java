package com.example.yourshopapplication.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.yourshopapplication.DAO.CategoryDAO;
import com.example.yourshopapplication.DAO.CategoryWithProductDAO;
import com.example.yourshopapplication.DAO.CurrencyDAO;
import com.example.yourshopapplication.DAO.OrderDAO;
import com.example.yourshopapplication.DAO.OrderDetailDAO;
import com.example.yourshopapplication.DAO.OrderWithPriceDAO;
import com.example.yourshopapplication.DAO.PermissionDAO;
import com.example.yourshopapplication.DAO.PriceDAO;
import com.example.yourshopapplication.DAO.ProductDAO;
import com.example.yourshopapplication.DAO.UserDAO;
import com.example.yourshopapplication.DAO.UserWithPermissionDAO;
import com.example.yourshopapplication.Model.Category;
import com.example.yourshopapplication.Model.CategoryProductCrossRef;
import com.example.yourshopapplication.Model.Currency;
import com.example.yourshopapplication.Model.Order;
import com.example.yourshopapplication.Model.OrderDetail;
import com.example.yourshopapplication.Model.Permission;
import com.example.yourshopapplication.Model.Price;
import com.example.yourshopapplication.Model.Product;
import com.example.yourshopapplication.Model.User;
import com.example.yourshopapplication.Model.UserPermissionCrossRef;

@Database(entities = {User.class, Permission.class, UserPermissionCrossRef.class,
        Category.class, Product.class, CategoryProductCrossRef.class, Currency.class
            , OrderDetail.class, Price.class, Order.class}, version = 1)
public abstract class StoreManagerDatabase extends RoomDatabase {
    private static StoreManagerDatabase db;
    public abstract UserDAO getUserDAO();
    public abstract PermissionDAO getPermissionDAO();
    public abstract UserWithPermissionDAO getUserWithPermissionDAO();
    public abstract CategoryDAO getCategoryDAO();
    public abstract ProductDAO getProductDAO();
    public abstract CategoryWithProductDAO getCategoryWithProductDAO();
    public abstract CurrencyDAO getCurrencyDAO();
    public abstract OrderDetailDAO getOrderDetailDAO();
    public abstract OrderWithPriceDAO getOrderWithPriceDAO();
    public abstract PriceDAO getPriceDAO();
    public abstract OrderDAO getOrderDAO();


    public static StoreManagerDatabase getInstance(Context context){
        if(db==null)
            return getDB(context);
        return db;
    }
    private synchronized static StoreManagerDatabase getDB(Context context){
        if(db==null)
            db = Room.databaseBuilder(context, StoreManagerDatabase.class, "StoreManager")
                    .allowMainThreadQueries().build();
        return db;
    }
}
