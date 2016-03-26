package com.handsome.qhb.bean;

import java.util.Arrays;

/**
 * Created by zhang on 2016/3/26.
 */
public class HB {
    private int id;
    private float[] hb;
    private int rid;
    private int type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float[] getHb() {
        return hb;
    }

    public void setHb(float[] hb) {
        this.hb = hb;
    }

    public int getRid() {
        return rid;
    }

    public void setRid(int rid) {
        this.rid = rid;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public HB(){

    }
    public HB(int id, float[] hb, int rid, int type) {
        this.id = id;
        this.hb = hb;
        this.rid = rid;
        this.type = type;
    }

    @Override
    public String toString() {
        return "HB{" +
                "id=" + id +
                ", hb=" + Arrays.toString(hb) +
                ", rid=" + rid +
                ", type=" + type +
                '}';
    }
}
