package com.parse.starter;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
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

import static com.parse.starter.Profile.usernameprofile;
import static com.parse.starter.myeventpage.myevent;
import static com.parse.starter.notmyeventpage.event;


/**
 * A simple {@link Fragment} subclass.
 */
public class Profileinformation extends Fragment implements View.OnClickListener{

    Button follow;
    public Profileinformation() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_profileinformation, container, false);
        //header

        follow = (Button)rootView.findViewById(R.id.follow);
        follow.setOnClickListener(this);
        ParseQuery<ParseUser> getinfo = ParseUser.getQuery();
        getinfo.whereEqualTo("username", usernameprofile);
        getinfo.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null) {
                    if (objects.size() > 0) {
                        for (final ParseObject user : objects) {
                            ParseFile image = (ParseFile) user.getParseFile("profilepicture");
                            if (image != null) {
                                CircleImageView pp = (CircleImageView)rootView.findViewById(R.id.profilepic);
                                DisplayImage displayImage = new DisplayImage();
                                displayImage.displayImage(image,pp);
                            }
                            TextView username = (TextView)rootView.findViewById(R.id.username);
                            username.setText(user.getString("username"));

                            final TextView followernum = (TextView)rootView.findViewById(R.id.followersnum);

                                ParseQuery<ParseUser> getfollowers = ParseUser.getQuery();
                                getfollowers.whereContains("following", usernameprofile);
                                getfollowers.findInBackground(new FindCallback<ParseUser>() {
                                                             @Override
                                                             public void done(List<ParseUser> objects, ParseException e) {
                                                                 if (e == null) {
                                                                     if (objects.size() > 0) {
                                                                        followernum.setText(String.valueOf(objects.size()));
                                                                     }
                                                                 }
                                                             }
                                                         });

                            TextView following = (TextView)rootView.findViewById(R.id.followingnum);
                            TextView report =(TextView)rootView.findViewById(R.id.report);
                            report.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(getActivity().getApplicationContext(), reportevent.class);
                                    intent.putExtra("eventid",user.getString("username"));
                                    intent.putExtra("eventname",user.getString("firstname")+" "+user.getString("lastname"));
                                    intent.putExtra("type","user");
                                    startActivity(intent);
                                }
                            });
                            if(user.getList("following")!=null){
                                following.setText(String.valueOf(user.getList("following").size()));
                            }
                            if(user.getString("username").matches(ParseUser.getCurrentUser().getUsername())){
                                follow.setText("Change avatar");
                                TextView email = (TextView)rootView.findViewById(R.id.email);
                                TextView emailtitle = (TextView)rootView.findViewById(R.id.emailtitle);
                                email.setText(user.getString("email"));
                                RelativeLayout reportlayout=(RelativeLayout)rootView.findViewById(R.id.reportlayout);
                                reportlayout.setVisibility(View.GONE);

                            }else{
                                TextView email = (TextView)rootView.findViewById(R.id.email);
                                TextView emailtitle = (TextView)rootView.findViewById(R.id.emailtitle);
                                email.setVisibility(View.GONE);
                                emailtitle.setVisibility(View.GONE);
                            }
                            if(ParseUser.getCurrentUser().getList("following")!=null){
                                if(ParseUser.getCurrentUser().getList("following").contains(usernameprofile))
                                {
                                    follow.setText("unfollow");
                                    follow.setBackground(getResources().getDrawable(R.drawable.roundedbutton2,null));
                                    follow.setTextColor(Color.parseColor("#C70039"));
                                }
                            }


                            TextView name = (TextView)rootView.findViewById(R.id.fullname);
                            name.setText(user.getString("firstname")+" "+user.getString("lastname"));




                            TextView bd = (TextView)rootView.findViewById(R.id.birthday);
                            bd.setText(user.getString("dateofbirth"));

                            TextView gender = (TextView)rootView.findViewById(R.id.gender);
                            gender.setText(user.getString("gender"));


                            ParseQuery<ParseObject> query2 = ParseQuery.getQuery("userrate");
                            query2.whereEqualTo("username",usernameprofile);
                            query2.findInBackground(new FindCallback<ParseObject>() {
                                @Override
                                public void done(List<ParseObject> objects, ParseException e) {
                                    if (e == null) {
                                        for (ParseObject rating : objects) {
                                            RatingBar attendeerate = (RatingBar)rootView.findViewById(R.id.attendeerate);
                                            attendeerate.setRating(rating.getNumber("attendeerate").floatValue());
                                            RatingBar creatorrate = (RatingBar)rootView.findViewById(R.id.creatorrate);
                                            creatorrate.setRating(rating.getNumber("creatorrate").floatValue());
                                        }
                                    }
                                }
                            });

                            ParseQuery<ParseObject> query3 = ParseQuery.getQuery("Events");
                            query3.whereContains("othermanagers",usernameprofile);
                            query3.findInBackground(new FindCallback<ParseObject>() {
                                @Override
                                public void done(List<ParseObject> objects, ParseException e) {
                                    if (e == null) {
                                        int numbofcreated=0;
                                        int numbofmanaged=0;
                                        for (ParseObject events : objects) {
                                            if(events.getString("createdby").matches(usernameprofile)){
                                                numbofcreated=numbofcreated+1;
                                            }
                                            if(events.getList("othermanagers").contains(usernameprofile)){
                                               numbofmanaged=numbofmanaged+1;
                                            }
                                        }
                                        TextView numbcreate = (TextView)rootView.findViewById(R.id.createdevents);
                                        numbcreate.setText(String.valueOf(numbofcreated));

                                        TextView numbmanaged = (TextView)rootView.findViewById(R.id.managedevents);
                                        numbmanaged.setText(String.valueOf(numbofmanaged));

                                    }
                                }
                            });

                            ParseQuery<ParseObject> query4 = ParseQuery.getQuery("userevent");
                            query4.whereEqualTo("username",usernameprofile);
                            query4.findInBackground(new FindCallback<ParseObject>() {
                                @Override
                                public void done(List<ParseObject> objects, ParseException e) {
                                    if (e == null) {
                                        int numbofapproved = 0;
                                        int numbofattended = 0;
                                        for (ParseObject events : objects) {
                                            if (events.getString("state").matches("attended")) {
                                                numbofattended = numbofattended + 1;
                                            }
                                        }
                                        TextView numbattended = (TextView) rootView.findViewById(R.id.attendedevents);
                                        numbattended.setText(String.valueOf(numbofattended));


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

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.follow){
            if(follow.getText().toString().matches("FOLLOW")){
                List<String> following = new ArrayList<String>();
                //following=ParseUser.getCurrentUser().getList("following");
                if(ParseUser.getCurrentUser().getList("following")==null){
                    following.add(usernameprofile);
                    ParseUser.getCurrentUser().put("following", following);
                }else {
                    following=ParseUser.getCurrentUser().getList("following");
                    following.add(usernameprofile);
                    ParseUser.getCurrentUser().put("following", following);
                }
                ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        final ParseObject notify = new ParseObject("notifications");
                        notify.put("sendername", ParseUser.getCurrentUser().getUsername().toString());
                        notify.put("recievername",usernameprofile);
                        notify.put("read","0");
                        notify.put("type","followedyou");
                        notify.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                FragmentTransaction ft = getFragmentManager().beginTransaction();
                                ft.detach(Profileinformation.this).attach(Profileinformation.this).commit();
                            }
                        });
                    }
                });


            }else if(follow.getText().toString().matches("unfollow")){

                List<String> following = new ArrayList<String>();
                following=ParseUser.getCurrentUser().getList("following");
                following.remove(usernameprofile);
                ParseUser.getCurrentUser().put("following",following);
                ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.detach(Profileinformation.this).attach(Profileinformation.this).commit();
                    }
                });
            }else if(follow.getText().toString().matches("Change avatar")){
                Intent intent = new Intent(getActivity().getApplicationContext(), changeprofilepp.class);
                startActivity(intent);
            }
        }
    }
}
