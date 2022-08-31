package com.example.yourshopapplication.Model;

import com.example.yourshopapplication.R;

public class Feature {
    private int img;
    private String name;
    private FeatureEnum type;

    public Feature( FeatureEnum type) {
        this.type = type;
        setNameAndImage();
    }

    private void setNameAndImage() {
        switch (type){
            case NewProduct:
                this.name = "New Product";
                this.img = R.drawable.icon_product;
                break;
            case EmployeeManager:
                this.name = "Employee Manager";
                this.img = R.drawable.icon_employee_manager;
                break;
            case Calender:
                this.name = "Calender Manager";
                this.img = R.drawable.icon_calender;
                break;
            case Payment:
                this.name = "Payment";
                this.img = R.drawable.icon_payment;
                break;
            case AddQuantityProduct:
                this.name = "Add Quantity Product";
                this.img = R.drawable.icon_add_product;
                break;
            case NewCategory:
                this.name = "New Category";
                this.img = R.drawable.icon_category;
                break;
            case ScanCode:
                this.name = "Scan Code";
                this.img = R.drawable.icon_scan;
                break;
            case CheckIn:
                this.name = "Check In";
                this.img = R.drawable.icon_check_in;
                break;
            case CheckOut:
                this.name = "Check Out";
                this.img = R.drawable.icon_check_out;
                break;
            case ChangeUser:
                this.name = "Change User";
                this.img = R.drawable.icon_change_user;
                break;
            default:
                break;
        }
    }

    public int getImg() {
        return img;
    }

    public String getName() {

        return name;
    }

    public FeatureEnum getType() {
        return type;
    }

    public void setType(FeatureEnum type) {
        this.type = type;
    }
}
