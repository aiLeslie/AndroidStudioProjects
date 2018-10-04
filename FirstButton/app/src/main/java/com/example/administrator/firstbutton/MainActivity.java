package com.example.administrator.firstbutton;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button)findViewById(R.id.button);
        ButtonListener buttonListener = new ButtonListener();
        button.setOnClickListener(buttonListener);
    }
    class ButtonListener implements View.OnClickListener{
        @Override
        public void onClick(View view){
            Toast.makeText(MainActivity.this,"Hello World",Toast.LENGTH_SHORT).show();
        }
    }
}
