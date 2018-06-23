package com.parse.starter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ChooseInterests extends AppCompatActivity {

    ArrayList<String> interests = new ArrayList<String>();
    ArrayList<String> checkedinterests = new ArrayList<>();
    ArrayAdapter arrayAdapter;

    public void doneinterests(View view){

        ParseUser.getCurrentUser().put("interests",checkedinterests);
        ParseUser.getCurrentUser().saveInBackground();
        Intent intent = new Intent(getApplicationContext(),Home.class);
        startActivity(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_interests);



        final ListView interestListView = (ListView) findViewById(R.id.interestlist);
        interestListView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_checked, interests);
        interestListView.setAdapter(arrayAdapter);
        final Button confirm = (Button) findViewById(R.id.confirm);

        interestListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckedTextView checkedTextView = (CheckedTextView) view;
                if(checkedTextView.isChecked()) {


                    checkedinterests.add(interests.get(position));
                    confirm.setText("Confirm");
                    //ParseUser.getCurrentUser().getList("interests").add(interests.get(position));
                    //ParseUser.getCurrentUser().saveInBackground();
                }
                else{

                    checkedinterests.remove(interests.get(position));
                    if(checkedinterests.isEmpty()){
                        confirm.setText("Skip");
                    }
                }
            }
        });

        ParseQuery<ParseObject> query = ParseQuery.getQuery("interests");
        query.whereEqualTo("availability",1);
        query.addAscendingOrder("name");

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null){
                    {

                        if (objects.size() > 0) {

                            for (ParseObject interest : objects) {

                                interests.add(interest.getString("name"));

                            }
                            arrayAdapter.notifyDataSetChanged();


                        }
                    }
                }
                else{
                    //e.printStackTrace();
                }
            }
        });



    }

}
