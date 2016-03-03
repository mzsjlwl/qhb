package com.handsome.qhb.bean;

/**
 * Created by zhang on 2016/3/3.
 */
public class Slider {
    private int sid;
    private String image;

    public Slider(){

    }
    public Slider(int sid,String image){
        this.sid = sid;
        this.image= image;
    }
    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {

        this.sid = sid;
    }
    public String getImage() {

        return image;
    }
    public void setImage(String image) {

        this.image = image;
    }

}
