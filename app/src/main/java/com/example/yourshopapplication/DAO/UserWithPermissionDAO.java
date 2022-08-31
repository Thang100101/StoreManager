package com.example.yourshopapplication.DAO;

import androidx.room.Dao;
import androidx.room.Entity;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.yourshopapplication.Model.UserPermissionCrossRef;
import com.example.yourshopapplication.Model.UserWithPermission;

import java.util.List;

@Dao
public interface UserWithPermissionDAO {
    @Transaction
    @Query("select * from User where User_id = :user_id")
    public UserWithPermission get(int user_id);

    @Insert
    public void add(UserPermissionCrossRef uwp);
}
