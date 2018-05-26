package com.example.liamkenny.unionapp;

import java.util.ArrayList;

public class Basket {

    //TODO: Add validations w/ try & exceptions

    private ArrayList<Basket_Product> basket_Items;
    private double totalPrice = 0;

    public Basket(ArrayList<Basket_Product> items) {
        this.basket_Items = items;
        for(Basket_Product bp: this.basket_Items){
            totalPrice += bp.getTotalCost();
        }
    }


    private void setBasket_Items(ArrayList<Basket_Product> list){
        this.basket_Items = list;
    }

    public ArrayList<Basket_Product> getBasket_Items() {
        return basket_Items;
    }

    public void recalculateTotalPrice(){
        double price = 0;
        if(this.getBasket_Items().size() == 0){
            this.totalPrice = price;
        }else {
            for (Basket_Product bp : basket_Items) {
                price += bp.getTotalCost();
                this.totalPrice = price;
            }
        }
    }

    public void addItem(Basket_Product item) {
        this.getBasket_Items().add(item);
        recalculateTotalPrice();
    }

    public void removeItem(int index) {
        this.getBasket_Items().remove(index);
        recalculateTotalPrice();
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

    public double getTotalPrice(){
        return this.totalPrice;
    }


}
