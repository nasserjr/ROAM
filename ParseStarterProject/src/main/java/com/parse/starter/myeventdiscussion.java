package com.parse.starter;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.parse.starter.myeventpage.myevent;
import static com.parse.starter.notmyeventpage.event;


/**
 * A simple {@link Fragment} subclass.
 */
public class myeventdiscussion extends Fragment implements View.OnClickListener {

    EditText post;

    public myeventdiscussion() {
        // Required empty public constructor
    }
    String answerstring;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_myeventdiscussion, container, false);

        final LinearLayout linearLayout = (LinearLayout) rootView.findViewById(R.id.questionandposts);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("questions");
        query.whereEqualTo("eventid",myevent.getId());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    {

                        if (objects.size() > 0) {



                            TextView questionstitle = new TextView(getActivity().getApplicationContext());
                            questionstitle.setText("Questions");
                            questionstitle.setTextSize(18);
                            questionstitle.setTypeface(null, Typeface.BOLD);
                            questionstitle.setPadding(0,30,0,0);
                            questionstitle.setTextColor(Color.parseColor("#696969"));
                            linearLayout.addView(questionstitle);


                            for (final ParseObject questions : objects) {


                                LinearLayout parent = new LinearLayout(getActivity().getApplicationContext());
                                parent.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                parent.setOrientation(LinearLayout.HORIZONTAL);

                                TextView asker = new TextView(getActivity().getApplicationContext());
                                asker.setText(questions.getString("askerusername"));
                                parent.addView(asker);
                                asker.setTextColor(Color.parseColor("#C70039"));
                                asker.setTypeface(null, Typeface.BOLD);
                                asker.setTextSize(16);

                                LinearLayout.LayoutParams askerparams = (LinearLayout.LayoutParams)asker.getLayoutParams();
                                askerparams.setMargins(0, 10, 0, 0);
                                asker.setLayoutParams(askerparams);


                                TextView questiondate = new TextView(getActivity().getApplicationContext());
                                Date questdate = questions.getCreatedAt();
                                SimpleDateFormat desiredFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                                String datestring = desiredFormat.format(questdate);
                                questiondate.setText(datestring);
                                parent.addView(questiondate);
                                questiondate.setTextColor(Color.parseColor("#AFAFAF"));
                                questiondate.setTextSize(14);

                                LinearLayout.LayoutParams dateparams = (LinearLayout.LayoutParams)questiondate.getLayoutParams();
                                dateparams.setMargins(40, 10, 0, 0);
                                questiondate.setLayoutParams(dateparams);

                                linearLayout.addView(parent);

                                LinearLayout.LayoutParams parentparams = (LinearLayout.LayoutParams)parent.getLayoutParams();
                                parentparams.setMargins(0, 70, 0, 0);
                                parent.setLayoutParams(parentparams);

                                TextView question = new TextView(getActivity().getApplicationContext());
                                question.setText(questions.getString("question"));
                                TextView answer = new TextView(getActivity().getApplicationContext());
                                question.setTextColor(Color.parseColor("#000000"));
                                linearLayout.addView(question);

                                if(questions.getString("answer").matches("")){
                                    final EditText postanswer = new EditText(getActivity().getApplicationContext());
                                    postanswer.setBackground(getResources().getDrawable(R.drawable.rounded_cornerpinkborder,null));
                                    linearLayout.addView(postanswer);
                                    LinearLayout.LayoutParams postanswerparams = (LinearLayout.LayoutParams)postanswer.getLayoutParams();
                                    postanswerparams.setMargins(0, 0, 0, 30);
                                    postanswer.setPadding(10,10,10,10);
                                    postanswer.setTextColor(Color.parseColor("#000000"));
                                    postanswer.setTag(questions.getObjectId());
                                    Button post = new Button(getActivity().getApplicationContext());
                                    post.setBackground(getResources().getDrawable(R.drawable.roundedbutton,null));
                                    linearLayout.addView(post);
                                    LinearLayout.LayoutParams postparams = (LinearLayout.LayoutParams)post.getLayoutParams();
                                    //post.setLayoutParams(new LinearLayout.LayoutParams(70, 40));
                                    postparams.setMargins(0, 0, 0, 30);
                                    postparams.gravity= Gravity.RIGHT;
                                    postparams.width=280;
                                    postparams.height=130;
                                    post.setLayoutParams(postparams);
                                    post.setText("Answer");
                                    post.setTextColor(Color.parseColor("#FFFFFF"));
                                    //post.setPadding(10,10,10,10);
                                    post.setOnClickListener(new View.OnClickListener() {
                                        public void onClick(View v) {
                                             String questionid = postanswer.getTag().toString();
                                            answerstring = postanswer.getText().toString();

                                            if (!answerstring.matches("")) {

                                                //Toast.makeText(getActivity().getApplicationContext(), answerstring, Toast.LENGTH_SHORT).show();
                                                ParseQuery<ParseObject> query = ParseQuery.getQuery("questions");

                                                query.whereEqualTo("objectId",questionid);

                                                query.findInBackground(new FindCallback<ParseObject>() {
                                                    @Override
                                                    public void done(List<ParseObject> objects, ParseException e) {

                                                        if (e == null && objects != null) {

                                                            for (ParseObject object : objects) {

                                                                object.put("answer", answerstring);
                                                                object.put("answerusername",ParseUser.getCurrentUser().getUsername());
                                                                object.saveInBackground(new SaveCallback() {
                                                                    @Override
                                                                    public void done(ParseException e) {
                                                                        if (e == null) {
                                                                            final ParseObject notify = new ParseObject("notifications");
                                                                            notify.put("sendername", ParseUser.getCurrentUser().getUsername().toString());
                                                                            notify.put("recievername",questions.getString("askerusername"));
                                                                            notify.put("read","0");
                                                                            notify.put("eventid",myevent.getId());
                                                                            notify.put("type","answeredquestion");
                                                                            notify.saveInBackground(new SaveCallback() {
                                                                                @Override
                                                                                public void done(ParseException e) {
                                                                                    postanswer.setText("");
                                                                                    Toast.makeText(getActivity().getApplicationContext(), "Your answer has been added", Toast.LENGTH_SHORT).show();
                                                                                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                                                                                    ft.detach(myeventdiscussion.this).attach(myeventdiscussion.this).commit();

                                                                                }
                                                                            });


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
                                else{
                                    answer.setText(questions.getString("answer"));
                                    answer.setBackgroundColor(Color.parseColor("#AFAFAF"));
                                    answer.setTextColor(Color.parseColor("#000000"));
                                    linearLayout.addView(answer);
                                    LinearLayout.LayoutParams answerparams = (LinearLayout.LayoutParams)answer.getLayoutParams();
                                    answerparams.setMargins(0, 0, 0, 30);
                                    answer.setPadding(10,10,10,10);
                                }

                                LinearLayout.LayoutParams questionparams = (LinearLayout.LayoutParams)question.getLayoutParams();
                                questionparams.setMargins(0, 10, 0, 30);
                                question.setLayoutParams(questionparams);




                            }


                        }
                    }
                } else {
                    //e.printStackTrace();
                }
            }
        });

        return rootView;
    }


    @Override
    public void onClick(View v) {


    }
}
