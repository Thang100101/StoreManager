package com.example.yourshopapplication.Activity.Main.Fragments.DetailProduct;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yourshopapplication.Activity.Capture.CaptureA;
import com.example.yourshopapplication.Activity.Main.MainStoreActivity;
import com.example.yourshopapplication.Activity.Payment.Cart;
import com.example.yourshopapplication.DAO.CurrencyDAO;
import com.example.yourshopapplication.DAO.ProductDAO;
import com.example.yourshopapplication.DAO.UserWithPermissionDAO;
import com.example.yourshopapplication.Database.StoreManagerDatabase;
import com.example.yourshopapplication.Model.Currency;
import com.example.yourshopapplication.Model.OrderDetail;
import com.example.yourshopapplication.Model.Permission;
import com.example.yourshopapplication.Model.Product;
import com.example.yourshopapplication.Model.User;
import com.example.yourshopapplication.Model.UserPermissionEnum;
import com.example.yourshopapplication.Model.UserWithPermission;
import com.example.yourshopapplication.R;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanIntentResult;
import com.journeyapps.barcodescanner.ScanOptions;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailProductFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailProductFragment extends Fragment implements DetailProductView{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DetailProductFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DetailProductFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetailProductFragment newInstance(String param1, String param2) {
        DetailProductFragment fragment = new DetailProductFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    public static final String NAME = DetailProductFragment.class.getName();
    private EditText editName, editPrice, editQuantity, editCode;
    private ImageView imgProduct;
    private Button btnEdit, btnAddToCart, btnBack, btnCancel, btnScan;
    private MainStoreActivity activity;
    private Product product;
    private User currentUser;
    private DetailProductPresenter productPresenter;
    private TextView txtType, txtCurrency;

    private ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == activity.RESULT_OK){
                        Intent intent = result.getData();
                        try {
                            Picasso.get().load(intent.getData()).into(imgProduct);
                            throw new IOException();
                        }catch (IOException e){

                        }

                    }
                }
            }
    );

    private ActivityResultLauncher<ScanOptions> launcherScanOptions = registerForActivityResult(new ScanContract(),
            new ActivityResultCallback<ScanIntentResult>() {
        @Override
        public void onActivityResult(ScanIntentResult result) {
            if(result.getContents() != null){
                editCode.setText(result.getContents());
            }
        }
    });

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail_product, container, false);
        Mapping(view);

        Bundle bundle = getArguments();
        if(bundle!=null){
            product = (Product) bundle.get("product");
            currentUser = (User) bundle.get("user");
            if(product!=null) {
                loadProduct();
            }
            if(currentUser != null){
                loadPermissionUser();
            }else{
                btnEdit.setVisibility(View.GONE);
            }
        }

        return view;
    }


    private void Mapping(View view) {
        activity = (MainStoreActivity) getActivity();
        productPresenter = new DetailProductPresenter(this, activity);
        editName = view.findViewById(R.id.edit_name);
        editPrice = view.findViewById(R.id.edit_price);
        editQuantity = view.findViewById(R.id.edit_quantity);
        editCode = view.findViewById(R.id.edit_code);
        imgProduct = view.findViewById(R.id.img_product);
        btnBack = view.findViewById(R.id.btn_back);
        btnAddToCart = view.findViewById(R.id.btn_add_to_cart);
        btnEdit = view.findViewById(R.id.btn_edit);
        btnCancel = view.findViewById(R.id.btn_cancel);
        btnScan = view.findViewById(R.id.btn_scan);
        txtType = view.findViewById(R.id.txt_type);
        txtCurrency = view.findViewById(R.id.txt_currency);
        txtType.setEnabled(false);
        txtCurrency.setEnabled(false);
        btnCancel.setVisibility(View.GONE);
        btnScan.setVisibility(View.GONE);
        btnEdit.setTag("read");

        eventHandler();
    }

    private void loadPermissionUser(){
        UserWithPermissionDAO uwpDAO = StoreManagerDatabase.getInstance(activity).getUserWithPermissionDAO();
        UserWithPermission uwp = uwpDAO.get(currentUser.getId());
        for(Permission p : uwp.getPermissions()){
            if(p.getType().equals(UserPermissionEnum.UpdateProduct) || p.getType().equals(UserPermissionEnum.Admin)){
                Toast.makeText(activity, "Permission Grant", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        btnEdit.setVisibility(View.GONE);
    }

    private void loadProduct(){
        product = StoreManagerDatabase.getInstance(activity).getProductDAO().get(product.getId());
        editName.setText(product.getName());
        imgProduct.setImageBitmap(product.getImageBitmap());
        editPrice.setText(product.getPrice()+"");
        editQuantity.setText(product.getQuantity()+"");
        txtType.setText(product.getType().toString());
        txtCurrency.setText(product.getCurrency());
        editCode.setText(product.getCode());
    }

    private void clearForcus(){
        editName.clearFocus();
        editPrice.clearFocus();
        editQuantity.clearFocus();
        editCode.clearFocus();
    }

    private void eventHandler(){
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.onBackPressed();
            }
        });

        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(product != null) {
                    productPresenter.addProductToCart(product);
                }
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editProduct();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelEdit();
            }
        });

        txtType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogType();
            }
        });
        txtCurrency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogCurrency();
            }
        });

        imgProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productPresenter.getImageFromGallery(launcher, activity);
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
                productPresenter.scanQRCode(launcherScanOptions, options, activity);
            }
        });
    }

    private void openDialogType(){
        Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.dialog_list_category);
        TextView txtTitle, txtMessage;
        ListView listView;
        txtTitle = dialog.findViewById(R.id.txt_title);
        txtMessage = dialog.findViewById(R.id.txt_message);
        listView = dialog.findViewById(R.id.listview_category);

        txtTitle.setText("Chọn loại cho sản phẩm");
        txtMessage.setText("Click để chọn loại cho sản phẩm");
        String [] types = {"Khối lượng (kg)","Số lượng (cái)"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, types);
        listView.setAdapter(adapter);
        Window window = dialog.getWindow();
        if(window == null)
            return;
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(i == 0){
                    product.setType(Product.ProductType.Mass);
                    txtType.setText("Mass");
                    setQuantity(product.getType());
                    dialog.dismiss();
                }else{
                    product.setType(Product.ProductType.Amount);
                    txtType.setText("Amount");
                    setQuantity(product.getType());
                    dialog.dismiss();
                }
            }
        });
    }

    private void openDialogCurrency(){
        Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.dialog_list_category);
        TextView txtTitle, txtMessage;
        ListView listView;
        txtTitle = dialog.findViewById(R.id.txt_title);
        txtMessage = dialog.findViewById(R.id.txt_message);
        listView = dialog.findViewById(R.id.listview_category);

        txtTitle.setText("Chọn tiền tệ cho sản phẩm");
        txtMessage.setText("Click để chọn tiền tệ cho sản phẩm");
        CurrencyDAO currencyDAO = StoreManagerDatabase.getInstance(activity).getCurrencyDAO();
        List<Currency> currencies = currencyDAO.getAll();
        ArrayAdapter<Currency> adapter = new ArrayAdapter<Currency>(activity, android.R.layout.simple_list_item_1, currencies);
        listView.setAdapter(adapter);
        Window window = dialog.getWindow();
        if(window == null)
            return;
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                product.setCurrency(adapter.getItem(i).toString());
                txtCurrency.setText(adapter.getItem(i).toString());
                dialog.dismiss();
            }
        });
    }

    private void setQuantity(Product.ProductType type){
        switch (type){
            case Mass:
                try{
                    double quantity = Double.parseDouble(editQuantity.getText().toString());
                    quantity = (double) Math.round(quantity *100)/100;
                    product.setQuantity(quantity);
                    editQuantity.setText(quantity+"");
                }catch (Exception e){
                    loadProduct();
                }
                break;
            case Amount:
                try{
                    double quantity = Double.parseDouble(editQuantity.getText().toString());
                    int amount = (int) quantity;
                    quantity = (double) amount;
                    product.setQuantity(quantity);
                    editQuantity.setText(quantity+"");
                }catch (Exception e){
                    loadProduct();
                }
                break;
        }
    }

    private void cancelEdit(){
        btnEdit.setTag("read");
        editName.setEnabled(false);
        editPrice.setEnabled(false);
        editQuantity.setEnabled(false);
        btnEdit.setBackgroundResource(R.drawable.icon_edit);
        btnCancel.setVisibility(View.GONE);
        txtType.setEnabled(false);
        txtCurrency.setEnabled(false);
        editCode.setEnabled(false);
        btnScan.setVisibility(View.GONE);
        loadProduct();
        clearForcus();
    }

    private void editProduct(){
        if(btnEdit.getTag().toString().equals("read")){
            btnEdit.setTag("write");
            btnEdit.setBackgroundResource(R.drawable.icon_done);
            btnCancel.setVisibility(View.VISIBLE);
            btnScan.setVisibility(View.VISIBLE);
            clearForcus();
        }
        else{
            btnEdit.setTag("read");
            btnEdit.setBackgroundResource(R.drawable.icon_edit);
            btnCancel.setVisibility(View.GONE);
            btnScan.setVisibility(View.GONE);
            updateProduct();
        }
        editName.setEnabled(!editName.isEnabled());
        editPrice.setEnabled(!editPrice.isEnabled());
        editQuantity.setEnabled(!editQuantity.isEnabled());
        txtType.setEnabled(!txtType.isEnabled());
        txtCurrency.setEnabled(!txtCurrency.isEnabled());
        editCode.setEnabled(!editCode.isEnabled());
    }
    private void updateProduct(){
        try{
            setQuantity(product.getType());
            double price = Double.parseDouble(editPrice.getText().toString());
            double quantity = Double.parseDouble(editQuantity.getText().toString());
            String name = editName.getText().toString();
            Bitmap bitmap = ((BitmapDrawable) imgProduct.getDrawable()).getBitmap();
            product.setName(name);
            product.setPrice(price);
            product.setQuantity(quantity);
            productPresenter.updateProduct(product);
        }catch (Exception e){
            loadProduct();
            clearForcus();
        }
    }

    private void keepEdit(){
        btnEdit.setTag("write");
        btnEdit.setBackgroundResource(R.drawable.icon_done);
        btnCancel.setVisibility(View.VISIBLE);
        editName.setEnabled(true);
        editPrice.setEnabled(true);
        editQuantity.setEnabled(true);
        txtType.setEnabled(true);
        txtCurrency.setEnabled(true);
    }

    @Override
    public void addProductToCartSuccess(Product product) {
        Toast.makeText(activity, "Đã thêm "+product.getName()+" vào giỏ hàng", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateProductSuccess(Product product) {
        Toast.makeText(activity, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
        loadProduct();
        clearForcus();
    }

    @Override
    public void updateProductFail() {
        Toast.makeText(activity, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
        loadProduct();
        clearForcus();
    }

    @Override
    public void existsProductName(Product product) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
        dialog.setMessage("Đã tồn tại tên sản phẩm này\n"+"Bạn chắc chắn muốn sửa không?");
        dialog.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                productPresenter.updateExistsProductName(product);
            }
        });
        dialog.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                keepEdit();
                return;
            }
        });
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                keepEdit();
                return;
            }
        });
        dialog.show();
    }
}