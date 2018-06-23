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
import com.parse.SaveCallback;

import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.parse.starter.myeventpage.myevent;


/**
 * A simple {@link Fragment} subclass.
 */
public class addmanagers extends Fragment {


    View rootView;
    LinearLayout currentmanagers;
    LinearLayout addmanagers;
    ParseFile image;
    List managers;
    public addmanagers() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_addmanagers, container, false);
        currentmanagers = (LinearLayout)rootView.findViewById(R.id.currentmanagers);
        addmanagers = (LinearLayout)rootView.findViewById(R.id.addmanagers);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Events");
        query.whereEqualTo("objectId",myevent.getId());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    {
                        if (objects.size() > 0) {
                            for (final ParseObject events : objects) {
                                managers = events.getList("othermanagers");
                                for (final Object managername : managers) {
                                    final LinearLayout request = new LinearLayout(getActivity().getApplicationContext());
                                    request.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                    //request.setGravity(Gravity.CENTER);
                                    request.setOrientation(LinearLayout.HORIZONTAL);
                                    currentmanagers.addView(request);
                                    LinearLayout.LayoutParams requestparams = (LinearLayout.LayoutParams) request.getLayoutParams();
                                    requestparams.setMargins(20, 20, 20, 20);
                                    request.setLayoutParams(requestparams);

                                    ParseQuery<ParseUser> query2 = ParseUser.getQuery();
                                    query2.whereEqualTo("username", managername.toString());
                                    query2.findInBackground(new FindCallback<ParseUser>() {
                                                                @Override
                                                                public void done(List<ParseUser> objects, ParseException e) {
                                                                    if (e == null) {
                                                                        {

                                                                            if (objects.size() > 0) {

                                                                                for (ParseObject user : objects) {

                                                                                    image = (ParseFile) user.getParseFile("profilepicture");
                                                                                    CircleImageView requestpp = new CircleImageView(getActivity().getApplicationContext());

                                                                                    request.addView(requestpp);

                                                                                    requestpp.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                                                                    // eventpicture.setAdjustViewBounds(true);
                                                                                    requestpp.getLayoutParams().height = 150;
                                                                                    requestpp.getLayoutParams().width = 150;
                                                                                    if (image != null) {
                                                                                        Uri imageUri = Uri.parse(image.getUrl());
                                                                                        Glide.with(getActivity().getApplicationContext()).load(imageUri)
                                                                                                .into(requestpp);
                                                                                    } else {
                                                                                        requestpp.setImageResource(R.drawable.profilepic);
                                                                                    }

                                                                                    RelativeLayout textcontainer = new RelativeLayout(getActivity().getApplicationContext());
                                                                                    request.addView(textcontainer);
                                                                                    textcontainer.getLayoutParams().height = 150;

                                                                                    TextView requestusername = new TextView(getActivity().getApplicationContext());
                                                                                    requestusername.setText(managername.toString());
                                                                                    requestusername.setOnClickListener(new View.OnClickListener() {
                                                                                        @Override
                                                                                        public void onClick(View v) {

                                                                                            ParseQuery<ParseObject> query3 = ParseQuery.getQuery("suspended");
                                                                                            query3.whereEqualTo("username", managername.toString());
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
                                                                                                                intent.putExtra("username", managername.toString());
                                                                                                                startActivity(intent);
                                                                                                            }
                                                                                                        }
                                                                                                    }
                                                                                                }
                                                                                            });


                                                                                        }
                                                                                    });
                                                                                    requestusername.setTextColor(Color.parseColor("#C70039"));
                                                                                    requestusername.setTextSize(20);
                                                                                    requestusername.setTypeface(null, Typeface.BOLD);
                                                                                    textcontainer.addView(requestusername);
                                                                                    RelativeLayout.LayoutParams textparams = (RelativeLayout.LayoutParams) requestusername.getLayoutParams();
                                                                                    textparams.addRule(RelativeLayout.CENTER_VERTICAL);
                                                                                    textparams.setMargins(20, 0, 0, 0);
                                                                                    requestusername.setLayoutParams(textparams);

                                                                                    if (!myevent.getCreatedby().matches(managername.toString())) {
                                                                                        RelativeLayout buttonscontainer = new RelativeLayout(getActivity().getApplicationContext());
                                                                                        request.addView(buttonscontainer);
                                                                                        buttonscontainer.getLayoutParams().height = 150;
                                                                                        final ImageView comeornot = new ImageView(getActivity().getApplicationContext());
                                                                                        buttonscontainer.addView(comeornot);
                                                                                        comeornot.getLayoutParams().height = 80;
                                                                                        comeornot.getLayoutParams().width = 80;
                                                                                        comeornot.setImageResource(R.drawable.trash);
                                                                                        comeornot.setOnClickListener(new View.OnClickListener() {
                                                                                            @Override
                                                                                            public void onClick(View v) {
                                                                                                comeornot.setVisibility(View.GONE);
                                                                                                Toast.makeText(getActivity().getApplicationContext(), managername.toString() + " is removed", Toast.LENGTH_SHORT).show();

                                                                                                managers.remove(managername.toString());
                                                                                                ParseQuery<ParseObject> query = ParseQuery.getQuery("Events");
                                                                                                query.whereEqualTo("objectId", myevent.getId());
                                                                                                query.findInBackground(new FindCallback<ParseObject>() {
                                                                                                    @Override
                                                                                                    public void done(List<ParseObject> objects, ParseException e) {
                                                                                                        if (e == null) {
                                                                                                            {
                                                                                                                if (objects.size() > 0) {
                                                                                                                    for (ParseObject events : objects) {
                                                                                                                        events.put("othermanagers", managers);
                                                                                                                        events.saveInBackground(new SaveCallback() {
                                                                                                                            @Override
                                                                                                                            public void done(ParseException e) {
                                                                                                                                managers.clear();
                                                                                                                                FragmentTransaction ft = getFragmentManager().beginTransaction();
                                                                                                                                ft.detach(addmanagers.this).attach(addmanagers.this).commit();
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
                                                                                        RelativeLayout.LayoutParams rejectparams = (RelativeLayout.LayoutParams) comeornot.getLayoutParams();
                                                                                        rejectparams.addRule(RelativeLayout.CENTER_VERTICAL);
                                                                                        rejectparams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                                                                                        rejectparams.setMargins(20, 0, 0, 0);

                                                                                    }

                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                    );
                                }
                            }
                        }


                    }
                    final List following = ParseUser.getCurrentUser().getList("following");


                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Events");
                    query.whereEqualTo("objectId", myevent.getId());
                    query.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> objects, ParseException e) {
                            if (e == null) {
                                {
                                    if (objects.size() > 0) {
                                        for (ParseObject events : objects) {
                                            final List managerz = events.getList("othermanagers");
                                            if(following!=null){
                                            for (final Object people : following) {
                                                if (!managerz.contains(people.toString())) {
                                                    {
                                                        final LinearLayout request = new LinearLayout(getActivity().getApplicationContext());
                                                        request.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                                        //request.setGravity(Gravity.CENTER);
                                                        request.setOrientation(LinearLayout.HORIZONTAL);
                                                        addmanagers.addView(request);
                                                        LinearLayout.LayoutParams requestparams = (LinearLayout.LayoutParams) request.getLayoutParams();
                                                        requestparams.setMargins(20, 20, 20, 20);
                                                        request.setLayoutParams(requestparams);

                                                        ParseQuery<ParseUser> query2 = ParseUser.getQuery();
                                                        query2.whereEqualTo("username", people.toString());
                                                        query2.findInBackground(new FindCallback<ParseUser>() {
                                                                                    @Override
                                                                                    public void done(List<ParseUser> objects, ParseException e) {
                                                                                        if (e == null) {
                                                                                            {

                                                                                                if (objects.size() > 0) {

                                                                                                    for (ParseObject user : objects) {

                                                                                                        image = (ParseFile) user.getParseFile("profilepicture");
                                                                                                        CircleImageView requestpp = new CircleImageView(getActivity().getApplicationContext());
                                                                                                        DisplayImage displayImage = new DisplayImage();

                                                                                                        request.addView(requestpp);

                                                                                                        requestpp.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                                                                                        // eventpicture.setAdjustViewBounds(true);
                                                                                                        requestpp.getLayoutParams().height = 150;
                                                                                                        requestpp.getLayoutParams().width = 150;
                                                                                                        if (image != null) {
                                                                                                            displayImage.displayCircleImage(image, requestpp);
                                                                                                        } else {
                                                                                                            requestpp.setImageResource(R.drawable.profilepic);
                                                                                                        }

                                                                                                        RelativeLayout textcontainer = new RelativeLayout(getActivity().getApplicationContext());
                                                                                                        request.addView(textcontainer);
                                                                                                        textcontainer.getLayoutParams().height = 150;

                                                                                                        TextView requestusername = new TextView(getActivity().getApplicationContext());
                                                                                                        requestusername.setText(people.toString());
                                                                                                        requestusername.setOnClickListener(new View.OnClickListener() {
                                                                                                            @Override
                                                                                                            public void onClick(View v) {

                                                                                                                Intent intent = new Intent(getActivity().getApplicationContext(), Profile.class);
                                                                                                                intent.putExtra("username", people.toString());
                                                                                                                startActivity(intent);

                                                                                                            }
                                                                                                        });
                                                                                                        requestusername.setTextColor(Color.parseColor("#C70039"));
                                                                                                        requestusername.setTextSize(20);
                                                                                                        requestusername.setTypeface(null, Typeface.BOLD);
                                                                                                        textcontainer.addView(requestusername);
                                                                                                        RelativeLayout.LayoutParams textparams = (RelativeLayout.LayoutParams) requestusername.getLayoutParams();
                                                                                                        textparams.addRule(RelativeLayout.CENTER_VERTICAL);
                                                                                                        textparams.setMargins(20, 0, 0, 0);
                                                                                                        requestusername.setLayoutParams(textparams);

                                                                                                        RelativeLayout buttonscontainer = new RelativeLayout(getActivity().getApplicationContext());
                                                                                                        request.addView(buttonscontainer);
                                                                                                        buttonscontainer.getLayoutParams().height = 150;
                                                                                                        final ImageView comeornot = new ImageView(getActivity().getApplicationContext());
                                                                                                        buttonscontainer.addView(comeornot);
                                                                                                        comeornot.getLayoutParams().height = 80;
                                                                                                        comeornot.getLayoutParams().width = 80;
                                                                                                        comeornot.setImageResource(R.drawable.addmanager);
                                                                                                        comeornot.setOnClickListener(new View.OnClickListener() {
                                                                                                            @Override
                                                                                                            public void onClick(View v) {
                                                                                                                comeornot.setVisibility(View.GONE);
                                                                                                                Toast.makeText(getActivity().getApplicationContext(), people.toString() + " is now a manager", Toast.LENGTH_SHORT).show();

                                                                                                                managerz.add(people.toString());
                                                                                                                ParseQuery<ParseObject> query = ParseQuery.getQuery("Events");
                                                                                                                query.whereEqualTo("objectId", myevent.getId());
                                                                                                                query.findInBackground(new FindCallback<ParseObject>() {
                                                                                                                    @Override
                                                                                                                    public void done(List<ParseObject> objects, ParseException e) {
                                                                                                                        if (e == null) {
                                                                                                                            {
                                                                                                                                if (objects.size() > 0) {
                                                                                                                                    for (ParseObject events : objects) {
                                                                                                                                        events.put("othermanagers", managerz);
                                                                                                                                        events.saveInBackground(new SaveCallback() {
                                                                                                                                            @Override
                                                                                                                                            public void done(ParseException e) {

                                                                                                                                                final ParseObject notify = new ParseObject("notifications");
                                                                                                                                                notify.put("sendername", ParseUser.getCurrentUser().getUsername().toString());
                                                                                                                                                notify.put("recievername",people.toString());
                                                                                                                                                notify.put("read","0");
                                                                                                                                                notify.put("eventid",myevent.getId());
                                                                                                                                                notify.put("type","manage");
                                                                                                                                                notify.saveInBackground(new SaveCallback() {
                                                                                                                                                    @Override
                                                                                                                                                    public void done(ParseException e) {
                                                                                                                                                        ParseQuery<ParseObject> query = ParseQuery.getQuery("userevent");
                                                                                                                                                        query.whereEqualTo("eventid", myevent.getId());
                                                                                                                                                        query.whereEqualTo("username", people.toString());
                                                                                                                                                        query.findInBackground(new FindCallback<ParseObject>() {
                                                                                                                                                            @Override
                                                                                                                                                            public void done(List<ParseObject> objects, ParseException e) {
                                                                                                                                                                if (e == null) {
                                                                                                                                                                    {
                                                                                                                                                                        if (objects.size() > 0) {
                                                                                                                                                                            for (ParseObject events : objects) {
                                                                                                                                                                                //Toast.makeText(getActivity().getApplicationContext(), "delete", Toast.LENGTH_SHORT).show();
                                                                                                                                                                                events.deleteInBackground();
                                                                                                                                                                                managerz.clear();
                                                                                                                                                                                FragmentTransaction ft = getFragmentManager().beginTransaction();
                                                                                                                                                                                ft.detach(addmanagers.this).attach(addmanagers.this).commit();
                                                                                                                                                                            }
                                                                                                                                                                        }else{
                                                                                                                                                                            FragmentTransaction ft = getFragmentManager().beginTransaction();
                                                                                                                                                                            ft.detach(addmanagers.this).attach(addmanagers.this).commit();

                                                                                                                                                                        }
                                                                                                                                                                    }
                                                                                                                                                                }
                                                                                                                                                            }
                                                                                                                                                        });
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


                                                                                                            }
                                                                                                        });
                                                                                                        RelativeLayout.LayoutParams rejectparams = (RelativeLayout.LayoutParams) comeornot.getLayoutParams();
                                                                                                        rejectparams.addRule(RelativeLayout.CENTER_VERTICAL);
                                                                                                        rejectparams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                                                                                                        rejectparams.setMargins(20, 0, 0, 0);


                                                                                                    }
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                }
                                                        );
                                                    }
                                                }

                                            }
                                        }
                                        }
                                    }
                                }
                            }
                        }
                    });



                }
            }
        });





        return  rootView;
                            }

                        }
