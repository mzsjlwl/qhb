package com.handsome.qhb.bean;

import java.util.List;

/**
 * Created by zhang on 2016/3/17.
 */
public class OrderJson {
    private int uid;
    private String addr;
    private String extra;
    private List<IdNum> product;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public List<IdNum> getProduct() {
        return product;
    }

    public void setProduct(List<IdNum> product) {
        this.product = product;
    }

    public OrderJson(){

    }

    public OrderJson(int uid,String addr, String extra, List<IdNum> product) {
        this.uid = uid;
        this.addr = addr;
        this.extra = extra;
        this.product = product;
    }
}
