package com.parse.starter;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hsalf.smilerating.BaseRating;
import com.hsalf.smilerating.SmileRating;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.parse.starter.notmyeventpage.event;

public class Rateevent extends AppCompatActivity implements View.OnClickListener{

    ImageView eventimage;
    String eventid;
    TextView howwas;
    String eventtitle;
    ParseFile eventpict;
    int rating = 0;
    TextView review;
    int imageexists=0;
    Uri selectedimage;
    ImageView demo;
    ImageView cancel;
    ImageView picbutton;
    Button submitz;
    float creatorrate;
    //String creatorid;
    List<String> managers = new ArrayList<>();
    float numenator;
    float dumenator;
    float userrating;
    float neweventrate;
    float newnumenator;
    float newdumenator;
    float creatornumenator=0;
    float creatordumenator=0;
    RelativeLayout loading;

    public void getPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                getPhoto();

            }


        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {

            selectedimage = data.getData();

            try {

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedimage);
                demo.setImageBitmap(bitmap);
                demo.setVisibility(View.VISIBLE);
                cancel.setVisibility(View.VISIBLE);
                picbutton.setVisibility(View.INVISIBLE);

                imageexists = 1;

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rateevent);
        eventimage = (ImageView)findViewById(R.id.eventimage);
        howwas = (TextView)findViewById(R.id.howwas);
        Bundle extras = getIntent().getExtras();
        eventid = extras.getString("eventId");
        SmileRating smileRating = (SmileRating) findViewById(R.id.smile_rating);
        review = (TextView)findViewById(R.id.review);
        Button submit = (Button)findViewById(R.id.submit);
        demo=(ImageView)findViewById(R.id.demo);
        cancel=(ImageView)findViewById(R.id.cancel);
        submitz=(Button)findViewById(R.id.submit);
        submitz.setEnabled(false);
        loading = (RelativeLayout)findViewById(R.id.loading);



        ParseQuery<ParseObject> eventinfo = ParseQuery.getQuery("Events");
        eventinfo.whereEqualTo("objectId",eventid);
        eventinfo.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null){

                    for (ParseObject event : objects) {

                        eventtitle = event.getString("eventname");
                        eventpict = event.getParseFile("eventpicture");


                }
                    DisplayImage eventpic = new DisplayImage();
                    eventpic.displayImage(eventpict,eventimage);
                    howwas.setText(howwas.getText().toString()+" "+eventtitle);



                }
            }
        });

        smileRating.setOnSmileySelectionListener(new SmileRating.OnSmileySelectionListener() {
            @Override
            public void onSmileySelected(@BaseRating.Smiley int smiley, boolean reselected) {
                // reselected is false when user selects different smiley that previously selected one
                // true when the same smiley is selected.
                // Except if it first time, then the value will be false.
                switch (smiley) {
                    case SmileRating.BAD:
                        rating=2;
                        submitz.setEnabled(true);
                        submitz.setBackground(getResources().getDrawable(R.drawable.roundedbutton,null));
                        break;
                    case SmileRating.GOOD:
                        rating=4;
                        submitz.setEnabled(true);
                        submitz.setBackground(getResources().getDrawable(R.drawable.roundedbutton,null));
                        break;
                    case SmileRating.GREAT:
                        rating=5;
                        submitz.setEnabled(true);
                        submitz.setBackground(getResources().getDrawable(R.drawable.roundedbutton,null));
                        break;
                    case SmileRating.OKAY:
                        rating=3;
                        submitz.setEnabled(true);
                        submitz.setBackground(getResources().getDrawable(R.drawable.roundedbutton,null));
                        break;
                    case SmileRating.TERRIBLE:
                        rating=1;
                        submitz.setEnabled(true);
                        submitz.setBackground(getResources().getDrawable(R.drawable.roundedbutton,null));
                        break;
                }
            }
        });

        picbutton = (ImageView)findViewById(R.id.picbutton);
        picbutton.setOnClickListener(this);
        cancel.setOnClickListener(this);



    }



    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Please rate the event", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.picbutton){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                    requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

                } else {

                    getPhoto();

                }

            } else {

                getPhoto();

            }
        }else if(v.getId() == R.id.cancel){
            imageexists=0;
            demo.setVisibility(View.GONE);
            cancel.setVisibility(View.GONE);
            picbutton.setVisibility(View.VISIBLE);
        }
    }

    public void submit(View view){

        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        loading.setVisibility(View.VISIBLE);
        ParseQuery<ParseObject> changerequeststatus = ParseQuery.getQuery("userevent");
        changerequeststatus.whereEqualTo("eventid",eventid);
        changerequeststatus.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null){
                    if (objects.size() > 0) {
                        for (ParseObject ticket : objects){
                            ticket.put("rated","yes");
                            ticket.saveInBackground();
                        }
                    }
                }
            }
        });

        ParseObject eventreview = new ParseObject("eventreview");
        eventreview.put("username", ParseUser.getCurrentUser().getUsername().toString());
        eventreview.put("eventid",eventid);
        eventreview.put("rate",rating);
        eventreview.put("review",review.getText().toString());
        if(imageexists==1){
            try {

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedimage);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();

                bitmap.compress(Bitmap.CompressFormat.JPEG, 60, stream);

                byte[] byteArray = stream.toByteArray();
                ParseFile file = new ParseFile("profpic.png", byteArray);
                eventreview.put("pic", file);

            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        eventreview.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                   //rating the event
                    ParseQuery<ParseObject> event = ParseQuery.getQuery("Events");
                    event.whereEqualTo("objectId",eventid);
                    event.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> objects, ParseException e) {
                            if(e==null){
                                if (objects.size() > 0) {
                                    for (ParseObject event : objects){
                                        numenator=event.getNumber("numenator").floatValue();
                                        dumenator=event.getNumber("dumenator").floatValue();
                                    }
                                    ParseQuery<ParseObject> rate = ParseQuery.getQuery("userrate");
                                    rate.whereEqualTo("username",ParseUser.getCurrentUser().getUsername());
                                    rate.findInBackground(new FindCallback<ParseObject>() {
                                        @Override
                                        public void done(List<ParseObject> objects, ParseException e) {
                                            if(e==null){
                                                if (objects.size() > 0) {
                                                    for (ParseObject rate : objects){
                                                        userrating=rate.getNumber("attendeerate").floatValue();
                                                    }
                                                    newnumenator = (numenator*dumenator)+(rating*userrating/5);
                                                    newdumenator = dumenator+1;
                                                    neweventrate=newnumenator/newdumenator;

                                                    ParseQuery<ParseObject> savingevent = ParseQuery.getQuery("Events");
                                                    savingevent.whereEqualTo("objectId",eventid);
                                                    savingevent.findInBackground(new FindCallback<ParseObject>() {
                                                        @Override
                                                        public void done(List<ParseObject> objects, ParseException e) {
                                                            if(e==null){
                                                                if (objects.size() > 0) {
                                                                    for (ParseObject event : objects){
                                                                        event.put("numenator",newnumenator);
                                                                        event.put("dumenator",newdumenator);
                                                                        event.put("rate",neweventrate);

                                                                        event.saveInBackground(new SaveCallback() {
                                                                            @Override
                                                                            public void done(ParseException e) {
                                                                                if(e==null) {

                                                                                    ParseQuery<ParseObject> getcreator = ParseQuery.getQuery("Events");
                                                                                    getcreator.whereEqualTo("objectId",eventid);
                                                                                    getcreator.findInBackground(new FindCallback<ParseObject>() {
                                                                                        @Override
                                                                                        public void done(List<ParseObject> objects, ParseException e) {
                                                                                            if(e==null) {
                                                                                                if (objects.size() > 0) {
                                                                                                    for (ParseObject event : objects) {
                                                                                                        managers = event.getList("othermanagers");
                                                                                                    }
                                                                                                }

                                                                                                for(final Object manager:managers){
                                                                                                ParseQuery<ParseObject> getevents = ParseQuery.getQuery("Events");
                                                                                                getevents.whereContains("othermanagers", manager.toString());
                                                                                                getevents.findInBackground(new FindCallback<ParseObject>() {
                                                                                                    @Override
                                                                                                    public void done(List<ParseObject> objects, ParseException e) {
                                                                                                        if (e == null) {
                                                                                                            if (objects.size() > 0) {
                                                                                                                for (ParseObject event : objects) {
                                                                                                                    creatornumenator = creatornumenator + event.getNumber("numenator").floatValue();
                                                                                                                    creatordumenator = creatordumenator + event.getNumber("dumenator").floatValue();

                                                                                                                    creatorrate = creatornumenator / creatordumenator;
                                                                                                                    final ParseQuery<ParseObject> creatorratez = ParseQuery.getQuery("userrate");
                                                                                                                    creatorratez.whereEqualTo("username", manager.toString());
                                                                                                                    creatorratez.findInBackground(new FindCallback<ParseObject>() {
                                                                                                                        @Override
                                                                                                                        public void done(List<ParseObject> objects, ParseException e) {
                                                                                                                            if (e == null) {
                                                                                                                                if (objects.size() > 0) {
                                                                                                                                    for (ParseObject creator : objects) {

                                                                                                                                        creator.put("creatorrate", creatorrate);
                                                                                                                                        creator.saveInBackground(new SaveCallback() {
                                                                                                                                            @Override
                                                                                                                                            public void done(ParseException e) {
                                                                                                                                                Intent intent = new Intent(getApplicationContext(), Home.class);
                                                                                                                                                startActivity(intent);
                                                                                                                                            }
                                                                                                                                        });

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
                                                                                        }
                                                                                    });
                                                                                }
                                                                                else{
                                                                                    Toast.makeText(Rateevent.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
                                        }
                                    });
                                }
                            }
                        }
                    });

                }
            }
        });

    }
}
