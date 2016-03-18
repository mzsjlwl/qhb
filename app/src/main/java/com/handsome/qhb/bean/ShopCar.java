package com.handsome.qhb.bean;

/**
 * Created by zhang on 2016/3/15.
 */
public class ShopCar {
    private int cid;
    private int pid;
    private int num;
    private String product;

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public ShopCar(){

    }
    public ShopCar(int cid, int pid, int num, String product) {
        this.cid = cid;
        this.pid = pid;
        this.num = num;
        this.product = product;
    }

    @Override
    public String toString() {
        return "ShopCar{" +
                "cid=" + cid +
                ", pid=" + pid +
                ", num=" + num +
                ", product='" + product + '\'' +
                '}';
    }
}
