package com.parse.starter;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class attended extends Fragment {


    public attended() {
        // Required empty public constructor
    }
View rootView;
    ArrayList<Tickets> ticketlist = new ArrayList<>();
    Tickets ticket;
    static Tickets selectedticket;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_attended, container, false);


        final LinearLayout ticketslist = (LinearLayout)rootView.findViewById(R.id.ticketlist);
        ParseQuery<ParseObject> query = ParseQuery.getQuery("userevent");
        query.whereEqualTo("state","attended");
        query.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null){
                    {

                        for (final ParseObject tickets : objects) {


                            ticket = new Tickets();
                            ticket.setEventid(tickets.get("eventid").toString());
                            ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Events");
                            query2.whereEqualTo("objectId",ticket.getEventid());
                            query2.whereEqualTo("state","done");
                            query2.findInBackground(new FindCallback<ParseObject>() {
                                @Override
                                public void done(List<ParseObject> objects, ParseException e) {
                                    if(e==null){
                                        {
                                            for (final ParseObject event : objects) {
                                                ticket.setEventname(event.get("eventname").toString());
                                                ticket.setEventdate(event.get("eventdate").toString());

                                                final LinearLayout ticketcontainer = new LinearLayout(getActivity().getApplicationContext());
                                                ticketcontainer.setTag(tickets.getObjectId());
                                                ticketcontainer.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                                ticketcontainer.setOrientation(LinearLayout.VERTICAL);
                                                LinearLayout.LayoutParams ticketparams = (LinearLayout.LayoutParams) ticketcontainer.getLayoutParams();
                                                ticketparams .setMargins(0, 30, 0, 30);
                                                ticketcontainer.setLayoutParams(ticketparams);
                                                ticketslist.addView(ticketcontainer);
                                                final TextView ticketname = new TextView(getActivity().getApplicationContext());
                                                ticketname.setTag(ticket.getEventname());
                                                ticketcontainer.addView(ticketname);
                                                ticketname.setText(ticket.getEventname());
                                                ticketname.setTextColor(Color.parseColor("#C70039"));
                                                ticketname.setTextSize(20);
                                                ticketname.setTypeface(null, Typeface.BOLD);

                                                TextView ticketdate = new TextView(getActivity().getApplicationContext());
                                                ticketcontainer.addView(ticketdate );
                                                ticketdate .setText(ticket.getEventdate());
                                                ticketdate .setTextColor(Color.parseColor("#AFAFAF"));
                                                ticketdate .setTextSize(15);
                                                ticketlist.add(ticket);
                                                ticketcontainer.setOnClickListener(new View.OnClickListener() {
                                                    public void onClick(View v) {

                                                        Intent intent = new Intent(getActivity().getApplicationContext(), archivedevent.class);
                                                        intent.putExtra("eventId", event.getObjectId());
                                                        intent.putExtra("eventname", event.getString("eventname"));
                                                        startActivity(intent);


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
                }
                else{
                    //e.printStackTrace();
                }
            }
        });

        return rootView;
    }

}
