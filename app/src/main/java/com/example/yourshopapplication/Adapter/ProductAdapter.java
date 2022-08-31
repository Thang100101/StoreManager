package com.example.yourshopapplication.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yourshopapplication.Model.Product;
import com.example.yourshopapplication.R;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductHolder>{

    List<Product> products;
    onItemClickListener listener;

    public interface onItemClickListener{
        public void onClickItem(Product product);
    }

    public void setProducts(List<Product> products) {
        this.products = products;
        notifyDataSetChanged();
    }

    public void setItemClickListener(onItemClickListener listener){
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_product, parent, false);
        return new ProductHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductHolder holder, int position) {
        Product product = products.get(position);
        if(product!=null){
            holder.txtName.setText(product.getName());

            holder.imgProduct.setImageBitmap(product.getImageBitmap());
            holder.imgProduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener!=null)
                        listener.onClickItem(product);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if(products==null)
            return 0;
        else
            return products.size();
    }

    public class ProductHolder extends RecyclerView.ViewHolder{
        private TextView txtName;
        private ImageView imgProduct;
        public ProductHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txt_name);
            imgProduct = itemView.findViewById(R.id.img_product);

        }
    }
}
