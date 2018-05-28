package com.example.liamkenny.unionapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class BasketItemAdapter extends RecyclerView.Adapter<BasketItemAdapter.MyViewHolder>{

    private Basket basket;
    private ArrayList<Basket_Product> basket_items;
    private BasketFragment b;

    private dbHelper db;
    private Context context;
    public class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView name;
        private TextView price;
        private ImageButton deleteItem;


        public MyViewHolder(View view){
            super(view);
            name = view.findViewById(R.id.product_name_b);
            price = view.findViewById(R.id.product_price_b);
            deleteItem = view.findViewById(R.id.basket_delete);

        }

    }

    public BasketItemAdapter(ArrayList<Basket_Product> basket_is, BasketFragment b){
        this.basket_items = basket_is;
        this.basket = new Basket(basket_is);
        this.b = b;

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        context = parent.getContext();
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.basket_item_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Basket_Product prod = basket_items.get(position);
        holder.name.setText(prod.getProduct().getProductName());
        holder.price.setText("£" + Double.toString(prod.getProduct().getProductPrice()));

        holder.deleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, "Removed " + prod.getProduct().getProductName() + " from basket.", Toast.LENGTH_SHORT).show();add
                basket.removeItem(position);
                basket_items = basket.getBasket_Items();
                basket.recalculateTotalPrice();

                notifyItemRemoved(position);
                notifyItemRangeChanged(position, basket_items.size());
                notifyDataSetChanged();
                b.setNewPrice(basket.getTotalPrice());
                b.updateBasket(basket);
                db = dbHelper.getInstance(context);
                db.updateProductQuantity(prod.getProduct(), false);

            }
        });



        /*holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {



            }
        });*/

    }

    public Basket getBasket(){
        return this.basket;
    }

    @Override
    public int getItemCount(){
        return basket_items.size();
    }

}
