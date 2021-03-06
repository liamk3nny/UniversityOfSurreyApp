package com.example.liamkenny.unionapp;

import android.content.Intent;
import android.media.Image;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private FirebaseAuth firebaseAuth;
    private NavigationView drawer;
    private TextView name;
    private TextView email;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupView();

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser fbUser = firebaseAuth.getCurrentUser();


    }

    private void setupView(){
        name = (TextView)findViewById(R.id.user_name);
        email = (TextView)findViewById(R.id.user_email);





        NavigationView drawer = (NavigationView)findViewById(R.id.navigation_view);
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setupDrawerContent(drawer);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void selectDrawerItem(MenuItem item){

        Fragment fragment = null;
        Class fragmentClass = null;
        boolean signout = false;
        switch(item.getItemId()){
            case R.id.shop:
                break;

            case R.id.events:
                break;

            case R.id.societies:
                break;

            case R.id.your_union:
                fragmentClass = YourUnionFragment.class;
                break;

            case R.id.voice:
                fragmentClass = VoiceFragment.class;
                break;

            case R.id.activity:
                fragmentClass = ActivityFragment.class;
                break;

            case R.id.support:
                fragmentClass = SupportFragment.class;
                break;

            case R.id.community:
                fragmentClass = CommunityFragment.class;
                break;

            case R.id.signout:
                signout = true;
                break;


        }


        if(fragmentClass!=null){

            try {
                fragment = (Fragment) fragmentClass.newInstance();
            }
            catch (Exception e){
                e.printStackTrace();
            }

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fragment_layout,fragment).commit();

            item.setChecked(false);
            //

            Toast.makeText(this, "Switching Fragment.", Toast.LENGTH_SHORT).show();
        }else if(signout){
            signOut();
        }
        drawerLayout.closeDrawers();
    }

    private void signOut(){
        firebaseAuth.signOut();
        Toast.makeText(this, "Signing out.", Toast.LENGTH_SHORT).show();
        finish();
        Intent logoutIntent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(logoutIntent);
    }



    private void setupDrawerContent(NavigationView navigationView){
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                selectDrawerItem(item);
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(this.drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawers();
        }else{
            super.onBackPressed();
        }

    }




}
