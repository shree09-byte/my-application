package com.example.realestate.Model;

public class Fav {
    private  String pid, address, image, price, phone;

    public Fav(){

    }

    public Fav(String pid,String phone ,String address, String image, String price) {
        this.pid = pid;
        this.address = address;
        this.image = image;
        this.price = price;
        this.phone = phone;
    }


    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
