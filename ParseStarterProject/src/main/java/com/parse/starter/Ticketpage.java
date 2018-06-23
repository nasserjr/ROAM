package com.parse.starter;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

import static com.parse.starter.Ticketlist.selectedticket;

public class Ticketpage extends AppCompatActivity {
    Handler handler;
    String ticketid;
    String state="";
    String eventid="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticketpage);

        Intent intent = getIntent();
        ;
        ticketid = intent.getStringExtra("ticketId");
        String eventname = intent.getStringExtra("eventname");

        TextView title = (TextView) findViewById(R.id.title);
        final ImageView qrcode = (ImageView) findViewById(R.id.qrcode);
        final DisplayImage displayImage = new DisplayImage();

        title.setText(eventname + " ticket");

        ParseQuery<ParseObject> query = ParseQuery.getQuery("userevent");
        query.whereEqualTo("objectId", ticketid);

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {


                        if (objects.size() > 0) {

                            for (ParseObject ticket : objects) {
                                displayImage.displayImage(ticket.getParseFile("qrcode"), qrcode);
                            }
                        }



                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            ParseQuery<ParseObject> query = ParseQuery.getQuery("userevent");
                            query.whereEqualTo("objectId", ticketid);

                            query.findInBackground(new FindCallback<ParseObject>() {
                                @Override
                                public void done(List<ParseObject> objects, ParseException e) {
                                    if (e == null) {


                                        if (objects.size() > 0) {

                                            for (ParseObject ticket : objects) {
                                               state = ticket.getString("state");
                                               eventid= ticket.getString("eventid");
                                            }
                                        }
                                    }
                                }
                            });
                            if(!state.matches("attended")) {
                                handler.postDelayed(this, 1000);
                            }
                             else {
                                Intent intent = new Intent(getApplicationContext(), Rateevent.class);
                                intent.putExtra("eventId", eventid);
                                //intent.putExtra("eventname", event.getName());
                                startActivity(intent);
                                }
                            }
                        }, 1000);

                }
            }
        });
    }}