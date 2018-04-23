package com.tetteh1568.valley_bread_client;

/**
 * Created by ROHiT on 17-Mar-18.
 */

public class Order {

    String username,itemname,itemprice,image,plates;

    public Order() {

    }

    public Order(String username, String itemname, String itemprice, String image,String plate) {
        this.username = username;
        this.itemname = itemname;
        this.itemprice = itemprice;
        this.plates=plate;
        this.image = image;
    }

    public String getPlates() {
        return plates;
    }

    public void setPlates(String plates) {
        this.plates = plates;
    }

    public String getItemprice() {
        return itemprice;
    }

    public void setItemprice(String itemprice) {
        this.itemprice = itemprice;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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


}
