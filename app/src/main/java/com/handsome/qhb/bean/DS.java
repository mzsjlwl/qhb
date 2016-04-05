package com.handsome.qhb.bean;

import java.io.Serializable;

/**
 * Created by zhang on 2016/4/4.
 */
public class DS  implements Serializable{
    private int id;
    private int rid;
    private int personNum;
    private int guess;
    private int result;

    public DS(){

    }
    public DS(int id, int rid, int personNum, int guess, int result) {
        this.id = id;
        this.rid = rid;
        this.personNum = personNum;
        this.guess = guess;
        this.result = result;
    }

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

    public int getPersonNum() {
        return personNum;
    }

    public void setPersonNum(int personNum) {
        this.personNum = personNum;
    }

    public int getGuess() {
        return guess;
    }

    public void setGuess(int guess) {
        this.guess = guess;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "DS{" +
                "id=" + id +
                ", rid=" + rid +
                ", personNum=" + personNum +
                ", guess=" + guess +
                ", result=" + result +
                '}';
    }
}
