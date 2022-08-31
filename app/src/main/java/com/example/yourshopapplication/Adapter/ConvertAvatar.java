package com.example.yourshopapplication.Adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;

public abstract class ConvertAvatar {
    public static byte[] getByte(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 10, baos);
        byte[] data = baos.toByteArray();
        return data;
    }
    public static Bitmap getBitMap(byte[] data){
        Bitmap bitmap = BitmapFactory.decodeByteArray(data,0, data.length);
        return bitmap;
    }
}
