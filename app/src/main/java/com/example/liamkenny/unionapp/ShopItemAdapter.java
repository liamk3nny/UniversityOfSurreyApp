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

public class ShopItemAdapter extends RecyclerView.Adapter<ShopItemAdapter.MyViewHolder> {

    private ArrayList<Product> productsList;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView price;
        private TextView category;
        private ImageButton addItem;


        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.product_name);
            price = view.findViewById(R.id.product_price);
            category = view.findViewById(R.id.product_cat);
            addItem = view.findViewById(R.id.shop_item_add);

        }
    }

    public ShopItemAdapter(ArrayList<Product> prods) {
        this.productsList = prods;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.shop_item_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Product prod = productsList.get(position);
        holder.name.setText(prod.getProductName());
        holder.price.setText("£" + Double.toString(prod.getProductPrice()));
        holder.category.setText(prod.getProductType());

        holder.addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper db = dbHelper.getInstance(context);

                if (db.isProductInDB(prod)) {
                    Toast.makeText(context, "Added " + prod.getProductName() + " to basket", Toast.LENGTH_SHORT).show();
                    db.updateProductQuantity(prod, true);
                } else if(!db.isProductInDB(prod)){

                    Toast.makeText(context, "Added " + prod.getProductName() + " to basket", Toast.LENGTH_SHORT).show();
                    db.addProductToBasket(prod);

                }
            }
        });
        /*holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {




                return true;
            }
        });*/

    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }

}
