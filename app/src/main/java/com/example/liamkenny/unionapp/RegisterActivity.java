package com.example.liamkenny.unionapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import java.util.HashMap;
import java.util.Map;
import java.lang.Object;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {


    private static final String TAG = "Register Activity";
    private EditText mFirstname;
    private EditText mSurname;
    private EditText mUsername;
    private EditText mPassword1;
    private EditText mPassword2;
    private Button mRegister;
    private TextView mAccount;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
            .setTimestampsInSnapshotsEnabled(true)
            .build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        db.setFirestoreSettings(settings);
        setupView();
        firebaseAuth = FirebaseAuth.getInstance();
        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()){
                    final String firstname = mFirstname.getText().toString().trim();
                    final String surname = mSurname.getText().toString().trim();
                    final String username = mUsername.getText().toString().trim();
                    String email = username + "@surrey.ac.uk".trim();
                    String password1 = mPassword1.getText().toString().trim();

                    //TODO Create User in Database
                    firebaseAuth.createUserWithEmailAndPassword(email, password1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                Log.d(TAG, "createUserWithEmail:succes");
                                //Create User in database
                                FirebaseUser user = firebaseAuth.getCurrentUser();
                                String userID = user.getUid();
                                Map<String, Object> studentData = new HashMap<>();
                                studentData.put("Forename", firstname);
                                studentData.put("Surname", surname);
                                studentData.put("Username", username);
                                studentData.put("URN", "");
                                studentData.put("AcademicYear", "");
                                studentData.put("ContactNumber", "");
                                studentData.put("Course", "");
                                studentData.put("Housenumber", "");
                                studentData.put("PostCode", "");
                                db.collection("Student")
                                        .document(userID)
                                        .set(studentData)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "Document written");
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Error adding document", e);
                                    }
                                });

                                Toast.makeText(RegisterActivity.this, "Registration Complete.", Toast.LENGTH_SHORT).show();
                                Intent registerIntent = new Intent(RegisterActivity.this, MainActivity.class);
                                startActivity(registerIntent);
                            }else{
                                Toast.makeText(RegisterActivity.this, "Registration Failed.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


                }

            }
        });

        mAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(loginIntent);
            }
        });
    }

    private void setupView(){
        mFirstname = (EditText)findViewById(R.id.etFirstname);
        mSurname = (EditText)findViewById(R.id.etSurname);
        mUsername = (EditText)findViewById(R.id.etUsername);
        mPassword1 = (EditText)findViewById(R.id.etPassword1);
        mPassword2 = (EditText)findViewById(R.id.etPassword2);
        mRegister = (Button)findViewById(R.id.btnRegister);
        mAccount = (TextView)findViewById(R.id.tvAccount);
    }

    private boolean validate(){
        boolean valid = false;
        String firstname = mFirstname.getText().toString();
        String surname = mSurname.getText().toString();
        String username = mUsername.getText().toString();
        String password1 = mPassword1.getText().toString().trim();
        String password2 = mPassword2.getText().toString().trim();
        String usernameCheck = "[a-z]{2}[0-9]{5}";

        if(firstname.isEmpty() || surname.isEmpty() || username.isEmpty() || password1.isEmpty() || password2.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "Please complete all fields.", Toast.LENGTH_SHORT).show();
            mPassword1.setText("");
            mPassword2.setText("");
        } else if (!Pattern.matches(usernameCheck, username)){
            Toast.makeText(RegisterActivity.this, "Invalid username - should follow ab01234", Toast.LENGTH_LONG).show();
            mUsername.setText("");
            mPassword1.setText("");
            mPassword2.setText("");
        } else if (!password1.equals(password2)) {
            Toast.makeText(RegisterActivity.this, "Passwords don't match", Toast.LENGTH_SHORT).show();
            mPassword1.setText("");
            mPassword2.setText("");
        } else {
            valid = true;
        }
        return valid;
    }
}
