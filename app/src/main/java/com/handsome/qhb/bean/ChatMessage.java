package com.handsome.qhb.bean;

/**
 * Created by zhang on 2016/3/24.
 */
public class ChatMessage {
    private int msgId;
    private int rid;
    private int uid;
    private String content;
    private String  nackname;
    private String date;
    private int status;

    public int getMsgId() {
        return msgId;
    }

    public void setMsgId(int msgId) {
        this.msgId = msgId;
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

    public ChatMessage(){

    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ChatMessage(int msgId, int rid, int uid, String content, String nackname, String date, int status) {
        this.msgId = msgId;
        this.rid = rid;
        this.uid = uid;
        this.content = content;
        this.nackname = nackname;
        this.date = date;
        this.status = status;
    }

    @Override
    public String toString() {
        return "ChatMessage{" +
                "msgId=" + msgId +
                ", rid=" + rid +
                ", uid=" + uid +
                ", content='" + content + '\'' +
                ", nackname='" + nackname + '\'' +
                ", date='" + date + '\'' +
                ", status=" + status +
                '}';
    }
}
