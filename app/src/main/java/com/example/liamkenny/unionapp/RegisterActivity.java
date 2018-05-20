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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterActivity extends AppCompatActivity {


    private EditText mFirstname;
    private EditText mSurname;
    private EditText mURN;
    private EditText mPassword1;
    private EditText mPassword2;
    private Button mRegister;
    private TextView mAccount;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setupView();
        firebaseAuth = FirebaseAuth.getInstance();
        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()){
                    //TODO change strings with name
                    String firstname = mFirstname.getText().toString().trim();
                    String surname = mSurname.getText().toString().trim();
                    String URN = mURN.getText().toString().trim();
                    String email = URN + "@surrey.ac.uk".trim();
                    String password1 = mPassword1.getText().toString().trim();

                    //TODO Create User in Database
                    firebaseAuth.createUserWithEmailAndPassword(email, password1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                Toast.makeText(RegisterActivity.this, "Registration Complete.", Toast.LENGTH_SHORT).show();
                                Intent registerIntent = new Intent(RegisterActivity.this, LoginActivity.class);
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
        mURN = (EditText)findViewById(R.id.etUsername);
        mPassword1 = (EditText)findViewById(R.id.etPassword1);
        mPassword2 = (EditText)findViewById(R.id.etPassword2);
        mRegister = (Button)findViewById(R.id.btnRegister);
        mAccount = (TextView)findViewById(R.id.tvAccount);
    }

    private boolean validate(){
        boolean valid = false;
        String firstname = mFirstname.getText().toString();
        String surname = mSurname.getText().toString();
        String URN = mURN.getText().toString();
        String password1 = mPassword1.getText().toString().trim();
        String password2 = mPassword2.getText().toString().trim();
        if(firstname.isEmpty() || surname.isEmpty() || URN.isEmpty() || password1.isEmpty() || password2.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "Please complete all fields.", Toast.LENGTH_SHORT).show();
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
