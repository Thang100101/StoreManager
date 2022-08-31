package com.example.yourshopapplication.Model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "UserPermission",primaryKeys = {"User_id","Permission_id"})
public class UserPermissionCrossRef {
    private int User_id;
    private int Permission_id;

    public UserPermissionCrossRef(int user_id, int permission_id) {
        User_id = user_id;
        Permission_id = permission_id;
    }
    public UserPermissionCrossRef(){}

    public int getUser_id() {
        return User_id;
    }

    public void setUser_id(int user_id) {
        User_id = user_id;
    }

    public int getPermission_id() {
        return Permission_id;
    }

    public void setPermission_id(int permission_id) {
        Permission_id = permission_id;
    }
}
