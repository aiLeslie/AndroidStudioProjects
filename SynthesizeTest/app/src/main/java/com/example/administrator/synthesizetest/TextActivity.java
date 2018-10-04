package com.example.administrator.synthesizetest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

public class TextActivity extends AppCompatActivity {

    private EditText editText;
    private SeekBar seekBar;
    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);

        editText = (EditText) findViewById(R.id.editText);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        textView = (TextView) findViewById(R.id.textView);

        seekBar.setProgress((int)(editText.getTextSize() / 3));
        textView.setText("(文字大小比例" + seekBar.getProgress() + "%\t文字大小" + editText.getTextSize() + ")");


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                int size = seekBar.getProgress();
                editText.setTextSize(size) ;
                //editText.setText(editText.getText().toString() + "\n(文字大小比例" + size + "%\t文字大小" + editText.getTextSize() + ")");
                editText.setSelection(editText.length());
                textView.setText("(文字大小比例" + seekBar.getProgress() + "%\t文字大小" + editText.getTextSize() + ")");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}
