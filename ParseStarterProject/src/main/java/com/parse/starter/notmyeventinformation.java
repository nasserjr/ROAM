package com.parse.starter;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.parse.starter.Home.invitationid;
import static com.parse.starter.myeventpage.myevent;
import static com.parse.starter.notmyeventpage.event;


/**
 * A simple {@link Fragment} subclass.
 */
public class notmyeventinformation extends Fragment implements View.OnClickListener{



    public notmyeventinformation() {
        // Required empty public constructor
    }



int requestsent;
    Button request;
    double approved=0;
    double attended =0;
    double canceledticket =0;
    double rate=0;
    TextView attendees;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_eventinformation, container, false);

       requestsent=0;
        TextView creatorname = (TextView)rootView.findViewById(R.id.creatorname);
        creatorname.setText(event.getCreatedby());
        creatorname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseQuery<ParseObject> query3 = ParseQuery.getQuery("suspended");
                query3.whereEqualTo("username", event.getCreatedby());
                query3.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        if (e == null) {
                            {
                                if(objects.size()>0){
                                    Intent intent = new Intent(getActivity().getApplicationContext(), suspendpage.class);
                                    startActivity(intent);
                                }else{
                                    Intent intent = new Intent(getActivity().getApplicationContext(), Profile.class);
                                    intent.putExtra("username",event.getCreatedby());
                                    startActivity(intent);
                                }
                            }
                        }
                    }
                });


            }
        });

        attendees = (TextView)rootView.findViewById(R.id.numbattendees);

        ParseQuery<ParseObject> requestquery3 = ParseQuery.getQuery("userevent");
        requestquery3.whereEqualTo("eventid",event.getId());
        requestquery3.whereEqualTo("state","attended");

        ParseQuery<ParseObject> requestquery2 = ParseQuery.getQuery("userevent");
        requestquery2.whereEqualTo("eventid",event.getId());
        requestquery2.whereEqualTo("state","approved");

        List<ParseQuery<ParseObject>> queries = new ArrayList<ParseQuery<ParseObject>>();
        queries.add(requestquery3);
        queries.add(requestquery2);

        ParseQuery<ParseObject> mainQuery = ParseQuery.or(queries);

        mainQuery.findInBackground(new FindCallback<ParseObject>() {
                                           @Override
                                           public void done(List<ParseObject> objects, ParseException e) {
                                               if (e == null) {

                                                   attendees.setText(String .valueOf(objects.size())+"/"+String.valueOf(event.getNumbofattendees()));

                                               }
                                           }
                                       });

        TextView report = (TextView)rootView.findViewById(R.id.report);
        report.setOnClickListener(this);
        final DisplayImage displayImage = new DisplayImage();
        ImageView eventpic = (ImageView)rootView.findViewById(R.id.eventpicture);
        displayImage.displayImage(event.getEventpicture(),eventpic);
        final CircleImageView creatorpic = (CircleImageView)rootView.findViewById(R.id.createdbyprof);
        request = (Button)rootView.findViewById(R.id.request);
        request.setOnClickListener(this);


        ParseQuery<ParseObject> requestquery1 = ParseQuery.getQuery("userevent");
        requestquery1.whereEqualTo("eventid",event.getId());
        requestquery1.whereEqualTo("username",ParseUser.getCurrentUser().getUsername());

        requestquery1.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null){
                    if (objects.size() > 0) {

                        for (ParseObject guests : objects) {
                            if(guests.getString("state").matches("request")) {
                                requestsent = 1;
                                request.setText("Cancel request");
                                request.setBackground(getResources().getDrawable(R.drawable.roundedcornergrey, null));
                            }else if(guests.getString("state").matches("declined")){
                                request.setVisibility(View.GONE);
                            }else if(guests.getString("state").matches("approved")){
                                requestsent = 2;
                                request.setText("Cancel approval");
                                request.setBackground(getResources().getDrawable(R.drawable.roundedcornergrey, null));
                            }else if(guests.getString("state").matches("attended")){
                                requestsent = 3;
                                request.setText("welcome");
                            }

                        }
                    }
                }
            }
        });

        ParseQuery<ParseUser> query = ParseUser.getQuery();

        query.whereEqualTo("username",event.getCreatedby());

        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null) {
                    {
                        if (objects.size() > 0) {

                            for (ParseObject user : objects) {


                                ParseFile image = (ParseFile) user.getParseFile("profilepicture");
                                if(image!=null) {
                                    displayImage.displayCircleImage(image, creatorpic);
                                }else {
                                    creatorpic.setImageResource(R.drawable.profilepic);
                                }


                            }
                        }
                    }
                } else {
                    //e.printStackTrace();
                }
            }
        });

        TextView description = (TextView)rootView.findViewById(R.id.about);
       description.setText(event.getEventdescription());
       TextView timeanddate = (TextView)rootView.findViewById(R.id.timeanddate);
       timeanddate.setText(event.getEventdate()+"  "+event.getEventtime());
        TextView location = (TextView)rootView.findViewById(R.id.location);
        location.setText(event.getAddress());
        TextView interest = (TextView)rootView.findViewById(R.id.interest);
        interest.setText(event.getInterest());
        TextView maxattendees = (TextView)rootView.findViewById(R.id.maxattendees);
        maxattendees.setText(maxattendees.getText()+String.valueOf(event.getNumbofattendees()));
        TextView agerange = (TextView)rootView.findViewById(R.id.agerange);
        agerange.setText(agerange.getText()+String.valueOf(event.getMinage())+" - "+String.valueOf(event.getMaxage()));
        TextView genderrestriction = (TextView)rootView.findViewById(R.id.genderrestriction);
        if(event.getAllowedgender().matches("all")){
            genderrestriction.setText(genderrestriction.getText()+"None");
        }
        else if(event.getAllowedgender().matches("Male")){
            genderrestriction.setText(genderrestriction.getText()+"Boys only");
        }
        else{
            genderrestriction.setText(genderrestriction.getText()+"Girls only");
        }

        Button request = (Button)rootView.findViewById(R.id.request);
        request.setOnClickListener(this);
        Button invitation = (Button)rootView.findViewById(R.id.invitations);
        invitation.setOnClickListener(this);







        return rootView;



    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.request) {


            if (requestsent==0) {

                ParseObject requests = new ParseObject("userevent");
                requests.put("eventid", event.getId());
                requests.put("username", ParseUser.getCurrentUser().getUsername());
                requests.put("userid",ParseUser.getCurrentUser().getObjectId());
                requests.put("state","request");
                requests.put("rated","no");
                requests.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {

                            ParseQuery<ParseObject> requestquery = ParseQuery.getQuery("Events");
                            requestquery.whereEqualTo("objectId",event.getId());
                            requestquery.findInBackground(new FindCallback<ParseObject>() {
                                                              @Override
                                                              public void done(List<ParseObject> objects, ParseException e) {
                                                                  if (e == null) {
                                                                      if (objects.size() > 0) {

                                                                          for (ParseObject event : objects) {
                                                                              List managers = event.getList("othermanagers");
                                                                              for(Object manager:managers){
                                                                                  final ParseObject notify = new ParseObject("notifications");
                                                                                  notify.put("sendername", ParseUser.getCurrentUser().getUsername().toString());
                                                                                  notify.put("recievername",manager.toString());
                                                                                  notify.put("read","0");
                                                                                  notify.put("eventid",event.getObjectId());
                                                                                  notify.put("type","sentrequest");
                                                                                  notify.saveInBackground(new SaveCallback() {
                                                                                      @Override
                                                                                      public void done(ParseException e) {

                                                                                      }
                                                                                  });
                                                                              }
                                                                          }
                                                                      }
                                                                  }
                                                              }
                                                          });

                            FragmentTransaction ft = getFragmentManager().beginTransaction();
                            ft.detach(notmyeventinformation.this).attach(notmyeventinformation.this).commit();

                        }
                    }
                });
            } else if(requestsent==1){
                ParseQuery<ParseObject> requestquery = ParseQuery.getQuery("userevent");
                requestquery.whereEqualTo("eventid",event.getId());
                requestquery.whereEqualTo("username",ParseUser.getCurrentUser().getUsername());
                requestquery.whereEqualTo("state","request");
                requestquery.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        if(e==null){
                            if (objects.size() > 0) {

                                for (ParseObject object : objects) {

                                    object.deleteInBackground();
                                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                                    ft.detach(notmyeventinformation.this).attach(notmyeventinformation.this).commit();

                                }

                            }
                        }
                    }
                });
            }else if(requestsent==2){
                {

                    {
                        ParseQuery<ParseObject> requestquery = ParseQuery.getQuery("userevent");
                        requestquery.whereEqualTo("eventid",event.getId());
                        requestquery.whereEqualTo("username",ParseUser.getCurrentUser().getUsername());
                        requestquery.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> objects, ParseException e) {
                                if(e==null){
                                    if (objects.size() > 0) {

                                        for (ParseObject object : objects) {
                                            object.put("state","canceledticket");
                                            object.saveInBackground(new SaveCallback() {
                                                @Override
                                                public void done(ParseException e) {
                                                    ParseQuery<ParseObject> query = ParseQuery.getQuery("userevent");
                                                    query.whereEqualTo("username",ParseUser.getCurrentUser().getUsername());
                                                    query.findInBackground(new FindCallback<ParseObject>() {
                                                        @Override
                                                        public void done(List<ParseObject> objects, ParseException e) {
                                                            if (e == null) {
                                                                {
                                                                    {
                                                                        if (objects.size() > 0) {
                                                                            for (ParseObject userevents : objects) {

                                                                                if (userevents.getString("state").matches("attended")) {
                                                                                    approved = approved + 1;
                                                                                    attended = attended + 1;
                                                                                } else if (userevents.getString("state").matches("approved")) {
                                                                                    approved = approved + 1;
                                                                                }
                                                                                else if(userevents.getString("state").matches("canceledticket")){
                                                                                    approved = approved + 1;
                                                                                    canceledticket = canceledticket + 1;
                                                                                }
                                                                            }
                                                                            rate = (((attended * 1)+(canceledticket*0.5)) / approved)*5;
                                                                            ParseQuery<ParseObject> query = ParseQuery.getQuery("userrate");
                                                                            query.whereEqualTo("username",ParseUser.getCurrentUser().getUsername());
                                                                            query.findInBackground(new FindCallback<ParseObject>() {
                                                                                @Override
                                                                                public void done(List<ParseObject> objects, ParseException e) {
                                                                                    if (e == null) {
                                                                                        {
                                                                                            if (objects.size() > 0) {
                                                                                                for (ParseObject user : objects) {
                                                                                                    user.put("attendeerate",rate);
                                                                                                    user.saveInBackground(new SaveCallback() {
                                                                                                        @Override
                                                                                                        public void done(ParseException e) {
                                                                                                            FragmentTransaction ft = getFragmentManager().beginTransaction();
                                                                                                            ft.detach(notmyeventinformation.this).attach(notmyeventinformation.this).commit();
                                                                                                        }
                                                                                                    });
                                                                                                }

                                                                                            }
                                                                                        }
                                                                                    }
                                                                                }
                                                                            });
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }

                                                            });
                                                }
                                            });

                                        }

                                    }
                                }
                            }
                        });
                    }

                }
            }
        }else if(v.getId()==R.id.invitations){
            invitationid = event.getId();
            sendinvitations fragment1 = new sendinvitations();
            FragmentTransaction fragmentTransaction1 = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction1.replace(R.id.content, fragment1);
            fragmentTransaction1.addToBackStack(null).commit();
        }else if(v.getId()==R.id.report){
            Intent intent = new Intent(getActivity().getApplicationContext(), reportevent.class);
            intent.putExtra("eventid",event.getId());
            intent.putExtra("eventname",event.getName());
            intent.putExtra("type","event");
            startActivity(intent);
        }


        }
        }


