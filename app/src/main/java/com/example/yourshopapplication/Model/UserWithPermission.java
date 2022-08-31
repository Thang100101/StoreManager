package com.example.yourshopapplication.Model;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

public class UserWithPermission {
    @Embedded
    private User user;
    @Relation(
            parentColumn = "User_id",
            entityColumn = "Permission_id",
            associateBy = @Junction(UserPermissionCrossRef.class)
    )
    private List<Permission> permissions;

    public UserWithPermission(User user, List<Permission> permissions) {
        this.user = user;
        this.permissions = permissions;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }
}
