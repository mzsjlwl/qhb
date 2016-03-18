package com.handsome.qhb.bean;

import java.io.Serializable;

/**
 * Created by zhang on 2016/3/16.
 */
public class Address  implements Serializable{
    private int aid;
    private String recePhone;
    private String receName;
    private String receAddr;

    public int getAid() {
        return aid;
    }

    public void setAid(int aid) {
        this.aid = aid;
    }

    public String getRecePhone() {
        return recePhone;
    }

    public void setRecePhone(String recePhone) {
        this.recePhone = recePhone;
    }

    public String getReceName() {
        return receName;
    }

    public void setReceName(String receName) {
        this.receName = receName;
    }

    public String getReceAddr() {
        return receAddr;
    }

    public void setReceAddr(String receAddr) {
        this.receAddr = receAddr;
    }

    public Address(){

    }
    public Address(String recePhone,String receName,String receAddr){
        this.recePhone = recePhone;
        this.receName = receName;
        this.receAddr = receAddr;
    }
    public Address(int aid,String recePhone, String receName, String receAddr) {
        this.aid = aid;
        this.recePhone = recePhone;
        this.receName = receName;
        this.receAddr = receAddr;
    }

    @Override
    public String toString() {
        return "Address{" +
                "aid='"+aid+'\''+
                "recePhone='" + recePhone + '\'' +
                ", receName='" + receName + '\'' +
                ", receAddr='" + receAddr + '\'' +
                '}';
    }
}
