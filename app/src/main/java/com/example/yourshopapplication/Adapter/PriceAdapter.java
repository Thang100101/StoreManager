package com.example.yourshopapplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yourshopapplication.Activity.Payment.Cart;
import com.example.yourshopapplication.Model.Price;
import com.example.yourshopapplication.R;

import java.util.List;

public class PriceAdapter extends RecyclerView.Adapter<PriceAdapter.PriceHolder> {
    List<Price> prices;
    Context context;
    Cart cart;

    public PriceAdapter(Context context) {
        this.context = context;
        cart = Cart.getInstance();
        prices = cart.getTotalPrice();
        cart.setOrderDetaillistener(new Cart.onChangeOrderDetailListener() {
            @Override
            public void onChange() {
                prices = cart.getTotalPrice();
                notifyDataSetChanged();
            }
        });
    }

    public List<Price> getPrices() {
        return prices;
    }

    public void setPrices(List<Price> prices) {
        this.prices = prices;
    }

    @NonNull
    @Override
    public PriceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_price, parent, false);
        return new PriceHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PriceHolder holder, int position) {
        Price price = prices.get(position);
        holder.txtPrice.setText(price.getTotalPrice()+"");
        holder.txtCurrency.setText(price.getCurrency());
    }

    @Override
    public int getItemCount() {
        if(prices == null)
            return 0;
        return prices.size();
    }

    public class PriceHolder extends RecyclerView.ViewHolder{
        TextView txtPrice, txtCurrency;
        public PriceHolder(@NonNull View itemView) {
            super(itemView);
            txtPrice = itemView.findViewById(R.id.txt_price);
            txtCurrency = itemView.findViewById(R.id.txt_currency);
        }
    }
}
