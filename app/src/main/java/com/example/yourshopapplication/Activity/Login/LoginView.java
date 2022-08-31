package com.example.yourshopapplication.Activity.Login;

public interface LoginView {
    public void signInSuccess(String type);
    public void signInFail();
    public void signOutSuccess();
    public void signOutFail();
    public void connectFail();
}
