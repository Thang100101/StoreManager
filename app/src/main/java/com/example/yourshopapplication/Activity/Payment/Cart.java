package com.example.yourshopapplication.Activity.Payment;

import android.content.Context;
import android.util.Log;

import com.example.yourshopapplication.Adapter.ConvertDatetime;
import com.example.yourshopapplication.DAO.OrderDAO;
import com.example.yourshopapplication.DAO.OrderDetailDAO;
import com.example.yourshopapplication.DAO.PriceDAO;
import com.example.yourshopapplication.DAO.ProductDAO;
import com.example.yourshopapplication.Database.StoreManagerDatabase;
import com.example.yourshopapplication.Model.Order;
import com.example.yourshopapplication.Model.OrderDetail;
import com.example.yourshopapplication.Model.Price;
import com.example.yourshopapplication.Model.Product;
import com.example.yourshopapplication.Model.User;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Cart {
    private List<OrderDetail> orderDetailList;
    private static Cart cart;
    private List<Price> prices;
    private onChangeOrderDetailListener orderDetailListener;
    private onPaymentListener onPaymentListener;
    private onClearCartListener onClearCartListener;
    private onAddProductListener onAddProductListener;


    public interface onChangeOrderDetailListener {
        public void onChange();
    }
    public interface onPaymentListener {
        public void onPayment();
    }
    public interface onClearCartListener{
        public void onClear();
    }
    public interface onAddProductListener{
        public void onAdd();
    }

    private Cart(){
        orderDetailList = new ArrayList<>();
        prices = new ArrayList<>();
    }

    public void setOrderDetaillistener(onChangeOrderDetailListener listener) {
        this.orderDetailListener = listener;
    }
    public void setPaymentListener(Cart.onPaymentListener listener){
        this.onPaymentListener = listener;
    }

    public void setOnClearCartListener(Cart.onClearCartListener onClearCartListener) {
        this.onClearCartListener = onClearCartListener;
    }

    public void setOnAddProductListener(Cart.onAddProductListener onAddProductListener) {
        this.onAddProductListener = onAddProductListener;
    }

    public void addProductToCart(Product product, Context context){
        boolean checkExists = false;
        for(OrderDetail od : orderDetailList){
            if(od.getProduct_id() == product.getId()){
                od.setQuantity(od.getQuantity()+1);
                checkExists = true;
                break;
            }
        }
        if(!checkExists){
            OrderDetail od = new OrderDetail();
            od.setProduct_id(product.getId());
            od.setQuantity(1);
            orderDetailList.add(od);
        }
        if(onAddProductListener != null)
            onAddProductListener.onAdd();
        onChangeOrderDetail(context);
//        if(orderDetailListener != null)
//            this.orderDetailListener.onChange();
    }

    public void onChangeOrderDetail(Context context){
        prices = new ArrayList<>();
        StoreManagerDatabase db = StoreManagerDatabase.getInstance(context);
        ProductDAO productDAO = db.getProductDAO();
        for(OrderDetail od : orderDetailList){
            Product product = productDAO.get(od.getProduct_id());
            changePriceList(product, od);
        }
        if(orderDetailListener != null)
            this.orderDetailListener.onChange();
        if(orderDetailList.size() == 0 && onClearCartListener != null){
            onClearCartListener.onClear();
        }
    }

    private void addPrice(OrderDetail od, Context context){
        StoreManagerDatabase db = StoreManagerDatabase.getInstance(context);
        ProductDAO productDAO = db.getProductDAO();
        Product product = productDAO.get(od.getProduct_id());
        changePriceList(product, od);
    }

    private void changePriceList(Product product, OrderDetail od){
        Price price = new Price();
        price.setTotalPrice(od.getQuantity()*product.getPrice());
        price.setCurrency(product.getCurrency());
        if(checkExistsCurrency(price)){
            for(Price p : prices){
                if(p.getCurrency().equals(price.getCurrency())){
                    p.setTotalPrice(p.getTotalPrice()+price.getTotalPrice());
                    break;
                }
            }
        }
        else{
            Log.d("add price","add");
            prices.add(price);
        }
    }

    public void removeProduct(Product product, Context context){
        OrderDetail detail = new OrderDetail();
        for(OrderDetail od : orderDetailList){
            if(od.getProduct_id() == product.getId()){
                detail = od;
            }
        }
        orderDetailList.remove(detail);
        onChangeOrderDetail(context);
    }

    public void changeOrderDetail(OrderDetail orderDetail){
        for(OrderDetail od : orderDetailList){
            if(od.getProduct_id() == orderDetail.getProduct_id()){
                od.setQuantity(orderDetail.getQuantity());
            }
        }
    }

    public List<OrderDetail> getOrderDetailList() {
        return orderDetailList;
    }

    public List<Price> getTotalPrice(){
//        StoreManagerDatabase db = StoreManagerDatabase.getInstance(context);
//        ProductDAO productDAO = db.getProductDAO();
//        for(OrderDetail od : orderDetailList){
//            Product product = productDAO.get(od.getProduct_id());
//            Price price = new Price();
//            price.setTotalPrice(od.getQuantity()*product.getPrice());
//            price.setCurrency(product.getCurrency());
//
//            if(checkExistsCurrency(price)){
//                for(Price p : prices){
//                    if(p.getCurrency().equals(price.getCurrency())){
//                        p.setTotalPrice(p.getTotalPrice()+price.getTotalPrice());
//                        break;
//                    }
//                }
//            }
//            else{
//                prices.add(price);
//            }
//        }

        return prices;
    }

    public void payment(Context context, User user){
        StoreManagerDatabase db = StoreManagerDatabase.getInstance(context);
        ProductDAO productDAO = db.getProductDAO();
        PriceDAO priceDAO = db.getPriceDAO();
        OrderDetailDAO orderDetailDAO = db.getOrderDetailDAO();
        OrderDAO orderDAO = db.getOrderDAO();
        Order order = new Order();
        order.setDay(ConvertDatetime.getDateString(Calendar.getInstance().getTime()));
        if(user != null) {
            order.setEmployeeId(user.getId());
        }
        int orderId = (int)orderDAO.add(order);
        for(OrderDetail od : orderDetailList){
            od.setOrder_id(orderId);
            orderDetailDAO.add(od);
            Product product = productDAO.get(od.getProduct_id());
            double quantity = product.getQuantity() - od.getQuantity();
            if(quantity<0){
                product.setQuantity(0);
            }else{
                product.setQuantity(quantity);
            }
            productDAO.update(product);

        }
        for(Price price : prices){
            price.setOrderId(orderId);
            priceDAO.add(price);
        }
        this.orderDetailList = new ArrayList<>();
        this.prices = new ArrayList<>();
        if(onPaymentListener != null){
            onPaymentListener.onPayment();
        }
        if(onClearCartListener != null){
            onClearCartListener.onClear();
        }
    }

    private boolean checkExistsCurrency(Price price){
        for(Price p : prices){
            if(p.getCurrency().equals(price.getCurrency())){
                return true;
            }
        }
        return false;
    }

    public static Cart getInstance(){
        if(cart == null){
            return getCart();
        }
        return cart;
    }
    private static synchronized Cart getCart(){
        if(cart == null){
            cart = new Cart();
            return cart;
        }
        return cart;
    }
}
