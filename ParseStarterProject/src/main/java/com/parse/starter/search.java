package com.parse.starter;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.parse.starter.notmyeventpage.event;

public class search extends AppCompatActivity {

    EditText searchbar;
    ParseFile image;
   LinearLayout requestslayout;

    public void search(View view){
        if(!searchbar.getText().toString().matches("")) {

            requestslayout.removeAllViews();

            ParseQuery<ParseUser> query3 = ParseUser.getQuery();
            query3.whereContains("firstname",searchbar.getText().toString().toUpperCase());
            query3.whereEqualTo("isAdmin",0);

            ParseQuery<ParseUser> query4 = ParseUser.getQuery();
            query4.whereContains("lastname",searchbar.getText().toString().toUpperCase());
            query4.whereEqualTo("isAdmin",0);

            ParseQuery<ParseUser> query2 = ParseUser.getQuery();
            query2.whereContains("username",searchbar.getText().toString().toLowerCase());
            query2.whereEqualTo("isAdmin",0);

            List<ParseQuery<ParseUser>> queries = new ArrayList<ParseQuery<ParseUser>>();
            queries.add(query2);
            queries.add(query3);
            queries.add(query4);

            ParseQuery<ParseUser> mainQuery = ParseQuery.or(queries);

            mainQuery.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> objects, ParseException e) {
                    if (e == null) {
                        {

                            if (objects.size() > 0) {

                                for (final ParseObject user : objects) {
                                    final LinearLayout request = new LinearLayout(getApplicationContext());
                                    request.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                    //request.setGravity(Gravity.CENTER);
                                    request.setOrientation(LinearLayout.HORIZONTAL);
                                    requestslayout.addView(request);
                                    LinearLayout.LayoutParams requestparams = (LinearLayout.LayoutParams) request.getLayoutParams();
                                    requestparams.setMargins(20, 20, 20, 20);
                                    request.setLayoutParams(requestparams);

                                    image = (ParseFile) user.getParseFile("profilepicture");
                                    CircleImageView requestpp = new CircleImageView(getApplicationContext());
                                    DisplayImage displayImage = new DisplayImage();

                                    request.addView(requestpp);

                                    requestpp.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                    // eventpicture.setAdjustViewBounds(true);
                                    requestpp.getLayoutParams().height = 250;
                                    requestpp.getLayoutParams().width = 250;
                                    if (image != null) {
                                        displayImage.displayCircleImage(image,requestpp);
                                    }else {
                                        requestpp.setImageResource(R.drawable.profilepic);
                                    }

                                    RelativeLayout textcontainer = new RelativeLayout(getApplicationContext());
                                    request.addView(textcontainer);
                                    textcontainer.getLayoutParams().height = 250;

                                    final TextView requestusername = new TextView(getApplicationContext());
                                    requestusername.setText(user.get("username").toString());
                                    requestusername.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            ParseQuery<ParseObject> query3 = ParseQuery.getQuery("suspended");
                                            query3.whereEqualTo("username", user.get("username").toString());
                                            query3.findInBackground(new FindCallback<ParseObject>() {
                                                @Override
                                                public void done(List<ParseObject> objects, ParseException e) {
                                                    if (e == null) {
                                                        {
                                                            if(objects.size()>0){
                                                                Intent intent = new Intent(getApplicationContext(), suspendpage.class);
                                                                startActivity(intent);
                                                            }else{
                                                                Intent intent = new Intent(getApplicationContext(), Profile.class);
                                                                intent.putExtra("username",user.get("username").toString());
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
                                    textparams.setMargins(20,0,0,0);
                                    requestusername.setLayoutParams(textparams);
                                }
                            }
                        }
                    }
                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Toolbar headerup = (Toolbar)findViewById(R.id.my_toolbar);
        setSupportActionBar(headerup);
        getSupportActionBar().setTitle("search");

      requestslayout  = (LinearLayout)findViewById(R.id.ticketlist);
        searchbar = (EditText)findViewById(R.id.searchtext);


    }


}
