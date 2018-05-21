package com.example.liamkenny.unionapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ShopItemAdapter extends RecyclerView.Adapter<ShopItemAdapter.MyViewHolder> {

    private ArrayList<Product> productsList;
    private Context context;
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
        context = parent.getContext();
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.shop_item_row, parent, false);
        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position){
        final Product prod = productsList.get(position);
        holder.name.setText(prod.getProductName());
        holder.price.setText("£" + Double.toString(prod.getProductPrice()));
        holder.category.setText(prod.getProductType());
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View view) {
                Toast.makeText(context, "Selected item " + position + "is: " + productsList.get(position).getProductName() , Toast.LENGTH_SHORT).show();

                return true;
            }
        });

    }

    @Override
    public int getItemCount(){
        return productsList.size();
    }

}
