package com.example.yourshopapplication.Activity.QuantityProduct;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.yourshopapplication.Activity.Capture.CaptureA;
import com.example.yourshopapplication.Activity.GrantPermission;
import com.example.yourshopapplication.Adapter.QuantityProductAdapter;
import com.example.yourshopapplication.DAO.ProductDAO;
import com.example.yourshopapplication.Database.StoreManagerDatabase;
import com.example.yourshopapplication.Model.Product;
import com.example.yourshopapplication.R;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanIntentResult;
import com.journeyapps.barcodescanner.ScanOptions;

public class QuantityActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView rclProduct;
    private SearchView searchView;
    private QuantityProductAdapter adapter;
    private boolean scan = false;

    private ActivityResultLauncher<ScanOptions> launcher = registerForActivityResult(
            new ScanContract(), new ActivityResultCallback<ScanIntentResult>() {
                @Override
                public void onActivityResult(ScanIntentResult result) {
                    if(result.getContents() != null){
                        ProductDAO productDAO = StoreManagerDatabase.getInstance(QuantityActivity.this).getProductDAO();
                        Product product = productDAO.getByCode(result.getContents());
                        if(product != null){
                            adapter.filterByCode(result.getContents());
                            scan = true;
                        }else{
                            Toast.makeText(QuantityActivity.this, "Không tìm thấy sản phẩm", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quantity);
        Mapping();
        customActionBar();
    }

    private void Mapping(){
        StoreManagerDatabase db = StoreManagerDatabase.getInstance(this);
        ProductDAO productDAO = db.getProductDAO();
        toolbar = findViewById(R.id.toolbar);
        rclProduct = findViewById(R.id.rclview_product);
        adapter = new QuantityProductAdapter(productDAO.getAll(), this);
        rclProduct.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rclProduct.setLayoutManager(layoutManager);
    }

    private void customActionBar(){
        toolbar.setTitle("Setting quantity of product");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                this.finish();
                break;
            case R.id.menu_scan:
                ScanOptions options = new ScanOptions();
                options.setBeepEnabled(true);
                options.setPrompt("Scan product");
                options.setOrientationLocked(true);
                options.setCaptureActivity(CaptureA.class);
                GrantPermission.scanQRCode(launcher, options, this);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(scan){
            adapter.reset();
            scan = false;
            return;
        }
        if(!searchView.isIconified()){
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search_scan, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }


}