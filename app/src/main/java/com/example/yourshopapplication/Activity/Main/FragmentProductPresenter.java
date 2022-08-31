package com.example.yourshopapplication.Activity.Main;

import android.os.Bundle;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.yourshopapplication.Activity.Main.Fragments.ListCategory.CategoryFragment;
import com.example.yourshopapplication.Activity.Main.Fragments.DetailProduct.DetailProductFragment;
import com.example.yourshopapplication.Activity.Main.Fragments.NewCategory.NewCategoryFragment;
import com.example.yourshopapplication.Activity.Main.Fragments.NewProduct.NewProductFragment;
import com.example.yourshopapplication.Activity.Main.Fragments.ProductByCategory.ProductByCategoryFragment;
import com.example.yourshopapplication.Model.Category;
import com.example.yourshopapplication.Model.Product;
import com.example.yourshopapplication.Model.User;
import com.example.yourshopapplication.R;
import com.example.yourshopapplication.Activity.Main.FragmentView;

public class FragmentProductPresenter {
    private FragmentView view;
    private FragmentManager fragmentManager;

    public FragmentProductPresenter(FragmentView view, FragmentManager fragmentManager) {
        this.view = view;
        this.fragmentManager = fragmentManager;
    }

    CategoryFragment fragment = new CategoryFragment();

    public void loadMainFragment(){
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.frame, fragment);
        transaction.commit();
    }

    public void removeFragment(){
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.remove(fragment);
        transaction.commit();
    }

    public void loadDetailFragment(Product product, User currentUser){
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        DetailProductFragment fragment = new DetailProductFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("product", product);
        bundle.putSerializable("user", currentUser);
        fragment.setArguments(bundle);
        transaction.replace(R.id.frame, fragment);
        transaction.addToBackStack(DetailProductFragment.NAME);
        transaction.commit();
        view.changeFragmentSuccess();
    }

    public void loadProductByCategoryFragment(Category category){
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        ProductByCategoryFragment fragment = new ProductByCategoryFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("category", category);
        fragment.setArguments(bundle);
        transaction.replace(R.id.frame, fragment);
        transaction.addToBackStack(ProductByCategoryFragment.NAME);
        transaction.commit();
        view.changeFragmentSuccess();
    }

    public void loadNewProductFragment(){
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        NewProductFragment fragment = new NewProductFragment();
        transaction.replace(R.id.frame, fragment);
        transaction.addToBackStack(NewProductFragment.NAME);
        transaction.commit();
        view.changeFragmentSuccess();
    }

    public void loadNewCategoryFragment(){
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        NewCategoryFragment fragment = new NewCategoryFragment();
        transaction.replace(R.id.frame, fragment);
        transaction.addToBackStack(NewCategoryFragment.NAME);
        transaction.commit();
        view.changeFragmentSuccess();
    }
}
