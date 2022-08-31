package com.example.yourshopapplication.Activity.Main.Fragments.NewProduct;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;

import com.example.yourshopapplication.Activity.GrantPermission;
import com.example.yourshopapplication.Adapter.CategoryAdapterSpinner;
import com.example.yourshopapplication.Adapter.CurrencyAdapterSpinner;
import com.example.yourshopapplication.DAO.CategoryDAO;
import com.example.yourshopapplication.DAO.CategoryWithProductDAO;
import com.example.yourshopapplication.DAO.CurrencyDAO;
import com.example.yourshopapplication.DAO.ProductDAO;
import com.example.yourshopapplication.Database.StoreManagerDatabase;
import com.example.yourshopapplication.Model.Category;
import com.example.yourshopapplication.Model.CategoryProductCrossRef;
import com.example.yourshopapplication.Model.Currency;
import com.example.yourshopapplication.Model.Product;
import com.example.yourshopapplication.R;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.ArrayList;
import java.util.List;

public class NewProductPresenter {
    private NewProductView newProductView;
    private Activity activity;
    private StoreManagerDatabase db;

    public NewProductPresenter(NewProductView newProductView, Activity activity) {
        this.newProductView = newProductView;
        this.activity = activity;
        db = StoreManagerDatabase.getInstance(activity);
    }

    public void grantPermissionImage(ActivityResultLauncher<Intent> activityResultLauncher){
        GrantPermission.openGallery(activityResultLauncher, activity);
    }
    public void grantPermissionCamera(ActivityResultLauncher<ScanOptions> launcher, ScanOptions options){
        GrantPermission.scanQRCode(launcher, options, activity);
    }

    private class DialogCurrencyHolder{
        Button btnAddCurrency, btnSubmit, btnCancel;
        EditText editCurrency;
        Spinner spinnerCurrency;
        CurrencyAdapterSpinner adapter;
        List<Currency> currenciesToAdd = new ArrayList<>();
    }

    private DialogCurrencyHolder holderCurrency;
    private Dialog dialogCurrency;
    public void createDialogCurrency(){
        if(dialogCurrency == null) {
            dialogCurrency = new Dialog(activity);
            dialogCurrency.setContentView(R.layout.dialog_list_currency);
        }

        Window window = dialogCurrency.getWindow();
        if(window == null)
            return;
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogCurrency.show();
        mappingForDialogCurrency(dialogCurrency);
        eventHanlderForDialogCurrency(dialogCurrency);
    }

    private void mappingForDialogCurrency(Dialog dialogCurrency){
        if(holderCurrency == null) {
            holderCurrency = new DialogCurrencyHolder();
            holderCurrency.btnAddCurrency = dialogCurrency.findViewById(R.id.btn_add_currency);
            holderCurrency.btnSubmit = dialogCurrency.findViewById(R.id.btn_submit);
            holderCurrency.btnCancel = dialogCurrency.findViewById(R.id.btn_cancel);
            holderCurrency.editCurrency = dialogCurrency.findViewById(R.id.edit_currency);
            holderCurrency.spinnerCurrency = dialogCurrency.findViewById(R.id.spinner_currency);
            holderCurrency.adapter = new CurrencyAdapterSpinner(activity, Currency.getDefaultCurrency(activity));
        }
        holderCurrency.spinnerCurrency.setAdapter(holderCurrency.adapter);
    }

    private void eventHanlderForDialogCurrency(Dialog dialog) {
        holderCurrency.btnAddCurrency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCurrencyToSpinner();
            }
        });

        holderCurrency.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCurrencyToDatabase(dialog);
            }
        });

        holderCurrency.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    public void setSelectionSpinnerCurrency(String currency){
        for(int i =0; i<holderCurrency.adapter.getCurrencies().size(); i++){
            if(holderCurrency.adapter.getCurrencies().get(i).getName().replace(" ","")
                    .equals(currency))
                holderCurrency.spinnerCurrency.setSelection(i);
        }
    }

    private void addCurrencyToSpinner(){
        String name = holderCurrency.editCurrency.getText().toString();
        if(name.replace(" ","").isEmpty()){
            Toast.makeText(activity, "Tiền tệ khi thêm không được bỏ trống", Toast.LENGTH_SHORT).show();
            return;
        }
        for(Currency currency : holderCurrency.adapter.getCurrencies()){
            if (currency.getName().replace(" ","").toLowerCase()
                    .equals(name.replace(" ","").toLowerCase())) {
                Toast.makeText(activity, "Loại tiền tệ này đã có", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        Currency currency = new Currency(name);
        holderCurrency.adapter.addItem(currency);
        Toast.makeText(activity, "Thêm thành công", Toast.LENGTH_SHORT).show();
        holderCurrency.editCurrency.setText("");
        holderCurrency.spinnerCurrency.setSelection(holderCurrency.adapter.getCount()-1);
        holderCurrency.currenciesToAdd.add(currency);
    }

    private void addCurrencyToDatabase(Dialog dialog){
        CurrencyDAO currencyDAO = db.getCurrencyDAO();
        Currency currencySelection ;
        for(Currency currency : holderCurrency.currenciesToAdd){
            currencyDAO.add(currency);
        }
        currencySelection = (Currency) holderCurrency.spinnerCurrency.getSelectedItem();
        for(Currency currency : Currency.getDefaultCurrency(activity)){
            if(currency.getName().replace(" ","")
                    .equals(currencySelection.getName().replace(" ",""))) {
                newProductView.setCurrencySelected(currency);
                dialog.dismiss();
            }
        }
    }

    private class DialogCategoryHolder{
//        List<Category> categoriesToSelect;
        ListView listViewCategory;
        ArrayAdapter<Category> adapter;
    }

    private Dialog dialogCategory;
    DialogCategoryHolder holderCategory;
    public void createDialogCategory(){
        CategoryDAO categoryDAO = db.getCategoryDAO();
        List<Category> categories = Category.getUnDefaultCategory(activity);
        if(categories == null || categories.size() == 0){
            Toast.makeText(activity, "Không có danh mục nào hiện tại", Toast.LENGTH_SHORT).show();
            return;
        }
        if(holderCategory != null && holderCategory.adapter != null){
            if(holderCategory.adapter.getCount() == 0){
                Toast.makeText(activity, "Không còn danh mục nào có thể chọn", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        if(dialogCategory == null) {
            dialogCategory = new Dialog(activity);
            dialogCategory.setContentView(R.layout.dialog_list_category);
        }

        Window window = dialogCategory.getWindow();
        if(window == null)
            return;
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogCategory.show();
        mappingForDialogCategory(dialogCategory);
    }

    private void mappingForDialogCategory(Dialog dialogCategory) {
        if(holderCategory == null) {
            holderCategory = new DialogCategoryHolder();
//            holderCategory.categoriesToSelect = Category.getDefaultCategory(activity);
            holderCategory.listViewCategory = dialogCategory.findViewById(R.id.listview_category);
            if (holderCategory.adapter == null) {
                holderCategory.adapter = new ArrayAdapter<Category>(
                        activity, android.R.layout.simple_list_item_1, Category.getUnDefaultCategory(activity));
            }

        }
        holderCategory.listViewCategory.setAdapter(holderCategory.adapter);
        holderCategory.listViewCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Category category = holderCategory.adapter.getItem(i);
                holderCategory.adapter.remove(category);
                newProductView.addCategoryToSpinner(category);
            }
        });
    }

    public void addCategoryToList(Category category){
        holderCategory.adapter.add(category);
        Toast.makeText(activity, "Đã xóa danh mục "+category.getName(), Toast.LENGTH_SHORT).show();
    }

    public void addProductToDatabase(Product product, List<Category> categories){
        CategoryWithProductDAO dao = db.getCategoryWithProductDAO();
        ProductDAO productDAO = db.getProductDAO();
        List<Product> products = productDAO.getAll();
        for(Product p : products){
            if(p.getName().replace(" ","").toLowerCase()
                .equals(product.getName().replace(" ","").toLowerCase())){
                newProductView.existsProductName(product, categories);
                return;
            }
            if(p.getCode().equals(product.getCode()) && !(product.getCode().isEmpty())){
                newProductView.existsProductCode();
                return;
            }
        }
        addProduct(product, categories);
    }

    public void addExistsProductName(Product product, List<Category> categories){
        addProduct(product, categories);
    }

    private void addProduct(Product product, List<Category> categories){
        CategoryWithProductDAO dao = db.getCategoryWithProductDAO();
        ProductDAO productDAO = db.getProductDAO();
        long id = productDAO.add(product);

        if(id<1) {
            newProductView.addProductFail();
            return;
        }

        for(Category category : categories){
            CategoryProductCrossRef c = new CategoryProductCrossRef();
            c.setProduct_id((int) id);
            c.setCategory_id(category.getId());
            dao.add(c);
        }

        if(categories.size()==0){
            CategoryProductCrossRef c = new CategoryProductCrossRef();
            c.setProduct_id((int) id);
            c.setCategory_id(2);
            dao.add(c);
        }

        if(holderCategory != null) {
            holderCategory.adapter = new ArrayAdapter<Category>(
                    activity, android.R.layout.simple_list_item_1, Category.getUnDefaultCategory(activity));
            holderCategory.listViewCategory.setAdapter(holderCategory.adapter);
        }
        newProductView.addProductSuccess();
    }

}
