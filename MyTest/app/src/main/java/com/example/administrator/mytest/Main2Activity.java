package com.example.administrator.mytest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

//        int a1 = getIntent().getIntExtra("id",0);
//        int a2 = R.id.buttonStartAll;
//        Toast.makeText(this, a1 == a2?"true":"false", Toast.LENGTH_SHORT).show();
        Button button = (Button) findViewById(R.id.buttonReset);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((EditText)findViewById(R.id.editTextAdmin)).setText("");
                ((EditText)findViewById(R.id.editTextPassword)).setText("");
            }
        });
        button = (Button) findViewById(R.id.buttonLogin);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String admin = ((EditText)findViewById(R.id.editTextAdmin)).getText().toString();
                String password = ((EditText)findViewById(R.id.editTextPassword)).getText().toString();
                if(admin.equals("p20160307113") && password.equals("110")){
                    Toast.makeText(Main2Activity.this, "login success", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent("startActivity");
                    startActivity(intent);
                }


            }
        });
        button = (Button) findViewById(R.id.buttonStartAll);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("startActivity");
                startActivity(intent);

            }
        });
    }
}
