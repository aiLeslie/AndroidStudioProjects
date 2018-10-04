package com.example.administrator.randomnumberbuilder;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    TextView textViewNumber;
    EditText editText1,editText2;
    RadioGroup radioGroup;
    Button button,rI,rD,selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText1 = (EditText)findViewById(R.id.editText1);
        editText2 = (EditText)findViewById(R.id.editText2);

        rI = (CompoundButton)findViewById(R.id.radioButtonInteger);
        rD = (CompoundButton)findViewById(R.id.radioButtonDouble);
        radioGroup = (RadioGroup)findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                if(i == rI.getId()){
                    selected = rI;
                    Toast.makeText(MainActivity.this, "你选中了Integer", Toast.LENGTH_SHORT).show();
                }else if(i == rD.getId()){
                    selected = rD;
                    Toast.makeText(MainActivity.this, "你选中了Double", Toast.LENGTH_SHORT).show();
                }

            }
        });

        textViewNumber = (TextView)findViewById(R.id.textViewNumber);
        (button = (Button)findViewById(R.id.button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    if (rI == selected) {
                        int idnumber1 = Integer.parseInt(editText1.getText().toString());
                        int idnumber2 = Integer.parseInt(editText2.getText().toString());
                        textViewNumber.setText((int)(Math.random() * Integer.MAX_VALUE) % Math.abs(idnumber1 - idnumber2) + (idnumber1 > idnumber2 ? idnumber2 : idnumber1) + "");
                    }

                        else if (rD == selected){
                            double dnumber1 = Double.parseDouble(editText1.getText().toString());
                            double dnumber2 = Double.parseDouble(editText2.getText().toString());
                            textViewNumber.setText((Math.random() * Double.MAX_VALUE) % Math.abs(dnumber1 - dnumber2) + (dnumber1 > dnumber2 ? dnumber2:dnumber1 ) + "");
                    }else{
                        Toast.makeText(MainActivity.this, "~请先选择数据类型~", Toast.LENGTH_SHORT).show();
                    }
                }catch (ClassCastException e){
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }catch (Exception e){
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}

