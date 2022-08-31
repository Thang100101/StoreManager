package com.example.yourshopapplication.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.yourshopapplication.Model.Permission;
import com.example.yourshopapplication.Model.UserPermissionEnum;

import java.util.List;

@Dao
public interface PermissionDAO {
    @Query("select * from Permission")
    public List<Permission> getAll();
    @Query("select * from Permission where Permission_id = :id")
    public Permission get(int id);
    @Query("select * from Permission where Type = :type")
    public Permission getByType(UserPermissionEnum type);

    @Insert
    public void add(Permission permission);
    @Delete
    public void delete(Permission permission);
    @Update
    public void update(Permission permission);
}