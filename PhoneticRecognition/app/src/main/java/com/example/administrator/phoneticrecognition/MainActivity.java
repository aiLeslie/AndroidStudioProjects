package com.example.administrator.phoneticrecognition;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            Button button = (Button) findViewById(R.id.buttonRecognition);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                    intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "开始语音");
                    intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 10);
                    startActivity(intent);

                }
            });
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
//            List<String> stringArrayListExtra = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
//            ListView listView = (ListView) findViewById(R.id.listView);
//            listView.setAdapter(new ArrayAdapter<String>(this,support_simple_spinner_dropdown_item,stringArrayListExtra));

        }
    }
}
