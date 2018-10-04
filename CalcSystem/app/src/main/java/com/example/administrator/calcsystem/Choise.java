package com.example.administrator.calcsystem;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class Choise extends AppCompatActivity {
    private Button std;
    private Button transition;

    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choise);

        std = (Button)findViewById(R.id.standard);


        std.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(Choise.this,Standard.class);
                startActivity(intent);
            }
        });

        transition = (Button)findViewById(R.id.NumberSystemTransition);
        //intent = new Intent(Choise.this,NumberSystemTransition.class);


        transition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Choise.this,NumberSystemTransition.class);

                startActivity(intent);
            }
        });
    }
}
