package com.example.yourshopapplication.Activity.EmployeeManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yourshopapplication.Activity.Main.LoginLocalManager;
import com.example.yourshopapplication.Activity.Main.LoginLocalView;
import com.example.yourshopapplication.Adapter.EmployeeAdapter;
import com.example.yourshopapplication.DAO.UserDAO;
import com.example.yourshopapplication.Database.StoreManagerDatabase;
import com.example.yourshopapplication.Model.User;
import com.example.yourshopapplication.R;
import com.google.android.gms.common.ErrorDialogFragment;

public class EmployeeManagerActivity extends AppCompatActivity implements LoginLocalView {

    private Toolbar toolbar;
    private LoginLocalManager localManager;
    private ProgressDialog progressDialog;
    private Dialog dialogSignUp;
    private RecyclerView rclEmployee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_manager);
        Mapping();
        customActionBar();
        getListEmployee();
    }

    private void Mapping(){
        toolbar = findViewById(R.id.toolbar);
        localManager = new LoginLocalManager(this);
        progressDialog = new ProgressDialog(this);
        dialogSignUp = new Dialog(this);
        rclEmployee = findViewById(R.id.rclview_employee);
    }

    private void customActionBar(){
        toolbar.setTitle("Quản lí nhân sự");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void getListEmployee(){
        UserDAO userDAO = StoreManagerDatabase.getInstance(this).getUserDAO();
        EmployeeAdapter adapter = new EmployeeAdapter(this, userDAO.getAll());
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rclEmployee.setAdapter(adapter);
        rclEmployee.setLayoutManager(layoutManager);
    }

    private void openDialogSignUp(){
        dialogSignUp.setContentView(R.layout.dialog_sign_up);
        TextView txtTitle = dialogSignUp.findViewById(R.id.txt_title);
        EditText editUsername = dialogSignUp.findViewById(R.id.edit_name);
        EditText editPassword = dialogSignUp.findViewById(R.id.edit_pass);
        EditText editConfirm = dialogSignUp.findViewById(R.id.edit_confirm_pass);
        Button btnSubmit = dialogSignUp.findViewById(R.id.btn_submit);
        Button btnCancel = dialogSignUp.findViewById(R.id.btn_cancel);

        txtTitle.setText("Tạo tài khoản nhân viên");

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogSignUp.dismiss();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogSignUp.dismiss();
                progressDialog.show();
                localManager.signUp(editUsername.getText().toString(), editPassword.getText().toString(),
                        editConfirm.getText().toString(), LoginLocalManager.SignUpType.Staff);
            }
        });

        dialogSignUp.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_employee, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                this.finish();
                break;
            case R.id.menu_add_employee:
                openDialogSignUp();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void signInLocalSuccess(User user) {
        Toast.makeText(this, "Đã thêm "+user.getName()+" vào danh sách", Toast.LENGTH_SHORT).show();
        progressDialog.dismiss();
        dialogSignUp.dismiss();
        getListEmployee();
    }

    @Override
    public void signInLocalFail() {

    }

    @Override
    public void signOutLocalSuccess(User nextUser) {

    }

    @Override
    public void signOutLocalFail() {

    }

    @Override
    public void alreadySignInThisUser(User user) {

    }

    @Override
    public void signUpFail(LoginLocalManager.SignUpFailType type) {
        switch (type){
            case DuplicateName:
                Toast.makeText(this, getString(R.string.duplicate_user), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                dialogSignUp.show();
                break;
            case ConfirmPassWrong:
                Toast.makeText(this, getString(R.string.confirm_not_correct), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                dialogSignUp.show();
                break;
            case Empty:
                Toast.makeText(this, getString(R.string.empty), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                dialogSignUp.show();
            default:
        }
    }
}