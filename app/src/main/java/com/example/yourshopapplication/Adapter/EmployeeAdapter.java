package com.example.yourshopapplication.Adapter;

import android.content.ContentValues;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yourshopapplication.Model.User;
import com.example.yourshopapplication.R;

import java.util.List;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.EmployeeHolder>{

    private onSettingClickListener listener;
    private Context context;
    private List<User> users;

    public interface onSettingClickListener{
        public void onClick(User user);
    }

    public EmployeeAdapter(Context context, List<User> users) {
        this.context = context;
        this.users = users;
    }

    public void setListener(onSettingClickListener listener) {
        this.listener = listener;
    }

    public void setUsers(List<User> users) {
        this.users = users;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public EmployeeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_employee, parent, false);
        return new EmployeeHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeeHolder holder, int position) {
        User user = users.get(position);
        if(user != null){
            holder.txtName.setText(user.getName());
            holder.btnSetting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null)
                        listener.onClick(user);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (users == null)
            return 0;
        else
            return users.size();
    }


    public class EmployeeHolder extends RecyclerView.ViewHolder{

        private TextView txtName;
        private Button btnSetting;

        public EmployeeHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txt_name);
            btnSetting = itemView.findViewById(R.id.btn_setting);
        }
    }
}
