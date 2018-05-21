package com.example.liamkenny.unionapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class ShopItemAdapter extends RecyclerView.Adapter<ShopItemAdapter.MyViewHolder> {

    private ArrayList<Product> productsList;

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView name;
        private TextView price;
        private TextView category;

        public MyViewHolder(View view){
            super(view);
            name = (TextView) view.findViewById(R.id.product_name);
            price = (TextView) view.findViewById(R.id.product_price);
            category = (TextView) view.findViewById(R.id.product_cat);
        }
    }

    public ShopItemAdapter(ArrayList<Product> prods){
        this.productsList = prods;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.shop_item_row, parent, false);
        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position){
        Product prod = productsList.get(position);
        holder.name.setText(prod.getProductName());
        holder.price.setText("£" + Double.toString(prod.getProductPrice()));
        holder.category.setText(prod.getProductType());
    }

    @Override
    public int getItemCount(){
        return productsList.size();
    }

}
