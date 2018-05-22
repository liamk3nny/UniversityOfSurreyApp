package com.example.liamkenny.unionapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class BasketItemAdapter extends RecyclerView.Adapter<BasketItemAdapter.MyViewHolder>{

    private Basket basket;
    private ArrayList<Basket_Product> basket_items;

    private Context context;
    public class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView name;
        private TextView price;


        public MyViewHolder(View view){
            super(view);
            name = view.findViewById(R.id.product_name_b);
            price = view.findViewById(R.id.product_price_b);

        }

    }

    public BasketItemAdapter(ArrayList<Basket_Product> basket_is){this.basket_items = basket_is;}


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        context = parent.getContext();
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.basket_item_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Basket_Product prod = basket_items.get(position);
        holder.name.setText(prod.getProduct().getProductName());
        holder.price.setText("£" + Double.toString(prod.getProduct().getProductPrice()));

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                Toast.makeText(context, "Removed " + prod.getProduct().getProductName() + " from basket.", Toast.LENGTH_SHORT).show();
                //TODO add to basket on click
                basket_items.remove(position);
                notifyDataSetChanged();
                return true;
            }
        });

    }

    public Basket getBasket(){
        return this.basket;
    }

    @Override
    public int getItemCount(){
        return basket_items.size();
    }

}
