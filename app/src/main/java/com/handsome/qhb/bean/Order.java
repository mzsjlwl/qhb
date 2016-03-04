package com.handsome.qhb.bean;

import java.util.Date;

/**
 * Created by zhang on 2016/3/4.
 */
public class Order  {
    private int oid;
    private String pname;
    private float price;
    private int uid;
    private int pid;
    private String receAddr;
    private String receName;
    private String recePhone;
    private Date time;

    public Order(){

    }
    public Order(int oid,String pname,float price,int uid,int pid,String receAddr,String receName,String recePhone,Date time){
        super();
        this.oid = oid;
        this.pname = pname;
        this.price = price;
        this.uid = uid;
        this.pid = pid;
        this.receAddr = receAddr;
        this.receName = receName;
        this.recePhone = recePhone;
        this.time = time;
    }

}
