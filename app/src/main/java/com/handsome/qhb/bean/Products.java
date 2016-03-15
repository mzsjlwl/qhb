package com.handsome.qhb.bean;

/**
 * Created by zhang on 2016/3/14.
 */
public class Products {
    private Product product;
    private int num;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public Products(){

    }

    public Products(Product product, int num) {
        this.product = product;
        this.num = num;
    }

    @Override
    public String toString() {
        return "Products{" +
                "product=" + product +
                ", num=" + num +
                '}';
    }
}
