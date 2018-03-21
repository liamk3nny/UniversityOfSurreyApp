package com.example.liamkenny.unionapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

    private TabLayout tabLayout;
    private ViewPager viewPager;

    private LinearLayoutManager eventLLManager = new LinearLayoutManager(this);



    private boolean doubleBackPress;

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

        tabLayout = (TabLayout)findViewById(R.id.home_tab_layout);
        viewPager = (ViewPager)findViewById(R.id.home_page_viewer);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        //Adding Fragments to the tabs
        adapter.addFragment(new UpcomingEventsFragment(), "Upcoming Events");
        adapter.addFragment(new NewsFragment(), "News");

        //Setting up adapter
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);






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
        String activity = null;
        switch(item.getItemId()){
            case R.id.home:
                activity = "home";
                break;

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
                activity = "signout";
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
            //fragmentManager.beginTransaction().replace(R.id.fragment_layout,fragment).commit();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            fragmentTransaction.add(R.id.fragment_layout, fragment);
            fragmentTransaction.addToBackStack(fragment.toString());
            fragmentTransaction.commit();
            item.setChecked(true);


            Toast.makeText(this, "Switching Fragment.", Toast.LENGTH_SHORT).show();
        }else if(activity == "signout"){
            confSignout();
        }else if(activity == "home"){
            //Intent homeIntent = new Intent(MainActivity.this, MainActivity.class);
            //startActivity(homeIntent);

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
        }else if(getSupportFragmentManager().getBackStackEntryCount() != 1){
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

        }else{
            super.onBackPressed();
        }

    }

    public void confSignout(){


        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
        alert.setTitle("Sign Out?");
        alert.setMessage("Are you sure you want to sign out?");
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                signOut();
            }
        });

        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alert.show();
    }




}
