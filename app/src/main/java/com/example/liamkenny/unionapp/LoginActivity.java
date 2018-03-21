package com.example.liamkenny.unionapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText mURN;
    private EditText mPassword;
    private Button mLogin;
    private TextView mRegister;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    private boolean doubleBackPress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setupView();
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser fbUser = firebaseAuth.getCurrentUser();
        firebaseAuth.signOut();

        progressDialog = new ProgressDialog(this);


        /*
            Listener for login button
            - logs in user if credentials are correct
            - error message for incorrect credentials
            - error message for incomplete form
         */
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String URN = mURN.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                String email = URN + "@surrey.ac.uk".trim();

                //Checks if the user has entered credentials
                if(URN.isEmpty() || password.isEmpty()){
                    Toast.makeText(LoginActivity.this, "Please fill in your credentials.", Toast.LENGTH_SHORT).show();
                    mPassword.setText("");
                }else{
                    login(email, password);
                }
            }
        });

        /*
            Opens the Register activity (to be removed at a later date)
            TODO: Remove this functionality once app is ready
         */
        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(registerIntent);
            }
        });
    }

    /*
        Method used to setup views when activity is loaded
     */
    private void setupView(){
        mURN = (EditText) findViewById(R.id.etUsername);
        mPassword = (EditText)findViewById(R.id.etPassword);
        mLogin = (Button)findViewById(R.id.btnLogin);
        mRegister = (TextView)findViewById(R.id.tvRegister);
    }

    /*
       Logs the user into the app through firebase.
     */
    private void login(String URN, String password){
        String email = URN + "@surrey.ac.uk".trim();

        progressDialog.setMessage("Checking Credentials...");
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(URN, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Login Successful.", Toast.LENGTH_SHORT).show();
                    finish();
                    Intent loginIntent =  new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(loginIntent);
                }else{
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Login Failed.", Toast.LENGTH_SHORT).show();
                    mPassword.setText("");
                }
            }
        });
    }

    /*
        Override of method when back button is pressed
        - checks for double press before closing app
     */
    @Override
    public void onBackPressed() {
        if (doubleBackPress) {
                super.onBackPressed();
                return;
        }

        this.doubleBackPress = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackPress=false;
            }
        }, 2000);
    }
}
