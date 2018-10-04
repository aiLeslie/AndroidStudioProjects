package com.bin.administrator.fileiodemo;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity{
    private EditText text ;
    static String rootPath = "storage/emulated/0/notepad.txt";
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text = (EditText)findViewById(R.id.editText);
       // Toast.makeText(MainActivity.this,"hello",Toast.LENGTH_LONG).show();


        File file = new File(rootPath);
        try(FileInputStream fileInputStream = new FileInputStream(file);
            FileOutputStream fileOutputStream = new FileOutputStream(file);){
            Toast.makeText(MainActivity.this,"读",Toast.LENGTH_LONG).show();
            byte[]content = new byte[fileInputStream.available()];
            fileInputStream.read(content);

            Toast.makeText(MainActivity.this,"读入成功",Toast.LENGTH_LONG).show();
            text.setText(content.toString());


        } catch(Exception e){

        }



    }
}
