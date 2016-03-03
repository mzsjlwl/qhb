package com.handsome.qhb.bean;

/**
 * Created by zhang on 2016/3/3.
 */
public class Product {
    private int pid;
    private String pname;
    private float price;
    private String introduce;
    private int sortId;
    private String picture;
    private String flag;

    public int getPid() {
        return pid;
    }

    public String getPname() {
        return pname;
    }

    public float getPrice() {
        return price;
    }

    public String getIntroduce() {
        return introduce;
    }

    public int getSortId() {
        return sortId;
    }

    public String getPicture() {
        return picture;
    }

    public String getFlag() {
        return flag;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public void setSortId(int sortId) {
        this.sortId = sortId;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
    public Product(){

    }
    public Product(int pid,String pname,float price,String introduce,int sortId,String picture,String flag ){
        this.pid = pid;
        this.pname = pname;
        this.price = price;
        this.introduce = introduce;
        this.sortId = sortId;
        this.picture = picture;
        this.flag = flag;
    }
}
