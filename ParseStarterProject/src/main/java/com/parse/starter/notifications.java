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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.SimpleDateFormat;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class notifications extends Fragment {


    public notifications() {
        // Required empty public constructor
    }

    String eventid;
    String eventname;
    View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_notifications, container, false);
        final LinearLayout bigcontainer = (LinearLayout)rootView.findViewById(R.id.bigcontainer);
        ParseQuery<ParseObject> query = ParseQuery.getQuery("notifications");
        query.whereEqualTo("recievername", ParseUser.getCurrentUser().getUsername());
        query.orderByDescending("createdAt");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    if (objects.size() > 0) {
                        for(final ParseObject notification : objects)
                        {

                            final LinearLayout containerapprovedvertical = new LinearLayout(getActivity().getApplicationContext());
                            containerapprovedvertical.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                            //request.setGravity(Gravity.CENTER);
                            containerapprovedvertical.setOrientation(LinearLayout.VERTICAL);
                            bigcontainer.addView(containerapprovedvertical);
                            LinearLayout.LayoutParams requestparams = (LinearLayout.LayoutParams) containerapprovedvertical.getLayoutParams();
                            requestparams.setMargins(20, 0, 20,0 );
                            containerapprovedvertical.setPadding(0,40,0,0);
                            containerapprovedvertical.setLayoutParams(requestparams);



                            final TextView content = new TextView(getActivity().getApplicationContext());
                            containerapprovedvertical.addView(content);
                            TextView date = new TextView(getActivity().getApplicationContext());
                            containerapprovedvertical.addView(date);


                            if(notification.getString("read").matches("0")) {
                                content.setTextColor(Color.parseColor("#000000"));
                                content.setTextSize(16);
                                date.setTextColor(Color.parseColor("#696969"));
                                date.setTextSize(10);

                            }else{
                                content.setTextColor(Color.parseColor("#AFAFAF"));
                                content.setTextSize(16);
                                date.setTextColor(Color.parseColor("#AFAFAF"));
                                date.setTextSize(10);
                            }

                            SimpleDateFormat desiredFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                            String newsdate = desiredFormat.format(notification.getCreatedAt());

                            date.setText(newsdate);

                            if(notification.getString("type").matches("invitation")){
                                ParseQuery<ParseObject> query = ParseQuery.getQuery("Events");
                                query.whereEqualTo("objectId", notification.getString("eventid"));
                                query.findInBackground(new FindCallback<ParseObject>() {
                                    @Override
                                    public void done(List<ParseObject> objects, ParseException e) {
                                        if (e == null) {
                                            if (objects.size() > 0) {
                                                for (final ParseObject event : objects) {

                                                    content.setText(notification.getString("sendername")+" invited you to "+event.getString("eventname"));
                                                    content.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            Intent intent = new Intent(getActivity().getApplicationContext(), notmyeventpage.class);
                                                            intent.putExtra("eventId", event.getObjectId());
                                                            intent.putExtra("eventname",event.getString("eventname"));
                                                            startActivity(intent);

                                                        }
                                                    });
                                                }
                                            }
                                        }
                                    }
                                });


                            }else if(notification.getString("type").matches("sentrequest")) {
                                ParseQuery<ParseObject> query = ParseQuery.getQuery("Events");
                                query.whereEqualTo("objectId", notification.getString("eventid"));
                                query.findInBackground(new FindCallback<ParseObject>() {
                                    @Override
                                    public void done(List<ParseObject> objects, ParseException e) {
                                        if (e == null) {
                                            if (objects.size() > 0) {
                                                for (final ParseObject event : objects) {
                                                    content.setText(notification.getString("sendername") + " requested a ticket for " + event.getString("eventname"));
                                                    content.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            if (event.getList("othermanagers").contains(ParseUser.getCurrentUser().getUsername())) {
                                                                Intent intent = new Intent(getActivity().getApplicationContext(), myeventpage.class);
                                                                intent.putExtra("eventId", event.getObjectId());
                                                                intent.putExtra("eventname", event.getString("eventname"));
                                                                startActivity(intent);
                                                            } else {
                                                                Intent intent = new Intent(getActivity().getApplicationContext(), notmyeventpage.class);
                                                                intent.putExtra("eventId", event.getObjectId());
                                                                intent.putExtra("eventname", event.getString("eventname"));
                                                                startActivity(intent);
                                                            }

                                                        }
                                                    });
                                                }
                                            }
                                        }
                                    }
                                });
                            }else if(notification.getString("type").matches("reminder")) {
                                ParseQuery<ParseObject> query = ParseQuery.getQuery("Events");
                                query.whereEqualTo("objectId", notification.getString("eventid"));
                                query.findInBackground(new FindCallback<ParseObject>() {
                                    @Override
                                    public void done(List<ParseObject> objects, ParseException e) {
                                        if (e == null) {
                                            if (objects.size() > 0) {
                                                for (final ParseObject event : objects) {
                                                    content.setText("You have an event today: " + event.getString("eventname"));
                                                    content.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            if (event.getList("othermanagers").contains(ParseUser.getCurrentUser().getUsername())) {
                                                                Intent intent = new Intent(getActivity().getApplicationContext(), myeventpage.class);
                                                                intent.putExtra("eventId", event.getObjectId());
                                                                intent.putExtra("eventname", event.getString("eventname"));
                                                                startActivity(intent);
                                                            } else {
                                                                Intent intent = new Intent(getActivity().getApplicationContext(), notmyeventpage.class);
                                                                intent.putExtra("eventId", event.getObjectId());
                                                                intent.putExtra("eventname", event.getString("eventname"));
                                                                startActivity(intent);
                                                            }

                                                        }
                                                    });
                                                }
                                            }
                                        }
                                    }
                                });
                            }

                            else if(notification.getString("type").matches("answeredquestion")){
                                {
                                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Events");
                                    query.whereEqualTo("objectId", notification.getString("eventid"));
                                    query.findInBackground(new FindCallback<ParseObject>() {
                                        @Override
                                        public void done(List<ParseObject> objects, ParseException e) {
                                            if (e == null) {
                                                if (objects.size() > 0) {
                                                    for (final ParseObject event : objects) {
                                                        content.setText("Your question on "+event.getString("eventname")+" is answered");
                                                        content.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {

                                                                if (event.getList("othermanagers").contains(ParseUser.getCurrentUser().getUsername())) {
                                                                    Intent intent = new Intent(getActivity().getApplicationContext(), myeventpage.class);
                                                                    intent.putExtra("eventId", event.getObjectId());
                                                                    intent.putExtra("eventname", event.getString("eventname"));
                                                                    startActivity(intent);
                                                                } else {
                                                                    Intent intent = new Intent(getActivity().getApplicationContext(), notmyeventpage.class);
                                                                    intent.putExtra("eventId", event.getObjectId());
                                                                    intent.putExtra("eventname", event.getString("eventname"));
                                                                    startActivity(intent);
                                                                }


                                                            }
                                                        });
                                                    }
                                                }
                                            }
                                        }
                                    });

                                }
                            }else if(notification.getString("type").matches("askedquestion")){
                                {
                                    {
                                        ParseQuery<ParseObject> query = ParseQuery.getQuery("Events");
                                        query.whereEqualTo("objectId", notification.getString("eventid"));
                                        query.findInBackground(new FindCallback<ParseObject>() {
                                            @Override
                                            public void done(List<ParseObject> objects, ParseException e) {
                                                if (e == null) {
                                                    if (objects.size() > 0) {
                                                        for (final ParseObject event : objects) {
                                                            content.setText(notification.getString("sendername")+" asked a question on "+event.getString("eventname"));
                                                            content.setOnClickListener(new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View v) {

                                                                    if (event.getList("othermanagers").contains(ParseUser.getCurrentUser().getUsername())) {
                                                                        Intent intent = new Intent(getActivity().getApplicationContext(), myeventpage.class);
                                                                        intent.putExtra("eventId", event.getObjectId());
                                                                        intent.putExtra("eventname", event.getString("eventname"));
                                                                        startActivity(intent);
                                                                    } else {
                                                                        Intent intent = new Intent(getActivity().getApplicationContext(), notmyeventpage.class);
                                                                        intent.putExtra("eventId", event.getObjectId());
                                                                        intent.putExtra("eventname", event.getString("eventname"));
                                                                        startActivity(intent);
                                                                    }



                                                                }
                                                            });
                                                        }
                                                    }
                                                }
                                            }
                                        });

                                    }
                                }
                            }else if(notification.getString("type").matches("followedyou")){
                                content.setText(notification.getString("sendername")+" followed you");
                                content.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        Intent intent = new Intent(getActivity().getApplicationContext(), Profile.class);
                                        intent.putExtra("username",notification.getString("sendername"));
                                        startActivity(intent);


                                    }
                                });
                            }else if(notification.getString("type").matches("manage")){

                                {
                                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Events");
                                    query.whereEqualTo("objectId", notification.getString("eventid"));
                                    query.findInBackground(new FindCallback<ParseObject>() {
                                        @Override
                                        public void done(List<ParseObject> objects, ParseException e) {
                                            if (e == null) {
                                                if (objects.size() > 0) {
                                                    for (final ParseObject event : objects) {
                                                        content.setText(notification.getString("sendername")+" made you a manager on "+event.getString("eventname"));
                                                        content.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {

                                                                if (event.getList("othermanagers").contains(ParseUser.getCurrentUser().getUsername())) {
                                                                    Intent intent = new Intent(getActivity().getApplicationContext(), myeventpage.class);
                                                                    intent.putExtra("eventId", event.getObjectId());
                                                                    intent.putExtra("eventname", event.getString("eventname"));
                                                                    startActivity(intent);
                                                                } else {
                                                                    Intent intent = new Intent(getActivity().getApplicationContext(), notmyeventpage.class);
                                                                    intent.putExtra("eventId", event.getObjectId());
                                                                    intent.putExtra("eventname", event.getString("eventname"));
                                                                    startActivity(intent);
                                                                }



                                                            }
                                                        });
                                                    }
                                                }
                                            }
                                        }
                                    });

                                }
                            }else if(notification.getString("type").matches("report")){
                                content.setText(notification.getString("sendername") + " reviewed your report.");

                            }else if(notification.getString("type").matches("blacklisted")){
                                content.setText("You have been blacklisted, you can not create events now");

                            }else if(notification.getString("type").matches("removedblacklisted")){
                                content.setText("Congratulations, you have been removed from the blacklist. You can create events now.");

                            }
                            else if(notification.getString("type").matches("approved")) {
                                ParseQuery<ParseObject> query = ParseQuery.getQuery("Events");
                                query.whereEqualTo("objectId", notification.getString("eventid"));
                                query.findInBackground(new FindCallback<ParseObject>() {
                                    @Override
                                    public void done(List<ParseObject> objects, ParseException e) {
                                        if (e == null) {
                                            if (objects.size() > 0) {
                                                for (final ParseObject event : objects) {
                                                    content.setText("You recieved a ticket for " + event.getString("eventname"));
                                                    content.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            Intent intent = new Intent(getActivity().getApplicationContext(), Ticketlist.class);
                                                            startActivity(intent);

                                                        }
                                                    });
                                                }
                                            }
                                        }
                                    }
                                });
                            }
                            notification.put("read","1");
                            notification.saveInBackground();

                        }
                    }
                }
            }
        });

        return rootView;
    }

}
