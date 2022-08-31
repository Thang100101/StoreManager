package com.example.yourshopapplication.Activity.Main;

import android.content.Context;

import com.example.yourshopapplication.DAO.PermissionDAO;
import com.example.yourshopapplication.DAO.UserWithPermissionDAO;
import com.example.yourshopapplication.Database.StoreManagerDatabase;
import com.example.yourshopapplication.Model.Feature;
import com.example.yourshopapplication.Model.FeatureEnum;
import com.example.yourshopapplication.Model.Permission;
import com.example.yourshopapplication.Model.User;
import com.example.yourshopapplication.Model.UserPermissionEnum;
import com.example.yourshopapplication.Model.UserWithPermission;

import java.util.ArrayList;
import java.util.List;

public class PermissionManager {
    private PermissionManagerView view;
    private StoreManagerDatabase db;
    private PermissionDAO pmsDAO;
    private UserWithPermissionDAO uwpDAO;

    public PermissionManager(PermissionManagerView view) {
        this.view = view;
        getDatabase();
        addPermission();
    }

    private void getDatabase() {
        db = StoreManagerDatabase.getInstance((Context) view);
        pmsDAO = db.getPermissionDAO();
        uwpDAO = db.getUserWithPermissionDAO();
    }
    public void addPermission(){
        List<Permission> permissions = pmsDAO.getAll();
        if(permissions == null || permissions.size() == 0)
        {
            for(UserPermissionEnum e : UserPermissionEnum.values())
            {
                Permission permission = new Permission(e);
                pmsDAO.add(permission);
            }
        }
    }
    public List<Permission> getAllPermission(){
        return pmsDAO.getAll();
    }

    public List<Feature> getFeatureForUserPermission(User user){
        List<Permission> permissions = getPermissionOfUser(user);
        List<Feature> features = new ArrayList<>();
        if(user==null) {
            features.clear();
            features.add(new Feature(FeatureEnum.CheckIn));
            return features;
        }else{
            features.clear();
            features.add(new Feature(FeatureEnum.CheckOut));
            features.add(new Feature(FeatureEnum.ChangeUser));
            features.add(new Feature(FeatureEnum.CheckIn));
        }
        for(Permission p : permissions){
            switch (p.getType()){
                case Admin:
                    features.clear();
                    features.add(new Feature(FeatureEnum.Payment));
                    features.add(new Feature(FeatureEnum.NewCategory));
                    features.add(new Feature(FeatureEnum.NewProduct));
                    features.add(new Feature(FeatureEnum.AddQuantityProduct));
                    features.add(new Feature(FeatureEnum.EmployeeManager));
                    features.add(new Feature(FeatureEnum.Calender));
                    features.add(new Feature(FeatureEnum.ScanCode));
                    features.add(new Feature(FeatureEnum.ChangeUser));
                    features.add(new Feature(FeatureEnum.CheckOut));
                    features.add(new Feature(FeatureEnum.CheckIn));
                    return features;
                case AddProduct:
                    features.add(new Feature(FeatureEnum.NewCategory));
                    features.add(new Feature(FeatureEnum.NewProduct));
                    features.add(new Feature(FeatureEnum.AddQuantityProduct));
                    break;
                case EmManager:
                    features.add(new Feature(FeatureEnum.EmployeeManager));
                    features.add(new Feature(FeatureEnum.Calender));
                    break;
                case Payment:
                    features.add(new Feature(FeatureEnum.Payment));
                default:
                    break;
            }
        }
        return features;
    }

    public List<Permission> getPermissionOfUser(User user){
        if(user == null)
            return null;
        UserWithPermission uwp = uwpDAO.get(user.getId());
        return uwp.getPermissions();
    }
}
