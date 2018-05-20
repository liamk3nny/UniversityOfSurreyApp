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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "tag";
    //Hamburger Menu items
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView drawer;
    private LinearLayout profileTab;
    private ImageView profilePic;

    //Firebase Items
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
            .setTimestampsInSnapshotsEnabled(true)
            .build();
    private String userID;
    private Map userInfo;

    //Tab layout items
    private TabLayout tabLayout;
    private ViewPager viewPager;


    private boolean doubleBackPress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //firebase user setup
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser fbUser = firebaseAuth.getCurrentUser();
        userID = fbUser.getUid();

        //Setup Firestore settings
        db.setFirestoreSettings(settings);
        DocumentReference docRef = db.collection("Student").document(userID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        userInfo = document.getData();
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
        //getStudentDoc();

        setupView();
    }

    /*
        Method used to setup views when activity is loaded
     */
    private void setupView() {

        //Layout items for tabbed fragments
        tabLayout = (TabLayout) findViewById(R.id.home_tab_layout);
        viewPager = (ViewPager) findViewById(R.id.home_page_viewer);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());


        //Adding Fragments to the tabs
        adapter.addFragment(new UpcomingEventsFragment(), "Upcoming Events");
        adapter.addFragment(new NewsFragment(), "News");

        //Setting up adapter
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);


        //Hamburger menu items
        NavigationView drawer = (NavigationView) findViewById(R.id.navigation_view);
        View headerView = drawer.getHeaderView(0);
        TextView nav_username = (TextView) headerView.findViewById(R.id.user_name);
        TextView nav_email = (TextView) headerView.findViewById(R.id.user_email);
        Log.d("Navbar", "intialiing nav bar");
        //if (userInfo.containsKey("Forename") && userInfo.containsKey("Surname")) {
        //    String username = userInfo.get("Forename") + " " + userInfo.get("Surname");
        //    nav_username.setText(username);
        //}

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setupDrawerContent(drawer);

        profileTab = (LinearLayout) findViewById(R.id.profile_layout);
        profilePic = (ImageView) findViewById(R.id.imgProfile);


    }

    /*
        Returns true if a menu item has been selected, opening the hamburger menu
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /*
        Finds which item of the hamburger menu has been selected and processes accordingly
     */
    public void selectDrawerItem(MenuItem item) {

        Fragment fragment = null;
        Class fragmentClass = null;
        String activity = null;

        //Checks which menu item has been selected
        switch (item.getItemId()) {
            case R.id.home:
                activity = "home";
                break;

            case R.id.profile:
                fragmentClass = ProfileFragment.class;

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

        //Processes the action based upon which item was selected, if any
        if (fragmentClass != null) {

            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
            //uncheckItems();

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_right);
            fragmentTransaction.add(R.id.fragment_layout, fragment);
            fragmentTransaction.addToBackStack(fragment.toString());
            fragmentTransaction.commit();

            item.setChecked(true);


            Toast.makeText(this, "Switching Fragment.", Toast.LENGTH_SHORT).show();
        } else if (activity == "signout") {
            confSignout();
        } else if (activity == "home") {
            //Intent homeIntent = new Intent(MainActivity.this, MainActivity.class);
            //startActivity(homeIntent);

        }
        drawerLayout.closeDrawers();
    }


    /*
        Sets up the content of the hamburger menu
     */
    private void setupDrawerContent(final NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectDrawerItem(item);
                return true;
            }
        });
    }

    /*
        Override of method when back button is pressed
        - closes drawers if open
        - checks for double press before closing app
     */
    @Override
    public void onBackPressed() {
        if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawers();
        } else {
            super.onBackPressed();
        }
    }

    /*
        Signs the user out of the app and on firebase
        - called if the user confirms signout from method below
     */
    private void signOut() {
        firebaseAuth.signOut();
        Toast.makeText(this, "Signing out.", Toast.LENGTH_SHORT).show();
        finish();
        Intent logoutIntent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(logoutIntent);
    }

    /*
        Checks whether the user is sure that they wish to sign out
        before calling the method to sign the user out from firebase.
     */
    public void confSignout() {
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

    public void getStudentDoc() {

    }

    /*
    //TODO: find a way to uncheck items in the menu
    //drawer isnt initialised...
    public void uncheckItems(){
        int size = drawer.getMenu().size();
        for (int i = 0; i < size; i++) {
            drawer.getMenu().getItem(i).setChecked(false);
        }
    }
    */


}
