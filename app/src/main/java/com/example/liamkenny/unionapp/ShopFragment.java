package com.example.liamkenny.unionapp;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class ShopFragment extends Fragment {

    private ArrayList<Product> products = new ArrayList<Product>();



    private static final String TAG = "ShopFragment";
    private static final String KEY_LAYOUT_MANAGER = "layoutManager";


    protected ShopFragment.LayoutManagerType mCurrentLayoutManagerType;
    protected RecyclerView mRecyclerView;
    protected ShopItemAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    private TextView title;
    private ImageButton basketButton;
    private Fragment fragment;
    private FirebaseAuth firebaseAuth;
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
        setupList();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "");
        View rootView = inflater
                .inflate(R.layout.fragment_shop, container, false);
        rootView.setTag(TAG);

        // BEGIN_INCLUDE(initializeRecyclerView)
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.shopRecycler);
        basketButton = (ImageButton) rootView.findViewById(R.id.basketButton);

        basketButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Activity activity = getActivity();
                Toast.makeText(activity,"This will replace current fragment with the basket fragment!",Toast.LENGTH_SHORT).show();
                /**
                try {
                    fragment = (Fragment) BasketFragment.class.newInstance();
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                //uncheckItems();
                FragmentManager fragmentManager = getFragmentManager();

                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_right);
                fragmentTransaction.replace(R.id.fragment_layout, fragment);
                fragmentTransaction.addToBackStack(fragment.toString());
                fragmentTransaction.commit();
                 **/
            }
        });
        // LinearLayoutManager is used here, this will layoutt the elements in a similar fashion
        // to the way ListView would layout elements. The RecyclerView.LayoutManager defines how
        // elements are laid out.
        mLayoutManager = new LinearLayoutManager(getActivity());

        setRecyclerViewLayoutManager();

        mAdapter = new ShopItemAdapter(products);
        // Set CustomAdapter as the adapter for RecyclerView.
        mRecyclerView.setAdapter(mAdapter);
        // END_INCLUDE(initializeRecyclerView)

        return rootView;
    }

    public void setRecyclerViewLayoutManager(){
        int scrollPosition = 0;

        // If a layout manager has already been set, get current scroll position.
        if (mRecyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }

        mLayoutManager = new LinearLayoutManager(getActivity());
        mCurrentLayoutManagerType = ShopFragment.LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(scrollPosition);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save currently selected layout manager.
        savedInstanceState.putSerializable(KEY_LAYOUT_MANAGER, mCurrentLayoutManagerType);
        super.onSaveInstanceState(savedInstanceState);
    }

    private void setupList(){


        db.collection("Product")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                String id = document.getId();
                                Map data = document.getData();
                                String name = document.getString("Product_name");
                                String cat = document.getString("ProductType");
                                double price = document.getDouble("Price");
                                Product prod = new Product(id,name,cat,price);

                                products.add(prod);
                                mAdapter.notifyDataSetChanged();

                                Log.d(TAG, products.get(0).getProductID());

                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });


        //TODO remove when sample size is bigger
        products.add(new Product("1", "Hoodie", "Hoodie", 25));

        products.add(new Product("2", "Baseball Cap", "Hoodie", 25));
        products.add(new Product("3", "CompSOC Beanie", "BEANIE", 25));
        products.add(new Product("4", "Blouse", "Hoodie", 25));
        products.add(new Product("5", "Flat Cap", "BEANIE", 25));
        products.add(new Product("6", "Jeans", "Hoodie", 25));
        products.add(new Product("7", "Headband", "BEANIE", 25));
        products.add(new Product("8", "Scarf", "Hoodie", 25));
        products.add(new Product("9", "Socks", "BEANIE", 25));



    }

}
