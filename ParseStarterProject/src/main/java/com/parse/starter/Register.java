package com.parse.starter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseACL;
import com.parse.ParseAnalytics;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Register extends AppCompatActivity implements View.OnClickListener {
    EditText dateofbirthtext;

    public void register(View view) throws ParseException {

        //refer to text boxes on the screen
        EditText emailtext = (EditText) findViewById(R.id.email);
        EditText usernametext = (EditText) findViewById(R.id.nameinheader);
        EditText passwordtext = (EditText) findViewById(R.id.password);
        EditText repasswordtext = (EditText) findViewById(R.id.repassword);
        EditText firstnametext = (EditText) findViewById(R.id.firstname);
        EditText lastnametext = (EditText) findViewById(R.id.lastname);
        dateofbirthtext = (EditText) findViewById(R.id.birthdate);

        //get their texts
        String email = emailtext.getText().toString();
        final String username = usernametext.getText().toString();
        String password = passwordtext.getText().toString();
        String repassword = repasswordtext.getText().toString();
        final String firstname = firstnametext.getText().toString();
        final String lastname = lastnametext.getText().toString();
        final String dateofbirth = dateofbirthtext.getText().toString();

        boolean correctdata=true;

        //if radiobox not clicked gender="" otherwise gender=text of radiobox
        RadioGroup gendergroup = (RadioGroup) findViewById(R.id.radioGender);
        int selectedgenderID = gendergroup.getCheckedRadioButtonId();
        String gender;
        if(selectedgenderID==-1){
            gender ="";
        }
        else{
            RadioButton selectedRadioButton = (RadioButton) findViewById(selectedgenderID);
            gender = selectedRadioButton.getText().toString();
        }

        //checking email format
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);


        //check if boxes are empty
        if(email.matches("")||username.matches("")||password.matches("") ||repassword.matches("")||firstname.matches("")
                || lastname.matches("")||dateofbirth.matches("") ||gender.matches("")){
            Toast.makeText(this, "Please fill in missing information.", Toast.LENGTH_SHORT).show();
            correctdata=false;
        }
        //check if email format is false
        else if(matcher.matches()==false){
            Toast.makeText(this, "Invalide Email format.", Toast.LENGTH_SHORT).show();
            correctdata=false;
        }else if(!strongpassword(password)){
            Toast.makeText(this, "Invalid password", Toast.LENGTH_SHORT).show();
            correctdata=false;
        }
        //check if passwords matches
        else if(!password.matches(repassword)){
            Toast.makeText(this, "Passwords do not match.", Toast.LENGTH_SHORT).show();
            correctdata=false;
        }
        else if(!Pattern.matches("[a-zA-Z]+", firstname)){
            Toast.makeText(this, "first name must only contain letters", Toast.LENGTH_SHORT).show();
            correctdata=false;
        }
        else if(!Pattern.matches("[a-zA-Z]+", lastname)){
            Toast.makeText(this, "last name must only contain letters", Toast.LENGTH_SHORT).show();
            correctdata=false;
        }
        else{
            //check format of date
            SimpleDateFormat sdf;
            Date date;
            try{
                sdf = new SimpleDateFormat("dd-MM-yyyy");
                date = sdf.parse(dateofbirth);
                if(!dateofbirth.equals(sdf.format(date))){
                    Toast.makeText(this, "invalide date format.", Toast.LENGTH_SHORT).show();
                    correctdata=false;
                }
            }
            catch (Exception e){
                Toast.makeText(this, "invalide date format.", Toast.LENGTH_SHORT).show();
                correctdata=false;
            }
        }

        //if data is entered correctly create new user and sign him up
        if(correctdata==true){


            ParseUser user = new ParseUser();
            user.setEmail(email);
            user.setUsername(username.toLowerCase());
            user.setPassword(password);
            user.put("firstname",firstname.toUpperCase());
            user.put("lastname",lastname.toUpperCase());
            user.put("dateofbirth",dateofbirth);
            user.put("gender",gender);
            user.put("suspended",0);
            user.put("isAdmin",0);
            ParseACL acl = new ParseACL();
            acl.setPublicReadAccess(true);
            acl.setPublicWriteAccess(true);
            user.setACL(acl);

            user.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(com.parse.ParseException e) {
                    if(e==null){

                        final ParseObject rate = new ParseObject("userrate");
                        rate.put("username",username);
                        rate.put("attendeerate",0);
                        rate.put("creatorrate",0);
                        rate.saveInBackground();
                        Intent intent = new Intent(getApplicationContext(),AddProfilePicture.class);
                        startActivity(intent);

                    }
                    else{
                        Toast.makeText(Register.this,e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });


        }



    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ParseUser.getCurrentUser().logOut();
        //Back to login
        TextView BackToLogin = (TextView) findViewById(R.id.backtologin);
        BackToLogin.setOnClickListener(this);

        LinearLayout background = (LinearLayout) findViewById(R.id.background);
        background.setOnClickListener(this);
        ParseAnalytics.trackAppOpenedInBackground(getIntent());
    }


    //if clicked on backtologin go to mainactivity class
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.backtologin) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
        //if clicked on background close keyboard
        else if(view.getId()==R.id.background){
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
        }
    }

    public boolean strongpassword(String password){

        boolean hasLetter = false;
        boolean hasDigit = false;

        if (password.length() >= 8) {
            for (int i = 0; i < password.length(); i++) {
                char x = password.charAt(i);
                if (Character.isLetter(x)) {

                    hasLetter = true;
                }

                else if (Character.isDigit(x)) {

                    hasDigit = true;
                }

                // no need to check further, break the loop
                if(hasLetter && hasDigit){
                    break;
                }

            }
            if (hasLetter && hasDigit) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

}
