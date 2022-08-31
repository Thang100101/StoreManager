package com.example.yourshopapplication.Activity.Main.Fragments.NewProduct;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yourshopapplication.Activity.Capture.CaptureA;
import com.example.yourshopapplication.Activity.Main.MainStoreActivity;
import com.example.yourshopapplication.Adapter.CategoryAdapterSpinner;
import com.example.yourshopapplication.DAO.CategoryDAO;
import com.example.yourshopapplication.DAO.CategoryWithProductDAO;
import com.example.yourshopapplication.DAO.ProductDAO;
import com.example.yourshopapplication.Database.StoreManagerDatabase;
import com.example.yourshopapplication.Model.Category;
import com.example.yourshopapplication.Model.Currency;
import com.example.yourshopapplication.Model.Product;
import com.example.yourshopapplication.R;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanIntentResult;
import com.journeyapps.barcodescanner.ScanOptions;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewProductFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewProductFragment extends Fragment implements NewProductView{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NewProductFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewProductFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewProductFragment newInstance(String param1, String param2) {
        NewProductFragment fragment = new NewProductFragment();
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

    private View view;
    private MainStoreActivity activity;
    private Button btnSubmit, btnCancel, btnChooseCategory, btnChooseCurrency, btnScan;
    private EditText editName, editPrice, editAmount, editQRCode;
    private TextView txtCurrency;
    private RadioGroup rdGroupType;
    private RadioButton rdMass, rdAmount;
    private ImageView imgProduct;
    private Dialog dialogCategory;
    private StoreManagerDatabase db;
    private ProductDAO productDAO;
    private CategoryWithProductDAO categoryWithProductDAO;
    private CategoryDAO categoryDAO;
    private Spinner spinnerCategory;
    private CategoryAdapterSpinner adapter;
    private NewProductPresenter newProductPresenter;
    private ScrollView scrollView;
    public static final String NAME = NewProductFragment.class.getName();
    private ActivityResultLauncher<ScanOptions> launcher = registerForActivityResult(new ScanContract(), new ActivityResultCallback<ScanIntentResult>() {
        @Override
        public void onActivityResult(ScanIntentResult result) {
            if(result.getContents() != null){
                editQRCode.setText(result.getContents());
            }
        }
    });

    private ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_new_product, container, false);
        Mapping(view);
        HandlerEvent();
        setItemForList();
        return view;
    }

    private void Mapping(View view){
        activity = (MainStoreActivity) getActivity();
        scrollView = view.findViewById(R.id.scroll_view);
        btnSubmit = view.findViewById(R.id.btn_submit);
        btnCancel = view.findViewById(R.id.btn_cancel);
        btnChooseCategory = view.findViewById(R.id.btn_choose_category);
        btnChooseCurrency = view.findViewById(R.id.btn_choose_currency);
        btnScan = view.findViewById(R.id.btn_scan);
        txtCurrency = view.findViewById(R.id.txt_currency);
        editName = view.findViewById(R.id.edit_name);
        editPrice = view.findViewById(R.id.edit_price);
        editAmount = view.findViewById(R.id.edit_amount);
        editQRCode = view.findViewById(R.id.edit_code);
        rdGroupType = view.findViewById(R.id.rdg_type);
        rdAmount = view.findViewById(R.id.rd_type_amount);
        rdMass = view.findViewById(R.id.rd_type_mass);
        imgProduct = view.findViewById(R.id.img_product);
        dialogCategory = new Dialog(activity);
        spinnerCategory = view.findViewById(R.id.spinner_category);

        db = StoreManagerDatabase.getInstance(activity);
        productDAO = db.getProductDAO();
        categoryWithProductDAO = db.getCategoryWithProductDAO();
        categoryDAO = db.getCategoryDAO();

        newProductPresenter = new NewProductPresenter(this, activity);

        if(adapter == null) {
            adapter = new CategoryAdapterSpinner(categoriesHaveSelect, activity, CategoryAdapterSpinner.Type.CanDelete);
            spinnerCategory.setAdapter(adapter);

            adapter.setDeleteClickListener(new CategoryAdapterSpinner.onDeleteClickListener() {
                @Override
                public void onClickDelete(Category category) {
                    newProductPresenter.addCategoryToList(category);
                    adapter.remove(category);
                }
            });
        }
    }

    private void setItemForList(){
        //Item for spinner


    }

    private void HandlerEvent(){
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addProduct();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btnChooseCurrency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogCurrency();
            }
        });

        btnChooseCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogCategory();
            }
        });

        imgProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newProductPresenter.grantPermissionImage(activityResultLauncher);
            }
        });

        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openScanQRCode();
            }
        });
    }

    private void openScanQRCode(){
        ScanOptions options = new ScanOptions();
        options.setPrompt("Scan Product");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureA.class);
        newProductPresenter.grantPermissionCamera(launcher, options);
    }


    private void addProduct(){
        if(editName.getText().toString().replace(" ","").isEmpty()){
            Toast.makeText(activity, "Tên sản phẩm không được để trống", Toast.LENGTH_SHORT).show();
            return;
        }
        Product product = new Product(editName.getText().toString());
        Bitmap bitmap = ((BitmapDrawable)imgProduct.getDrawable()).getBitmap();
        product.setImageByBitmap(bitmap);
        product.setCurrency(txtCurrency.getText().toString());
        String price = editPrice.getText().toString();
        if(price.replace(" ","").isEmpty()){
            Toast.makeText(activity, "Giá cả không được để trống", Toast.LENGTH_SHORT).show();
            return;
        }
        product.setPrice(Double.parseDouble(price));
        String amount = editAmount.getText().toString();
        if(amount.replace(" ","").isEmpty()){
            Toast.makeText(activity, "Số lượng của món hàng không được để trống", Toast.LENGTH_SHORT).show();
            return;
        }
        product.setQuantity(Integer.parseInt(amount));
        if(rdAmount.isChecked())
            product.setType(Product.ProductType.Amount);
        else
            product.setType(Product.ProductType.Mass);

        product.setCode(editQRCode.getText().toString());
        newProductPresenter.addProductToDatabase(product, categoriesHaveSelect);
    }

    //Dialog Currency

    Currency currencySelection;
    private void openDialogCurrency(){
        newProductPresenter.createDialogCurrency();
        newProductPresenter.setSelectionSpinnerCurrency(txtCurrency.getText().toString());
    }


    @Override
    public void setCurrencySelected(Currency currency) {
        currencySelection = currency;
        txtCurrency.setText(currencySelection.getName());
    }


    //Dialog Category
    List<Category> categoriesHaveSelect = new ArrayList<>();
    private void openDialogCategory(){
        newProductPresenter.createDialogCategory();
    }

    @Override
    public void addCategoryToSpinner(Category category) {
        adapter.add(category);
        Toast.makeText(activity, "Đã thêm danh mục "+category.getName(), Toast.LENGTH_SHORT).show();
    }


    @Override
    public void existsProductName(Product product, List<Category> categories) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
        dialog.setTitle("Thông báo!!");
        dialog.setMessage("Tên sản phẩm này đã tồn tại\n"+"Bạn có chắc muốn thêm chứ?");
        dialog.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                newProductPresenter.addExistsProductName(product, categories);
            }
        });
        dialog.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                return;
            }
        });
        dialog.show();

    }

    @Override
    public void addProductSuccess() {
        Toast.makeText(activity, "Thêm sản phẩm thành công", Toast.LENGTH_SHORT).show();
        reset();
    }

    @Override
    public void addProductFail() {
        Toast.makeText(activity, "Thêm sản phẩm thất bại", Toast.LENGTH_SHORT).show();
        reset();
    }

    @Override
    public void existsProductCode() {
        editQRCode.requestFocus();
        scrollView.scrollTo(0,0);
        Toast.makeText(activity, "Mã sản phẩm đã tồn tại", Toast.LENGTH_SHORT).show();
    }

    private void reset(){
        scrollView.scrollTo(0,0);
        editName.setText("");
        editPrice.setText("");
        editAmount.setText("0");
        editQRCode.setText("");
        Picasso.get().load(R.drawable.icon_image).into(imgProduct);
        adapter.setCategories(new ArrayList<Category>());
    }
}