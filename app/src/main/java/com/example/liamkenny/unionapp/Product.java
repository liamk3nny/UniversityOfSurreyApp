package com.example.liamkenny.unionapp;

public class Product {

    //TODO: Add validations w/ try & exceptions

    private String productID;
    private String productName;
    private String productType;
    private double productPrice;

    public Product(){

    }

    public Product(String id, String name, String type, double price) {

        super();
        this.productID = id;
        this.productName = name;
        this.productType = type;
        this.productPrice = price;

    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }


    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductType() {

        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }


}
