package com.parse.starter;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class changeinterests extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changeinterests);

        Toolbar headerup = (Toolbar)findViewById(R.id.my_toolbar);
        setSupportActionBar(headerup);
        getSupportActionBar().setTitle("Change Interests");

        final List myinterests = ParseUser.getCurrentUser().getList("interests");
        final LinearLayout interestlist = (LinearLayout)findViewById(R.id.interests);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("interests");
        query.whereEqualTo("availability",1);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    {

                        for (final ParseObject interest : objects) {
                            final  LinearLayout interestcontainer = new LinearLayout(getApplicationContext());
                            interestcontainer.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                            //request.setGravity(Gravity.CENTER);
                            interestcontainer.setOrientation(LinearLayout.HORIZONTAL);
                            interestlist.addView(interestcontainer);
                            LinearLayout.LayoutParams requestparams = (LinearLayout.LayoutParams) interestcontainer.getLayoutParams();
                            requestparams.setMargins(0, 0, 0, 30);
                            interestcontainer.setLayoutParams(requestparams);

                            TextView interestname = new TextView(getApplicationContext());
                            interestcontainer.addView(interestname);
                            interestname.setTextColor(Color.parseColor("#696969"));
                            interestname.setTextSize(18);
                            interestname.setText(interest.getString("name"));
                            interestname.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                            RelativeLayout checkcontainer = new RelativeLayout(getApplicationContext());
                            interestcontainer.addView(checkcontainer);

                            final CheckBox interestchecked = new CheckBox(getApplicationContext());
                            interestchecked.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                            interestchecked.setTextColor(Color.parseColor("#C70039"));
                            interestchecked.setButtonTintList(getResources().getColorStateList(R.color.maincolor));
                            checkcontainer.addView(interestchecked);

                            RelativeLayout.LayoutParams rejectparams = (RelativeLayout.LayoutParams) interestchecked.getLayoutParams();
                            rejectparams.addRule(RelativeLayout.CENTER_VERTICAL);
                            rejectparams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                            rejectparams.setMargins(0,0,0,0);

                            if(myinterests!=null) {
                                if (myinterests.contains(interest.getString("name"))) {
                                    interestchecked.setChecked(true);
                                }
                            }

                            interestchecked.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if(interestchecked.isChecked()){
                                        if(myinterests!=null) {
                                            myinterests.add(interest.getString("name"));
                                            ParseUser.getCurrentUser().put("interests", myinterests);
                                            ParseUser.getCurrentUser().saveInBackground();
                                        }else{
                                            List<String> inter = new ArrayList<>();
                                            inter.add(interest.getString("name"));
                                            ParseUser.getCurrentUser().put("interests", myinterests);
                                            ParseUser.getCurrentUser().saveInBackground();

                                        }
                                    }else{
                                        myinterests.remove(interest.getString("name"));
                                        ParseUser.getCurrentUser().put("interests",myinterests);
                                        ParseUser.getCurrentUser().saveInBackground();
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
