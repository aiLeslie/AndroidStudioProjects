package com.example.mobileio;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity {
    private FileInputStream in = null;
    private FileOutputStream out = null;
    private File file;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

            try {
                Toast.makeText(this, "授权成功", Toast.LENGTH_SHORT).show();
                file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),"水果");
                //if (!file.exists()) file.createNewFile();
                Toast.makeText(this, file.getAbsolutePath() + " exists? " + file.exists(), Toast.LENGTH_SHORT).show();
                TextView textView = (TextView) this.findViewById(R.id.textView);
                textView.setText(file.getAbsolutePath());
               /* out = new FileOutputStream(file,true);
                out.write("hello".getBytes());*/

            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
                e.printStackTrace();

            } /*finally {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }*/

        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }
        /*try (FileInputStream fileInputStream = in = new FileInputStream(file)) {
            byte[] bytes = new byte[in.available()];
            in.read(bytes,0,bytes.length);
            Toast.makeText(this, new String(bytes), Toast.LENGTH_SHORT).show();
            TextView textView = (TextView) this.findViewById(R.id.textView);
            textView.setText(file.getAbsolutePath());


        }catch (Exception e){
            Log.i("bin debug", e.getMessage());
        }*/


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "授权成功", Toast.LENGTH_SHORT).show();

        }
    }
}
