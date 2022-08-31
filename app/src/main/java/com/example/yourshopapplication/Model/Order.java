package com.example.yourshopapplication.Model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.type.DateTime;

@Entity(tableName = "Order")
public class Order {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "Order_id")
    private Integer id;
    @ColumnInfo(name = "Day")
    private String day;
    @ColumnInfo(name = "Employee_id")
    private Integer employeeId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }


    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }
}
