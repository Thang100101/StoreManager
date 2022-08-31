package com.example.yourshopapplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yourshopapplication.DAO.CategoryWithProductDAO;
import com.example.yourshopapplication.Database.StoreManagerDatabase;
import com.example.yourshopapplication.Model.Category;
import com.example.yourshopapplication.Model.Product;
import com.example.yourshopapplication.R;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryHodler>{

    private List<Category> categories;
    private Context context;
    private ProductAdapter adapter;
    private onCategoryClickListener listener;
    private ProductAdapter.onItemClickListener productClickListener;
    private CategoryWithProductDAO categoryWithProductDAO;
    private StoreManagerDatabase db;

    public CategoryAdapter(Context context) {
        this.context = context;
        db = StoreManagerDatabase.getInstance(context);
        categoryWithProductDAO = db.getCategoryWithProductDAO();
    }

    public void setCategories(List<Category> categories){
        this.categories = categories;
        notifyDataSetChanged();
    }
    public void setProductClickListener(ProductAdapter.onItemClickListener listener){
        this.productClickListener = listener;
    }

    public void setOnClickCategoryListener(onCategoryClickListener listener){
        this.listener = listener;
    }

    public interface onCategoryClickListener{
        public void onClickCategory(Category category);
    }

    @NonNull
    @Override
    public CategoryHodler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_category, parent, false);
        return new CategoryHodler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryHodler holder, int position) {
        Category category = categories.get(position);
        if(category == null)
            return;
        if(category.getType().equals(Category.CategoryType.Default) && category.getName().equals("Home")){
            holder.txtName.setVisibility(View.GONE);
            holder.rclProduct.setVisibility(View.GONE);
            return;
        }
        if(category.getType().equals(Category.CategoryType.Default) && category.getName().equals("None")){
            if(categoryWithProductDAO.get(category.getId()).getProducts().size()==0){
                holder.txtName.setVisibility(View.GONE);
                holder.rclProduct.setVisibility(View.GONE);
                return;
            }
        }

        holder.txtName.setText(category.getName());
        adapter = new ProductAdapter();
        List<Product> products = categoryWithProductDAO.get(category.getId()).getProducts();

        adapter.setProducts(products);
        adapter.setItemClickListener(this.productClickListener);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        holder.rclProduct.setLayoutManager(layoutManager);
        holder.rclProduct.setAdapter(adapter);

    }

    @Override
    public int getItemCount() {
        if(categories == null)
            return 0;
        else
            return categories.size();
    }

    public class CategoryHodler extends RecyclerView.ViewHolder{
        private TextView txtName;
        private RecyclerView rclProduct;
        public CategoryHodler(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txt_category);
            rclProduct = itemView.findViewById(R.id.rclview_product);
        }
    }
}
