package com.handsome.qhb.bean;

import java.io.Serializable;

/**
 * Created by zhang on 2016/3/13.
 */
public class User implements Serializable {
    private int uid;
    private String username;
    private String nackname;
    private float integral;
    private String photo;
    private String token;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNackname() {
        return nackname;
    }

    public void setNackname(String nackname) {
        this.nackname = nackname;
    }

    public float getIntegral() {
        return integral;
    }

    public void setIntegral(float integral) {
        this.integral = integral;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User(){

    }

    public User(int uid, String username, String nackname, float integral, String photo, String token) {
        this.uid = uid;
        this.username = username;
        this.nackname = nackname;
        this.integral = integral;
        this.photo = photo;
        this.token = token;
    }

    @Override
    public String toString() {
        return "User{" +
                "uid=" + uid +
                ", username='" + username + '\'' +
                ", nackname='" + nackname + '\'' +
                ", integral=" + integral +
                ", photo='" + photo + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
