package com.example.finalprojectmobile.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;

import com.example.finalprojectmobile.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class InsertActivity extends AppCompatActivity{
    Button uploadButton;

    ActivityResultLauncher<Intent> activityResultLaunch = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    Uri resultImage = data.getData();
                    String[] filePath = { MediaStore.Images.Media.DATA };
                    Cursor c = getContentResolver().query(resultImage,filePath, null, null, null);
                    c.moveToFirst();
                    int columnIndex = c.getColumnIndex(filePath[0]);
                    String picturePath = c.getString(columnIndex);
                    c.close();
                    try {
                        byte[] imageFile = fetchImage(picturePath);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);



        uploadButton.setOnClickListener(v->{
            Intent iGallery = new Intent(Intent.ACTION_PICK);
            iGallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            activityResultLaunch.launch(iGallery);
        });

    }

//    public void viewImage(View view)
//    {
//        Cursor c = db.rawQuery("select * from imageTb", null);
//        if(c.moveToNext())
//        {
//            byte[] image = c.getBlob(0);
//            Bitmap bmp= BitmapFactory.decodeByteArray(image, 0 , image.length);
//            imageView.setImageBitmap(bmp);
//            Toast.makeText(this,"Done", Toast.LENGTH_SHORT).show();
//        }
//    }

    public byte[] fetchImage(String path) throws IOException {
        File folder = new File(path);
        FileInputStream fis = new FileInputStream(folder);
        byte[] image= new byte[fis.available()];
        fis.read(image);
        fis.close();
        return image;
    }

//    https://stackoverflow.com/questions/6016000/how-to-open-phones-gallery-through-code

    public void uploadImage(){

    }
}