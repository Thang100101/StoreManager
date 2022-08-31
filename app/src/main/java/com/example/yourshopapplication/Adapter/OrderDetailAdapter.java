package com.example.yourshopapplication.Adapter;

import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yourshopapplication.Activity.Payment.PaymentActivity;
import com.example.yourshopapplication.DAO.ProductDAO;
import com.example.yourshopapplication.Database.StoreManagerDatabase;
import com.example.yourshopapplication.Activity.Payment.Cart;
import com.example.yourshopapplication.Model.OrderDetail;
import com.example.yourshopapplication.Model.Product;
import com.example.yourshopapplication.R;

import java.util.List;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.OrderDetailHolder> {
    private List<OrderDetail> orderDetailList;
    private PaymentActivity context;
    private Cart cart;

    public OrderDetailAdapter(PaymentActivity context){
        this.context = context;
        cart = Cart.getInstance();
        orderDetailList = cart.getOrderDetailList();
        cart.setPaymentListener(new Cart.onPaymentListener() {
            @Override
            public void onPayment() {
                orderDetailList = cart.getOrderDetailList();
                notifyDataSetChanged();
            }
        });
        cart.setOnAddProductListener(new Cart.onAddProductListener() {
            @Override
            public void onAdd() {
                orderDetailList = cart.getOrderDetailList();
                notifyDataSetChanged();
            }
        });

    }


    public List<OrderDetail> getOrderDetailList() {
        return orderDetailList;
    }

    public void setOrderDetailList(List<OrderDetail> orderDetailList) {
        this.orderDetailList = orderDetailList;
    }

    @NonNull
    @Override
    public OrderDetailHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_order_detail, parent, false);
        return new OrderDetailHolder(view);
    }

    private OrderDetail odFlag;

    @Override
    public void onBindViewHolder(@NonNull OrderDetailHolder holder, int position) {
        OrderDetail orderDetail = orderDetailList.get(position);
        StoreManagerDatabase db = StoreManagerDatabase.getInstance(context);
        if(orderDetail != null){
            ProductDAO productDAO = db.getProductDAO();
            Product product = productDAO.get(orderDetail.getProduct_id());
            double price = product.getPrice() * orderDetail.getQuantity();
            holder.txtProduct.setText(product.getName());
            holder.txtPrice.setText(price +" "+product.getCurrency());
            holder.editAmount.setText(orderDetail.getQuantity()+"");
            holder.imgProduct.setImageBitmap(product.getImageBitmap());

            holder.btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    double quantity = Double.parseDouble(holder.editAmount.getText().toString());
                    quantity +=1;
                    quantity = (double) Math.round(quantity *100)/100;
                    orderDetail.setQuantity(quantity);
                    double price = product.getPrice() * orderDetail.getQuantity();
                    holder.txtPrice.setText(price + " " + product.getCurrency());
                    holder.editAmount.setText(quantity +"");
                    cart.onChangeOrderDetail(context);
                }
            });
            holder.btnReduce.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    double quantity = Double.parseDouble(holder.editAmount.getText().toString());
                    if(quantity > 1) {
                        quantity -= 1.0;
                        quantity = (double) Math.round(quantity *100)/100;
                        orderDetail.setQuantity(quantity);
                        double price = product.getPrice() * orderDetail.getQuantity();
                        holder.txtPrice.setText(price + " " + product.getCurrency());
                        holder.editAmount.setText(quantity + "");
                        cart.onChangeOrderDetail(context);
                    }else{
                        showAcceptDeleteDialog(product, holder, orderDetail);
                    }
                }
            });
            holder.editAmount.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                    if(i == EditorInfo.IME_ACTION_DONE || keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER){
                        try{
                            double quantity = Double.parseDouble(holder.editAmount.getText().toString());

                            if(quantity == 0){
                                showAcceptDeleteDialog(product, holder, orderDetail);
                                odFlag = orderDetail;
                                return false;
                            }else if(quantity <1 && product.getType().equals(Product.ProductType.Amount)){
                                holder.editAmount.setText("1");
                                editAmount(holder, orderDetail);
                                return false;
                            }

                            if(product.getType().equals(Product.ProductType.Amount)) {
                                editAmount(holder, orderDetail);
                            }else{
                                quantity = (double) Math.round(quantity *100)/100;
                                orderDetail.setQuantity(quantity);
                                holder.txtPrice.setText(product.getPrice() * quantity + " "+ product.getCurrency());
                                holder.editAmount.setText(quantity +"");
                                cart.onChangeOrderDetail(context);
                            }

                        }catch (Exception e){
                            holder.editAmount.setText(orderDetail.getQuantity()+"");
                        }

                    }
                    return false;
                }
            });
            holder.editAmount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    if(!b){
                        try{
                            double quantity = Double.parseDouble(holder.editAmount.getText().toString());

                            if(quantity == 0){
                                if(odFlag == null || odFlag != orderDetail) {
                                    showAcceptDeleteDialog(product, holder, orderDetail);
                                    odFlag = orderDetail;
                                    return;
                                }
                            }else if(quantity <1 && product.getType().equals(Product.ProductType.Amount)){
                                holder.editAmount.setText("1");
                                editAmount(holder, orderDetail);
                                return;
                            }

                            if(product.getType().equals(Product.ProductType.Amount)) {
                                editAmount(holder, orderDetail);
                            }else{
                                quantity = (double) Math.round(quantity *100)/100;
                                orderDetail.setQuantity(quantity);
                                holder.txtPrice.setText(product.getPrice() * quantity + " "+ product.getCurrency());
                                holder.editAmount.setText(quantity +"");
                                cart.onChangeOrderDetail(context);
                            }

                        }catch (Exception e){
                            holder.editAmount.setText(orderDetail.getQuantity()+"");
                        }
                    }
                }
            });
        }
    }

    private void showAcceptDeleteDialog(Product product, OrderDetailHolder holder, OrderDetail orderDetail){
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setMessage("Bạn có chắc muốn xóa "+product.getName()+" khỏi danh sách không");
        dialog.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                OrderDetail orderDetail = new OrderDetail();
                for(OrderDetail od : orderDetailList){
                    if(od.getProduct_id() == product.getId()){
                        orderDetail = od;
                    }
                }
                orderDetailList.remove(orderDetail);
                cart.onChangeOrderDetail(context);
                notifyDataSetChanged();
            }
        });
        dialog.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                holder.editAmount.setText(orderDetail.getQuantity()+"");
            }
        });
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                holder.editAmount.setText(orderDetail.getQuantity()+"");
            }
        });
        dialog.show();
        return;
    }


    private void editAmount(OrderDetailHolder holder, OrderDetail orderDetail){
        Product product = StoreManagerDatabase.getInstance(context).getProductDAO().get(orderDetail.getProduct_id());
        holder.editAmount.clearFocus();
        double quantity = Double.parseDouble(holder.editAmount.getText().toString());
        int a = (int) quantity;
        quantity = (double) a;
        orderDetail.setQuantity(quantity);
        holder.editAmount.setText(quantity +"");
        holder.txtPrice.setText(product.getPrice() * quantity + " "+ product.getCurrency());
        cart.onChangeOrderDetail(context);
    }



    @Override
    public int getItemCount() {
        if(orderDetailList == null){
            return 0;
        }
        return orderDetailList.size();
    }

    public class OrderDetailHolder extends RecyclerView.ViewHolder{
        ImageView imgProduct;
        TextView txtProduct, txtPrice;
        EditText editAmount;
        Button btnAdd, btnReduce;

        public OrderDetailHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.img_product);
            txtProduct = itemView.findViewById(R.id.txt_product);
            txtPrice = itemView.findViewById(R.id.txt_price);
            editAmount = itemView.findViewById(R.id.edit_amount);
            btnAdd = itemView.findViewById(R.id.btn_add);
            btnReduce = itemView.findViewById(R.id.btn_reduce);
        }
    }
}
