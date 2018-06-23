package com.parse.starter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.parse.starter.myeventpage.myevent;

public class welcome extends AppCompatActivity {

    String userid;
    String usernamez;
    TextView welcome;
    TextView whoops;
    TextView notexist;
    CircleImageView pp;
    double approved =0;
    double attended =0;
    double cancelledticket=0;
    double rate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        welcome = (TextView)findViewById(R.id.welcome);
        whoops = (TextView)findViewById(R.id.whoops);
        notexist = (TextView)findViewById(R.id.notexist);
        pp = (CircleImageView)findViewById(R.id.profilepic);

        userid = getIntent().getExtras().getString("userid");
        ParseQuery<ParseObject> query = ParseQuery.getQuery("userevent");
        query.whereEqualTo("eventid",myevent.getId());
        query.whereEqualTo("userid",userid);
        query.whereEqualTo("state","approved");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    {
                        if (objects.size() > 0) {
                            for (ParseObject requests : objects) {
                                usernamez=requests.getString("username");
                                    ParseQuery<ParseUser> query2 = ParseUser.getQuery();
                                    query2.whereEqualTo("username",usernamez);
                                    query2.findInBackground(new FindCallback<ParseUser>() {
                                        @Override
                                        public void done(List<ParseUser> objects, ParseException e) {
                                            if (e == null) {
                                                {

                                                    if (objects.size() > 0) {

                                                        for (ParseObject user : objects) {

                                                            ParseFile image = (ParseFile) user.getParseFile("profilepicture");
                                                            if(image!=null){
                                                                DisplayImage displayImage = new DisplayImage();
                                                                displayImage.displayCircleImage(image,pp);
                                                            }else{
                                                                pp.setImageResource(R.drawable.profilepic);
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }});
                                requests.put("state","attended");
                                requests.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if (e == null) {

                                            ParseQuery<ParseObject> query = ParseQuery.getQuery("userevent");
                                            query.whereEqualTo("username",usernamez);
                                            query.findInBackground(new FindCallback<ParseObject>() {
                                                @Override
                                                public void done(List<ParseObject> objects, ParseException e) {
                                                    if (e == null) {
                                                        {
                                                            if (objects.size() > 0) {
                                                                for (ParseObject userevents : objects) {

                                                                    if (userevents.getString("state").matches("attended")) {
                                                                        approved = approved + 1;
                                                                        attended = attended + 1;
                                                                    } else if (userevents.getString("state").matches("approved")) {
                                                                        approved = approved + 1;
                                                                    } else if(userevents.getString("state").matches("canceledticket")){
                                                                        cancelledticket = cancelledticket + 1;
                                                                        approved = approved + 1;
                                                                    }
                                                                }
                                                                    rate = (((attended *1)+(cancelledticket*0.5)) / approved)*5;
                                                                ParseQuery<ParseObject> query = ParseQuery.getQuery("userrate");
                                                                query.whereEqualTo("username", usernamez);
                                                                query.findInBackground(new FindCallback<ParseObject>() {
                                                                    @Override
                                                                    public void done(List<ParseObject> objects, ParseException e) {
                                                                        if (e == null) {
                                                                            {
                                                                                if (objects.size() > 0) {
                                                                                    for (ParseObject user : objects) {
                                                                                        user.put("attendeerate",rate);
                                                                                        user.saveInBackground();
                                                                                        welcome.setText(welcome.getText() + " " + usernamez);
                                                                                        welcome.setVisibility(View.VISIBLE);
                                                                                        pp.setVisibility(View.VISIBLE);
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
                                            });
                                        }

                                    }
                                });

                            }
                        }else{
                            whoops.setVisibility(View.VISIBLE);
                            notexist.setVisibility(View.VISIBLE);
                        }
                    }
                } else {
                    //e.printStackTrace();
                }
            }
        });
    }
}
