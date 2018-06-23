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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.parse.starter.myeventpage.myevent;


/**
 * A simple {@link Fragment} subclass.
 */
public class requests extends Fragment {

    LinearLayout requestslayout;
    ParseFile image;
    Bitmap bitmap;
    String busy;

    public requests() {
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
        query.whereEqualTo("state","request");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    {

                        if (objects.size() > 0) {

                            for (final ParseObject requests : objects) {

                              final  LinearLayout request = new LinearLayout(getActivity().getApplicationContext());
                                request.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                //request.setGravity(Gravity.CENTER);
                                request.setOrientation(LinearLayout.HORIZONTAL);
                                requestslayout.addView(request);
                                LinearLayout.LayoutParams requestparams = (LinearLayout.LayoutParams) request.getLayoutParams();
                                requestparams.setMargins(20, 20, 20, 20);
                                request.setLayoutParams(requestparams);

                                ParseQuery<ParseUser> query2 = ParseUser.getQuery();
                                query2.whereEqualTo("username",requests.get("username"));
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

                                                                               final TextView requestusername = new TextView(getActivity().getApplicationContext());
                                                                               requestusername.setText(requests.get("username").toString());
                                                                               requestusername.setOnClickListener(new View.OnClickListener() {
                                                                                   @Override
                                                                                   public void onClick(View v) {
                                                                                       Intent intent = new Intent(getActivity().getApplicationContext(), Profile.class);
                                                                                       intent.putExtra("username",requests.get("username").toString());
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

                                                                             final ImageView reject = new ImageView(getActivity().getApplicationContext());
                                                                             reject.setTag(requests.getObjectId());
                                                                               reject.setOnClickListener(new View.OnClickListener() {
                                                                                   public void onClick(View v) {
                                                                                       String requestid = reject.getTag().toString();
                                                                                       ParseQuery<ParseObject> requestquery = ParseQuery.getQuery("userevent");
                                                                                       requestquery.whereEqualTo("objectId",requestid);
                                                                                       requestquery.findInBackground(new FindCallback<ParseObject>() {
                                                                                           @Override
                                                                                           public void done(List<ParseObject> objects, ParseException e) {
                                                                                               if(e==null){
                                                                                                   if (objects.size() > 0) {

                                                                                                       for (ParseObject object : objects) {

                                                                                                           object.put("state","declined");
                                                                                                           object.saveInBackground(new SaveCallback() {
                                                                                                               @Override
                                                                                                               public void done(ParseException e) {
                                                                                                                   if (e == null) {
                                                                                                                       Toast.makeText(getActivity().getApplicationContext(), "Request deleted", Toast.LENGTH_SHORT).show();
                                                                                                                       FragmentTransaction ft = getFragmentManager().beginTransaction();
                                                                                                                       ft.detach(requests.this).attach(requests.this).commit();
                                                                                                                   }
                                                                                                               }
                                                                                                           });


                                                                                                       }

                                                                                                   }
                                                                                               }
                                                                                           }
                                                                                       });


                                                                                   }
                                                                               });



                                                                               buttonscontainer.addView(reject);
                                                                               reject.getLayoutParams().height = 150;
                                                                               reject.getLayoutParams().width = 150;

                                                                               reject.setImageResource(R.drawable.rejectcross);

                                                                               RelativeLayout.LayoutParams rejectparams = (RelativeLayout.LayoutParams) reject.getLayoutParams();
                                                                               rejectparams.addRule(RelativeLayout.CENTER_VERTICAL);
                                                                               rejectparams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                                                                               rejectparams.setMargins(20,0,0,0);

                                                                               final ImageView accept = new ImageView(getActivity().getApplicationContext());
                                                                               accept.setTag(requests.getObjectId());
                                                                               accept.setOnClickListener(new View.OnClickListener() {
                                                                                   public void onClick(View v) {

                                                                                       DateFormat sourceFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                                                                                       Date eventDateandtime = null;
                                                                                       try {
                                                                                           eventDateandtime = sourceFormat.parse(myevent.getEventdate()+" "+myevent.getEventtime());
                                                                                       } catch (java.text.ParseException e1) {
                                                                                           e1.printStackTrace();
                                                                                       }

                                                                                       Calendar cal = Calendar.getInstance(); // creates calendar
                                                                                       cal.setTime(new Date()); // sets calendar time/date
                                                                                       cal.add(Calendar.HOUR_OF_DAY, 2);

                                                                                       if ( eventDateandtime.before(cal.getTime())) {
                                                                                           Toast.makeText(getActivity().getApplicationContext(), "You can not accept now", Toast.LENGTH_SHORT).show();

                                                                                       }else {

                                                                                           ParseQuery<ParseObject> requestquery1 = ParseQuery.getQuery("userevent");
                                                                                           requestquery1.whereEqualTo("eventid", myevent.getId());
                                                                                           requestquery1.whereEqualTo("state", "approved");
                                                                                           requestquery1.findInBackground(new FindCallback<ParseObject>() {
                                                                                                                              @Override
                                                                                                                              public void done(List<ParseObject> objects, ParseException e) {
                                                                                                                                  if (e == null) {
                                                                                                                                      if (objects.size() >= myevent.getNumbofattendees()) {
                                                                                                                                          Toast.makeText(getActivity().getApplicationContext(), "Maximum limit of attendees reached", Toast.LENGTH_SHORT).show();
                                                                                                                                      }
                                                                                                                                      else{
                                                                                                                                          ParseQuery<ParseObject> requestquery1 = ParseQuery.getQuery("userevent");
                                                                                                                                          requestquery1.whereEqualTo("username", requests.get("username").toString());
                                                                                                                                          requestquery1.whereEqualTo("state", "approved");
                                                                                                                                          requestquery1.findInBackground(new FindCallback<ParseObject>() {
                                                                                                                                              @Override
                                                                                                                                              public void done(List<ParseObject> objects, ParseException e) {
                                                                                                                                                  if (e == null) {
                                                                                                                                                      if (objects.size() > 0) {
                                                                                                                                                          for (ParseObject object : objects) {
                                                                                                                                                              String eventid = object.getString("eventid");

                                                                                                                                                              ParseQuery<ParseObject> requestquery1 = ParseQuery.getQuery("Events");
                                                                                                                                                              requestquery1.whereEqualTo("objectId", eventid);
                                                                                                                                                              requestquery1.findInBackground(new FindCallback<ParseObject>() {
                                                                                                                                                                  @Override
                                                                                                                                                                  public void done(List<ParseObject> objects, ParseException e) {
                                                                                                                                                                      if (e == null) {
                                                                                                                                                                          if (objects.size() > 0) {

                                                                                                                                                                              for (ParseObject object : objects) {
                                                                                                                                                                                  String eventdatestring = object.getString("eventdate");
                                                                                                                                                                                  String eventtimestring = object.getString("eventtime");
                                                                                                                                                                                  if (eventdatestring.matches(myevent.getEventdate()) && eventtimestring.matches(myevent.getEventtime())) {
                                                                                                                                                                                      Toast.makeText(getActivity().getApplicationContext(), "User is already approved in event on same time", Toast.LENGTH_SHORT).show();
                                                                                                                                                                                  } else {
                                                                                                                                                                                      String requestid = accept.getTag().toString();
                                                                                                                                                                                      ParseQuery<ParseObject> requestquery = ParseQuery.getQuery("userevent");
                                                                                                                                                                                      requestquery.whereEqualTo("objectId", requestid);
                                                                                                                                                                                      requestquery.findInBackground(
                                                                                                                                                                                              new FindCallback<ParseObject>() {
                                                                                                                                                                                                  @Override
                                                                                                                                                                                                  public void done(List<ParseObject> objects, ParseException e) {
                                                                                                                                                                                                      if (e == null) {
                                                                                                                                                                                                          if (objects.size() > 0) {

                                                                                                                                                                                                              for (ParseObject object : objects) {
                                                                                                                                                                                                                  String toQr = requests.get("username").toString();
                                                                                                                                                                                                                  //Toast.makeText(getActivity().getApplicationContext(), toQr, Toast.LENGTH_SHORT).show();
                                                                                                                                                                                                                  MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                                                                                                                                                                                                                  try {
                                                                                                                                                                                                                      BitMatrix bitMatrix = multiFormatWriter.encode(toQr, BarcodeFormat.QR_CODE, 200, 200);
                                                                                                                                                                                                                      BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                                                                                                                                                                                                                      bitmap = barcodeEncoder.createBitmap(bitMatrix);
                                                                                                                                                                                                                      //image.setImageBitmap(bitmap);
                                                                                                                                                                                                                  } catch (WriterException errorcode) {
                                                                                                                                                                                                                      errorcode.printStackTrace();
                                                                                                                                                                                                                  }

                                                                                                                                                                                                                  ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                                                                                                                                                                                                  bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);

                                                                                                                                                                                                                  byte[] byteArray = stream.toByteArray();
                                                                                                                                                                                                                  ParseFile file = new ParseFile("qrcode.png", byteArray);
                                                                                                                                                                                                                  object.put("state", "approved");
                                                                                                                                                                                                                  object.put("qrcode", file);
                                                                                                                                                                                                                  object.saveInBackground(new SaveCallback() {
                                                                                                                                                                                                                      @Override
                                                                                                                                                                                                                      public void done(ParseException e) {

                                                                                                                                                                                                                          final ParseObject notify = new ParseObject("notifications");
                                                                                                                                                                                                                          notify.put("sendername", ParseUser.getCurrentUser().getUsername().toString());
                                                                                                                                                                                                                          notify.put("recievername", requests.get("username").toString());
                                                                                                                                                                                                                          notify.put("read", "0");
                                                                                                                                                                                                                          notify.put("eventid", myevent.getId());
                                                                                                                                                                                                                          notify.put("type", "approved");
                                                                                                                                                                                                                          notify.saveInBackground(new SaveCallback() {
                                                                                                                                                                                                                              @Override
                                                                                                                                                                                                                              public void done(ParseException e) {
                                                                                                                                                                                                                                  if (e == null) {
                                                                                                                                                                                                                                      Toast.makeText(getActivity().getApplicationContext(), "Ticket sent", Toast.LENGTH_SHORT).show();
                                                                                                                                                                                                                                      FragmentTransaction ft = getFragmentManager().beginTransaction();
                                                                                                                                                                                                                                      ft.detach(requests.this).attach(requests.this).commit();
                                                                                                                                                                                                                                  } else {

                                                                                                                                                                                                                                      Toast.makeText(getActivity().getApplicationContext(), "Error sending ticket", Toast.LENGTH_SHORT).show();

                                                                                                                                                                                                                                  }

                                                                                                                                                                                                                              }
                                                                                                                                                                                                                          });

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
                                                                                                                                                                  }
                                                                                                                                                              });
                                                                                                                                                          }
                                                                                                                                                      }else{
                                                                                                                                                          String requestid = accept.getTag().toString();
                                                                                                                                                          ParseQuery<ParseObject> requestquery = ParseQuery.getQuery("userevent");
                                                                                                                                                          requestquery.whereEqualTo("objectId", requestid);
                                                                                                                                                          requestquery.findInBackground(
                                                                                                                                                                  new FindCallback<ParseObject>() {
                                                                                                                                                                      @Override
                                                                                                                                                                      public void done(List<ParseObject> objects, ParseException e) {
                                                                                                                                                                          if (e == null) {
                                                                                                                                                                              if (objects.size() > 0) {

                                                                                                                                                                                  for (ParseObject object : objects) {
                                                                                                                                                                                      String toQr =object.getString("userid").toString();
                                                                                                                                                                                      //Toast.makeText(getActivity().getApplicationContext(), toQr, Toast.LENGTH_SHORT).show();
                                                                                                                                                                                      MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                                                                                                                                                                                      try {
                                                                                                                                                                                          BitMatrix bitMatrix = multiFormatWriter.encode(toQr, BarcodeFormat.QR_CODE, 200, 200);
                                                                                                                                                                                          BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                                                                                                                                                                                          bitmap = barcodeEncoder.createBitmap(bitMatrix);
                                                                                                                                                                                          //image.setImageBitmap(bitmap);
                                                                                                                                                                                      } catch (WriterException errorcode) {
                                                                                                                                                                                          errorcode.printStackTrace();
                                                                                                                                                                                      }

                                                                                                                                                                                      ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                                                                                                                                                                      bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);

                                                                                                                                                                                      byte[] byteArray = stream.toByteArray();
                                                                                                                                                                                      ParseFile file = new ParseFile("qrcode.png", byteArray);
                                                                                                                                                                                      object.put("state", "approved");
                                                                                                                                                                                      object.put("qrcode", file);
                                                                                                                                                                                      object.saveInBackground(new SaveCallback() {
                                                                                                                                                                                          @Override
                                                                                                                                                                                          public void done(ParseException e) {

                                                                                                                                                                                              final ParseObject notify = new ParseObject("notifications");
                                                                                                                                                                                              notify.put("sendername", ParseUser.getCurrentUser().getUsername().toString());
                                                                                                                                                                                              notify.put("recievername", requests.get("username").toString());
                                                                                                                                                                                              notify.put("read", "0");
                                                                                                                                                                                              notify.put("eventid", myevent.getId());
                                                                                                                                                                                              notify.put("type", "approved");
                                                                                                                                                                                              notify.saveInBackground(new SaveCallback() {
                                                                                                                                                                                                  @Override
                                                                                                                                                                                                  public void done(ParseException e) {
                                                                                                                                                                                                      if (e == null) {
                                                                                                                                                                                                          Toast.makeText(getActivity().getApplicationContext(), "Ticket sent", Toast.LENGTH_SHORT).show();
                                                                                                                                                                                                          FragmentTransaction ft = getFragmentManager().beginTransaction();
                                                                                                                                                                                                          ft.detach(requests.this).attach(requests.this).commit();
                                                                                                                                                                                                      } else {

                                                                                                                                                                                                          Toast.makeText(getActivity().getApplicationContext(), "Error sending ticket", Toast.LENGTH_SHORT).show();

                                                                                                                                                                                                      }

                                                                                                                                                                                                  }
                                                                                                                                                                                              });

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


                                                                               buttonscontainer.addView(accept);
                                                                               accept.getLayoutParams().height = 150;
                                                                               accept.getLayoutParams().width = 150;

                                                                               accept.setImageResource(R.drawable.accept);

                                                                               RelativeLayout.LayoutParams acceptparams = (RelativeLayout.LayoutParams) accept.getLayoutParams();
                                                                               acceptparams.addRule(RelativeLayout.CENTER_VERTICAL);
                                                                               acceptparams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                                                                               acceptparams.addRule(RelativeLayout.LEFT_OF, reject.getId());
                                                                               acceptparams.setMargins(0,0,180,0);

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


    public void onBackPressed()
    {

    }


}
