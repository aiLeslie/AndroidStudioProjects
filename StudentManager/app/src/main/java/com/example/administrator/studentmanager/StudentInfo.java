package com.example.administrator.studentmanager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class StudentInfo extends AppCompatActivity {
    TextView name,number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_info);

        /*name = (TextView)findViewById(R.id.InfoName);
        number = (TextView)findViewById(R.id.InfoNumber);


        FileInputStream in = null;
        String studentInfo = null;
        try{
        in = openFileInput("studentInfo");

        byte[] info = new byte[in.available()];
        in.read(info);
        studentInfo = new String(info);
    } catch (Exception e) {
        Toast.makeText(StudentInfo.this, e.getMessage(), Toast.LENGTH_LONG).show();
    } finally {
        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
        name.setText(studentInfo.substring(studentInfo.indexOf(" ") + 1,studentInfo.lastIndexOf("")));
        number.setText(studentInfo.substring(studentInfo.lastIndexOf("") +1));*/
    }
}
