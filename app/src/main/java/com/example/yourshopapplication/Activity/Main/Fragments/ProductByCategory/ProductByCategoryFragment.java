package com.example.yourshopapplication.Activity.Main.Fragments.ProductByCategory;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.example.yourshopapplication.Activity.Main.MainStoreActivity;
import com.example.yourshopapplication.Adapter.CategoryAdapterSpinner;
import com.example.yourshopapplication.Adapter.ProductAdapter;
import com.example.yourshopapplication.DAO.CategoryDAO;
import com.example.yourshopapplication.DAO.CategoryWithProductDAO;
import com.example.yourshopapplication.Database.StoreManagerDatabase;
import com.example.yourshopapplication.Model.Category;
import com.example.yourshopapplication.Model.CategoryWithProduct;
import com.example.yourshopapplication.Model.Product;
import com.example.yourshopapplication.R;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProductByCategoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductByCategoryFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProductByCategoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProductFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProductByCategoryFragment newInstance(String param1, String param2) {
        ProductByCategoryFragment fragment = new ProductByCategoryFragment();
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

    public static final String NAME = ProductByCategoryFragment.class.getName();
    private RecyclerView rclProduct;
    private View view;
    private MainStoreActivity activity;
    private ProductAdapter adapter;
    private CategoryAdapterSpinner adapterSpinner;
    private Spinner spinnerCategory;
    private StoreManagerDatabase db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_product, container, false);
        Mapping(view);

        Bundle bundle = getArguments();
        if(bundle!=null){
            Category category = (Category) bundle.get("category");
            if(category!=null){
                setItemForList(category);
            }
        }

        return view;
    }

    private void Mapping(View view){
        rclProduct = view.findViewById(R.id.rclview_product);
        adapter = new ProductAdapter();
        activity = (MainStoreActivity) getActivity();
        db = StoreManagerDatabase.getInstance(activity);
        spinnerCategory = view.findViewById(R.id.spinner_category);
        adapterSpinner = new CategoryAdapterSpinner(getCategories(), activity, CategoryAdapterSpinner.Type.Default);
    }

    private void setItemForList(Category category){
        //Item for recyclerview
        CategoryWithProductDAO categoryWithProductDAO = db.getCategoryWithProductDAO();
        CategoryWithProduct categoryWithProduct = categoryWithProductDAO.get(category.getId());
        adapter.setProducts(categoryWithProduct.getProducts());
        adapter.setItemClickListener(new ProductAdapter.onItemClickListener() {
            @Override
            public void onClickItem(Product product) {
                activity.goToDetailFragment(product);
            }
        });
        rclProduct.setAdapter(adapter);
        GridLayoutManager layoutManager = new GridLayoutManager(activity, 2);
        rclProduct.setLayoutManager(layoutManager);

        //Item for spinner
        spinnerCategory.setAdapter(adapterSpinner);
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Category category1 = (Category) adapterSpinner.getItem(i);
                if(category1.getType().equals(Category.CategoryType.Default) && category1.getName().equals("Home"))
                    activity.onBackPressed();
                adapter.setProducts(categoryWithProductDAO.get(category1.getId()).getProducts());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        for(int i=0; i<adapterSpinner.getCount(); i++){
            Category category1 = (Category) adapterSpinner.getItem(i);
            if(category1.getId() == category.getId())
                category = (Category) adapterSpinner.getItem(i);
        }
        spinnerCategory.setSelection(adapterSpinner.getPosition(category));
    }

    private List<Category> getCategories(){
        CategoryDAO categoryDAO = db.getCategoryDAO();
        List<Category> categories = categoryDAO.getAll();
        return categories;
    }

}