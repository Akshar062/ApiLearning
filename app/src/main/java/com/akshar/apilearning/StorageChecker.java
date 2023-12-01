package com.akshar.apilearning;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

public class StorageChecker {
    public static void checkStorageAvailability(Context context){
        boolean isStorageExist = false;
        boolean isStorageWritable = false;

        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)){
            isStorageExist = true;
        } else if(Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)){
            isStorageExist = true;
            isStorageWritable = false;
            Toast.makeText(context, "Storage is read only", Toast.LENGTH_SHORT).show();
        } else {
            isStorageWritable = false;
            Toast.makeText(context, "Storage is not exist", Toast.LENGTH_SHORT).show();
        }
    }
}