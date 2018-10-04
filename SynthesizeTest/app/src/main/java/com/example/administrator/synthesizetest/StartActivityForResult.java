package com.example.administrator.synthesizetest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class StartActivityForResult extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_for_result);

        Button button = (Button) findViewById(R.id.buttonForResult);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                EditText text = (EditText) findViewById(R.id.editTextForResult);
                intent.putExtra("dataReturn",text.getText().toString());
                setResult(RESULT_OK,intent);
                Toast.makeText(StartActivityForResult.this, "transmission success", Toast.LENGTH_SHORT).show();
            }
        });



    }


}
