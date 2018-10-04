package com.example.administrator.calcsystem;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.Queue;

public class NumberSystemTransition extends AppCompatActivity implements View.OnClickListener {
    private EditText editText10, editText2, editText8, editText16;
    private Button rst, transition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_number_transition);

        editText2 = (EditText) findViewById(R.id.editText2);
        editText8 = (EditText) findViewById(R.id.editText8);
        editText10 = (EditText) findViewById(R.id.editText10);
        editText16 = (EditText) findViewById(R.id.editText16);

        rst = (Button) findViewById(R.id.reset);
        transition = (Button) findViewById(R.id.transition);
        rst.setOnClickListener(this);
        transition.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.reset:
                editText2.setText("");
                editText8.setText("");
                editText10.setText("");
                editText16.setText("");
                break;

            case R.id.transition:
                try {
                    int i = 0;
                    int number = Integer.MIN_VALUE;
                    if (!editText10.getText().toString().equals("")) {
                        for (i = 0; i < editText10.getText().toString().length(); i++) {
                            if (!Character.isDigit(editText10.getText().toString().charAt(i))) {
                                Toast.makeText(NumberSystemTransition.this, "十进制数不为纯数字", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                        if (i == editText10.getText().toString().length()) {
                            number = Integer.parseInt(editText10.getText().toString());
                            StringBuffer bin = new StringBuffer();
                            int temp = number;
                            while (true) {
                                bin.append(temp % 2);
                                temp = temp / 2;
                                if (temp == 0) break;
                            }
                            editText2.setText(bin.reverse().toString());
                            editText8.setText(String.format("%o", number).toCharArray(), 0, String.format("%o", number).toCharArray().length);
                            editText10.setText(String.format("%d", number).toCharArray(), 0, String.format("%d", number).toCharArray().length);
                            editText16.setText(String.format("0x%x", number).toCharArray(), 0, String.format("0x%x", number).toCharArray().length);
                            return;
                        }
                    }
                    if (!editText2.getText().toString().equals("")) {
                        Queue<Integer> queue = new LinkedList<>();
                        StringBuffer stringBuffer2 = new StringBuffer(editText2.getText().toString());

                        while(stringBuffer2.indexOf(" ") != -1){

                            queue.offer(stringBuffer2.indexOf(" "));
                            stringBuffer2.deleteCharAt(stringBuffer2.indexOf(" "));

                        }
                        editText2.setText(stringBuffer2.toString());
                        for (i = 0; i < editText2.getText().toString().length(); i++) {
                            if (!Character.isDigit(editText2.getText().toString().charAt(i))) {
                                Toast.makeText(NumberSystemTransition.this, "二进制数不为纯数字", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if (editText2.getText().toString().charAt(i) != '0' && editText2.getText().toString().charAt(i) != '1') {
                                Toast.makeText(NumberSystemTransition.this, "二进制数不含0与1之外的数", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }

                        if (i == editText2.getText().toString().length()) {
                            number = Integer.parseInt(editText2.getText().toString());

                            int deci = Integer.valueOf(number + "", 2);
                            while(queue.peek() != null)
                            stringBuffer2.insert(queue.poll()," ");
                            editText2.setText(stringBuffer2.toString());
                            editText8.setText(Integer.toOctalString(deci));
                            editText10.setText(deci + "");
                            editText16.setText("0x" + Integer.toHexString(deci));
                            return;
                        }
                    }


                    if (!editText8.getText().toString().equals("")) {
                        for (i = 0; i < editText8.getText().toString().length(); i++) {
                            if (editText8.getText().toString().charAt(i) != '0' && editText8.getText().toString().charAt(i) != '1'
                                    && editText8.getText().toString().charAt(i) != '2' && editText8.getText().toString().charAt(i) != '3'
                                    && editText8.getText().toString().charAt(i) != '4' && editText8.getText().toString().charAt(i) != '5'
                                    && editText8.getText().toString().charAt(i) != '6' && editText8.getText().toString().charAt(i) != '7') {
                                Toast.makeText(NumberSystemTransition.this, "八进制数不为纯数字", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                        if (i == editText8.getText().toString().length()) {
                            number = Integer.parseInt(editText8.getText().toString());

                            int deci = Integer.valueOf(number + "", 8);
                            editText2.setText(Integer.toBinaryString(deci));
                            editText8.setText(Integer.toOctalString(deci));
                            editText10.setText(deci + "");
                            editText16.setText("0x" + Integer.toHexString(deci));
                            return;


                        }
                    }


                    if (!editText16.getText().toString().equals(""))
                    {
                        for (i = 0; i < editText16.getText().toString().length(); i++) {
                            if (!Character.isDigit(editText16.getText().toString().charAt(i)) && editText16.getText().toString().charAt(i) != 'a' && editText16.getText().toString().charAt(i) != 'b'
                                    && editText16.getText().toString().charAt(i) != 'c' && editText16.getText().toString().charAt(i) != 'd'
                                    && editText16.getText().toString().charAt(i) != 'e' && editText16.getText().toString().charAt(i) != 'f') {
                                Toast.makeText(NumberSystemTransition.this, "十六进制数不为纯数字", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                        if (i == editText16.getText().toString().length()) {
                            //number = Integer.parseInt(editText16.getText().toString());

                            int deci = Integer.valueOf(editText16.getText().toString(), 16);
                            editText2.setText(Integer.toBinaryString(deci));
                            editText8.setText(Integer.toOctalString(deci));
                            editText10.setText(deci + "");
                            editText16.setText("0x" + Integer.toHexString(deci));
                            return;

                        }

                    }
                    else {
                        Toast.makeText(NumberSystemTransition.this, "您没有输入数字哦~", Toast.LENGTH_SHORT).show();
                    }

                    break;
                }catch (Exception  e ){
                    Toast.makeText(NumberSystemTransition.this,e.getMessage(),Toast.LENGTH_LONG).show();
                }
        }
    }

}

