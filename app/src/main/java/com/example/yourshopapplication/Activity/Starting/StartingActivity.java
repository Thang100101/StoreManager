package com.example.yourshopapplication.Activity.Starting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yourshopapplication.Activity.Login.LoginActivity;
import com.example.yourshopapplication.Activity.Login.LoginFacebook;
import com.example.yourshopapplication.Activity.Login.LoginGoogle;
import com.example.yourshopapplication.Activity.Login.LoginManagerPresenter;
import com.example.yourshopapplication.Activity.Main.MainStoreActivity;
import com.example.yourshopapplication.R;
import com.example.yourshopapplication.Activity.Login.LoginView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

public class StartingActivity extends AppCompatActivity implements LoginView,
        NavigationView.OnNavigationItemSelectedListener {
    private LoginManagerPresenter login;
    private SharedPreferences prefer;
    private ProgressDialog progressDialog;
    private Button btnNewStore, btnCloneStore;
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting);
        Mapping();
        customActionBar();
        eventHandler();
        editNavigation();
    }

    private void eventHandler() {
        btnNewStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartingActivity.this, MainStoreActivity.class);
                startActivity(intent);
                StartingActivity.this.finish();
            }
        });
    }

    private void editNavigation(){
//        View headerView = navigationView.inflateHeaderView(R.layout.navigation_gf_login_header);
        View headerView = navigationView.getHeaderView(0);
        ImageView imgAvatar = headerView.findViewById(R.id.img_avatar);
        TextView txtName = headerView.findViewById(R.id.txt_name);
        TextView txtEmail = headerView.findViewById(R.id.txt_email);
        FirebaseUser user = login.getUser();
        Menu menu = navigationView.getMenu();
        navigationView.setNavigationItemSelectedListener(this::onNavigationItemSelected);
        if(user==null) {
            menu.getItem(0).setVisible(false);
            menu.getItem(1).setVisible(false);
            return;
        }else {
            Picasso.get().load(user.getPhotoUrl()).into(imgAvatar);
            txtName.setText(user.getDisplayName());
            txtEmail.setText(user.getEmail());
            menu.getItem(2).setVisible(false);
        }

    }

    private void customActionBar() {
        toolbar.setTitle("Starting");
        setSupportActionBar(toolbar);
    }

    private void Mapping(){
        prefer = getSharedPreferences("login", MODE_PRIVATE);

        if(prefer.getString("type","").equals("facebook"))
            login = new LoginFacebook(this,this);
        else
            login = new LoginGoogle(this, this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.wait));
        btnNewStore = findViewById(R.id.btn_new_store);
        btnCloneStore = findViewById(R.id.btn_clone_store);
        toolbar = findViewById(R.id.toolbar);
        drawer = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navigation);
    }

    @Override
    public void signInSuccess(String type) {

    }

    @Override
    public void signInFail() {

    }

    @Override
    public void signOutSuccess() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        this.finish();
        progressDialog.dismiss();
        Toast.makeText(this, getResources().getString(R.string.signout_success), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void signOutFail() {

    }

    @Override
    public void connectFail() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        FirebaseUser user = login.getUser();
        MenuItem item = menu.getItem(0);
        if(user==null)
            item.setIcon(R.drawable.icon_un_login);
        else
            item.setIcon(R.drawable.icon_have_login);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_login_status:
                drawer.openDrawer(GravityCompat.END);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_signout:
                login.signOut();
                break;
            case R.id.menu_signin:
                Intent intent = new Intent(StartingActivity.this, LoginActivity.class);
                intent.putExtra("signin",true);
                startActivity(intent);
                this.finish();
                break;
            case R.id.menu_upload:
                Toast.makeText(this, "click upload", Toast.LENGTH_SHORT).show();
                break;
        }
        return false;
    }
}