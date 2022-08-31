package com.example.yourshopapplication.Adapter;

import android.icu.util.Freezable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yourshopapplication.Model.Feature;
import com.example.yourshopapplication.R;

import java.util.List;

public class FeatureAdapter extends RecyclerView.Adapter<FeatureAdapter.FeatureHolder>{

    private List<Feature> features;
    private onItemClickListener listener;
    public interface onItemClickListener{
        public void onClickItem(Feature feature);
    }

    public void setFeatures(List<Feature> features){
        this.features = features;
        notifyDataSetChanged();
    }
    public void setItemClickListener(onItemClickListener listener){
        this.listener = listener;
    }

    @NonNull
    @Override
    public FeatureHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_feature,null);
        return new FeatureHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeatureHolder holder, int position) {
        Feature f = features.get(position);
        if(f != null){
            holder.imgFeature.setImageResource(f.getImg());
            holder.txtFeature.setText(f.getName());

            holder.imgFeature.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onClickItem(f);
                }
            });

            holder.txtFeature.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onClickItem(f);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if(features != null)
            return features.size();
        return 0;
    }

    public class FeatureHolder extends RecyclerView.ViewHolder{
        private ImageView imgFeature;
        private TextView txtFeature;
        public FeatureHolder(@NonNull View itemView) {
            super(itemView);
            imgFeature = itemView.findViewById(R.id.img_feature);
            txtFeature = itemView.findViewById(R.id.txt_feature);
        }
    }
}
