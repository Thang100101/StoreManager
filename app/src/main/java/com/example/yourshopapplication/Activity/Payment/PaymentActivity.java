package com.example.yourshopapplication.Activity.Payment;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yourshopapplication.Activity.Capture.CaptureA;
import com.example.yourshopapplication.Activity.GrantPermission;
import com.example.yourshopapplication.Adapter.OrderDetailAdapter;
import com.example.yourshopapplication.Adapter.PriceAdapter;
import com.example.yourshopapplication.DAO.ProductDAO;
import com.example.yourshopapplication.Database.StoreManagerDatabase;
import com.example.yourshopapplication.Model.OrderDetail;
import com.example.yourshopapplication.Model.Product;
import com.example.yourshopapplication.Model.User;
import com.example.yourshopapplication.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanIntentResult;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.List;

public class PaymentActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private User employee;
    private LinearLayout btnPayment, btnScan;
    private TextView txtNoti;
    private RecyclerView rclOrderDetail;
    private OrderDetailAdapter adapter;

    private ActivityResultLauncher<ScanOptions> launcher = registerForActivityResult(new ScanContract(),
            new ActivityResultCallback<ScanIntentResult>() {
                @Override
                public void onActivityResult(ScanIntentResult result) {
                    if(result.getContents() != null){
                        addProductToCartByCode(result.getContents());
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        Mapping();
        customActionBar();
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundle");
        employee = (User) bundle.getSerializable("employee");
    }

    private void Mapping(){
        toolbar = findViewById(R.id.toolbar);
        btnPayment = findViewById(R.id.btn_payment);
        btnScan = findViewById(R.id.btn_scan);
        btnPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPaymentDialog();
            }
        });

        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ScanOptions options = new ScanOptions();
                options.setCaptureActivity(CaptureA.class);
                options.setPrompt("Scan product");
                options.setOrientationLocked(true);
                options.setBeepEnabled(true);
                GrantPermission.scanQRCode(launcher, options, PaymentActivity.this);
            }
        });
        txtNoti = findViewById(R.id.txt_noti);
        if(Cart.getInstance().getOrderDetailList() == null ||
            Cart.getInstance().getOrderDetailList().size() == 0){
            txtNoti.setVisibility(View.VISIBLE);
            btnPayment.setVisibility(View.GONE);
        }

        Cart.getInstance().setOnClearCartListener(new Cart.onClearCartListener() {
            @Override
            public void onClear() {
                btnPayment.setVisibility(View.GONE);
                txtNoti.setVisibility(View.VISIBLE);
            }
        });

        adapter = new OrderDetailAdapter(this);
        rclOrderDetail = findViewById(R.id.rclview_orderdetail);
        rclOrderDetail.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rclOrderDetail.setLayoutManager(layoutManager);
    }


    private void addProductToCartByCode(String code){
        ProductDAO productDAO = StoreManagerDatabase.getInstance(this).getProductDAO();
        Product product = productDAO.getByCode(code);
        if(product != null) {
            Cart.getInstance().addProductToCart(product, this);
            btnPayment.setVisibility(View.VISIBLE);
        }
        else
            Toast.makeText(this, "Không tìm thấy sản phẩm", Toast.LENGTH_SHORT).show();
    }

    private void openPaymentDialog(){
        View view = getLayoutInflater().inflate(R.layout.bottom_sheet_payment, null);
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(view);
        dialog.show();
        RecyclerView rclPrice = view.findViewById(R.id.rclview_price);
        Button btnSubmit = view.findViewById(R.id.btn_submit);
        Button btnCancel = view.findViewById(R.id.btn_cancel);
        PriceAdapter adapter = new PriceAdapter(this);
        rclPrice.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rclPrice.setLayoutManager(layoutManager);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Payment();
                dialog.dismiss();
            }
        });

    }

    private void Payment(){
        Cart.getInstance().payment(PaymentActivity.this, employee);
        Toast.makeText(PaymentActivity.this, "Thanh toán thành công", Toast.LENGTH_SHORT).show();
    }
    private void customActionBar(){
        toolbar.setTitle("Payment");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                this.finish();
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}