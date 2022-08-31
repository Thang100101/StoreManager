package com.example.yourshopapplication.Activity.Main.Fragments.NewCategory;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.yourshopapplication.Activity.Main.MainStoreActivity;
import com.example.yourshopapplication.Adapter.ProductAdapterListView;
import com.example.yourshopapplication.DAO.CategoryDAO;
import com.example.yourshopapplication.DAO.CategoryWithProductDAO;
import com.example.yourshopapplication.DAO.ProductDAO;
import com.example.yourshopapplication.Database.StoreManagerDatabase;
import com.example.yourshopapplication.Model.Category;
import com.example.yourshopapplication.Model.CategoryProductCrossRef;
import com.example.yourshopapplication.Model.Product;
import com.example.yourshopapplication.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewCategoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewCategoryFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NewCategoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewCategoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewCategoryFragment newInstance(String param1, String param2) {
        NewCategoryFragment fragment = new NewCategoryFragment();
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
    private StoreManagerDatabase db;
    private ProductDAO productDAO;
    private CategoryDAO categoryDAO;
    private CategoryWithProductDAO categoryWithProductDAO;
    private EditText editName;
    private Button btnAddProduct, btnSubmit;
    private Dialog dialog;
    private List<Product> toSelectProducts, haveSelectProducts;
    private ProductAdapterListView adapterToSelect, adapterHaveSelect;
    private List<Category> categories;
    private ListView listHaveSelectProduct;
    public static final String NAME = NewCategoryFragment.class.getName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_new_category, container, false);
        Mapping(view);
        HandlerEvent();
        return view;
    }

    private void Mapping(View view){
        activity = (MainStoreActivity) getActivity();
        db = StoreManagerDatabase.getInstance(activity);
        productDAO = db.getProductDAO();
        categoryDAO = db.getCategoryDAO();
        categoryWithProductDAO = db.getCategoryWithProductDAO();
        editName = view.findViewById(R.id.edit_name);
        btnAddProduct = view.findViewById(R.id.btn_add_product);
        btnSubmit = view.findViewById(R.id.btn_submit);
        listHaveSelectProduct = view.findViewById(R.id.listview_product);
        dialog = new Dialog(activity);
        categories = Category.getDefaultCategory(activity);
        setItemForList();
    }

    private void setItemForList(){
        toSelectProducts = productDAO.getAll();
        haveSelectProducts = new ArrayList<>();
        adapterToSelect = new ProductAdapterListView(toSelectProducts, activity, ProductAdapterListView.Type.Default);
        adapterHaveSelect = new ProductAdapterListView(haveSelectProducts, activity, ProductAdapterListView.Type.CanDelete);
        listHaveSelectProduct.setAdapter(adapterHaveSelect);
    }

    private void HandlerEvent() {
        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editName.getText().toString();
                if(!checkCategory(name)){
                    return;
                }
                Category category = new Category(name);
                category.setType(Category.CategoryType.UnDefault);
                int cate_id = (int) categoryDAO.add(category);
                categories = Category.getDefaultCategory(activity);
                for(Product product : haveSelectProducts){
                    CategoryProductCrossRef categoryProductCrossRef = new CategoryProductCrossRef();
                    categoryProductCrossRef.setCategory_id(cate_id);
                    categoryProductCrossRef.setProduct_id(product.getId());
                    categoryWithProductDAO.add(categoryProductCrossRef);
                }
                editName.setText("");
                adapterHaveSelect.clear();

                toSelectProducts = productDAO.getAll();
                adapterToSelect.setProducts(toSelectProducts);
                Toast.makeText(activity, "Thêm thành công", Toast.LENGTH_SHORT).show();
            }
        });

        adapterHaveSelect.setDeleteClickListener(new ProductAdapterListView.onDeleteClickListener() {
            @Override
            public void onClickDelete(Product product) {
                adapterHaveSelect.removeItem(product);
                adapterToSelect.addItem(product);
            }
        });

    }

    private boolean checkCategory(String name){
        if(name.replace(" ","").isEmpty()) {
            Toast.makeText(activity, "Tên không được để trống", Toast.LENGTH_SHORT).show();
            return false;
        }
        for(Category category : categories){
            if(category.getName().equals(name)){
                Toast.makeText(activity, "Tên danh mục đã tồn tại", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    private void openDialog(){
        if(toSelectProducts == null || toSelectProducts.size() == 0){
            Toast.makeText(activity, "Không có sản phẩm nào", Toast.LENGTH_SHORT).show();
            return;
        }
        dialog.setContentView(R.layout.dialog_list_product);
        ListView listToSelectProduct = dialog.findViewById(R.id.listview_product);
        listToSelectProduct.setAdapter(adapterToSelect);
        listToSelectProduct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Product product = (Product) adapterToSelect.getItem(i);
                if(product == null)
                    return;
                adapterHaveSelect.addItem(product);
                adapterToSelect.removeItem(product);

                Toast.makeText(activity, "Thêm thành công", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        Window window = dialog.getWindow();
        if(window == null)
            return;
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    @Override
    public void onResume() {
        adapterHaveSelect.clear();
        toSelectProducts = productDAO.getAll();
        adapterToSelect.setProducts(toSelectProducts);
        editName.setText("");
        super.onResume();
    }
}