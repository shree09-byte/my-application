package com.example.realestate.Model;

public class AdminOrders {
    private String fullname, phone, address, date, time, pirce, pid;
    public AdminOrders(){

    }

    public AdminOrders(String fullname, String phone, String address, String date, String time, String pirce, String pid) {
        this.fullname = fullname;
        this.phone = phone;
        this.address = address;
        this.date = date;
        this.time = time;
        this.pirce = pirce;
        this.pid = pid;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPirce() {
        return pirce;
    }

    public void setPirce(String pirce) {
        this.pirce = pirce;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }
}
