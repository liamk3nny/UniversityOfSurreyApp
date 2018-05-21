package com.example.liamkenny.unionapp;

import java.util.ArrayList;

public class Basket {

    //TODO: Add validations w/ try & exceptions

    private ArrayList<Basket_Product> basket_Items;


    public Basket(ArrayList<Basket_Product> items) {
        this.basket_Items = items;
    }

    private void setBasket_Items(ArrayList<Basket_Product> list){
        this.basket_Items = list;
    }

    public ArrayList<Basket_Product> getBasket_Items() {
        return basket_Items;
    }


    public void addItem(Basket_Product item) {
        this.getBasket_Items().add(item);
    }

    public void removeItem(int index) {
        this.getBasket_Items().remove(index);
    }

    public void incItemQuantity(int index) {
        this.getBasket_Items().get(index).incQuantity();
    }

    public void decItemQuantity(int index) {
        this.getBasket_Items().get(index).decQuantity();
    }

    public void clearBasket(){
        this.basket_Items = null;
    }


}
