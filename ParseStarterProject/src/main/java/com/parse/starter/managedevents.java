package com.parse.starter;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class managedevents extends AppCompatActivity {

    ArrayList<Tickets> ticketlist = new ArrayList<>();
    Tickets ticket;
    static Tickets selectedticket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticketlist);

        Toolbar headerup = (Toolbar)findViewById(R.id.my_toolbar);
        setSupportActionBar(headerup);
        getSupportActionBar().setTitle("Managed Events");


        final LinearLayout ticketslist = (LinearLayout)findViewById(R.id.ticketlist);
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Events");
        query.whereEqualTo("state","notyet");
        query.whereContains("othermanagers",ParseUser.getCurrentUser().getUsername());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null){
                    {

                                            for (final ParseObject event : objects) {
                                                ticket = new Tickets();
                                                ticket.setEventname(event.get("eventname").toString());
                                                ticket.setEventdate(event.get("eventdate").toString());

                                                final LinearLayout ticketcontainer = new LinearLayout(getApplicationContext());
                                                ticketcontainer.setTag(event.getObjectId());
                                                ticketcontainer.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                                ticketcontainer.setOrientation(LinearLayout.VERTICAL);
                                                LinearLayout.LayoutParams ticketparams = (LinearLayout.LayoutParams) ticketcontainer.getLayoutParams();
                                                ticketparams .setMargins(0, 30, 0, 30);
                                                ticketcontainer.setLayoutParams(ticketparams);
                                                ticketslist.addView(ticketcontainer);
                                                final TextView ticketname = new TextView(getApplicationContext());
                                                ticketname.setTag(ticket.getEventname());
                                                ticketcontainer.addView(ticketname);
                                                ticketname.setText(ticket.getEventname());
                                                ticketname.setTextColor(Color.parseColor("#C70039"));
                                                ticketname.setTextSize(20);
                                                ticketname.setTypeface(null, Typeface.BOLD);

                                                TextView ticketdate = new TextView(getApplicationContext());
                                                ticketcontainer.addView(ticketdate );
                                                ticketdate .setText(ticket.getEventdate());
                                                ticketdate .setTextColor(Color.parseColor("#AFAFAF"));
                                                ticketdate .setTextSize(15);
                                                ticketlist.add(ticket);
                                                ticketcontainer.setOnClickListener(new View.OnClickListener() {
                                                    public void onClick(View v) {

                                                        if (event.getList("othermanagers").contains(ParseUser.getCurrentUser().getUsername())) {
                                                            Intent intent = new Intent(getApplicationContext(), myeventpage.class);
                                                            intent.putExtra("eventId", event.getObjectId());
                                                            intent.putExtra("eventname", event.getString("eventname"));
                                                            startActivity(intent);
                                                        } else {
                                                            Intent intent = new Intent(getApplicationContext(), notmyeventpage.class);
                                                            intent.putExtra("eventId", event.getObjectId());
                                                            intent.putExtra("eventname", event.getString("eventname"));
                                                            startActivity(intent);
                                                        }

                                                    }
                                                });
                                            }


                                        }
                                    }

                                    else{
                                        //e.printStackTrace();
                                    }
                                }
                            });



                        }



                    }
