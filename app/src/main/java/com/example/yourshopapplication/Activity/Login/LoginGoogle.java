package com.example.yourshopapplication.Activity.Login;

import android.app.Activity;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginGoogle extends LoginManagerPresenter  {
    private GoogleSignInAccount account;

    public LoginGoogle(LoginView view, Activity activity) {
        super(view, activity);
        this.view = view;
        this.activity = activity;
    }

    @Override
    public void signIn(Intent data) {
        GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
        account = result.getSignInAccount();
        if(result.isSuccess()){
            if(account!=null) {
                account = result.getSignInAccount();
                String idToken = account.getIdToken();
                AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
                auth.signInWithCredential(credential)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    view.signInSuccess("google");
                                } else
                                    view.signInFail();
                            }
                        });
            }
        }
    }

    @Override
    public void openSignIn(int RQ_CODE) {
        Intent intent = Auth.GoogleSignInApi.getSignInIntent(gac);
        activity.startActivityForResult(intent, RQ_CODE);
    }

    public GoogleApiClient getGac() {
        return gac;
    }


}
