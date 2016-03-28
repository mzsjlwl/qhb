package com.handsome.qhb.bean;

import java.util.Arrays;

/**
 * Created by zhang on 2016/3/26.
 */
public class RandomBonus {
    private int id;
    private float[] bonus;
    private int rid;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float[] getBonus() {
        return bonus;
    }

    public void setBonus(float[] bonus) {
        this.bonus = bonus;
    }

    public int getRid() {
        return rid;
    }

    public void setRid(int rid) {
        this.rid = rid;
    }

    public RandomBonus(){

    }
    public RandomBonus(int id, float[] bonus, int rid) {
        this.id = id;
        this.bonus = bonus;
        this.rid = rid;
    }

    @Override
    public String toString() {
        return "RandomBonus{" +
                "id=" + id +
                ", bonus=" + Arrays.toString(bonus) +
                ", rid=" + rid +
                '}';
    }
}
