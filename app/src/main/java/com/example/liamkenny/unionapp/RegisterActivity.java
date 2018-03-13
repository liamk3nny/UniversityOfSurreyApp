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

public class RegisterActivity extends AppCompatActivity {

    private EditText mName;
    private EditText mURN;
    private EditText mPassword1;
    private EditText mPassword2;
    private Button mRegister;
    private TextView mAccount;
    private FirebaseAuth firebaseAuth;

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
                    String name = mName.getText().toString().trim();
                    String URN = mURN.getText().toString().trim();
                    String email = URN + "@surrey.ac.uk".trim();
                    String password1 = mPassword1.getText().toString().trim();

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
        mName = (EditText)findViewById(R.id.etName);
        mURN = (EditText)findViewById(R.id.etURN);
        mPassword1 = (EditText)findViewById(R.id.etPassword1);
        mPassword2 = (EditText)findViewById(R.id.etPassword2);
        mRegister = (Button)findViewById(R.id.btnRegister);
        mAccount = (TextView)findViewById(R.id.tvAccount);
    }

    private boolean validate(){
        boolean valid = false;
        String name = mName.getText().toString();
        String URN = mURN.getText().toString();
        String password1 = mPassword1.getText().toString().trim();
        String password2 = mPassword2.getText().toString().trim();
        if(name.isEmpty() || URN.isEmpty() || password1.isEmpty() || password2.isEmpty()){
            Toast.makeText(RegisterActivity.this, "Please complete all fields.", Toast.LENGTH_SHORT).show();
            mPassword1.setText("");
            mPassword2.setText("");
        }else{
            valid = true;
        }
        return valid;
    }
}
