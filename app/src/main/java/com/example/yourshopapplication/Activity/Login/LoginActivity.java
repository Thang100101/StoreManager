package com.example.yourshopapplication.Activity.Login;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.yourshopapplication.Activity.Starting.StartingActivity;
import com.example.yourshopapplication.Model.DocForApp;
import com.example.yourshopapplication.R;
import com.facebook.CallbackManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity implements LoginView {
    private LoginManagerPresenter loginGoogle, loginFacebook;
    private Button btnSignInGoogle, btnSignInFacebook, btnAccess;
    private static final int RQ_CODE_GOOGLE=1, RQ_CODE_FACEBOOK=2;
    private FirebaseAuth auth;
    private SharedPreferences prefer;
    private Intent intent;
    private DatabaseReference dataRef;
    private CallbackManager callbackManager;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Mapping();
        EventHandler();
        Intent intent = getIntent();
        if(intent.getBooleanExtra("signin", false))
            btnSignInGoogle.callOnClick();
    }

    private void EventHandler() {
        btnSignInGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginGoogle.openSignIn(RQ_CODE_GOOGLE);
            }
        });

        btnSignInFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginFacebook.openSignIn(RQ_CODE_FACEBOOK);
            }
        });

        btnAccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, StartingActivity.class);
                startActivity(intent);
                LoginActivity.this.finish();
            }
        });
    }


    private void Mapping() {
        btnSignInGoogle = findViewById(R.id.btn_signin_google);
        btnSignInFacebook = findViewById(R.id.btn_signin_facebook);
        btnAccess = findViewById(R.id.btn_access);
        auth = FirebaseAuth.getInstance();
        dataRef = FirebaseDatabase.getInstance(DocForApp.getUrlFirebase()).getReference();
        loginGoogle = new LoginGoogle(this, this);
        loginFacebook = new LoginFacebook(this,this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.wait));
    }


    @Override
    public void signInSuccess(String type) {
        progressDialog.dismiss();
        SharedPreferences.Editor editor = prefer.edit();
        editor.putString("type", type);
        editor.commit();
        Toast.makeText(this, getResources().getString(R.string.signin_success), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(LoginActivity.this, StartingActivity.class);
        startActivity(intent);
        this.finish();
    }

    @Override
    public void signInFail() {
        Toast.makeText(this, "Sign in fail", Toast.LENGTH_SHORT).show();
        progressDialog.dismiss();
    }

    @Override
    public void signOutSuccess() {
        Toast.makeText(this, getResources().getString(R.string.signout_success), Toast.LENGTH_SHORT).show();
        progressDialog.dismiss();
    }

    @Override
    public void signOutFail() {
        Toast.makeText(this, "Sign out fail", Toast.LENGTH_SHORT).show();
        progressDialog.dismiss();
    }

    @Override
    public void connectFail() {
        Toast.makeText(this, "Connect Failed", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        intent = data;
        intent.putExtra("rqcode",requestCode);
        intent.putExtra("rscode",resultCode);
        if(resultCode!=RESULT_OK){
            this.signInFail();
            return;
        }
        if(requestCode==RQ_CODE_GOOGLE && resultCode==RESULT_OK){
            progressDialog.show();
            loginGoogle.signIn(intent);
        }else{
            progressDialog.show();
            loginFacebook.signIn(intent);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        prefer = getSharedPreferences("login", MODE_PRIVATE);
        if(auth.getCurrentUser()!=null) {
            Intent intent = new Intent(LoginActivity.this, StartingActivity.class);
            startActivity(intent);
            this.finish();
        }
    }
}