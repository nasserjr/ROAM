package com.parse.starter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;

import static com.parse.starter.myeventpage.myevent;

public class Home extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private static boolean userPressedBackAgain = false;
    String ticketnum;
    NavigationView mSidebar;
    static String invitationid;
    BottomNavigationViewEx navigation;
    static QBadgeView badge;
    Date todayWithZeroTime;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:


                    getSupportActionBar().setTitle("What is around");
                    getSupportActionBar().setSubtitle(null);
                    MapFragment fragment1 = new MapFragment();
                    FragmentTransaction fragmentTransaction1 = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction1.replace(R.id.content, fragment1);
                    fragmentTransaction1.commit();
                    return true;
                case R.id.navigation_create:
                    ParseQuery<ParseObject> query2 = ParseQuery.getQuery("blacklist");
                    query2.whereEqualTo("username",ParseUser.getCurrentUser().getUsername());
                    query2.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> objects, ParseException e) {
                            if (e == null) {
                                if (objects.size() > 0) {
                                    Toast.makeText(Home.this, "You can not perform this action", Toast.LENGTH_SHORT).show();
                                }else {
                                    getSupportActionBar().setTitle("Create");
                                    getSupportActionBar().setSubtitle(null);
                                    MapCreate fragment2 = new MapCreate();
                                    FragmentTransaction fragmentTransaction2 = getSupportFragmentManager().beginTransaction();
                                    fragmentTransaction2.replace(R.id.content, fragment2);
                                    fragmentTransaction2.commit();
                                }
                            }
                        }
                    });

                    return true;
                case R.id.navigation_dashboard:

                    getSupportActionBar().setTitle("Newsfeed");
                    timeline fragment3 = new timeline();
                    FragmentTransaction fragmentTransaction3 = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction3.replace(R.id.content, fragment3);
                    fragmentTransaction3.commit();
                    return true;

                case R.id.navigation_notifications:
                    addBadgeAt(3,0,badge);
                    getSupportActionBar().setTitle("Notifications");
                    notifications fragment4 = new notifications();
                    FragmentTransaction fragmentTransaction4 = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction4.replace(R.id.content, fragment4);
                    fragmentTransaction4.commit();
                    return true;
            }
            return false;
        }

    };


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        badge = new QBadgeView(this);
        navigation = (BottomNavigationViewEx) findViewById(R.id.navigation);
            ParseQuery<ParseObject> query2 = ParseQuery.getQuery("userevent");
            query2.whereEqualTo("username",ParseUser.getCurrentUser().getUsername());
            query2.whereEqualTo("state","attended");
            query2.whereEqualTo("rated","no");
            query2.findInBackground(new FindCallback<ParseObject>() {
                                       @Override
                                       public void done(List<ParseObject> objects, ParseException e) {
                                           if (e == null) {


                                               if (objects.size() > 0) {

                                                   for (ParseObject unratedevent : objects) {
                                                       Intent intent = new Intent(getApplicationContext(), Rateevent.class);
                                                       intent.putExtra("eventId", unratedevent.getString("eventid"));
                                                       //intent.putExtra("eventname", event.getName());
                                                       startActivity(intent);
                                                   }
                                               }
                                           }
                                       }
                                   });


       //LayoutInflater inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
/*
      final  View vi = inflater.inflate(R.layout.header, null); //log.xml is your file.
      final TextView tv = (TextView)vi.findViewById(R.id.nameinheader); //get a reference to the textview on the log.xml file.

        String x="hamada";
        updateTv(tv,x,vi);
        Toast.makeText(this, tv.getText().toString(), Toast.LENGTH_SHORT).show();*/

        mDrawerLayout = (DrawerLayout) findViewById(R.id.container);

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
        navigation = (BottomNavigationViewEx) findViewById(R.id.navigation);
        navigation.enableAnimation(false);
        navigation.enableShiftingMode(false);
        navigation.enableItemShiftingMode(false);
        navigation.setTextVisibility(false);
        //navigation.setItemHeight(180);
        navigation.setIconSize(34,34);
        navigation.setIconsMarginTop(18);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);



        final DateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Date today = new Date();

        try {
            todayWithZeroTime = sdf.parse(sdf.format(today));
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        ParseQuery<ParseObject> query4 = ParseQuery.getQuery("userevent");
        query4.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
        query4.whereEqualTo("state", "approved");
        query4.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    {
                        for (final ParseObject approvals : objects) {
                            ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Events");
                            query2.whereEqualTo("objectId", approvals.getString("eventid"));
                            query2.findInBackground(new FindCallback<ParseObject>() {
                                @Override
                                public void done(List<ParseObject> objects, ParseException e) {
                                    if (e == null) {
                                        {
                                            for (final ParseObject event : objects) {
                                                Date strDate = null;
                                                try {
                                                    strDate = sdf.parse(event.getString("eventdate"));
                                                    if (todayWithZeroTime.equals(strDate)) {
                                                        ParseQuery<ParseObject> query2 = ParseQuery.getQuery("notifications");
                                                        query2.whereEqualTo("eventid",event.getObjectId());
                                                        query2.whereEqualTo("type","reminder");
                                                        query2.whereEqualTo("recievername",ParseUser.getCurrentUser().getUsername());
                                                        query2.findInBackground(new FindCallback<ParseObject>() {
                                                            @Override
                                                            public void done(List<ParseObject> objects, ParseException e) {
                                                                if (e == null) {
                                                                    {
                                                                        if(objects.size()==0){
                                                                            final ParseObject notify = new ParseObject("notifications");
                                                                            notify.put("recievername", ParseUser.getCurrentUser().getUsername());
                                                                            notify.put("read", "0");
                                                                            notify.put("eventid",event.getObjectId());
                                                                            notify.put("type", "reminder");
                                                                            notify.saveInBackground();
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        });
                                                    }
                                                } catch (java.text.ParseException e1) {
                                                    e1.printStackTrace();
                                                }
                                            }

                                        }
                                    }
                                }
                            });
                        }
                    }
                }
            }});

        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                if(ParseUser.getCurrentUser()!=null) {
                    ParseQuery<ParseObject> query3 = ParseQuery.getQuery("notifications");
                    query3.whereEqualTo("recievername", ParseUser.getCurrentUser().getUsername());
                    query3.whereEqualTo("read", "0");
                    query3.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> objects, ParseException e) {
                            if (e == null) {
                                {

                                    addBadgeAt(3, objects.size(), badge);
                                }
                            }
                        }
                    });
                }
                handler.postDelayed(this, 5000);
            }
        };

//Start
        handler.postDelayed(runnable, 5000);

        // sets calendar time/date



        Toolbar headerup = (Toolbar)findViewById(R.id.my_toolbar);
        setSupportActionBar(headerup);


        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setupDrawerContent(mSidebar);

        getSupportActionBar().setTitle("What is around?");
        getSupportActionBar().setSubtitle(null);
        setTitle("fragment one"); //title of action bar
        MapFragment fragment1 = new MapFragment();
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

    public void addBadgeAt(int position, int number,QBadgeView badge) {
        // add badge


        if(number==0){
            badge.setBadgeNumber(0).bindTarget(navigation.getBottomNavigationItemView(position));
            badge.setVisibility(View.INVISIBLE);
        }else {


                    badge.setVisibility(View.VISIBLE);
                    badge.setBadgeNumber(number)
                    .setGravityOffset(20, 2, true)
                    .setBadgeTextColor(Color.parseColor("#C70039"))
                    .setBadgeBackgroundColor(Color.parseColor("#FFFFFF"))
                    .bindTarget(navigation.getBottomNavigationItemView(position));
        }


    }




}

