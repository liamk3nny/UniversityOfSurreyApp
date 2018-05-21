package com.example.liamkenny.unionapp;

public class Product {

    //TODO: Add validations w/ try & exceptions

    private int productID;
    private String productName;
    private PRODUCT_TYPE productType;
    private double productPrice;


    public Product(int id, String name, PRODUCT_TYPE type, double price) {

        super();
        this.productID = id;
        this.productName = name;
        this.productType = type;
        this.productPrice = price;

    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
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

    public PRODUCT_TYPE getProductType() {

        return productType;
    }

    public void setProductType(PRODUCT_TYPE productType) {
        this.productType = productType;
    }


}
