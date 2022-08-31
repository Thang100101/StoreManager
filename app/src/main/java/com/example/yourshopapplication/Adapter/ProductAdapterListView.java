package com.example.yourshopapplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yourshopapplication.Model.Product;
import com.example.yourshopapplication.R;

import java.util.List;

public class ProductAdapterListView extends BaseAdapter {
    private List<Product> products;
    private Context context;
    private LayoutInflater inflater;
    private Type type;
    private onDeleteClickListener deleteClickListener;

    public ProductAdapterListView(List<Product> products, Context context, Type type) {
        this.products = products;
        this.context = context;
        this.type = type;
        inflater = LayoutInflater.from(context);
    }

    public void setProducts(List<Product> products) {
        this.products = products;
        notifyDataSetChanged();
    }

    public List<Product> getProducts() {
        return products;
    }

    public void addItem(Product product){
        this.products.add(product);
        notifyDataSetChanged();
    }

    public void clear(){
        this.products.clear();
        notifyDataSetChanged();
    }

    public void setDeleteClickListener(onDeleteClickListener listener){
        this.deleteClickListener = listener;
    }

    public void removeItem(Product product){
        this.products.remove(product);
        notifyDataSetChanged();
    }


    public enum Type{
        Default, CanDelete;
    }

    public interface onDeleteClickListener{
        public void onClickDelete(Product product);
    }

    @Override
    public int getCount() {
        if(products == null)
            return 0;
        return products.size();
    }

    @Override
    public Object getItem(int i) {
        return products.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.list_product_2, null);
        TextView txtName = view.findViewById(R.id.txt_product);
        ImageView imgProduct = view.findViewById(R.id.img_product);
        ImageView imgDelete = view.findViewById(R.id.img_delete);
        imgDelete.setVisibility(View.GONE);
        imgProduct.setImageBitmap(products.get(i).getImageBitmap());
        txtName.setText(products.get(i).getName());
        if(type.equals(Type.CanDelete)) {
            imgDelete.setVisibility(View.VISIBLE);
            imgDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteClickListener.onClickDelete(products.get(i));
                }
            });
        }
        return view;
    }
}
