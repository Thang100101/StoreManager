package com.example.yourshopapplication.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.yourshopapplication.Model.User;

import java.util.List;

@Dao
public interface UserDAO {
    @Query("select * from User")
    public List<User> getAll();
    @Query("select * from User where User_id = :id")
    public User get(int id);
    @Query("select * from User where Name = :name")
    public List<User> getByName(String name);
    @Query("select * from User where Name = :name and Password = :password")
    public User getBySignIn(String name, String password);

    @Insert
    public void add(User user);
    @Update
    public void update(User user);
}