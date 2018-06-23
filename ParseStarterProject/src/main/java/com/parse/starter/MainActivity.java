/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.starter;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

  private static boolean userPressedBackAgain = false;

  public void login(View view) {

    //refer to the text boxes on the screen
    EditText usernametext = (EditText) findViewById(R.id.nameinheader);
    EditText passwordtext = (EditText) findViewById(R.id.password);

    //get their textx
    String username = usernametext.getText().toString();
    String password = passwordtext.getText().toString();

    //check if they are empty
    if (username.matches("") || password.matches("")) {
      Toast.makeText(this, "Please fill in missing information.", Toast.LENGTH_SHORT).show();
    } else {
      ParseUser.logInInBackground(username, password, new LogInCallback() {
        @Override
        public void done(ParseUser user, ParseException e) {

          if (user != null) {
            ParseQuery<ParseObject> query3 = ParseQuery.getQuery("suspended");
            query3.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
            query3.findInBackground(new FindCallback<ParseObject>() {
              @Override
              public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                  {
                    if(objects.size()>0){
                      ParseUser.logOut();
                      Intent intent = new Intent(getApplicationContext(), suspendpage.class);
                      startActivity(intent);
                    }else{
                      if(ParseUser.getCurrentUser().getNumber("isAdmin").intValue()==1)
                      {
                        ParseUser.logOut();
                        Toast.makeText(MainActivity.this, "Invalid username/password", Toast.LENGTH_SHORT).show();
                      }
                      else{
                        Intent intent = new Intent(getApplicationContext(), Home.class);
                        startActivity(intent);
                      }
                    }
                  }
                }
              }
            });
          } else {
            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
          }

        }
      });
    }
  }

  //runs when application runs
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    if (ParseUser.getCurrentUser() != null) {

      ParseQuery<ParseObject> query3 = ParseQuery.getQuery("suspended");
      query3.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
      query3.findInBackground(new FindCallback<ParseObject>() {
        @Override
        public void done(List<ParseObject> objects, ParseException e) {
          if (e == null) {
            {
              if(objects.size()>0){
                ParseUser.logOut();
                Intent intent = new Intent(getApplicationContext(), suspendpage.class);
                startActivity(intent);
              }else{
                Intent intent = new Intent(getApplicationContext(), Home.class);
                startActivity(intent);
              }
            }
          }
        }
      });


    }


    TextView GoToRegister = (TextView) findViewById(R.id.GoToRegister);
    GoToRegister.setOnClickListener(this);


    RelativeLayout background = (RelativeLayout) findViewById(R.id.background);
    ImageView logo = (ImageView) findViewById(R.id.logo);
    background.setOnClickListener(this);
    logo.setOnClickListener(this);


    ParseAnalytics.trackAppOpenedInBackground(getIntent());
  }

  //if clicked on GoToRegister go to register class
  @Override
  public void onClick(View view) {
    if (view.getId() == R.id.GoToRegister) {
      Intent intent = new Intent(getApplicationContext(), Register.class);
      startActivity(intent);
    }
    //if clicked on background or logo close keyboard
    else if (view.getId() == R.id.background || view.getId() == R.id.logo) {

      InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
      inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }
  }


}