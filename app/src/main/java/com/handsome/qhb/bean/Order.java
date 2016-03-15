package com.handsome.qhb.bean;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by zhang on 2016/3/4.
 */
public class Order  implements Serializable {
    private Gson gson = new Gson();
    private int oid;
    private String products;
    private float totalMoney;
    private List<Products> productsList;

    private String receAddr;
    private String receName;
    private String recePhone;
    private Date time;
    private int state;

    public int getOid() {
        return oid;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void setOid(int oid) {
        this.oid = oid;
    }

    public String getProducts() {
        return products;
    }

    public void setProducts(String products) {
        this.products = products;
    }

    public float getTotalMoney() {
        if(getProductsList()!=null){
            for(Products products: getProductsList()){
                totalMoney=totalMoney+products.getProduct().getPrice()*products.getNum();
             }
            return totalMoney;
        }
        return 0;
    }



    public List<Products> getProductsList() {
        if(products!=null){
            productsList =gson.fromJson(getProducts(), new TypeToken<List<Products>>() {
            }.getType());
            return productsList;
        }
        return null;
    }



    public String getReceAddr() {
        return receAddr;
    }

    public void setReceAddr(String receAddr) {
        this.receAddr = receAddr;
    }

    public String getReceName() {
        return receName;
    }

    public void setReceName(String receName) {
        this.receName = receName;
    }

    public String getRecePhone() {
        return recePhone;
    }

    public void setRecePhone(String recePhone) {
        this.recePhone = recePhone;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Order(){

    }



    public Order(int oid, String products, float totalMoney, List<Products> productsList, String receAddr, String receName, String recePhone, Date time,int state) {
        this.oid = oid;
        this.products = products;
        this.totalMoney = totalMoney;
        this.productsList = productsList;
        this.receAddr = receAddr;
        this.receName = receName;
        this.recePhone = recePhone;
        this.time = time;
        this.state = state;
    }

    @Override
    public String toString() {
        return "Order{" +
                "oid=" + oid +
                ", products='" + products + '\'' +
                ", totalMoney=" + totalMoney +
                ", productsList=" + productsList +
                ", receAddr='" + receAddr + '\'' +
                ", receName='" + receName + '\'' +
                ", recePhone='" + recePhone + '\'' +
                ", time=" + time +
                ", state=" + state +
                '}';
    }
}
