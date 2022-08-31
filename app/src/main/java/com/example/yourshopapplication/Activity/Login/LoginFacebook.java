package com.example.yourshopapplication.Activity.Login;

import android.app.Activity;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;

import java.util.Arrays;


public class LoginFacebook extends LoginManagerPresenter{
    private CallbackManager callbackManager;
    private LoginManager loginManager;
    private AccessToken token;
    public LoginFacebook(LoginView view, Activity activity) {
        super(view, activity);
        loginManager = LoginManager.getInstance();
        callbackManager = CallbackManager.Factory.create();
    }


    @Override
    public void signIn(Intent data) {
        int rqcode = data.getIntExtra("rqcode",0);
        int rscode = data.getIntExtra("rscode",0);
        loginManager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                token = loginResult.getAccessToken();
                handlerFBAccessToken(token);
            }

            @Override
            public void onCancel() {
                view.signInFail();
            }

            @Override
            public void onError(FacebookException error) {
                view.signInFail();
            }
        });
        callbackManager.onActivityResult(rqcode, rscode, data);
    }
    private void handlerFBAccessToken(AccessToken token){
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        auth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    view.signInSuccess("facebook");
                }else
                    view.signInFail();
            }
        });
    }




    @Override
    public void openSignIn(int RQ_CODE) {
        loginManager.logInWithReadPermissions(activity, Arrays.asList("email","public_profile"));
    }


}
