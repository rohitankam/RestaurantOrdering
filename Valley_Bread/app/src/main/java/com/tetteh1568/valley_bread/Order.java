package com.tetteh1568.valley_bread;

/**
 * Created by ROHiT on 17-Mar-18.
 */

public class Order {

    String username,itemname;

    public Order(String username, String itemname) {
        this.username = username;
        this.itemname = itemname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public Order() {

    }
}
