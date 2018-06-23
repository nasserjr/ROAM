package com.parse.starter;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.parse.starter.Profile.usernameprofile;
import static com.parse.starter.myeventpage.myevent;
import static com.parse.starter.notmyeventpage.event;


/**
 * A simple {@link Fragment} subclass.
 */
public class Profilenewsfeed extends Fragment {

    List<TimelineContent> timelineContents = new ArrayList<TimelineContent>();
    TimelineContent news = new TimelineContent();
    ParseFile image;
    private View rootView;
    RelativeLayout loading;

    public Profilenewsfeed() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_profilenewsfeed, container, false);
        final LinearLayout bigcontainer = (LinearLayout) rootView.findViewById(R.id.bigcontainer);
        loading = (RelativeLayout)rootView.findViewById(R.id.loading);




                    //getapprovalsandattendance
                    ParseQuery<ParseObject> query2 = ParseQuery.getQuery("userevent");
                    query2.whereEqualTo("username", usernameprofile);
                    query2.whereNotEqualTo("state", "declined");
                    query2.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> objects, ParseException e) {
                            if (e == null) {
                                {
                                    for (ParseObject event : objects) {
                                        TimelineContent news = new TimelineContent();
                                        news.setActor(event.getString("username"));
                                        news.setEventid(event.getString("eventid"));
                                        news.setDate(event.getCreatedAt());
                                        news.setType(event.getString("state"));
                                        timelineContents.add(news);
                                    }


                                    //geteventcreations
                                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Events");
                                    query.whereEqualTo("createdby", usernameprofile);
                                    query.findInBackground(new FindCallback<ParseObject>() {
                                        @Override
                                        public void done(List<ParseObject> objects, ParseException e) {
                                            if (e == null) {
                                                {
                                                    for (ParseObject event : objects) {
                                                        TimelineContent news = new TimelineContent();
                                                        news.setActor(event.getString("createdby"));
                                                        news.setEventid(event.getObjectId());
                                                        news.setDate(event.getCreatedAt());
                                                        news.setType("creation");
                                                        timelineContents.add(news);
                                                    }
                                                }
                                                //getreviews
                                                ParseQuery<ParseObject> query3 = ParseQuery.getQuery("eventreview");
                                                query3.whereEqualTo("username", usernameprofile);
                                                query3.findInBackground(new FindCallback<ParseObject>() {
                                                    @Override
                                                    public void done(List<ParseObject> objects, ParseException e) {
                                                        if (e == null) {
                                                            {
                                                                for (ParseObject reviews : objects) {
                                                                    TimelineContent news = new TimelineContent();
                                                                    news.setActor(reviews.getString("username"));
                                                                    news.setEventid(reviews.getString("eventid"));
                                                                    news.setDate(reviews.getCreatedAt());
                                                                    news.setType("review");
                                                                    timelineContents.add(news);
                                                                }

                                                                Collections.sort(timelineContents, new Comparator<TimelineContent>() {
                                                                    @Override
                                                                    public int compare(TimelineContent o1, TimelineContent o2) {
                                                                        return o2.getDate().compareTo(o1.getDate());
                                                                    }
                                                                });

                                                                //start from here
                                                                showtimeline(bigcontainer, timelineContents);

                                                            }
                                                        }
                                                    }
                                                });
                                            }
                                        }
                                    });
                                }
                            }

                        }
                    });
                    return rootView;
                }






    public void showtimeline(LinearLayout bigcontainer, final List<TimelineContent> Contents) {

        for (int i = 0; i < Contents.size(); i++) {

            final LinearLayout containerapprovedvertical = new LinearLayout(getActivity().getApplicationContext());
            containerapprovedvertical.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            //request.setGravity(Gravity.CENTER);
            containerapprovedvertical.setOrientation(LinearLayout.VERTICAL);
            bigcontainer.addView(containerapprovedvertical);
            LinearLayout.LayoutParams requestparams = (LinearLayout.LayoutParams) containerapprovedvertical.getLayoutParams();
            requestparams.setMargins(20, 0, 20,0 );
            containerapprovedvertical.setPadding(0,40,0,0);
            containerapprovedvertical.setLayoutParams(requestparams);

            if(i!=0){
                containerapprovedvertical.setBackground(getResources().getDrawable(R.drawable.seperators,null));
            }



            final LinearLayout containerapprovedhorizontal = new LinearLayout(getActivity().getApplicationContext());
            containerapprovedhorizontal.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            //request.setGravity(Gravity.CENTER);
            containerapprovedhorizontal.setOrientation(LinearLayout.HORIZONTAL);
            containerapprovedvertical.addView(containerapprovedhorizontal);

            ParseQuery<ParseUser> getinfo = ParseUser.getQuery();
            getinfo.whereEqualTo("username", Contents.get(i).getActor());
            final int finalI = i;
            getinfo.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> objects, ParseException e) {
                    if (e == null) {
                        if (objects.size() > 0) {
                            for (ParseObject user : objects) {
                                image = (ParseFile) user.getParseFile("profilepicture");
                                CircleImageView requestpp = new CircleImageView(getActivity().getApplicationContext());
                                DisplayImage displayImage = new DisplayImage();
                                containerapprovedhorizontal.addView(requestpp);
                                requestpp.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                // eventpicture.setAdjustViewBounds(true);
                                requestpp.getLayoutParams().height = 150;
                                requestpp.getLayoutParams().width = 150;
                                if (image != null) {
                                    displayImage.displayCircleImage(image, requestpp);
                                } else {
                                    requestpp.setImageResource(R.drawable.profilepic);
                                }
                            }
                        }

                        final LinearLayout containerapprovedvertical2 = new LinearLayout(getActivity().getApplicationContext());
                        containerapprovedvertical2.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        //request.setGravity(Gravity.CENTER);
                        containerapprovedvertical2.setOrientation(LinearLayout.VERTICAL);
                        containerapprovedhorizontal.addView(containerapprovedvertical2);

                        final LinearLayout containerapprovedhorizontal2 = new LinearLayout(getActivity().getApplicationContext());
                        containerapprovedhorizontal2.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        //request.setGravity(Gravity.CENTER);
                        containerapprovedhorizontal2.setOrientation(LinearLayout.HORIZONTAL);
                        containerapprovedvertical2.addView(containerapprovedhorizontal2);

                        RelativeLayout textcontainer2 = new RelativeLayout(getActivity().getApplicationContext());
                        containerapprovedvertical2.addView(textcontainer2);
                        textcontainer2.getLayoutParams().height = 80;

                        SimpleDateFormat desiredFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                        String newsdate = desiredFormat.format(Contents.get(finalI).getDate());
                        TextView date = new TextView(getActivity().getApplicationContext());
                        date.setText(newsdate);
                        date.setTextColor(Color.parseColor("#AFAFAF"));
                        date.setTextSize(10);
                        textcontainer2.addView(date);
                        RelativeLayout.LayoutParams textparams2 = (RelativeLayout.LayoutParams) date.getLayoutParams();
                        textparams2.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                        textparams2.setMargins(10, 0, 0, 0);
                        date.setLayoutParams(textparams2);


                        RelativeLayout textcontainer = new RelativeLayout(getActivity().getApplicationContext());
                        containerapprovedhorizontal2.addView(textcontainer);
                        textcontainer.getLayoutParams().height = 110;

                        TextView username = new TextView(getActivity().getApplicationContext());
                        username.setText(Contents.get(finalI).getActor() + " ");
                        username.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getActivity().getApplicationContext(), Profile.class);
                                intent.putExtra("username",Contents.get(finalI).getActor());
                                startActivity(intent);

                            }
                        });
                        username.setTextColor(Color.parseColor("#C70039"));
                        username.setTextSize(16);
                        username.setTypeface(null, Typeface.BOLD);
                        textcontainer.addView(username);
                        RelativeLayout.LayoutParams textparams = (RelativeLayout.LayoutParams) username.getLayoutParams();
                        textparams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                        textparams.setMargins(10, 0, 0, 0);
                        username.setLayoutParams(textparams);

                        RelativeLayout textcontainer3 = new RelativeLayout(getActivity().getApplicationContext());
                        containerapprovedhorizontal2.addView(textcontainer3);
                        textcontainer3.getLayoutParams().height = 110;

                        TextView whathappen = new TextView(getActivity().getApplicationContext());
                        if (Contents.get(finalI).getType().matches("approved")) {
                            whathappen.setText("recieved a ticket for ");
                        } else if (Contents.get(finalI).getType().matches("request")) {
                            whathappen.setText("requested a ticket for ");
                        } else if (Contents.get(finalI).getType().matches("attended")) {
                            whathappen.setText("attended an event ");
                        } else if (Contents.get(finalI).getType().matches("creation")) {
                            whathappen.setText("created an event ");
                        } else if (Contents.get(finalI).getType().matches("review")) {
                            whathappen.setText("rated ");
                        }

                        whathappen.setTextColor(Color.parseColor("#696969"));
                        whathappen.setTextSize(16);
                        textcontainer3.addView(whathappen);
                        RelativeLayout.LayoutParams textparams3 = (RelativeLayout.LayoutParams) whathappen.getLayoutParams();
                        textparams3.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                        textparams3.setMargins(0, 0, 0, 0);
                        whathappen.setLayoutParams(textparams3);

                        RelativeLayout textcontainer4 = new RelativeLayout(getActivity().getApplicationContext());
                        containerapprovedhorizontal2.addView(textcontainer4);
                        textcontainer4.getLayoutParams().height = 110;

                        final TextView eventname = new TextView(getActivity().getApplicationContext());
                        eventname.setText(Contents.get(finalI).getActor() + " ");
                        eventname.setTextColor(Color.parseColor("#C70039"));
                        eventname.setTextSize(16);
                        eventname.setTypeface(null, Typeface.BOLD);
                        textcontainer4.addView(eventname);
                        RelativeLayout.LayoutParams textparams4 = (RelativeLayout.LayoutParams) eventname.getLayoutParams();
                        textparams4.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                        textparams4.setMargins(0, 0, 0, 0);
                        eventname.setLayoutParams(textparams4);

                        ParseQuery<ParseObject> query = ParseQuery.getQuery("Events");
                        query.whereEqualTo("objectId", Contents.get(finalI).getEventid());
                        query.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> objects, ParseException e) {
                                if (e == null) {
                                    {
                                        if (objects.size() > 0) {
                                            for (ParseObject event : objects) {
                                                eventname.setText(event.getString("eventname"));
                                                eventname.setId(finalI);
                                                eventname.setOnClickListener(new View.OnClickListener() {
                                                    public void onClick(View v) {
                                                        int position = eventname.getId();
                                                        final String eventid = Contents.get(position).getEventid();

                                                        final ParseQuery<ParseObject> event = ParseQuery.getQuery("Events");
                                                        event.whereEqualTo("objectId", eventid);
                                                        event.findInBackground(new FindCallback<ParseObject>() {
                                                            @Override
                                                            public void done(List<ParseObject> objects, ParseException e) {
                                                                if (e == null) {
                                                                    if (objects.size() > 0) {
                                                                        for (ParseObject events : objects) {
                                                                            if (events.getString("createdby").matches(ParseUser.getCurrentUser().getUsername())) {
                                                                                Intent intent = new Intent(getActivity().getApplicationContext(), myeventpage.class);
                                                                                intent.putExtra("eventId", eventid);
                                                                                intent.putExtra("eventname", events.getString("eventname"));
                                                                                startActivity(intent);
                                                                            } else {
                                                                                Intent intent = new Intent(getActivity().getApplicationContext(), notmyeventpage.class);
                                                                                intent.putExtra("eventId", eventid);
                                                                                intent.putExtra("eventname", events.getString("eventname"));
                                                                                startActivity(intent);
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
                            }
                        });

                        if(Contents.get(finalI).getType().matches("review")){

                            final TextView rate = new TextView(getActivity().getApplicationContext());
                            rate.setTextColor(Color.parseColor("#000000"));
                            rate.setTextSize(18);
                            rate.setTypeface(null, Typeface.BOLD);
                            rate.setPadding(20,0,0,20);
                            containerapprovedvertical.addView(rate);
                            ParseQuery<ParseObject> review = ParseQuery.getQuery("eventreview");
                            review.whereEqualTo("username",Contents.get(finalI).getActor());
                            review.whereEqualTo("eventid",Contents.get(finalI).getEventid());
                            review.findInBackground(new FindCallback<ParseObject>() {
                                @Override
                                public void done(List<ParseObject> objects, ParseException e) {
                                    if (e == null) {
                                        if (objects.size() > 0) {
                                            for (ParseObject reviews : objects) {
                                                if(reviews.getNumber("rate").intValue()==1){
                                                    rate.setText("Terrible!");
                                                }else if(reviews.getNumber("rate").intValue()==2){
                                                    rate.setText("Bad");
                                                }else if(reviews.getNumber("rate").intValue()==3){
                                                    rate.setText("Okay");
                                                }else if(reviews.getNumber("rate").intValue()==4){
                                                    rate.setText("Good");
                                                }else if(reviews.getNumber("rate").intValue()==5){
                                                    rate.setText("Great!");
                                                }

                                                String review;
                                                review=reviews.getString("review");
                                                if(!review.matches("")){
                                                    final TextView kalam = new TextView(getActivity().getApplicationContext());
                                                    rate.setPadding(20,0,0,0);
                                                    kalam.setTextColor(Color.parseColor("#696969"));
                                                    kalam.setTextSize(16);
                                                    kalam.setPadding(20,0,0,20);
                                                    containerapprovedvertical.addView(kalam);
                                                    kalam.setText(review);
                                                }
                                                if(reviews.getParseFile("pic")!=null){
                                                    ParseFile imagez = (ParseFile) reviews.getParseFile("pic");
                                                    ImageView eventpicture = new ImageView(getActivity().getApplicationContext());
                                                    containerapprovedvertical.addView(eventpicture);
                                                    Uri imageUri = Uri.parse(imagez.getUrl());
                                                    Glide.with(getActivity().getApplicationContext()).load(imageUri)
                                                            .into(eventpicture);
                                                    eventpicture.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                                    // eventpicture.setAdjustViewBounds(true);
                                                    eventpicture.getLayoutParams().height = 600;
                                                    eventpicture.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
                                                    eventpicture.setPadding(10,0,10,20);
                                                }
                                            }
                                        }
                                    }
                                }
                            });

                        }


                    }

                }
            });
        };
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        rootView = null; // now cleaning up!
    }
}

