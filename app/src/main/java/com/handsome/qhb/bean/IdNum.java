package com.handsome.qhb.bean;

/**
 * Created by zhang on 2016/3/17.
 */
public class IdNum {
    private int id;
    private int num;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public IdNum(){

    }
    public IdNum(int id, int num) {
        this.id = id;
        this.num = num;
    }
}
