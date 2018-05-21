package com.example.liamkenny.unionapp;

public class Basket_Product {

    //TODO: Add validations w/ try & exceptions

    private Product product;
    private int quantity;
    private double totalCost;

    public Basket_Product(Product item, int quant) {
        this.product = item;
        this.quantity = quant;
        double price = item.getProductPrice() * quant;
        this.totalCost = price;
    }

    private void setCost(double cost) {
        this.totalCost = cost;
    }

    private void setQuantity(int quantity) {
        this.quantity = quantity;
        double cost = this.getProduct().getProductPrice() * this.getQuantity();
        this.setCost(cost);
    }

    public int getQuantity() {
        return this.quantity;
    }


    public void incQuantity() {
        int original = this.getQuantity();
        original ++;
        this.setQuantity(original);
    }

    public void decQuantity() {
        int original = this.getQuantity();
        original --;
        this.setQuantity(original);
    }

    public Product getProduct() {
        return this.product;
    }

    public double getTotalCost() {
        return this.totalCost;
    }
}
