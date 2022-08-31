package com.example.yourshopapplication.Model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Permission")
public class Permission {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "Permission_id")
    private Integer id;
    @ColumnInfo(name = "Type")
    private UserPermissionEnum type;

    public Permission(UserPermissionEnum type) {
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public UserPermissionEnum getType() {
        return type;
    }

    public void setType(UserPermissionEnum type) {
        this.type = type;
    }
}
