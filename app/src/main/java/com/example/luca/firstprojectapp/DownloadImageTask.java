package com.example.luca.firstprojectapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.facebook.Profile;

import java.io.InputStream;

/**
 * Created by Mary on 24/05/2015.
 */
public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

   // private ProgressDialog mDialog;
    private ImageView bmImage;
    private Profile profile;

    public DownloadImageTask(ImageView bmImage, Profile profile) {
        this.bmImage = bmImage;
        this.profile = profile;
    }

    protected void onPreExecute() {
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", "image download error");
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
        //set image of your imageview
        bmImage.setImageBitmap(result);
        //close
       // mDialog.dismiss();
    }


}
