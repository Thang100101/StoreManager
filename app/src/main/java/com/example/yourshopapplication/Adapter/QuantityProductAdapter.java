package com.example.yourshopapplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yourshopapplication.DAO.ProductDAO;
import com.example.yourshopapplication.Database.StoreManagerDatabase;
import com.example.yourshopapplication.Model.Product;
import com.example.yourshopapplication.R;

import java.util.ArrayList;
import java.util.List;

public class QuantityProductAdapter extends RecyclerView.Adapter<QuantityProductAdapter.QuantityProductHolder>
        implements Filterable {

    private List<Product> products;
    private List<Product> productNew;
    private Context context;

    public QuantityProductAdapter(List<Product> products, Context context) {
        this.products = products;
        this.productNew = products;
        this.context = context;
    }

    @NonNull
    @Override
    public QuantityProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_quantity_product, parent, false);
        return new QuantityProductHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuantityProductHolder holder, int position) {
        Product product = productNew.get(position);
        StoreManagerDatabase db = StoreManagerDatabase.getInstance(context);
        ProductDAO productDAO = db.getProductDAO();
        if(product != null){
            holder.imgProduct.setImageBitmap(product.getImageBitmap());
            holder.txtQuantity.setText("Số lượng: "+product.getQuantity());
            holder.txtName.setText(product.getName());
            holder.btnSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    double quantity = Double.parseDouble(holder.editQuantity.getText().toString());
                    quantity = (double) Math.round(quantity *100)/100;
                    if(product.getType().equals(Product.ProductType.Amount)){
                        int a = (int) quantity;
                        quantity = (double) a;
                    }
                    product.setQuantity(quantity);
                    productDAO.update(product);
                    holder.txtQuantity.setText("Số lượng: "+product.getQuantity());
                    holder.editQuantity.setText("0");
                    holder.editQuantity.clearFocus();
                    notifyDataSetChanged();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if(productNew == null)
            return 0;
        else
            return productNew.size();
    }

    public void filterByCode(String code){
        List<Product> list = new ArrayList<>();
        for(Product p : products){
            if(p.getCode().toLowerCase().equals(code.toLowerCase()) && !p.getCode().isEmpty()){
                list.add(p);
                productNew = list;
                notifyDataSetChanged();
                return;
            }
        }
    }

    public void reset(){
        productNew = products;
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String search = charSequence.toString();
                if(search.isEmpty()){
                    productNew = products;
                }else{
                    List<Product> prList = new ArrayList<>();
                    for(Product p : products){
                        if(p.getName().toLowerCase().contains(search.toLowerCase())){
                            prList.add(p);
                        }
                    }
                    productNew = prList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = productNew;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                productNew = (List<Product>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class QuantityProductHolder extends RecyclerView.ViewHolder{

        private ImageView imgProduct;
        private TextView txtName, txtQuantity;
        private EditText editQuantity;
        private Button btnSubmit;

        public QuantityProductHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.img_product);
            txtName = itemView.findViewById(R.id.txt_product_name);
            txtQuantity = itemView.findViewById(R.id.txt_quantity);
            editQuantity = itemView.findViewById(R.id.edit_quantity);
            btnSubmit = itemView.findViewById(R.id.btn_submit);
        }
    }
}
