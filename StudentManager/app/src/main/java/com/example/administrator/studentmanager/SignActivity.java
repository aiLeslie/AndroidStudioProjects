package com.example.administrator.studentmanager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.HashSet;
import java.util.Set;

public class SignActivity extends AppCompatActivity {
    RadioButton male;
    RadioButton female;
    EditText name;
    EditText number;
    EditText password;
    Button rst;
    Button finish;
    String sex = "female";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);
        number = (EditText) findViewById(R.id.SignNumber);
        name = (EditText) findViewById(R.id.signName);
        password = (EditText) findViewById(R.id.signPassword);
        male = (RadioButton) findViewById(R.id.male);
        female = (RadioButton) findViewById(R.id.female);
        rst = (Button) findViewById(R.id.buttonRst);
        finish = (Button) findViewById(R.id.buttonFinish);
        ButtonListener buttonListener = new ButtonListener();
        rst.setOnClickListener(buttonListener);
        finish.setOnClickListener(buttonListener);

    }

    class Listener implements RadioGroup.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.female:
                    sex = "female";
                    break;
                case R.id.male:
                    sex = "male";
                    break;
            }
        }
    }

    class ButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.buttonFinish:
                    /*Students student = new Students(name.getText().toString().trim(),Integer.parseInt(number.getText().toString()), sex.trim(), password.getText().toString().trim());
                    FileOutputStream out = null;
                    try{
                        out = openFileOutput("studentInfo", Context.MODE_PRIVATE);
//                        out.write((number.getText().toString().trim()+ " " +name.getText().toString().trim()+ " " +password.getText().toString().trim()).getBytes());
                        Toast.makeText(SignActivity.this,"注册成功",Toast.LENGTH_LONG).show();
                    }catch (Exception e){
                        Toast.makeText(SignActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                    }finally {
                        if(out != null){
                            try {
                                out.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }*/
                    SharedPreferences.Editor editor = getSharedPreferences("studentInfo", MODE_PRIVATE).edit();
                    Set<String> set = new HashSet<String>();
                    set.add("number" + number.getText().toString().trim());
                    set.add("name" + name.getText().toString().trim());
                    set.add("sex" + sex);
                    set.add("password" + password.getText().toString().trim());
                    editor.putStringSet(number.getText().toString().trim(),set);
                    editor.apply();
                    String content = new String();
                    for (String i : set){
                        content += i;
                        content += "\n";
                    }

                    Toast.makeText(SignActivity.this,content + "\n注册成功",Toast.LENGTH_LONG).show();


                    break;
                case R.id.buttonRst:
                    name.setText("");
                    number.setText("");
                    password.setText("");


                    break;

            }
        }
    }


}
