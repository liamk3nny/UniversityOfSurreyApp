package com.example.liamkenny.unionapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import java.util.Map;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
            .setTimestampsInSnapshotsEnabled(true)
            .build();
    private String userID;
    private TextView firstnameText;
    private TextView surnameText;
    private TextView yearText;
    private TextView urnText;
    private TextView usernameText;
    private TextView phoneText;
    private TextView houseText;
    private TextView postCodeText;
    private Button saveButton;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);


        //firebase user setup
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser fbUser = firebaseAuth.getCurrentUser();
        userID = fbUser.getUid();

        //Setup Firestore settings
        db.setFirestoreSettings(settings);

        firstnameText = view.findViewById(R.id.profile_firstname);
        surnameText = view.findViewById(R.id.profile_surname);
        yearText = view.findViewById(R.id.profile_year);
        usernameText = view.findViewById(R.id.profile_username);
        urnText = view.findViewById(R.id.profile_urn);
        phoneText = view.findViewById(R.id.profile_phone);
        houseText = view.findViewById(R.id.profile_house);
        postCodeText = view.findViewById(R.id.profile_postcode);
        saveButton = view.findViewById(R.id.profile_save);

        final DocumentReference docRef = db.collection("Student").document(userID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        Map userInfo = document.getData();
                        String firstname = (String) userInfo.get("Forename");
                        String surname = (String) userInfo.get("Surname");
                        String year = (String) userInfo.get("AcademicYear");
                        String username = (String) userInfo.get("Username");
                        String urn = (String) userInfo.get("URN");
                        String phone = (String) userInfo.get("ContactNumber");
                        String house = (String) userInfo.get("Housenumber");
                        String postCode = (String) userInfo.get("PostCode");
                        firstnameText.setText(firstname);
                        surnameText.setText(surname);
                        yearText.setText(year);
                        usernameText.setText(username);
                        urnText.setText(urn);
                        phoneText.setText(phone);
                        houseText.setText(house);
                        postCodeText.setText(postCode);
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                docRef.update(
                        "Forename", firstnameText.getText().toString().trim(),
                        "Surname", surnameText.getText().toString().trim(),
                        "AcademicYear", yearText.getText().toString().trim(),
                        "Username", usernameText.getText().toString().trim(),
                        "URN", urnText.getText().toString().trim(),
                        "ContactNumber", phoneText.getText().toString().trim(),
                        "Housenumber", houseText.getText().toString().trim(),
                        "PostCode", postCodeText.getText().toString().trim()

                ).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            //Toast.makeText(this, "Information has been update sucessfully", Toast.LENGTH_SHORT).show();
                            //Toast.makeText()
                        }
                    }
                });
            }
        });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
