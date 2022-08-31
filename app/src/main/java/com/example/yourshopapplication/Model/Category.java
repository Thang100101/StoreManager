package com.example.yourshopapplication.Model;

import android.content.Context;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.yourshopapplication.DAO.CategoryDAO;
import com.example.yourshopapplication.Database.StoreManagerDatabase;
import com.example.yourshopapplication.R;

import java.io.Serializable;
import java.util.List;

@Entity(tableName = "Category")
public class Category implements Serializable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "Category_id")
    private Integer id;
    @ColumnInfo(name = "Name")
    private String name;
    @ColumnInfo(name = "Type")
    private CategoryType type;

    public Category(String name) {
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

    public CategoryType getType() {
        return type;
    }

    public void setType(CategoryType type) {
        this.type = type;
    }

    private static List<Category> categories;

    public static List<Category> getDefaultCategory(Context context){
        if(categories == null || categories.size() == 0){
            StoreManagerDatabase db = StoreManagerDatabase.getInstance(context);
            CategoryDAO dao = db.getCategoryDAO();
            categories = dao.getAll();
        }
        return categories;
    }

    public static List<Category> updateDefaultCategory(Context context, Category category){
        StoreManagerDatabase db = StoreManagerDatabase.getInstance(context);
        CategoryDAO dao = db.getCategoryDAO();
        dao.update(category);
        categories = dao.getAll();
        return categories;
    }

    public static List<Category> addAndUpdate(Context context, Category category){
        StoreManagerDatabase db = StoreManagerDatabase.getInstance(context);
        CategoryDAO dao = db.getCategoryDAO();
        dao.add(category);
        categories = dao.getAll();
        return categories;
    }

    public static void createDefaultCategory(Context context){
        StoreManagerDatabase db = StoreManagerDatabase.getInstance(context);
        CategoryDAO dao = db.getCategoryDAO();
        List<Category> categories = dao.getAll();
        if(categories == null || categories.size() == 0){
            Category category = new Category("Home");
            category.setType(CategoryType.Default);
            dao.add(category);
            Category category1 = new Category("None");
            category1.setType(CategoryType.Default);
            dao.add(category1);
        }
    }

    public static List<Category> getUnDefaultCategory(Context context){
        StoreManagerDatabase db = StoreManagerDatabase.getInstance(context);
        CategoryDAO dao = db.getCategoryDAO();
        List<Category> categories = dao.getUnDefault(CategoryType.UnDefault);
//        if(categories != null){
//
//            for(Category category : categories){
//                if(category.getType().equals(CategoryType.Default)){
//
//                }
//            }
//            return categories;
//        }
        if(categories != null && categories.size() != 0){
            return categories;
        }
        return null;
    }

    public enum CategoryType{
        Default, UnDefault;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
