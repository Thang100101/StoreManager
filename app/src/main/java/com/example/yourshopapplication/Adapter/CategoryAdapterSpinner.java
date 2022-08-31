package com.example.yourshopapplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yourshopapplication.Model.Category;
import com.example.yourshopapplication.R;

import java.util.List;

public class CategoryAdapterSpinner extends BaseAdapter {

    private List<Category> categories;
    private Context context;
    private LayoutInflater inflater;
    private Type type;
    private onDeleteClickListener deleteClickListener;

    public interface onDeleteClickListener{
        public void onClickDelete(Category category);
    }

    public void setDeleteClickListener(onDeleteClickListener deleteClickListener) {
        this.deleteClickListener = deleteClickListener;
    }

    public CategoryAdapterSpinner(List<Category> categories, Context context, Type type) {
        this.categories = categories;
        this.context = context;
        this.type = type;
        inflater = LayoutInflater.from(context);
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
        notifyDataSetChanged();
    }

    public void add(Category category){
        this.categories.add(category);
        notifyDataSetChanged();
    }

    public void remove(Category category){
        for(Category c : categories){
            if(c.getId().equals(category.getId())) {
                category = c;
                break;
            }
        }
        categories.remove(category);
        notifyDataSetChanged();
    }
    public void remove(int position){
        categories.remove(position);
        notifyDataSetChanged();
    }

    public enum Type{
        Default, CanDelete;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
//        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if(type.equals(Type.CanDelete)){
            convertView = inflater.inflate(R.layout.list_dropdown_item_2, parent, false);
        }else {
            convertView = inflater.inflate(R.layout.list_dropdown_item, parent, false);
        }
        TextView txtName = convertView.findViewById(R.id.txt_name);
        Category category = categories.get(position);
        if(category != null){
            txtName.setText(category.getName());
            if(type.equals(Type.CanDelete)) {
                ImageView imgDelete = convertView.findViewById(R.id.img_delete);
                imgDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(deleteClickListener != null){
                            deleteClickListener.onClickDelete(categories.get(position));
                        }
                    }
                });
            }
        }
        return convertView;
    }

    @Override
    public int getCount() {
        if(categories == null)
            return 0;
        return categories.size();
    }

    public int getPosition(Category category){
        if(categories == null)
            return -1;
        for(int i =0; i<categories.size(); i++){
            if(category.equals(categories.get(i))){
                return i;
            }
        }
        return -1;
    }

    @Override
    public Object getItem(int i) {
        return categories.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.item_selected_spinner, null);
        TextView txtName = view.findViewById(R.id.txt_select);
        Category category = categories.get(i);
        if(category != null){
            txtName.setText(category.getName());
        }
        return view;
    }
}
