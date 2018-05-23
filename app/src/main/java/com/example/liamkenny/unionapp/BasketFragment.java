package com.example.liamkenny.unionapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import java.util.ArrayList;

public class BasketFragment extends Fragment {

    private  TextView totalPriceView;
    private Basket basket;
    private ArrayList<Basket_Product> products = new ArrayList<Basket_Product>();


    private static final String TAG = "BasketFragment";
    private static final String KEY_LAYOUT_MANAGER = "layoutManager";


    protected BasketFragment.LayoutManagerType mCurrentLayoutManagerType;
    protected RecyclerView mRecyclerView;
    protected BasketItemAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    private Button checkoutButton;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
            .setTimestampsInSnapshotsEnabled(true)
            .build();

    private enum LayoutManagerType {
        LINEAR_LAYOUT_MANAGER
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db.setFirestoreSettings(settings);




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater
                .inflate(R.layout.fragment_basket, container, false);
        rootView.setTag(TAG);



        totalPriceView = rootView.findViewById(R.id.TotalPriceTV);
        // BEGIN_INCLUDE(initializeRecyclerView)
        mRecyclerView = rootView.findViewById(R.id.basketRecycler);

        checkoutButton = rootView.findViewById(R.id.CHECKOUT_BUTTON);
        checkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "TOTAL PRICE IS: " + basket.getTotalPrice(), Toast.LENGTH_SHORT).show();
                for(Basket_Product b: basket.getBasket_Items()){

                    Log.d("BASKET_fRAG", "ITEMS IN BASKET INCLUDE: " + b.getProduct().getProductName());
                }
            }
        });


        mLayoutManager = new LinearLayoutManager(getActivity());

        setRecyclerViewLayoutManager();

        mAdapter = new BasketItemAdapter(products, this);
        // Set CustomAdapter as the adapter for RecyclerView.
        mRecyclerView.setAdapter(mAdapter);


        Product prod = new Product("1", "Product 1", "Hoodie", 10);
        Product prod2 = new Product("2", "Product 2", "Hoodie", 5);
        Product prod3 = new Product("3", "Product 3", "Hoodie", 30);
        Basket_Product bp = new Basket_Product(prod, 1);
        Basket_Product bp1 = new Basket_Product(prod2, 1);
        Basket_Product bp2 = new Basket_Product(prod3, 1);
        products.add(bp);
        products.add(bp1);
        products.add(bp2);
        mAdapter.notifyDataSetChanged();
        basket = new Basket(products);

        this.setNewPrice(basket.getTotalPrice());

        return rootView;
    }

    public void setRecyclerViewLayoutManager() {
        int scrollPosition = 0;

        // If a layout manager has already been set, get current scroll position.
        if (mRecyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }

        mLayoutManager = new LinearLayoutManager(getActivity());
        mCurrentLayoutManagerType = BasketFragment.LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(scrollPosition);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save currently selected layout manager.
        savedInstanceState.putSerializable(KEY_LAYOUT_MANAGER, mCurrentLayoutManagerType);
        super.onSaveInstanceState(savedInstanceState);
    }

    public  void setNewPrice(double price){
        totalPriceView.setText("£" + String.valueOf(price));
    }

    public void updateBasket(Basket b){
        this.basket = b;
    }









}
