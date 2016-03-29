package com.handsome.qhb.bean;

import java.io.Serializable;

/**
 * Created by zhang on 2016/3/24.
 */
public class ChatMessage implements Serializable{
    //若为空,则为普通信息，若不为空,则根据type获取红包的id
    private int id;
    private int rid;
    private int uid;
    private String content;
    private String  nackname;
    private String date;
    //消息是否发送成功，用于让加载框失效
    private int status;
    //消失类型，用于区别普通消息，红包消息,猜单双消息
    private int type;
    //红包总钱数
    private float bonus_total;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getRid() {
        return rid;
    }

    public void setRid(int rid) {
        this.rid = rid;
    }

    public String  getContent() {
        return content;
    }

    public void setContent(String  content) {
        this.content = content;
    }

    public String  getNackname() {
        return nackname;
    }

    public void setNackname(String  nackname) {
        this.nackname = nackname;
    }


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


    public float getBonus_total() {
        return bonus_total;
    }

    public void setBonus_total(float bonus_total) {
        this.bonus_total = bonus_total;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ChatMessage(){

    }


    public ChatMessage(int id, int rid, int uid, String content, String nackname, String date, int status, int type, float bonus_total) {
        this.id = id;
        this.rid = rid;
        this.uid = uid;
        this.content = content;
        this.nackname = nackname;
        this.date = date;
        this.status = status;
        this.type = type;
        this.bonus_total = bonus_total;
    }

    @Override
    public String toString() {
        return "ChatMessage{" +
                "id=" + id +
                ", rid=" + rid +
                ", uid=" + uid +
                ", content='" + content + '\'' +
                ", nackname='" + nackname + '\'' +
                ", date='" + date + '\'' +
                ", status=" + status +
                ", type=" + type +
                ", bonus_total=" + bonus_total +
                '}';
    }
}
