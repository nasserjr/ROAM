package com.parse.starter;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.parse.starter.MapFragment.currentlocation;


/**
 * A simple {@link Fragment} subclass.
 */
public class listfragment extends Fragment implements View.OnClickListener {

    ArrayList<Event> eventarray = new ArrayList<>();
    int i;
    boolean filterinterest = false;
    LinearLayout linearLayout;
    View rootView;
    List<String> myinterests;
    TextView searchtext;
    boolean searchexist=false;
    String search;
    public listfragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_listfragment, container, false);

        Button mapview = (Button) rootView.findViewById(R.id.mapview);
        mapview.setOnClickListener(this);
        Button search = (Button) rootView.findViewById(R.id.search);
        search.setOnClickListener(this);
        searchtext = (TextView)rootView.findViewById(R.id.gosearch);
        eventarray.clear();
        final CheckBox interestbox = (CheckBox)rootView.findViewById(R.id.interestfilter);
        interestbox.setOnClickListener(this);


            myinterests = ParseUser.getCurrentUser().getList("interests");


        ParseQuery<ParseObject> query = ParseQuery.getQuery("Events");
        query.whereEqualTo("state","notyet");
        query.orderByDescending("promoted");
        query.addDescendingOrder("createdAt");

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null){

                    if (objects.size() > 0) {

                        for (ParseObject events : objects) {

                            Event event = new Event();
                            event.setId(events.getObjectId());
                            event.setInterest(events.getString("intereset"));
                            event.setName(events.getString("eventname"));
                            event.setCreatedby(events.getString("createdby"));
                            event.setAddress(events.getString("address"));
                            event.setEventpicture(events.getParseFile("eventpicture"));
                            event.setEventdate(events.getString("eventdate"));
                            event.setEventtime(events.getString("eventtime"));
                            event.setPromoted(events.getNumber("promoted").intValue());
                            event.setOthermanagers(events.<String>getList("othermanagers"));
                            ParseGeoPoint point = events.getParseGeoPoint("location");
                            Double lat = point.getLatitude();
                            Double log = point.getLongitude();
                            LatLng eventlocation = new LatLng(lat, log);
                            event.setLocation(eventlocation);
                            eventarray.add(event);

                        }


                        interestbox.setVisibility(View.VISIBLE);
                        showevents(rootView);


                    }
                }
                else{
                    //e.printStackTrace();
                }
            }
        });







        return  rootView;
    }


    public void showevents(View view) {
        linearLayout = (LinearLayout) view.findViewById(R.id.eventlayout);


        for (i = 0; i < eventarray.size(); i++) {


            if (searchexist == true) {
                if (eventarray.get(i).getName().toLowerCase().contains(search.toLowerCase()) ||
                        eventarray.get(i).getAddress().toLowerCase().contains(search.toLowerCase())||
                        eventarray.get(i).getEventdate().toLowerCase().contains(search.toLowerCase())||
                        eventarray.get(i).getEventtime().toLowerCase().contains(search.toLowerCase()) ) {

                    if (filterinterest == false) {


                        final LinearLayout eventcontent = new LinearLayout(getActivity().getApplicationContext());
                        eventcontent.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        eventcontent.setOrientation(LinearLayout.VERTICAL);
                        LinearLayout nameanddate = new LinearLayout(getActivity().getApplicationContext());
                        nameanddate.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        nameanddate.setOrientation(LinearLayout.HORIZONTAL);
                        linearLayout.addView(eventcontent);
                        eventcontent.setBackgroundColor(Color.parseColor("#C70039"));
                        LinearLayout.LayoutParams contentparams = (LinearLayout.LayoutParams) eventcontent.getLayoutParams();
                        contentparams.setMargins(0, 0, 0, 40);
                        eventcontent.setLayoutParams(contentparams);
                        TextView eventname = new TextView(getActivity().getApplicationContext());
                        TextView eventdate = new TextView(getActivity().getApplicationContext());
                        TextView eventaddress = new TextView(getActivity().getApplicationContext());
                        ImageView eventpicture = new ImageView(getActivity().getApplicationContext());
                        eventname.setText(eventarray.get(i).getName());
                        eventdate.setText(eventarray.get(i).getEventdate() + " " + eventarray.get(i).getEventtime());
                        eventaddress.setText(eventarray.get(i).getAddress());
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

                        if (eventarray.get(i).getEventpicture() != null) {

                            Uri imageUri = Uri.parse(eventarray.get(i).getEventpicture().getUrl());
                            Glide.with(getActivity().getApplicationContext()).load(imageUri)
                                    .into(eventpicture);

                        } else {
                            eventpicture.setImageResource(R.drawable.profilepic);
                        }

                        if(eventarray.get(i).getPromoted()==1){
                            TextView promoted = new TextView(getActivity().getApplicationContext());
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

                                int position = eventcontent.getId();
                                Event event = eventarray.get(position);
                                if (event.getOthermanagers().contains(ParseUser.getCurrentUser().getUsername())) {
                                    Intent intent = new Intent(getActivity().getApplicationContext(), myeventpage.class);
                                    intent.putExtra("eventId", event.getId());
                                    intent.putExtra("eventname", event.getName());
                                    startActivity(intent);
                                } else {
                                    Intent intent = new Intent(getActivity().getApplicationContext(), notmyeventpage.class);
                                    intent.putExtra("eventId", event.getId());
                                    intent.putExtra("eventname", event.getName());
                                    startActivity(intent);
                                }

                            }
                        });
                    }


                    else {
                        if (myinterests.contains(eventarray.get(i).getInterest())) {
                            final LinearLayout eventcontent = new LinearLayout(getActivity().getApplicationContext());
                            eventcontent.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                            eventcontent.setOrientation(LinearLayout.VERTICAL);
                            LinearLayout nameanddate = new LinearLayout(getActivity().getApplicationContext());
                            nameanddate.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                            nameanddate.setOrientation(LinearLayout.HORIZONTAL);
                            linearLayout.addView(eventcontent);
                            eventcontent.setBackgroundColor(Color.parseColor("#C70039"));
                            LinearLayout.LayoutParams contentparams = (LinearLayout.LayoutParams) eventcontent.getLayoutParams();
                            contentparams.setMargins(0, 0, 0, 40);
                            eventcontent.setLayoutParams(contentparams);
                            TextView eventname = new TextView(getActivity().getApplicationContext());
                            TextView eventdate = new TextView(getActivity().getApplicationContext());
                            TextView eventaddress = new TextView(getActivity().getApplicationContext());
                            ImageView eventpicture = new ImageView(getActivity().getApplicationContext());
                            eventname.setText(eventarray.get(i).getName());
                            eventdate.setText(eventarray.get(i).getEventdate() + " " + eventarray.get(i).getEventtime());
                            eventaddress.setText(eventarray.get(i).getAddress());
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

                            if (eventarray.get(i).getEventpicture() != null) {

                                Uri imageUri = Uri.parse(eventarray.get(i).getEventpicture().getUrl());
                                Glide.with(getActivity().getApplicationContext()).load(imageUri)
                                        .into(eventpicture);

                            } else {
                                eventpicture.setImageResource(R.drawable.profilepic);
                            }

                            if(eventarray.get(i).getPromoted()==1){
                                TextView promoted = new TextView(getActivity().getApplicationContext());
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
                                    int position = eventcontent.getId();
                                    Event event = eventarray.get(position);
                                    if (event.getOthermanagers().contains(ParseUser.getCurrentUser().getUsername())) {
                                        Intent intent = new Intent(getActivity().getApplicationContext(), myeventpage.class);
                                        intent.putExtra("eventId", event.getId());
                                        intent.putExtra("eventname", event.getName());
                                        startActivity(intent);
                                    } else {
                                        Intent intent = new Intent(getActivity().getApplicationContext(), notmyeventpage.class);
                                        intent.putExtra("eventId", event.getId());
                                        intent.putExtra("eventname", event.getName());
                                        startActivity(intent);
                                    }

                                }
                            });


                        }

                    }}
            } else {
                if (filterinterest == false) {


                    final LinearLayout eventcontent = new LinearLayout(getActivity().getApplicationContext());
                    eventcontent.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    eventcontent.setOrientation(LinearLayout.VERTICAL);
                    LinearLayout nameanddate = new LinearLayout(getActivity().getApplicationContext());
                    nameanddate.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    nameanddate.setOrientation(LinearLayout.HORIZONTAL);
                    linearLayout.addView(eventcontent);
                    eventcontent.setBackgroundColor(Color.parseColor("#C70039"));
                    LinearLayout.LayoutParams contentparams = (LinearLayout.LayoutParams) eventcontent.getLayoutParams();
                    contentparams.setMargins(0, 0, 0, 40);
                    eventcontent.setLayoutParams(contentparams);
                    TextView eventname = new TextView(getActivity().getApplicationContext());
                    TextView eventdate = new TextView(getActivity().getApplicationContext());
                    TextView eventaddress = new TextView(getActivity().getApplicationContext());
                    ImageView eventpicture = new ImageView(getActivity().getApplicationContext());
                    eventname.setText(eventarray.get(i).getName());
                    eventdate.setText(eventarray.get(i).getEventdate() + " " + eventarray.get(i).getEventtime());
                    eventaddress.setText(eventarray.get(i).getAddress());
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

                    if (eventarray.get(i).getEventpicture() != null) {

                        Uri imageUri = Uri.parse(eventarray.get(i).getEventpicture().getUrl());
                        Glide.with(getActivity().getApplicationContext()).load(imageUri)
                                .into(eventpicture);

                    } else {
                        eventpicture.setImageResource(R.drawable.profilepic);
                    }

                    if(eventarray.get(i).getPromoted()==1){
                        TextView promoted = new TextView(getActivity().getApplicationContext());
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
                            int position = eventcontent.getId();
                            Event event = eventarray.get(position);
                            if (event.getOthermanagers().contains(ParseUser.getCurrentUser().getUsername())) {
                                Intent intent = new Intent(getActivity().getApplicationContext(), myeventpage.class);
                                intent.putExtra("eventId", event.getId());
                                intent.putExtra("eventname", event.getName());
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(getActivity().getApplicationContext(), notmyeventpage.class);
                                intent.putExtra("eventId", event.getId());
                                intent.putExtra("eventname", event.getName());
                                startActivity(intent);
                            }

                        }
                    });
                } else {
                    if (myinterests.contains(eventarray.get(i).getInterest())) {
                        final LinearLayout eventcontent = new LinearLayout(getActivity().getApplicationContext());
                        eventcontent.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        eventcontent.setOrientation(LinearLayout.VERTICAL);
                        LinearLayout nameanddate = new LinearLayout(getActivity().getApplicationContext());
                        nameanddate.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        nameanddate.setOrientation(LinearLayout.HORIZONTAL);
                        linearLayout.addView(eventcontent);
                        eventcontent.setBackgroundColor(Color.parseColor("#C70039"));
                        LinearLayout.LayoutParams contentparams = (LinearLayout.LayoutParams) eventcontent.getLayoutParams();
                        contentparams.setMargins(0, 0, 0, 40);
                        eventcontent.setLayoutParams(contentparams);
                        TextView eventname = new TextView(getActivity().getApplicationContext());
                        TextView eventdate = new TextView(getActivity().getApplicationContext());
                        TextView eventaddress = new TextView(getActivity().getApplicationContext());
                        ImageView eventpicture = new ImageView(getActivity().getApplicationContext());
                        eventname.setText(eventarray.get(i).getName());
                        eventdate.setText(eventarray.get(i).getEventdate() + " " + eventarray.get(i).getEventtime());
                        eventaddress.setText(eventarray.get(i).getAddress());
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

                        if (eventarray.get(i).getEventpicture() != null) {

                            Uri imageUri = Uri.parse(eventarray.get(i).getEventpicture().getUrl());
                            Glide.with(getActivity().getApplicationContext()).load(imageUri)
                                    .into(eventpicture);

                        } else {
                            eventpicture.setImageResource(R.drawable.profilepic);
                        }

                        if(eventarray.get(i).getPromoted()==1){
                            TextView promoted = new TextView(getActivity().getApplicationContext());
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
                                int position = eventcontent.getId();
                                Event event = eventarray.get(position);

                                if (event.getOthermanagers().contains(ParseUser.getCurrentUser().getUsername())) {
                                    Intent intent = new Intent(getActivity().getApplicationContext(), myeventpage.class);
                                    intent.putExtra("eventId", event.getId());
                                    intent.putExtra("eventname", event.getName());
                                    startActivity(intent);
                                } else {
                                    Intent intent = new Intent(getActivity().getApplicationContext(), notmyeventpage.class);
                                    intent.putExtra("eventId", event.getId());
                                    intent.putExtra("eventname", event.getName());
                                    startActivity(intent);
                                }


                            }
                        });


                    }

                }
            }
       /* TextView questionstitle = new TextView(getActivity().getApplicationContext());
        questionstitle.setText("Questions");
        questionstitle.setTextSize(18);
        questionstitle.setTypeface(null, Typeface.BOLD);
        questionstitle.setTextColor(Color.parseColor("#696969"));
        linearLayout.addView(questionstitle); */

        }
    }


    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.mapview){
            Fragment mapview = new MapFragment();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.content, mapview);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.addToBackStack(null);
            ft.commit();
        }
        else if(v.getId()==R.id.interestfilter)
        {
            CheckBox checkBox = (CheckBox) v;
            if (checkBox.isChecked()) {
                filterinterest=true;
                linearLayout.removeAllViews();
                showevents(rootView);

            } else {
                filterinterest=false;
                linearLayout.removeAllViews();
                showevents(rootView);
            }
        }
        else if(v.getId()==R.id.search){
            search = searchtext.getText().toString();
            if(search.matches("")){
                searchexist=true;
                linearLayout.removeAllViews();
                showevents(rootView);
            }
            else{
                searchexist=true;
                linearLayout.removeAllViews();
                showevents(rootView);
            }

        }
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
