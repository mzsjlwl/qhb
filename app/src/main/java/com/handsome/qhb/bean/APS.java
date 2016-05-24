package com.handsome.qhb.bean;

/**
 * Created by zhang on 2016/4/21.
 */
public class APS {
    private int id;
    private int rid;
    private int uid;
    private String  alert;
    private String  nackname;
    private String date;
    //消失类型，用于区别普通消息，红包消息,猜单双消息
    private int type;
    //头像
    private String photo;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRid() {
        return rid;
    }

    public void setRid(int rid) {
        this.rid = rid;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getAlert() {
        return alert;
    }

    public void setAlert(String alert) {
        this.alert = alert;
    }

    public String getNackname() {
        return nackname;
    }

    public void setNackname(String nackname) {
        this.nackname = nackname;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public APS(){

    }
    public APS(int id, int rid, int uid, String alert, String nackname, String date, int type, String photo) {
        this.id = id;
        this.rid = rid;
        this.uid = uid;
        this.alert = alert;
        this.nackname = nackname;
        this.date = date;
        this.type = type;
        this.photo = photo;
    }
}
