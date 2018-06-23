package com.parse.starter;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile extends AppCompatActivity {

    private TextView mTextMessage;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private static boolean userPressedBackAgain = false;
    String ticketnum;
    NavigationView mSidebar;
    static String usernameprofile;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_information:
                    Profileinformation fragment1 = new Profileinformation();
                    FragmentTransaction fragmentTransaction1 = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction1.replace(R.id.content, fragment1);
                    fragmentTransaction1.commit();

                    return true;
                case R.id.navigation_newsfeed:
                    Profilenewsfeed fragment2 = new Profilenewsfeed();
                    FragmentTransaction fragmentTransaction2 = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction2.replace(R.id.content, fragment2);
                    fragmentTransaction2.commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);


        mDrawerLayout = (DrawerLayout) findViewById(R.id.container);
        Bundle extras = getIntent().getExtras();
        usernameprofile = extras.getString("username");

        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.Open, R.string.Close);
        mDrawerLayout.addDrawerListener(mToggle);
        mSidebar = (NavigationView) findViewById(R.id.navView);


        ParseUser currentuser = ParseUser.getCurrentUser();
        NavigationView navigationView =(NavigationView)findViewById(R.id.navView);
        View headerView = LayoutInflater.from(this).inflate(R.layout.header,navigationView,false);
        headerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Profile.class);
                intent.putExtra("username",ParseUser.getCurrentUser().getUsername());
                startActivity(intent);

            }
        });
        TextView name = (TextView)headerView.findViewById(R.id.nameinheader);
        CircleImageView imageheader = (CircleImageView)headerView.findViewById(R.id.profpic);
        name.setText(currentuser.getUsername());
        ParseFile image = (ParseFile) currentuser.getParseFile("profilepicture");

//call the function

        DisplayImage displayImage = new DisplayImage();
        displayImage.displayCircleImage(image,imageheader);
        navigationView.addHeaderView(headerView);

        final Menu menu = mSidebar.getMenu();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("userevent");
        query.whereEqualTo("state","approved");
        query.whereEqualTo("username",ParseUser.getCurrentUser().getUsername());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null){
                    {

                        ticketnum = String.valueOf(objects.size());

                        MenuItem tickets = menu.findItem(R.id.mytickets);
                        tickets.setTitle("My Tickets"+" ("+ticketnum+")");
                    }
                }
                else{
                    //e.printStackTrace();
                }
            }
        });
        ParseQuery<ParseObject> query3 = ParseQuery.getQuery("Events");
        query3.whereEqualTo("state","notyet");
        query3.whereContains("othermanagers",ParseUser.getCurrentUser().getUsername());
        query3.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null){
                    {

                        ticketnum = String.valueOf(objects.size());

                        MenuItem tickets = menu.findItem(R.id.management);
                        tickets.setTitle(tickets.getTitle()+" ("+ticketnum+")");
                    }
                }
                else{
                    //e.printStackTrace();
                }
            }
        });

        ParseQuery<ParseObject> query5 = ParseQuery.getQuery("userevent");
        query5.whereEqualTo("state","request");
        query5.whereEqualTo("username",ParseUser.getCurrentUser().getUsername());
        query5.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null){
                    {

                        ticketnum = String.valueOf(objects.size());

                        MenuItem tickets = menu.findItem(R.id.pending);
                        tickets.setTitle("Pending Requests "+" ("+ticketnum+")");
                    }
                }
                else{
                    //e.printStackTrace();
                }
            }
        });




        //mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationViewEx navigation = (BottomNavigationViewEx) findViewById(R.id.navigation);
        navigation.enableAnimation(false);
        navigation.enableShiftingMode(false);
        navigation.enableItemShiftingMode(false);
        navigation.setTextVisibility(true);
        navigation.setTextSize(20);
        navigation.setItemHeight(130);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        Toolbar headerup = (Toolbar)findViewById(R.id.my_toolbar);
        setSupportActionBar(headerup);


        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setupDrawerContent(mSidebar);

        getSupportActionBar().setTitle("Profile");
        getSupportActionBar().setSubtitle(null);
        Profileinformation fragment1 = new Profileinformation();
        FragmentTransaction fragmentTransaction1 = getSupportFragmentManager().beginTransaction();
        fragmentTransaction1.replace(R.id.content, fragment1);
        fragmentTransaction1.commit();





    }






    public void selectItemDrawer(MenuItem item) {
        Fragment myFragment = null;
        Class fragmentClass = null;
        switch (item.getItemId()) {
            case R.id.mytickets:
                Intent intent = new Intent(this, Ticketlist.class);
                startActivity(intent);
                // fragmentClass = Tickets.class;
                break;
            case R.id.management:
                Intent intent4 = new Intent(this, managedevents.class);
                startActivity(intent4);
                //fragmentClass = Management.class;
                break;
            case R.id.pending:
                Intent intent5 = new Intent(this, pending.class);
                startActivity(intent5);
                //fragmentClass = Calender.class;
                break;
            case R.id.changeinterests:
                Intent intent3 = new Intent(this, changeinterests.class);
                startActivity(intent3);
                break;

            case R.id.home:
                Intent intent6 = new Intent(getApplicationContext(), Home.class);
                startActivity(intent6);
                //ParseUser.logOut();
                break;

            case R.id.archive:
                Intent intent7 = new Intent(getApplicationContext(), archive.class);
                startActivity(intent7);
                //ParseUser.logOut();
                break;
            case R.id.find:
                Intent intent8 = new Intent(getApplicationContext(), search.class);
                startActivity(intent8);
                //ParseUser.logOut();
                break;
            case R.id.suggestions:
                Intent intent9 = new Intent(getApplicationContext(), eventsugesstions.class);
                startActivity(intent9);
                //ParseUser.logOut();
                break;

            case R.id.logout:
                ParseUser.logOut();
                Intent intent2 = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent2);
                //ParseUser.logOut();
                break;
        }
        try {
            myFragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
       /*
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content,myFragment).commit();
        item.setChecked(true);
        setTitle(item.getTitle());
        */

        DrawerLayout d1 = (DrawerLayout) findViewById(R.id.container);
        if (d1.isDrawerOpen(GravityCompat.START)) {
            d1.closeDrawer(GravityCompat.START);
        }
    }

    private void setupDrawerContent(NavigationView nav_view) {
        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                selectItemDrawer(item);
                return true;
            }
        });





    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }






}
