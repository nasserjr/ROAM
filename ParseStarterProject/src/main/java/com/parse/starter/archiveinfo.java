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
import android.widget.RatingBar;
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
import static com.parse.starter.archivedevent.*;
import static com.parse.starter.myeventpage.myevent;


/**
 * A simple {@link Fragment} subclass.
 */
public class archiveinfo extends Fragment implements View.OnClickListener{



    public archiveinfo() {
        // Required empty public constructor
    }

    double approved=0;
    double attended =0;
    TextView attendees;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_archiveinfo, container, false);

        TextView creatorname = (TextView)rootView.findViewById(R.id.creatorname);
        creatorname.setText(archivedevent.getCreatedby());
        creatorname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), Profile.class);
                intent.putExtra("username",archivedevent.getCreatedby());
                startActivity(intent);

            }
        });

        attendees = (TextView)rootView.findViewById(R.id.numbattendees);

        ParseQuery<ParseObject> requestquery2 = ParseQuery.getQuery("userevent");
        requestquery2.whereEqualTo("eventid",archivedevent.getId());
        requestquery2.whereEqualTo("state","attended");
        requestquery2.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {

                    attendees.setText(String .valueOf(objects.size())+"/"+String.valueOf(archivedevent.getNumbofattendees()));

                }
            }
        });

        TextView report = (TextView)rootView.findViewById(R.id.report);
        report.setOnClickListener(this);
        final DisplayImage displayImage = new DisplayImage();
        ImageView eventpic = (ImageView)rootView.findViewById(R.id.eventpicture);
        displayImage.displayImage(archivedevent.getEventpicture(),eventpic);
        final CircleImageView creatorpic = (CircleImageView)rootView.findViewById(R.id.createdbyprof);


        ParseQuery<ParseUser> query = ParseUser.getQuery();

        query.whereEqualTo("username",archivedevent.getCreatedby());

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
        description.setText(archivedevent.getEventdescription());
        TextView timeanddate = (TextView)rootView.findViewById(R.id.timeanddate);
        timeanddate.setText(archivedevent.getEventdate()+"  "+archivedevent.getEventtime());
        TextView location = (TextView)rootView.findViewById(R.id.location);
        location.setText(archivedevent.getAddress());
        TextView interest = (TextView)rootView.findViewById(R.id.interest);
        interest.setText(archivedevent.getInterest());
        RatingBar eventrate = (RatingBar)rootView.findViewById(R.id.eventrate);

            eventrate.setRating(archivedevent.getRate());

        TextView maxattendees = (TextView)rootView.findViewById(R.id.maxattendees);
        maxattendees.setText(maxattendees.getText()+String.valueOf(archivedevent.getNumbofattendees()));
        TextView agerange = (TextView)rootView.findViewById(R.id.agerange);
        agerange.setText(agerange.getText()+String.valueOf(archivedevent.getMinage())+" - "+String.valueOf(archivedevent.getMaxage()));
        TextView genderrestriction = (TextView)rootView.findViewById(R.id.genderrestriction);
        if(archivedevent.getAllowedgender().matches("all")){
            genderrestriction.setText(genderrestriction.getText()+"None");
        }
        else if(archivedevent.getAllowedgender().matches("Male")){
            genderrestriction.setText(genderrestriction.getText()+"Boys only");
        }
        else{
            genderrestriction.setText(genderrestriction.getText()+"Girls only");
        }

        return rootView;



    }

    @Override
    public void onClick(View v) {
if(v.getId()==R.id.report){
            Intent intent = new Intent(getActivity().getApplicationContext(), reportevent.class);
            intent.putExtra("eventid",archivedevent.getId());
            intent.putExtra("eventname",archivedevent.getName());
            intent.putExtra("type","event");
            startActivity(intent);
        }


    }
}


