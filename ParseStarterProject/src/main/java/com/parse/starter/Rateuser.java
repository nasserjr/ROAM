package com.parse.starter;

import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

import static com.parse.starter.myeventpage.myevent;

/**
 * Created by Nasser on 4/5/2018.
 */

public class Rateuser {

    int approved=0;
    int attended=0;
    String uname;
    int rate;

    public void rate(String username) {

        uname=username;
        ParseQuery<ParseObject> query = ParseQuery.getQuery("userevent");
        query.whereEqualTo("username",username);
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
                                }

                                rate = ((attended * 5)) / approved;
                                ParseQuery<ParseObject> query = ParseQuery.getQuery("userrate");
                                query.whereEqualTo("username", uname);
                                query.findInBackground(new FindCallback<ParseObject>() {
                                    @Override
                                    public void done(List<ParseObject> objects, ParseException e) {
                                        if (e == null) {
                                            {
                                                if (objects.size() > 0) {
                                                    for (ParseObject user : objects) {
                                                        user.put("attendeerate",rate);
                                                        user.saveInBackground();
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
            }
        });
    }
}
