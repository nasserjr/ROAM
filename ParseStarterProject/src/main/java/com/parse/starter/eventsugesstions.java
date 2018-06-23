package com.parse.starter;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.model.LatLng;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import static com.parse.starter.MapFragment.currentlocation;

public class eventsugesstions extends AppCompatActivity {


    List<String> myinterests;
    LinearLayout linearLayout;
    int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventsugesstions);

            myinterests = ParseUser.getCurrentUser().getList("interests");


        Toolbar headerup = (Toolbar)findViewById(R.id.my_toolbar);
        setSupportActionBar(headerup);
        getSupportActionBar().setTitle("Events you may like");

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Events");
        query.whereEqualTo("state","notyet");
        query.addDescendingOrder("createdAt");

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null){

                    if (objects.size() > 0) {
                        linearLayout = (LinearLayout)findViewById(R.id.eventlayout);

                        for (final ParseObject events : objects) {
                            if (myinterests.contains(events.getString("intereset"))&&distance(currentlocation.latitude,currentlocation.longitude,events.getParseGeoPoint("location").getLatitude(),events.getParseGeoPoint("location").getLongitude())<100000) {
                                final LinearLayout eventcontent = new LinearLayout(getApplicationContext());
                                eventcontent.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                eventcontent.setOrientation(LinearLayout.VERTICAL);
                                LinearLayout nameanddate = new LinearLayout(getApplicationContext());
                                nameanddate.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                nameanddate.setOrientation(LinearLayout.HORIZONTAL);
                                linearLayout.addView(eventcontent);
                                eventcontent.setBackgroundColor(Color.parseColor("#C70039"));
                                LinearLayout.LayoutParams contentparams = (LinearLayout.LayoutParams) eventcontent.getLayoutParams();
                                contentparams.setMargins(0, 0, 0, 40);
                                eventcontent.setLayoutParams(contentparams);
                                TextView eventname = new TextView(getApplicationContext());
                                TextView eventdate = new TextView(getApplicationContext());
                                TextView eventaddress = new TextView(getApplicationContext());
                                ImageView eventpicture = new ImageView(getApplicationContext());
                                eventname.setText(events.getString("eventname"));
                                eventdate.setText(events.getString("eventdate") + " " + events.getString("eventtime"));
                                eventaddress.setText(events.getString("address"));
                                eventaddress.setTextColor(Color.parseColor("#FFFFFF"));
                                eventdate.setTextColor(Color.parseColor("#700020"));
                                eventname.setTextColor(Color.parseColor("#FFFFFF"));
                                eventname.setTypeface(null, Typeface.BOLD);
                                eventname.setTextSize(16);


           /*
            android.view.ViewGroup.LayoutParams params = view.getLayoutParams();
            params.width = 100;
            params.height =150;
            eventpicture.setLayoutParams(params);
            */

                                if (events.getParseFile("eventpicture") != null) {

                                    Uri imageUri = Uri.parse(events.getParseFile("eventpicture").getUrl());
                                    Glide.with(getApplicationContext()).load(imageUri)
                                            .into(eventpicture);

                                } else {
                                    eventpicture.setImageResource(R.drawable.profilepic);
                                }

                                if(events.getNumber("promoted").intValue()==1){
                                    TextView promoted = new TextView(getApplicationContext());
                                    promoted.setText("sponsored");
                                    promoted.setTextColor(Color.parseColor("#FFFFFF"));
                                    promoted.setPadding(5,5,5,5);
                                    promoted.setTextSize(14);
                                    eventcontent.addView(promoted);
                                }

                                eventcontent.addView(eventpicture);
                                eventpicture.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                // eventpicture.setAdjustViewBounds(true);
                                eventpicture.getLayoutParams().height = 600;
                                eventpicture.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
                                nameanddate.addView(eventname);
                                nameanddate.addView(eventdate);
                                LinearLayout.LayoutParams textparams = (LinearLayout.LayoutParams) eventname.getLayoutParams();
                                textparams.setMargins(10, 5, 0, 5);
                                eventname.setLayoutParams(textparams);

                                LinearLayout.LayoutParams dateparams = (LinearLayout.LayoutParams) eventdate.getLayoutParams();
                                dateparams.setMargins(40, 5, 0, 5);
                                eventdate.setLayoutParams(dateparams);

                                eventcontent.addView(nameanddate);
                                eventcontent.addView(eventaddress);
                                eventaddress.setLayoutParams(textparams);
                                eventcontent.setId(i);

                                eventcontent.setOnClickListener(new View.OnClickListener() {
                                    public void onClick(View v) {
                                        if (events.getList("othermanagers").contains(ParseUser.getCurrentUser().getUsername())) {
                                            Intent intent = new Intent(getApplicationContext(), myeventpage.class);
                                            intent.putExtra("eventId", events.getObjectId());
                                            intent.putExtra("eventname", events.getString("eventname"));
                                            startActivity(intent);
                                        } else {
                                            Intent intent = new Intent(getApplicationContext(), notmyeventpage.class);
                                            intent.putExtra("eventId", events.getObjectId());
                                            intent.putExtra("eventname", events.getString("eventname"));
                                            startActivity(intent);
                                        }

                                    }
                                });


                            }


                        }

                    }
                }
                else{
                    //e.printStackTrace();
                }
            }
        });

    }
    public float distance (double lat_a, double lng_a, double lat_b, double lng_b )
    {
        double earthRadius = 3958.75;
        double latDiff = Math.toRadians(lat_b-lat_a);
        double lngDiff = Math.toRadians(lng_b-lng_a);
        double a = Math.sin(latDiff /2) * Math.sin(latDiff /2) +
                Math.cos(Math.toRadians(lat_a)) * Math.cos(Math.toRadians(lat_b)) *
                        Math.sin(lngDiff /2) * Math.sin(lngDiff /2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double distance = earthRadius * c;

        int meterConversion = 1609;

        return new Float(distance * meterConversion).floatValue();
    }

}
