package com.parse.starter;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.SimpleDateFormat;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.parse.starter.archivedevent.archivedevent;


/**
 * A simple {@link Fragment} subclass.
 */
public class reviews extends Fragment {


    public reviews() {
        // Required empty public constructor
    }

View rootView;
    ParseFile image;
    int i=0;;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_reviews, container, false);
        final LinearLayout bigcontainer = (LinearLayout) rootView.findViewById(R.id.bigcontainer);



        final ParseQuery<ParseObject> review = ParseQuery.getQuery("eventreview");
        review.whereEqualTo("eventid",archivedevent.getId());
        review.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    if (objects.size() > 0) {
                        for (final ParseObject reviews : objects) {
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
                            i++;

                            final LinearLayout containerapprovedhorizontal = new LinearLayout(getActivity().getApplicationContext());
                            containerapprovedhorizontal.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                            //request.setGravity(Gravity.CENTER);
                            containerapprovedhorizontal.setOrientation(LinearLayout.HORIZONTAL);
                            containerapprovedvertical.addView(containerapprovedhorizontal);

                            ParseQuery<ParseUser> getinfo = ParseUser.getQuery();
                            getinfo.whereEqualTo("username", reviews.getString("username"));
                            final int finalI = i;
                            getinfo.findInBackground(new FindCallback<ParseUser>() {
                                    @Override
                                    public void done(List<ParseUser> objects, ParseException e) {
                                        if (e == null) {
                                            if (objects.size() > 0) {
                                                for (ParseObject user : objects) {
                                                    {
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
                                            String newsdate = desiredFormat.format(reviews.getCreatedAt());
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
                                            username.setText(reviews.getString("username") + " ");
                                            username.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    Intent intent = new Intent(getActivity().getApplicationContext(), Profile.class);
                                                    intent.putExtra("username",reviews.getString("username"));
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
                                                whathappen.setText("rated ");

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
                                            eventname.setTextColor(Color.parseColor("#C70039"));
                                            eventname.setTextSize(16);
                                            eventname.setTypeface(null, Typeface.BOLD);
                                            textcontainer4.addView(eventname);
                                            RelativeLayout.LayoutParams textparams4 = (RelativeLayout.LayoutParams) eventname.getLayoutParams();
                                            textparams4.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                                            textparams4.setMargins(0, 0, 0, 0);
                                            eventname.setLayoutParams(textparams4);

                                            ParseQuery<ParseObject> query = ParseQuery.getQuery("Events");
                                            query.whereEqualTo("objectId", reviews.getString("eventid"));
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
                                                                            final ParseQuery<ParseObject> event = ParseQuery.getQuery("Events");
                                                                            event.whereEqualTo("objectId", reviews.getString("eventid"));
                                                                            event.findInBackground(new FindCallback<ParseObject>() {
                                                                                @Override
                                                                                public void done(List<ParseObject> objects, ParseException e) {
                                                                                    if (e == null) {
                                                                                        if (objects.size() > 0) {
                                                                                            for (ParseObject events : objects) {
                                                                                                Intent intent = new Intent(getActivity().getApplicationContext(), archivedevent.class);
                                                                                                intent.putExtra("eventId", events.getObjectId());
                                                                                                intent.putExtra("eventname", events.getString("eventname"));
                                                                                                startActivity(intent);
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

                                            final TextView rate = new TextView(getActivity().getApplicationContext());
                                            rate.setTextColor(Color.parseColor("#000000"));
                                            rate.setTextSize(18);
                                            rate.setTypeface(null, Typeface.BOLD);
                                            rate.setPadding(20,0,0,20);
                                            containerapprovedvertical.addView(rate);

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
                                                ImageView eventpicture = new ImageView(getActivity().getApplicationContext());
                                                containerapprovedvertical.addView(eventpicture);
                                                DisplayImage displayImage = new DisplayImage();
                                                displayImage.displayImage(reviews.getParseFile("pic"), eventpicture);
                                                eventpicture.setScaleType(ImageView.ScaleType.FIT_CENTER);
                                                eventpicture.setAdjustViewBounds(true);
                                                eventpicture.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
                                                eventpicture.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
                                            }


                                        }
                                    }

                            });



                        }
                    }
                }
            }
        });

        return rootView;
    }

}
