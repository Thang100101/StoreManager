package com.example.yourshopapplication.Activity.Main;

import com.example.yourshopapplication.Model.User;
import com.example.yourshopapplication.Activity.Main.LoginLocalManager;

public interface LoginLocalView {
    public void signInLocalSuccess(User user);
    public void signInLocalFail();
    public void signOutLocalSuccess(User nextUser);
    public void signOutLocalFail();
    public void signUpFail(LoginLocalManager.SignUpFailType type);
    public void alreadySignInThisUser(User user);
}
