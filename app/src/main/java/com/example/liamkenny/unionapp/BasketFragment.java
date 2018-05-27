package com.example.liamkenny.unionapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.wallet.AutoResolveHelper;
import com.google.android.gms.wallet.PaymentData;
import com.google.android.gms.wallet.PaymentDataRequest;
import com.google.android.gms.wallet.PaymentMethodToken;
import com.google.android.gms.wallet.PaymentsClient;
import com.google.android.gms.wallet.TransactionInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class BasketFragment extends Fragment {

    private  TextView totalPriceView;
    private dbHelper database;
    private Basket basket;
    private ArrayList<Basket_Product> products;

    private long totalPrice;
    private PaymentsClient mPaymentsClient;
    private static final int LOAD_PAYMENT_DATA_REQUEST_CODE = 991;

    private static final String TAG = "BasketFragment";
    private static final String KEY_LAYOUT_MANAGER = "layoutManager";


    protected BasketFragment.LayoutManagerType mCurrentLayoutManagerType;
    protected RecyclerView mRecyclerView;
    protected BasketItemAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    private Button checkoutButton;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
            .setTimestampsInSnapshotsEnabled(true)
            .build();
    private String userID;
    private String email;
    private String username;
    private Fragment fragment;

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
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser fbUser = firebaseAuth.getCurrentUser();
        userID = fbUser.getUid();

        DocumentReference docRef = db.collection("Student").document(userID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        Map userInfo = document.getData();
                        username = userInfo.get("Forename") + " " + userInfo.get("Surname");
                        email = userInfo.get("Username") + "@surrey.ac.uk";

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });


        checkoutButton = rootView.findViewById(R.id.CHECKOUT_BUTTON);
        checkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(basket.getBasket_Items().isEmpty()){
                    Toast.makeText(getActivity(), "Your Basket is currently empty", Toast.LENGTH_LONG).show();
                }else{
                    requestPayment(view);
                }




            }
        });


        mLayoutManager = new LinearLayoutManager(getActivity());

        setRecyclerViewLayoutManager();


        // Set CustomAdapter as the adapter for RecyclerView.

        database = dbHelper.getInstance(getActivity());
        basket = database.extractBasketFromDB();
        products = basket.getBasket_Items();
        mAdapter = new BasketItemAdapter(products, this);
        mRecyclerView.setAdapter(mAdapter);
        this.setNewPrice(basket.getTotalPrice());
        mPaymentsClient = PaymentsUtil.createPaymentsClient(getActivity());
        checkIsReadyToPay();
        return rootView;
    }


    private void checkIsReadyToPay() {
        // The call to isReadyToPay is asynchronous and returns a Task. We need to provide an
        // OnCompleteListener to be triggered when the result of the call is known.
        PaymentsUtil.isReadyToPay(mPaymentsClient).addOnCompleteListener(
                new OnCompleteListener<Boolean>() {
                    public void onComplete(Task<Boolean> task) {
                        try {
                            boolean result = task.getResult(ApiException.class);


                        } catch (ApiException exception) {
                            // Process error
                            Log.w("isReadyToPay failed", exception);
                        }
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case LOAD_PAYMENT_DATA_REQUEST_CODE:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        PaymentData paymentData = PaymentData.getFromIntent(data);
                        handlePaymentSuccess(paymentData);
                        break;
                    case Activity.RESULT_CANCELED:
                        // Nothing to here normally - the user simply cancelled without selecting a
                        // payment method.
                        break;
                    case AutoResolveHelper.RESULT_ERROR:
                        Status status = AutoResolveHelper.getStatusFromIntent(data);
                        handleError(status.getStatusCode());
                        break;
                }

                // Re-enables the Pay with Google button.
                checkoutButton.setClickable(true);
                break;
        }
    }

    public void requestPayment(View view) {
        // Disables the button to prevent multiple clicks.
        checkoutButton.setClickable(false);

        // The price provided to the API should include taxes and shipping.
        // This price is not displayed to the user.
        totalPrice = (long)basket.getTotalPrice() * 1000000;
        String price = PaymentsUtil.microsToString(totalPrice*1000000);

        TransactionInfo transaction = PaymentsUtil.createTransaction(price);
        PaymentDataRequest request = PaymentsUtil.createPaymentDataRequest(transaction);
        Task<PaymentData> futurePaymentData = mPaymentsClient.loadPaymentData(request);


        // Since loadPaymentData may show the UI asking the user to select a payment method, we use
        // AutoResolveHelper to wait for the user interacting with it. Once completed,
        // onActivityResult will be called with the result.
        AutoResolveHelper.resolveTask(futurePaymentData, this.getActivity(), LOAD_PAYMENT_DATA_REQUEST_CODE);
        checkoutButton.setClickable(true);
    }
    private void handlePaymentSuccess(PaymentData paymentData) {
        // PaymentMethodToken contains the payment information, as well as any additional
        // requested information, such as billing and shipping address.
        //


        // Refer to your processor's documentation on how to proceed from here.
        PaymentMethodToken token = paymentData.getPaymentMethodToken();

        // getPaymentMethodToken will only return null if PaymentMethodTokenizationParameters was
        // not set in the PaymentRequest.
        if (token != null) {
            // If the gateway is set to example, no payment information is returned - instead, the
            // token will only consist of "examplePaymentMethodToken".
            if (token.getToken().equals("examplePaymentMethodToken")) {
                String prods = "    ";
                for(Basket_Product p: basket.getBasket_Items()){
                    prods += "\n    ";
                    prods += p.getProduct().getProductName();
                }

                AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                        .setTitle("Thank you for your order")
                        .setMessage("Payment processed: " + "\n"
                                + "\n   Paid: £" + this.round(basket.getTotalPrice(), 2) + "\n"
                                + "\n   Items as follows: \n"
                                +   prods
                        )
                        .setPositiveButton("OK", null)
                        .create();
                alertDialog.show();
            }

            String billingName = paymentData.getCardInfo().getBillingAddress().getName();
            Toast.makeText(getActivity(), getString(R.string.payments_show_name, billingName), Toast.LENGTH_LONG).show();
            String timeStamp = new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime());
            HashMap<String, Object> data = new HashMap<>();
            HashMap<String, Object> prods = new HashMap<>();
            int counter = 1;
            for(Basket_Product b: basket.getBasket_Items()){
                prods.put("Product " + counter, b.getProduct().getProductName() );
                counter++;
            }

            data.put("user_email",email );
            data.put("price", basket.getTotalPrice());
            data.put("date", timeStamp );
            data.put("items", prods);
            data.put("payment_method", "GooglePay");

            double time = System.currentTimeMillis();
            db.collection("Order")
                    .add(data);

            // Use token.getToken() to get the token string.
            Log.d("PaymentData", "PaymentMethodToken received");
            Activity activity = getActivity();

            basket.clearBasket();
            database.clearAll();

            try {
                fragment = ShopFragment.class.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
            //uncheckItems();
            FragmentManager fragmentManager = getFragmentManager();

            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_right);
            fragmentTransaction.replace(R.id.fragment_layout, fragment);
            fragmentTransaction.addToBackStack(fragment.toString());
            fragmentTransaction.commit();

        }
    }

    private void handleError(int statusCode) {
        // At this stage, the user has already seen a popup informing them an error occurred.
        // Normally, only logging is required.
        // statusCode will hold the value of any constant from CommonStatusCode or one of the
        // WalletConstants.ERROR_CODE_* constants.
        Log.w("loadPaymentData failed", String.format("Error code: %d", statusCode));
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
        price = this.round(price, 2);
        totalPriceView.setText("£" + String.valueOf(price));
    }

    public void updateBasket(Basket b){
        this.basket = b;
    }

    public double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }









}
