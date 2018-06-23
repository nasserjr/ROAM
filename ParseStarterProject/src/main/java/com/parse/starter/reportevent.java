package com.parse.starter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class reportevent extends AppCompatActivity {

    String eventid;
    RelativeLayout bye;
    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reportevent);
        Bundle extras = getIntent().getExtras();
        eventid = extras.getString("eventid");
        String eventname = extras.getString("eventname");
        type=extras.getString("type");
        bye=(RelativeLayout)findViewById(R.id.bye);

        TextView hi = (TextView)findViewById(R.id.reporttitle);
        hi.setText(hi.getText().toString()+eventname.toUpperCase());
    }

    public void submit(View view){
        RadioGroup gendergroup = (RadioGroup) findViewById(R.id.reporttype);
        int selectedtypeID = gendergroup.getCheckedRadioButtonId();
        String gender;
        if(selectedtypeID==-1){
            gender ="";
        }
        else{
            RadioButton selectedRadioButton = (RadioButton) findViewById(selectedtypeID);
            gender = selectedRadioButton.getText().toString();
        }

        TextView details = (TextView)findViewById(R.id.details);
        String detailstring = details.getText().toString();

        if(gender.matches("")){
            Toast.makeText(this, "Please choose report type", Toast.LENGTH_SHORT).show();
        }else{
            final ParseObject report = new ParseObject("reports");
            report.put("sendername", ParseUser.getCurrentUser().getUsername().toString());
            report.put("eventoruser", type);
            report.put("reporttype",gender);
            report.put("reported",eventid);
            report.put("state",0);
            if(!details.getText().toString().matches("")){
                report.put("details",details.getText().toString());
            }
            report.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    bye.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    public void home(View view){
        Intent intent = new Intent(getApplicationContext(), Home.class);
        startActivity(intent);
    }
}
