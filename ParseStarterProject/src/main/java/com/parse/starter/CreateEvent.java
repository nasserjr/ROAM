package com.parse.starter;

import android.*;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.app.FragmentTransaction;
import android.app.TimePickerDialog;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CreateEvent extends AppCompatActivity implements View.OnClickListener {

    ArrayList<String> items = new ArrayList<String>();

    CheckBox malecb, femalecb;
    int imageexist = 0;
    Uri selectedimage;
    ImageView cancel;
    ImageView eventImage;
    Spinner interest_dropdown;
    boolean allowedmales = true;
    boolean allowedfemales = true;
    LatLng eventLocation;
    Button confirm;


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
                ImageView eventImage = (ImageView) findViewById(R.id.eventpicture);
                eventImage.setImageBitmap(bitmap);
                cancel = (ImageView) findViewById(R.id.cancel);
                cancel.setVisibility(View.VISIBLE);
                imageexist = 1;

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        confirm = (Button) findViewById(R.id.confirmbtn);
        //confirm.setOnClickListener(this);

        items.add("Interest");
        ParseQuery<ParseObject> query = ParseQuery.getQuery("interests");
        query.whereEqualTo("availability", 1);
        query.addAscendingOrder("name");

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    {

                        if (objects.size() > 0) {

                            for (ParseObject interest : objects) {

                                items.add(interest.getString("name"));

                            }


                        }
                    }
                } else {
                    //e.printStackTrace();
                }
            }
        });
        Bundle extras = getIntent().getExtras();
        String address = extras.getString("address");
        eventLocation = getIntent().getExtras().getParcelable("eventLocation");
        //The key argument here must match that used in the other activity

        //get the spinner from the xml.
        interest_dropdown = (Spinner) findViewById(R.id.interests_spinner);
        //create a list of items for the spinner.
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_layout, items) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    // Disable the second item from Spinner
                    return false;
                } else {

                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the disable item text color
                    tv.setTextColor(Color.GRAY);
                    tv.setBackgroundColor(Color.WHITE);
                } else {
                    tv.setTextColor(Color.WHITE);
                }
                return view;
            }


        };
        ;
        //adapter.setDropDownViewResource(R.layout.spinner_layout);
        interest_dropdown.setAdapter(adapter);


        TextView addresstext = (TextView) findViewById(R.id.address);
        addresstext.setText(address);
        TextView txtDate = (TextView) findViewById(R.id.event_date);
        txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateDialog dialog = new DateDialog(v);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                dialog.show(ft, "DatePicker");
            }
        });

        final TextView txtTime = (TextView) findViewById(R.id.event_time);
        txtTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);


                TimePickerDialog timePickerDialog = new TimePickerDialog(CreateEvent.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                String hours=Integer.toString(hourOfDay);
                                String minutes=Integer.toString(minute);
                                if(hours.length()==1){
                                    hours="0"+hours;
                                }
                                if(minutes.length()==1){
                                    minutes="0"+minutes;
                                }
                                txtTime.setText(hours + ":" + minutes);
                                txtTime.setTextColor(Color.BLACK);
                            }
                        }, hour, minute, false);
                timePickerDialog.show();
            }
        });


        malecb = (CheckBox) findViewById(R.id.malecb);
        femalecb = (CheckBox) findViewById(R.id.femalecb);

        EditText name = (EditText) findViewById(R.id.eventname);


       /* Button confirmbtn=(Button)findViewById(R.id.confirmbtn);
        confirmbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/

        eventImage = (ImageView) findViewById(R.id.eventpicture);
        eventImage.setOnClickListener(this);
        cancel = (ImageView) findViewById(R.id.cancel);
        cancel.setOnClickListener(this);

    }

    //set in query the gender
    public void CheckOne(View v) {
        if (malecb.isChecked()) ;
        //hshof byt3ml ezay
        if (femalecb.isChecked()) ;
        //set bl qery gender female kman
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.eventpicture) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                    requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

                } else {

                    getPhoto();

                }

            } else {

                getPhoto();

            }
        } else if (view.getId() == R.id.cancel) {
            eventImage.setImageDrawable(getResources().getDrawable(R.drawable.profilepic));
            imageexist = 0;
            cancel.setVisibility(View.INVISIBLE);
        }
    }

    public void itemClickedMale(View v) {
        //code to check if this checkbox is checked!
        CheckBox checkBox = (CheckBox) v;
        if (checkBox.isChecked()) {
            allowedmales = true;
        } else {
            allowedmales = false;
        }

    }

    public void itemClickedFemale(View v) {
        //code to check if this checkbox is checked!
        CheckBox checkBox = (CheckBox) v;
        if (checkBox.isChecked()) {
            allowedfemales = true;
        } else {
            allowedfemales = false;
        }

    }


    public void confirmz(View view) throws java.text.ParseException {
        TextView date = (TextView) findViewById(R.id.event_date);
        TextView time = (TextView) findViewById(R.id.event_time);
        EditText address = (EditText) findViewById(R.id.address);
        EditText description = (EditText) findViewById(R.id.description);
        EditText numberofattendees = (EditText) findViewById(R.id.numberattendees);
        EditText minage = (EditText) findViewById(R.id.minage);
        EditText maxage = (EditText) findViewById(R.id.maxage);
        EditText eventtitle = (EditText) findViewById(R.id.eventname);

        String datestring = date.getText().toString();
        String timestring = time.getText().toString();
        String addressstring = address.getText().toString();
        String descriptionstring = description.getText().toString();
        String numbattendeesstring = numberofattendees.getText().toString();
        String minimumagestring = minage.getText().toString();
        String maximumagestring = maxage.getText().toString();
        String eventtitlestring = eventtitle.getText().toString();
        String allowedgender = "";
        String interest = interest_dropdown.getSelectedItem().toString();









        if (allowedmales == false && allowedfemales == true) {
            allowedgender = "Female";
        } else if (allowedmales == true && allowedfemales == false) {
            allowedgender = "Male";
        } else if (allowedmales == true && allowedfemales == true) {
            allowedgender = "all";
        }

        if (datestring.matches("Date") || timestring.matches("Time") || addressstring.matches("") || descriptionstring.matches("")
                || numbattendeesstring.matches("") || minimumagestring.matches("") || maximumagestring.matches("")
                || eventtitlestring.matches("") || allowedgender.matches("") || interest.matches("Interest")) {
            Toast.makeText(this, "Please fill in missing information", Toast.LENGTH_SHORT).show();
        } else if (numbattendeesstring.matches("0")) {
            Toast.makeText(this, "Number of attendees should be greater than 0", Toast.LENGTH_SHORT).show();
        } else if (Integer.parseInt(minimumagestring) > Integer.parseInt(maximumagestring)) {
            Toast.makeText(this, "Maximum age should be greater than or equal minimum age", Toast.LENGTH_SHORT).show();
        }
        else {

            DateFormat sourceFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
           Date eventDateandtime = sourceFormat.parse(datestring+" "+timestring);

            Calendar cal = Calendar.getInstance(); // creates calendar
            cal.setTime(new Date()); // sets calendar time/date
            cal.add(Calendar.HOUR_OF_DAY, 5);


            if ( eventDateandtime.before(cal.getTime())) {
                Toast.makeText(this, "There must be at least 5 hourse before the event", Toast.LENGTH_SHORT).show();

            }
               else{
                int minimumage = Integer.parseInt(minimumagestring);
                int maximumage = Integer.parseInt(maximumagestring);
                int numbattendees = Integer.parseInt(numbattendeesstring);

                try {

                    confirm.setVisibility(View.INVISIBLE);
                    final ParseObject createEvent = new ParseObject("Events");
                    createEvent.put("createdby", ParseUser.getCurrentUser().getUsername().toString());
                    createEvent.put("eventname", eventtitlestring);
                    createEvent.put("eventdate", datestring);
                    createEvent.put("eventtime", timestring);
                    createEvent.put("eventname", eventtitlestring);
                    createEvent.put("description", descriptionstring);
                    createEvent.put("address", addressstring);
                    createEvent.put("numberofattendees", numbattendees);
                    createEvent.put("minage", minimumage);
                    createEvent.put("maxage", maximumage);
                    createEvent.put("intereset", interest);
                    createEvent.put("rate", 0);
                    createEvent.put("numenator", 0);
                    createEvent.put("dumenator", 0);
                    createEvent.put("promoted", 0);
                    createEvent.put("state","notyet");
                    ParseGeoPoint parseGeoPoint = new ParseGeoPoint(eventLocation.latitude, eventLocation.longitude);
                    createEvent.put("location", parseGeoPoint);
                    createEvent.put("allowedgender", allowedgender);
                    List<String> managers = new ArrayList<String>();
                    managers.add(ParseUser.getCurrentUser().getUsername());
                    createEvent.put("othermanagers", managers);
                    createEvent.put("checkedbyadmin", 0);


                    if (imageexist == 1) {
                        try {

                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedimage);

                            ByteArrayOutputStream stream = new ByteArrayOutputStream();

                            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);

                            byte[] byteArray = stream.toByteArray();
                            ParseFile file = new ParseFile("profpic.png", byteArray);
                            createEvent.put("eventpicture", file);

                        } catch (Exception e) {
                            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }



                    createEvent.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {

                                confirm.setVisibility(View.INVISIBLE);
                                Toast.makeText(CreateEvent.this, "Your event has been created", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(),Home.class);
                                startActivity(intent);

                            }
                        }
                    });

                } catch (Exception e) {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }


        }

    }

}



