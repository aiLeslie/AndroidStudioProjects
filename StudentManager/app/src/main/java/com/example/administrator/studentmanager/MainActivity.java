package com.example.administrator.studentmanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private EditText editTextNumber,editTextPassword;
    private Button buttonLogin,buttonSign;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextNumber = (EditText)findViewById(R.id.editTextNumber);
        editTextPassword = (EditText)findViewById(R.id.editTextPassword);

        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        buttonSign = (Button)findViewById(R.id.buttonSign);
        buttonSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,SignActivity.class);
                startActivity(intent);
            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{

//                FileInputStream in = null;
//                String studentInfo = null;

                try {
//                    in = openFileInput("studentInfo");
//                    if (in == null) {
//                        Toast.makeText(MainActivity.this, "~请先注册~", Toast.LENGTH_LONG).show();
//                        return;
//                    }
                    /*byte[] info = new byte[in.available()];
                    in.read(info);
                    studentInfo = new String(info);*/
                    SharedPreferences preferences = getSharedPreferences("studentInfo", MODE_PRIVATE);
                    Set<String> set = new HashSet<String>();
                    preferences.getStringSet(editTextNumber.getText().toString().trim(), set);
                    String number = null;
                    String password = null;
                    for (String i : set){
                        if(i.indexOf("number") == 0){
                            number = i.substring(i.indexOf("number") + 6);
                        }
                        if(i.indexOf("password") == 0){
                            password = i.substring(i.indexOf("password") + 8);
                        }
                    }
                    if (number == null || password == null)Toast.makeText(MainActivity.this, "不存在此学生", Toast.LENGTH_SHORT).show();

                    if (editTextNumber.getText().toString().trim().equals(number)) {
                        if (editTextPassword.getText().toString().trim().equals(password)) {
                            Toast.makeText(MainActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MainActivity.this,StudentInfo.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(MainActivity.this, "密码错误哦！", Toast.LENGTH_SHORT).show();
                            return;
                        }

                    } else {
                        Toast.makeText(MainActivity.this, "不存在此学生", Toast.LENGTH_SHORT).show();
                        return;
                    }

                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                } /*finally {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }*/

            }catch (Exception e){
                    Toast.makeText(MainActivity.this,e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }


        });
    }
}
