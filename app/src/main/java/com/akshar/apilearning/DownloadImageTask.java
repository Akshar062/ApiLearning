package com.akshar.apilearning;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

    private ImageDownloadListener listener;

    public DownloadImageTask(ImageDownloadListener listener) {
        this.listener = listener;
    }
    @Override
    protected Bitmap doInBackground(String... strings) {
        String imageAddress = strings[0];
        Bitmap bitmap = null;
        try {
            URL url = new URL(imageAddress);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            bitmap = BitmapFactory.decodeStream(input);

            // Save the image to the "Cats" folder in the Downloads directory
            saveImageToDownloads(bitmap, "Cats", "image.jpg");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        if (listener != null) {
            listener.onImageDownloaded(result);
        }
    }

    public interface ImageDownloadListener {
        void onImageDownloaded(Bitmap bitmap);
    }

    private void saveImageToDownloads(Bitmap bitmap, String folderName, String fileName) {
        try {
            File downloadsDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            File catsFolder = new File(downloadsDirectory, folderName);

            if (!catsFolder.exists()) {
                catsFolder.mkdirs();
            }

            File imageFile = new File(catsFolder, fileName);
            FileOutputStream fos = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
