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

import com.google.android.gms.maps.model.LatLng;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class notmyeventpage extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private static boolean userPressedBackAgain = false;
    static Event event = new Event();
    String ticketnum;
    NavigationView mSidebar;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_information:
                    notmyeventinformation fragment1 = new notmyeventinformation();
                    FragmentTransaction fragmentTransaction1 = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction1.replace(R.id.content, fragment1);
                    fragmentTransaction1.commit();

                    return true;
                case R.id.navigation_discussion:
                    notmyeventdiscussion fragment2 = new notmyeventdiscussion();
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
        setContentView(R.layout.activity_notmyevent);



        Toolbar headerup = (Toolbar)findViewById(R.id.my_toolbar);
        setSupportActionBar(headerup);
        Bundle extras = getIntent().getExtras();
        String eventid = extras.getString("eventId");
        String eventname = extras.getString("eventname");
        getSupportActionBar().setTitle(eventname);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Events");
        query.whereEqualTo("objectId",eventid);

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null){
                    {

                        if (objects.size() > 0) {

                            for (ParseObject events : objects) {



                                event.setId(events.getObjectId());
                                event.setInterest(events.getString("intereset"));
                                event.setName(events.getString("eventname"));
                                event.setCreatedby(events.getString("createdby"));
                                ParseGeoPoint point = events.getParseGeoPoint("location");
                                Double lat = point.getLatitude();
                                Double log = point.getLongitude();
                                LatLng eventlocation = new LatLng(lat, log);
                                event.setLocation(eventlocation);
                                event.setAddress(events.getString("address"));
                                event.setAllowedgender(events.getString("allowedgender"));
                                event.setCreatedAt(events.getDate("createdAt"));
                                event.setEventdate(events.getString("eventdate"));
                                event.setEventtime(events.getString("eventtime"));
                                event.setEventdescription(events.getString("description"));
                                event.setNumbofattendees(events.getInt("numberofattendees"));
                                event.setMinage(events.getInt("minage"));
                                event.setMaxage(events.getInt("maxage"));
                                event.setOthermanagers(events.<String>getList("othermanagers"));
                                /*
                                if(events.getList("requested")!=null){
                                    event.setRequested(events.<String>getList("requested"));
                                }*/
                                event.setEventpicture(events.getParseFile("eventpicture"));


                            }
                        }
                    }

                    notmyeventinformation fragment1 = new notmyeventinformation();
                    FragmentTransaction fragmentTransaction1 = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction1.replace(R.id.content, fragment1);
                    fragmentTransaction1.commit();

                }
                else{
                    //e.printStackTrace();
                }
            }
        });


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

        DisplayImage displayImage = new DisplayImage();
        displayImage.displayCircleImage(image,imageheader);
        navigationView.addHeaderView(headerView);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.container);

        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.Open, R.string.Close);
        mDrawerLayout.addDrawerListener(mToggle);
        mSidebar = (NavigationView) findViewById(R.id.navView);

        final Menu menu = mSidebar.getMenu();
        ParseQuery<ParseObject> query4 = ParseQuery.getQuery("userevent");
        query4.whereEqualTo("state","approved");
        query4.whereEqualTo("username",ParseUser.getCurrentUser().getUsername());
        query4.findInBackground(new FindCallback<ParseObject>() {
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


        BottomNavigationViewEx navigation = (BottomNavigationViewEx) findViewById(R.id.navigation);
        navigation.enableAnimation(false);
        navigation.enableShiftingMode(false);
        navigation.enableItemShiftingMode(false);
        navigation.setTextVisibility(true);
        navigation.setTextSize(20);
        navigation.setItemHeight(130);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setupDrawerContent(mSidebar);


        //Toast.makeText(this,event.getName() , Toast.LENGTH_SHORT).show();




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
            case R.id.find:
                Intent intent8 = new Intent(getApplicationContext(), search.class);
                startActivity(intent8);
                //ParseUser.logOut();
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
            case R.id.suggestions:
                Intent intent9 = new Intent(getApplicationContext(), eventsugesstions.class);
                startActivity(intent9);
                //ParseUser.logOut();
                break;
            case R.id.archive:
                Intent intent7 = new Intent(getApplicationContext(), archive.class);
                startActivity(intent7);
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
