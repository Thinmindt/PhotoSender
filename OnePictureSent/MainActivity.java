package com.example.greg.photosender;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.os.Environment.DIRECTORY_PICTURES;


public class MainActivity extends AppCompatActivity {

    private ImageView imgPreviewPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void btnSelectPhoto(View v) {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        startActivityForResult(cameraIntent, 10);
    }

    private String getPictureName() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String timestamp = sdf.format(new Date());
        return "SendImage" + timestamp + ".jpg";
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 10) {
                Bitmap cameraImage = (Bitmap) data.getExtras().get("data");
                AsyncTaskRunner runner = new AsyncTaskRunner();
                runner.execute(cameraImage);
            }
        }
    }

//    private void sendImage(Bitmap cameraImage, Socket client) {
//        try {
//            ByteArrayOutputStream stream = new ByteArrayOutputStream();
//            cameraImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
//            byte[] byteArray = stream.toByteArray();
//
//            OutputStream os = client.getOutputStream();
//            os.write(byteArray, 0, byteArray.length);
//            os.flush();
//
//            client.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    private class AsyncTaskRunner extends AsyncTask<Bitmap, Void, String> {

        @Override
        protected String doInBackground(Bitmap... cameraImage) {
            try {
                Socket client = new Socket("192.168.1.117", 6013);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                cameraImage[0].compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();

                OutputStream os = client.getOutputStream();
                os.write(byteArray, 0, byteArray.length);
                os.flush();

                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "Sent";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }
    }
}
