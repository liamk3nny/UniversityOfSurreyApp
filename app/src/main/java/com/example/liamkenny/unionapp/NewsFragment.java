package com.example.liamkenny.unionapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


public class NewsFragment extends Fragment {
    private static final String TAG = "NewsFragment";
    private static final String KEY_LAYOUT_MANAGER = "layoutManager";
    private static final int DATASET_COUNT = 3;

    protected LayoutManagerType mCurrentLayoutManagerType;
    protected RecyclerView mRecyclerView;
    protected NewsAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;

    protected String[] mNewsNames;
    protected String[] mNewsInfo;
    protected String[] mnewsDates;
    private FirebaseAuth firebaseAuth;
    private String userID;
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater
                .inflate(R.layout.fragment_news, container, false);
        rootView.setTag(TAG);

        //firebase user setup
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser fbUser = firebaseAuth.getCurrentUser();
        userID = fbUser.getUid();

        //Setup Firestore settings
        db.setFirestoreSettings(settings);

        initDataset();

        // BEGIN_INCLUDE(initializeRecyclerView)
        mRecyclerView = rootView.findViewById(R.id.news_recycler_view);

        // LinearLayoutManager is used here, this will layout the elements in a similar fashion
        // to the way ListView would layout elements. The RecyclerView.LayoutManager defines how
        // elements are laid out.
        mLayoutManager = new LinearLayoutManager(getActivity());

        setRecyclerViewLayoutManager();

        mAdapter = new NewsAdapter(mNewsNames, mNewsInfo);
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
        mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(scrollPosition);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save currently selected layout manager.
        savedInstanceState.putSerializable(KEY_LAYOUT_MANAGER, mCurrentLayoutManagerType);
        super.onSaveInstanceState(savedInstanceState);
    }

    /**
     * Generates Strings for RecyclerView's adapter. This data would usually come
     * from a local content provider or remote server.
     */
    private void initDataset() {
        mNewsNames = new String[DATASET_COUNT];
        mNewsInfo = new String[DATASET_COUNT];
        db.collection("News")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int i = 0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                String newsTitle = (String) document.getData().get("Title");
                                String newsSubTitle = (String) document.getData().get("SubTitle");
                                mNewsNames[i] = newsTitle;
                                mNewsInfo[i] = newsSubTitle;;
                                i++;
                            }

                            mAdapter.notifyDataSetChanged();
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

    }
}
