package com.example.administrator.flagapitest.mirror.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.administrator.flagapitest.R;

import java.io.File;

public class TransmissionActivity extends AppCompatActivity {
    private static final byte start = 0;
    private static final byte[] name = "name".getBytes();//index( 2 ,6) nextIndex (2 + fileName.lengeth ,6 + fileName.lengeth )
    private static final byte end = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transmission);


//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.setType("*/*");
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
//        startActivityForResult(intent,1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            File file = new File(String.valueOf(uri));
            Toast.makeText(TransmissionActivity.this, file.getName(), Toast.LENGTH_SHORT).show();

//            String[] proj = {MediaStore.Images.Media.DATA};
//            Cursor actualimagecursor = managedQuery(uri, proj, null, null, null);
//            int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//            actualimagecursor.moveToFirst();
//            String img_path = actualimagecursor.getString(actual_image_column_index);
//            file = new File(img_path);
//            Toast.makeText(TransmissionActivity.this, file.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}
