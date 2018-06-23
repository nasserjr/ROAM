package com.parse.starter;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.Manifest;
import android.widget.Button;
import android.widget.Toast;


import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.R.attr.data;

public class AddProfilePicture extends AppCompatActivity implements View.OnClickListener {

    CircleImageView profilepicture;
    Button cont;
    Button remove;
    int imageexist=0;
    Uri selectedimage;

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
                profilepicture = (CircleImageView) findViewById(R.id.profile_image);
                profilepicture.setImageBitmap(bitmap);
                cont = (Button) findViewById(R.id.cont);
                cont.setText("Continue");
                remove=(Button) findViewById(R.id.remove);
                remove.setVisibility(View.VISIBLE);
                imageexist=1;

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    public  void upload (View view){

        if(imageexist==1){


            try {

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedimage);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();

                bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);

                byte[] byteArray = stream.toByteArray();
                ParseFile file = new ParseFile("profpic.png", byteArray);
                ParseUser.getCurrentUser().put("profilepicture",file);
                ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {

                        if (e == null) {

                            Intent intent = new Intent(getApplicationContext(), ChooseInterests.class);
                            startActivity(intent);

                        } else {

                            Toast.makeText(AddProfilePicture.this, "Image could not be shared", Toast.LENGTH_SHORT).show();

                        }

                    }
                });


            } catch (IOException e) {
                Log.d("error",e.getMessage());
            }

        }
        else{
            Intent intent = new Intent(getApplicationContext(), ChooseInterests.class);
            startActivity(intent);
        }
    }

    public  void remove (View view){
        profilepicture.setImageDrawable(getResources().getDrawable(R.drawable.clickme));
        imageexist=0;
        cont.setText("Skip");
        remove.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_profile_picture);

        profilepicture = (CircleImageView) findViewById(R.id.profile_image);
        profilepicture.setOnClickListener(this);
    }

    public void onClick(View view) {
        if (view.getId() == R.id.profile_image) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

                } else {

                    getPhoto();

                }

            } else {

                getPhoto();

            }
        }
    }
}
