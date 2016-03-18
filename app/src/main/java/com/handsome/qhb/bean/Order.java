package com.handsome.qhb.bean;

import java.io.Serializable;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by zhang on 2016/3/4.
 */
public class Order  implements Serializable {
    private int oid;
    private String products;
    private String receAddr;
    private String receName;
    private String recePhone;
    private String time;
    private int state;

    public int getOid() {
        return oid;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time)  {

        this.time= time;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Order(){

    }

    public Order(int oid, String products, String receAddr, String receName, String recePhone, String time, int state) {
        this.oid = oid;
        this.products = products;
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
                ", product='" + products + '\'' +
                ", receAddr='" + receAddr + '\'' +
                ", receName='" + receName + '\'' +
                ", recePhone='" + recePhone + '\'' +
                ", time=" + time +
                ", state=" + state +
                '}';
    }
}
