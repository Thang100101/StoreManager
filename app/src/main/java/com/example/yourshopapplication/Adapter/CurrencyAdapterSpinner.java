package com.example.yourshopapplication.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.yourshopapplication.Model.Currency;
import com.example.yourshopapplication.R;

import java.util.List;

public class CurrencyAdapterSpinner extends BaseAdapter {

    private Context context;
    private List<Currency> currencies;
    private LayoutInflater inflater;

    public CurrencyAdapterSpinner(Context context, List<Currency> currencies) {
        this.context = context;
        this.currencies = currencies;
        inflater = LayoutInflater.from(context);
    }

    public void addItem(Currency currency){
        currencies.add(currency);
        notifyDataSetChanged();
    }
    public void removeItem(Currency currency){
        currencies.remove(currency);
        notifyDataSetChanged();
    }
    public List<Currency> getCurrencies(){
        return this.currencies;
    }

    @Override
    public int getCount() {
        if(currencies == null)
            return 0;
        else
            return currencies.size();
    }

    @Override
    public Object getItem(int i) {
        if(currencies == null)
            return null;
        else
            return currencies.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.list_dropdown_item, parent, false);
        TextView txtName = convertView.findViewById(R.id.txt_name);
        if(currencies.get(position) != null)
            txtName.setText(currencies.get(position).getName());
        return convertView;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.item_selected_spinner, null);
        view.setBackgroundColor(Color.rgb(200,200,200));
        TextView txtName = view.findViewById(R.id.txt_select);
        if(currencies.get(i) != null){
            txtName.setText(currencies.get(i).getName());
        }
        return view;
    }
}
