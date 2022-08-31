package com.example.yourshopapplication.Activity.Main.Fragments.ListCategory;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.example.yourshopapplication.Activity.Main.MainStoreActivity;
import com.example.yourshopapplication.Adapter.CategoryAdapter;
import com.example.yourshopapplication.Adapter.CategoryAdapterSpinner;
import com.example.yourshopapplication.Adapter.ProductAdapter;
import com.example.yourshopapplication.DAO.CategoryDAO;
import com.example.yourshopapplication.Database.StoreManagerDatabase;
import com.example.yourshopapplication.Model.Category;
import com.example.yourshopapplication.Model.Product;
import com.example.yourshopapplication.R;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CategoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CategoryFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CategoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CategoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CategoryFragment newInstance(String param1, String param2) {
        CategoryFragment fragment = new CategoryFragment();
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

    private RecyclerView rclCategory;
    private View view;
    private MainStoreActivity activity;
    private Spinner spinnerCategory;
    private CategoryAdapter adapter;
    private CategoryAdapterSpinner adapterSpinner;
    private StoreManagerDatabase db;
    private boolean haveCreate = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("Back to fragment: ", "Hello");
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_category, container, false);
        Mapping(view);
        setItemForList();
        haveCreate = true;
        return view;
    }
    private void Mapping(View view){
        rclCategory = view.findViewById(R.id.rclview_category);
        activity = (MainStoreActivity) getActivity();
        db = StoreManagerDatabase.getInstance(activity);
        spinnerCategory = view.findViewById(R.id.spinner_category);
        adapter = new CategoryAdapter(activity);
        adapterSpinner = new CategoryAdapterSpinner(getCategories(), activity, CategoryAdapterSpinner.Type.Default);
    }

    private void setItemForList(){
        //Item for RecyclerView
        adapter.setCategories(getCategories());
        adapter.setProductClickListener(new ProductAdapter.onItemClickListener() {
            @Override
            public void onClickItem(Product product) {
                activity.goToDetailFragment(product);
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        rclCategory.setLayoutManager(layoutManager);
        rclCategory.setAdapter(adapter);

        //Item for Spinner
        spinnerCategory.setAdapter(adapterSpinner);
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Category category = (Category) adapterSpinner.getItem(i);
                if(category.getType().equals(Category.CategoryType.Default) && category.getName().equals("Home"))
                    return;
                activity.goToListProductForCategoryFragment(category);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    @Override
    public void onResume() {
        spinnerCategory.setSelection(0);
        super.onResume();
    }

    private List<Category> getCategories(){
        CategoryDAO categoryDAO = db.getCategoryDAO();
        List<Category> categories = categoryDAO.getAll();
        return categories;
    }
}