package com.example.yourshopapplication.Activity.Main;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.yourshopapplication.DAO.PermissionDAO;
import com.example.yourshopapplication.DAO.UserDAO;
import com.example.yourshopapplication.DAO.UserWithPermissionDAO;
import com.example.yourshopapplication.Database.StoreManagerDatabase;
import com.example.yourshopapplication.Model.Permission;
import com.example.yourshopapplication.Model.User;
import com.example.yourshopapplication.Model.UserPermissionCrossRef;
import com.example.yourshopapplication.Model.UserPermissionEnum;

import java.util.ArrayList;
import java.util.List;

public class LoginLocalManager {
    private StoreManagerDatabase db;
    private UserDAO userDAO;
    private LoginLocalView view;
    private List<User> localUsers;
    private List<Permission> permissions;
    private PermissionDAO pmsDAO;
    private UserWithPermissionDAO uwpDAO;
    private SharedPreferences prefer;
    private SharedPreferences.Editor editor;

    public LoginLocalManager(LoginLocalView view) {
        this.view = view;
        getDatabase();
        prefer = ((Activity) view).getSharedPreferences("user", Context.MODE_PRIVATE);
        editor = prefer.edit();
    }

    private void getDatabase() {
        db = StoreManagerDatabase.getInstance((Context) view);
        userDAO = db.getUserDAO();
        localUsers = new ArrayList<>();
        pmsDAO = db.getPermissionDAO();
        permissions = pmsDAO.getAll();
        uwpDAO = db.getUserWithPermissionDAO();
    }
    public void signIn(String name, String password){
        if(name.isEmpty() || password.isEmpty())
            view.signInLocalFail();
        User user = userDAO.getBySignIn(name, password);
        if(user==null)
            view.signInLocalFail();
        else {
            for(User u : localUsers){
                if(u.getId() == user.getId()) {
                    view.alreadySignInThisUser(u);
                    return;
                }
            }

            localUsers.add(user);
            view.signInLocalSuccess(user);
            editor.commit();
        }

    }
    public void signOut(User user){
        User userFlag = user;
        for(User u : localUsers){
            if(user.getId() == u.getId()) {
                userFlag = u;
                localUsers.remove(userFlag);
                if(localUsers.size() >= 1)
                    view.signOutLocalSuccess(localUsers.get(0));
                else
                    view.signOutLocalSuccess(null);
                return;
            }
        }
        view.signOutLocalFail();
    }
    public void signUp(String name, String password, String confirmPassord, SignUpType type){
        if(!password.equals(confirmPassord))
        {
            view.signUpFail(SignUpFailType.ConfirmPassWrong);
            return;
        }
        if(name.replace(" ","").isEmpty() ||
                password.replace(" ","").isEmpty()||
                    confirmPassord.replace(" ","").isEmpty())
        {
            view.signUpFail(SignUpFailType.Empty);
            return;
        }
        User user = new User(name, password);
        List<User> users = userDAO.getByName(user.getName());
        for(User u : users){
            if(u.getName().equals(user.getName())){
                view.signUpFail(SignUpFailType.DuplicateName);
                return;
            }
        }
        userDAO.add(user);
        if(type.equals(SignUpType.Admin)){
            Log.d("Admin","Set permission "+permissions.size() );
            User user1 = userDAO.getBySignIn(name, password);
            Permission permission = pmsDAO.getByType(UserPermissionEnum.Admin);
            UserPermissionCrossRef uwp = new UserPermissionCrossRef(user1.getId(), permission.getId());
            uwpDAO.add(uwp);
        }
        signIn(user.getName(), user.getPassword());
    }
    public boolean haveManager(){
        List<User> users = userDAO.getAll();
        if(users == null || users.size()==0) {
            return false;
        }
        else {
            return true;
        }
    }

    public void reSignIn(){
        String username = prefer.getString("username","");
        String password = prefer.getString("password","");
        if(username.isEmpty() || password.isEmpty())
            return;
        signIn(username, password);
    }

    public StoreManagerDatabase getDb() {
        return db;
    }

    public UserDAO getUserDAO() {
        return userDAO;
    }

    public LoginLocalView getView() {
        return view;
    }

    public List<User> getLocalUsers() {
        return localUsers;
    }

    public enum SignUpType {
        Admin, Staff;
    }

    public enum SignUpFailType {
        DuplicateName, ConfirmPassWrong, Empty;
    }

}
