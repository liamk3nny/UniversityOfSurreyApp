package com.example.liamkenny.unionapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private ArrayList<Event> events = new ArrayList<>();

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

        //getEvents();

        setupView();
    }

    private void getEvents() {
        events.clear();
        db.collection("Event")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                String eventName = (String) document.getData().get("EventName");
                                String eventInfo = (String) document.getData().get("Description");
                                String socID = (String) document.getData().get("SocID");
                                String startTime = (String) document.getData().get("StartTime");
                                String endTime = (String) document.getData().get("EndTime");
                                String location = (String) document.getData().get("Location");
                                Event event = new Event(eventName, eventInfo, socID, startTime, endTime, location);
                                events.add(event);

                            }

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

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
        drawer = (NavigationView) findViewById(R.id.navigation_view);
        View headerView = drawer.getHeaderView(0);
        final TextView nav_username = (TextView) headerView.findViewById(R.id.user_name);
        final TextView nav_email = (TextView) headerView.findViewById(R.id.user_email);

        DocumentReference docRef = db.collection("Student").document(userID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        userInfo = document.getData();
                        String username = userInfo.get("Forename") + " " + userInfo.get("Surname");
                        String email = userInfo.get("Username") + "@surrey.ac.uk";
                        nav_username.setText(username);
                        nav_email.setText(email);
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setupDrawerContent(drawer);

        profileTab = (LinearLayout) findViewById(R.id.profile_layout);
        profilePic = (ImageView) findViewById(R.id.imgProfile);

        headerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = null;
                Class fragmentClass = ProfileFragment.class;

                if (fragmentClass != null) {

                    try {
                        fragment = (Fragment) fragmentClass.newInstance();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                    fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_right);
                    fragmentTransaction.add(R.id.fragment_layout, fragment);
                    fragmentTransaction.addToBackStack(fragment.toString());
                    fragmentTransaction.commit();
                }
                drawerLayout.closeDrawers();
            }
        });
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

            case R.id.shop:
                fragmentClass = ShopFragment.class;
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

            unCheckAllMenuItems(drawer.getMenu());

            item.setChecked(true);

        } else if (activity == "signout") {
            confSignout();
        } else if (activity == "home") {
            Intent homeIntent = new Intent(this, MainActivity.class);
            startActivity(homeIntent);


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

    private void unCheckAllMenuItems(@NonNull final Menu menu) {
        int size = menu.size();
        for (int i = 0; i < size; i++) {
            final MenuItem item = menu.getItem(i);
            if(item.hasSubMenu()) {
                // Un check sub menu items
                unCheckAllMenuItems(item.getSubMenu());
            } else {
                item.setChecked(false);
            }
        }
    }


}
