package com.example.yourshopapplication.Activity.Login;

import android.app.Activity;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.example.yourshopapplication.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;

public abstract class LoginManagerPresenter {
    private static int gac_id=0;
    protected LoginView view;
    protected Activity activity;
    protected GoogleSignInOptions gso;
    protected GoogleSignInClient gsc;
    protected GoogleApiClient gac;
    protected FirebaseAuth auth;
    protected FirebaseUser user;
    public LoginManagerPresenter(LoginView view, Activity activity) {
        this.view = view;
        this.activity = activity;
        connectGoogle();
    }
    private void connectGoogle() {
        Calendar calendar = Calendar.getInstance();
        gso =new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(activity.getString(R.string.web_client))
                .requestEmail().build();
        gsc = GoogleSignIn.getClient(activity,gso);
        gac = LoginManagerPresenter.connectAPI(gac, view, activity, gso);
        auth = FirebaseAuth.getInstance();
    }
    private static GoogleApiClient connectAPI(GoogleApiClient gac, LoginView view, Activity activity, GoogleSignInOptions gso){
        gac = new GoogleApiClient.Builder(activity).enableAutoManage((FragmentActivity) activity, gac_id
                ,new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        view.connectFail();
                    }
                }).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();
        gac_id++;
        return gac;
    }

    public abstract void signIn(Intent data);
    public void signOut(){
        auth.signOut();
        Auth.GoogleSignInApi.signOut(gac).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                if(status.isSuccess())
                    view.signOutSuccess();
                else
                    view.signOutFail();
            }
        });
    }
    public abstract void openSignIn(int RQ_CODE);

    public FirebaseUser getUser(){ return FirebaseAuth.getInstance().getCurrentUser();}


}
