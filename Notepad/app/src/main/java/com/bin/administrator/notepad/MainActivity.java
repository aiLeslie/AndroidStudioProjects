package com.bin.administrator.notepad;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        Button button = (Button) findViewById(R.id.buttonStart);
        button.setOnClickListener(this);
        button = (Button) findViewById(R.id.buttonRead);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttonStart) {
            FileOutputStream out = null;
            try {
                out = openFileOutput("data", MODE_PRIVATE);
                EditText editText = (EditText) findViewById(R.id.editText);
                out.write(editText.getText().toString().getBytes());
                Toast.makeText(this, "save succeed", Toast.LENGTH_SHORT).show();
            } catch (FileNotFoundException e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        } else if (v.getId() == R.id.buttonRead) {
            FileInputStream in = null;
            try {
                in = openFileInput("data");
                EditText editText = (EditText) findViewById(R.id.editText);
                byte[] bytes = new byte[in.available()];
                in.read(bytes);
                editText.setText(new String(bytes));
                Toast.makeText(this, "read succeed", Toast.LENGTH_SHORT).show();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
