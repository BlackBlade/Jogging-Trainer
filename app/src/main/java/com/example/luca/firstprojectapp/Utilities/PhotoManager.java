package com.example.luca.firstprojectapp.Utilities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by Marina Londei.
 * Singleton for creating an image file for storing pictures in the phone.
 */
public class PhotoManager {

    private static PhotoManager instance = new PhotoManager();

    private PhotoManager() {
    }

    public static PhotoManager getInstance() {
        return instance;
    }

    private static final String JPEG_FILE_PREFIX = "IMG_";
    private static final String JPEG_FILE_SUFFIX = ".jpg";

    //control if the camera exixts on this device
    public boolean isIntentAvaliable(Context context, String action) {
        final PackageManager packageManager = context.getPackageManager();
        final Intent intent = new Intent(action);

        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    private File getAlbumStorageDir(String albumName) {
        return new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), albumName);
    }

    public File createImageFile() throws IOException {

        String imageFileName = JPEG_FILE_PREFIX + "_";
        File image = File.createTempFile(imageFileName, JPEG_FILE_SUFFIX, getAlbumDir());

        return image;
    }

    private File getAlbumDir() {

        File storageDir = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {

            storageDir = getAlbumStorageDir("CameraDirectory");


            if (storageDir != null) {
                if (!storageDir.mkdirs()) {

                    if (!storageDir.exists()) {

                        Log.d("App-Error", "failed to create directory");

                        return null;

                    }
                }
            }
        } else {
            Log.v("APP", "External storage is not mounted READ/WRITE.");
        }
        return storageDir;


    }


    public Bitmap decodeSampledBitmapFromMemory(int reqWidth, int reqHeight, String currentPhotoPath) throws IOException {

// Get the dimensions of the bitmap

        BitmapFactory.Options bmOptions=new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds=true;
        BitmapFactory.decodeFile(currentPhotoPath,bmOptions);

        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize=calculateInSampleSize(bmOptions, reqWidth, reqHeight);;
        Bitmap bitmap=BitmapFactory.decodeFile(currentPhotoPath,bmOptions);

        return bitmap;
    }

    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {

        // Dimensioni dell'immagine
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            int halfHeight = height / 2;
            int halfWidth = width / 2;

            // Calcoliamo il piu grande valore di inSampleSize in una potenza di 2
            //manteniamo sempre la l'altezza e la larghezza maggiori rispetto all'altezza e alla larghezza richiesta
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

}
