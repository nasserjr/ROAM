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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.parse.starter.Home.invitationid;
import static com.parse.starter.myeventpage.myevent;


/**
 * A simple {@link Fragment} subclass.
 */
public class myeventinformation extends Fragment implements View.OnClickListener{



    public myeventinformation() {
        // Required empty public constructor
    }



    int numberofrequests=0;
    int numberofguests=0;
    View rootView;
    TextView numbrequests;
    TextView numbguests;
    TextView managersnumb;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_myeventinformation, container, false);


        final DisplayImage displayImage = new DisplayImage();
        ImageView eventpic = (ImageView)rootView.findViewById(R.id.eventpicture);
        displayImage.displayImage(myevent.getEventpicture(),eventpic);
        TextView description = (TextView)rootView.findViewById(R.id.about);
        description.setText( myevent.getEventdescription());
        TextView timeanddate = (TextView)rootView.findViewById(R.id.timeanddate);
        timeanddate.setText( myevent.getEventdate()+"  "+ myevent.getEventtime());
        TextView location = (TextView)rootView.findViewById(R.id.location);
        location.setText( myevent.getAddress());
        TextView interest = (TextView)rootView.findViewById(R.id.interest);
        interest.setText( myevent.getInterest());
        TextView maxattendees = (TextView)rootView.findViewById(R.id.maxattendees);
        maxattendees.setText(maxattendees.getText()+String.valueOf( myevent.getNumbofattendees()));
        TextView agerange = (TextView)rootView.findViewById(R.id.agerange);
        agerange.setText(agerange.getText()+String.valueOf( myevent.getMinage())+" - "+String.valueOf( myevent.getMaxage()));
        TextView genderrestriction = (TextView)rootView.findViewById(R.id.genderrestriction);
       if(myevent.getAllowedgender().matches("all")){
            genderrestriction.setText(genderrestriction.getText()+"None");
        }
        else if(myevent.getAllowedgender().matches("Male")){
            genderrestriction.setText(genderrestriction.getText()+"Boys only");
        }
        else{
            genderrestriction.setText(genderrestriction.getText()+"Girls only");
        }

        ParseQuery<ParseObject> query4 = ParseQuery.getQuery("Events");
        query4.whereEqualTo("objectId",myevent.getId());
        query4.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    {

                        for (ParseObject events : objects) {

                            managersnumb = (TextView) rootView.findViewById(R.id.managersnumb);
                            managersnumb.setText(String.valueOf(events.getList("othermanagers").size()));
                        }
                    }
                } else {
                    //e.printStackTrace();
                }
            }


        });

        ParseQuery<ParseObject> query = ParseQuery.getQuery("userevent");
        query.whereEqualTo("eventid",myevent.getId());
        query.whereEqualTo("state","request");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    {

                        numberofrequests=objects.size();
                        numbrequests = (TextView)rootView.findViewById(R.id.numberrequestss);
                        numbrequests.setText(String.valueOf(numberofrequests));
                    }
                } else {
                    //e.printStackTrace();
                }
            }


        });

        ParseQuery<ParseObject> query2 = ParseQuery.getQuery("userevent");
        query2.whereEqualTo("eventid",myevent.getId());
        query2.whereEqualTo("state","approved");

        ParseQuery<ParseObject> query3 = ParseQuery.getQuery("userevent");
        query3.whereEqualTo("eventid",myevent.getId());
        query3.whereEqualTo("state","attended");

        List<ParseQuery<ParseObject>> queries = new ArrayList<ParseQuery<ParseObject>>();
        queries.add(query2);
        queries.add(query3);

        ParseQuery<ParseObject> mainQuery = ParseQuery.or(queries);

        mainQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    {

                        numberofguests=objects.size();
                        numbguests = (TextView)rootView.findViewById(R.id.numberguests);
                        numbguests.setText(String.valueOf(numberofguests));
                    }
                } else {
                    //e.printStackTrace();
                }
            }


        });

        /*
        ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Events");
        query2.whereEqualTo("eventid",myevent.getId());
        query2.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    {
                        for (ParseObject events : objects) {
                            if (events.getList("approved") != null) {
                                myevent.setApproved(events.<String>getList("approved"));
                                numbguests = (TextView)rootView.findViewById(R.id.numberguests);
                                numbguests.setText(String.valueOf(myevent.getApproved().size()));
                            }
                        }
                    }
                } else {
                    //e.printStackTrace();
                }
            }
        });
        */



        RelativeLayout requestlayout = (RelativeLayout)rootView.findViewById(R.id.requestslayout);
        requestlayout.setOnClickListener(this);
        CircleImageView gotoscan = (CircleImageView)rootView.findViewById(R.id.scan);
        gotoscan.setOnClickListener(this);

        RelativeLayout guestslayout = (RelativeLayout)rootView.findViewById(R.id.guestslayout);
        guestslayout.setOnClickListener(this);

        RelativeLayout managnumb = (RelativeLayout)rootView.findViewById(R.id.following);
        managnumb .setOnClickListener(this);

        Button invitations = (Button)rootView.findViewById(R.id.invitations);
        invitations.setOnClickListener(this);

        Button archive = (Button)rootView.findViewById(R.id.archive);
        archive.setOnClickListener(this);





        return rootView;



    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.following) {
            addmanagers fragment1 = new addmanagers();
            FragmentTransaction fragmentTransaction1 = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction1.replace(R.id.content, fragment1);
            fragmentTransaction1.addToBackStack(null).commit();

    }else if(v.getId()==R.id.requestslayout){
           requests fragment1 = new requests();
            FragmentTransaction fragmentTransaction1 = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction1.replace(R.id.content, fragment1);
            fragmentTransaction1.addToBackStack(null).commit();
           // Toast.makeText(getActivity().getApplicationContext(), "eshta", Toast.LENGTH_SHORT).show();
        }
        else if(v.getId()==R.id.scan){

            Intent intent = new Intent(getActivity().getApplicationContext(), QRscanner.class);
            startActivity(intent);
        }
        else if(v.getId()==R.id.guestslayout){
            guestlist fragment1 = new guestlist();
            FragmentTransaction fragmentTransaction1 = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction1.replace(R.id.content, fragment1);
            fragmentTransaction1.addToBackStack(null).commit();
        } else if(v.getId()==R.id.invitations){
            invitationid=myevent.getId();
            sendinvitations fragment1 = new sendinvitations();
            FragmentTransaction fragmentTransaction1 = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction1.replace(R.id.content, fragment1);
            fragmentTransaction1.addToBackStack(null).commit();

        }else if(v.getId()==R.id.archive){
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Events");
            query.whereEqualTo("objectId",myevent.getId());
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if (e == null) {
                        {
                            for (ParseObject events : objects) {
                                events.put("state","done");
                                events.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        Intent intent = new Intent(getActivity().getApplicationContext(), archivedevent.class);
                                        intent.putExtra("eventId", myevent.getId());
                                        intent.putExtra("eventname", myevent.getName());
                                        startActivity(intent);
                                    }
                                });
                            }
                        }
                    } else {
                        //e.printStackTrace();
                    }
                }


            });

        }
}}


