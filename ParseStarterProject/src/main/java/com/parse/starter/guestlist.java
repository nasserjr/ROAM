package com.parse.starter;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.parse.starter.myeventpage.myevent;


/**
 * A simple {@link Fragment} subclass.
 */
public class guestlist extends Fragment {

    LinearLayout requestslayout;
    ParseFile image;
    Bitmap bitmap;

    public guestlist() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =inflater.inflate(R.layout.fragment_requests, container, false);

        final LinearLayout requestslayout = (LinearLayout)rootView.findViewById(R.id.requestlist);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("userevent");
        query.whereEqualTo("eventid",myevent.getId());
        query.whereEqualTo("state","approved");

        ParseQuery<ParseObject> query2 = ParseQuery.getQuery("userevent");
        query2.whereEqualTo("eventid",myevent.getId());
        query2.whereEqualTo("state","attended");

        List<ParseQuery<ParseObject>> queries = new ArrayList<ParseQuery<ParseObject>>();
        queries.add(query);
        queries.add(query2);

        ParseQuery<ParseObject> mainQuery = ParseQuery.or(queries);

        mainQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    {

                        if (objects.size() > 0) {

                            for (final ParseObject guests : objects) {

                                final  LinearLayout request = new LinearLayout(getActivity().getApplicationContext());
                                request.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                //request.setGravity(Gravity.CENTER);
                                request.setOrientation(LinearLayout.HORIZONTAL);
                                requestslayout.addView(request);
                                LinearLayout.LayoutParams requestparams = (LinearLayout.LayoutParams) request.getLayoutParams();
                                requestparams.setMargins(20, 20, 20, 20);
                                request.setLayoutParams(requestparams);

                                ParseQuery<ParseUser> query2 = ParseUser.getQuery();
                                query2.whereEqualTo("username",guests.get("username"));
                                query2.findInBackground(new FindCallback<ParseUser>() {
                                                            @Override
                                                            public void done(List<ParseUser> objects, ParseException e) {
                                                                if (e == null) {
                                                                    {

                                                                        if (objects.size() > 0) {

                                                                            for (ParseObject user : objects) {

                                                                                image = (ParseFile) user.getParseFile("profilepicture");
                                                                                CircleImageView requestpp = new CircleImageView(getActivity().getApplicationContext());


                                                                                request.addView(requestpp);

                                                                                requestpp.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                                                                // eventpicture.setAdjustViewBounds(true);
                                                                                requestpp.getLayoutParams().height = 250;
                                                                                requestpp.getLayoutParams().width = 250;
                                                                                if (image != null) {
                                                                                    Uri imageUri = Uri.parse(image.getUrl());
                                                                                    Glide.with(getActivity().getApplicationContext()).load(imageUri)
                                                                                            .into(requestpp);
                                                                                }else {
                                                                                    requestpp.setImageResource(R.drawable.profilepic);
                                                                                }

                                                                                RelativeLayout textcontainer = new RelativeLayout(getActivity().getApplicationContext());
                                                                                request.addView(textcontainer);
                                                                                textcontainer.getLayoutParams().height = 250;

                                                                                TextView requestusername = new TextView(getActivity().getApplicationContext());
                                                                                requestusername.setText(guests.get("username").toString());
                                                                                requestusername.setOnClickListener(new View.OnClickListener() {
                                                                                    @Override
                                                                                    public void onClick(View v) {
                                                                                        Intent intent = new Intent(getActivity().getApplicationContext(), Profile.class);
                                                                                        intent.putExtra("username",guests.get("username").toString());
                                                                                        startActivity(intent);

                                                                                    }
                                                                                });
                                                                                requestusername.setTextColor(Color.parseColor("#C70039"));
                                                                                requestusername.setTextSize(20);
                                                                                requestusername.setTypeface(null, Typeface.BOLD);
                                                                                textcontainer.addView(requestusername);
                                                                                RelativeLayout.LayoutParams textparams = (RelativeLayout.LayoutParams) requestusername.getLayoutParams();
                                                                                textparams.addRule(RelativeLayout.CENTER_VERTICAL);
                                                                                textparams.setMargins(20,0,0,0);
                                                                                requestusername.setLayoutParams(textparams);

                                                                                RelativeLayout buttonscontainer = new RelativeLayout(getActivity().getApplicationContext());
                                                                                request.addView(buttonscontainer);
                                                                                buttonscontainer.getLayoutParams().height = 250;

                                                                                final ImageView comeornot = new ImageView(getActivity().getApplicationContext());

                                                                                buttonscontainer.addView(comeornot);
                                                                                comeornot.getLayoutParams().height = 100;
                                                                                comeornot.getLayoutParams().width = 100;

                                                                                if(guests.getString("state").matches("approved")){

                                                                                    comeornot.setImageResource(R.drawable.notattended);


                                                                                }else if(guests.getString("state").matches("attended")){

                                                                                    comeornot.setImageResource(R.drawable.attended);
                                                                                }





                                                                                RelativeLayout.LayoutParams rejectparams = (RelativeLayout.LayoutParams) comeornot.getLayoutParams();
                                                                                rejectparams.addRule(RelativeLayout.CENTER_VERTICAL);
                                                                                rejectparams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                                                                                rejectparams.setMargins(20,0,0,0);


                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                );








                            }

                        }
                    }
                }
            }
        });
        return  rootView;
    }

}
