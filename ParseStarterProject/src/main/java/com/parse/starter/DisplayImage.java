package com.parse.starter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;

import java.io.ByteArrayOutputStream;
import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Nasser on 3/6/2018.
 */

public class DisplayImage {



   public void displayCircleImage(ParseFile thumbnail, final CircleImageView img) {

        if (thumbnail != null) {
            thumbnail.getDataInBackground(new GetDataCallback() {

                @Override
                public void done(byte[] data, ParseException e) {

                    if (e == null) {

                        final BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inPreferredConfig= Bitmap.Config.RGB_565;
                        options.inJustDecodeBounds = false;
                        Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                        ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
                        bmp.compress(Bitmap.CompressFormat.JPEG,40,bytearrayoutputstream);


                        if (bmp != null) {

                            Log.e("parse file ok", " null");
                            img.setImageBitmap(bmp);
                        }
                                            } else {
                        Log.e("paser after downloade", " null");
                    }

                }
            });
        } else {

            Log.e("parse file", " null");

            // img.setImageResource(R.drawable.ic_launcher);

            //img.setPadding(10, 10, 10, 10);
        }
}

    public void displayImage(ParseFile thumbnail, final ImageView img) {

        if (thumbnail != null) {
            thumbnail.getDataInBackground(new GetDataCallback() {

                @Override
                public void done(byte[] data, ParseException e) {

                    if (e == null) {
                        final BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inPreferredConfig= Bitmap.Config.RGB_565;
                        options.inJustDecodeBounds = false;
                        Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length,options);
                        ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
                        bmp.compress(Bitmap.CompressFormat.JPEG,40,bytearrayoutputstream);


                        if (bmp != null) {

                            Log.e("parse file ok", " null");
                            img.setImageBitmap(bmp);
                        }
                    } else {
                        Log.e("paser after downloade", " null");
                    }

                }
            });
        } else {

            Log.e("parse file", " null");

            // img.setImageResource(R.drawable.ic_launcher);

            //img.setPadding(10, 10, 10, 10);
        }
    }

}
