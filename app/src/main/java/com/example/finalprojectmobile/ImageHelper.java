package com.example.finalprojectmobile;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class ImageHelper {
    public static void setImageViewWithByteArray(ImageView view, byte[] data) {
        if(data == null) return;
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        view.setImageBitmap(bitmap);
    }

    public static byte[] imageResizing(byte[] data){
        if(data == null) return null;
        while (data.length > 500000){
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            Bitmap resized = Bitmap.createScaledBitmap(bitmap, (int)(bitmap.getWidth()*0.8), (int)(bitmap.getHeight()*0.8), true);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            resized.compress(Bitmap.CompressFormat.PNG, 100, stream);
            data = stream.toByteArray();
        }
        return data;
    }

    public static byte[] getBytesArrayFromURI(Uri uri, Context context) {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];
            int len = 0;
            while ((len = inputStream.read(buffer)) != -1) {
                byteBuffer.write(buffer, 0, len);
            }
            return imageResizing(byteBuffer.toByteArray());
        }catch(Exception e) {
            Log.d("exception", "Oops! Something went wrong.");
        }
        return null;
    }
}
