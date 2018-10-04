package com.example.administrator.calcsystem;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Standard extends AppCompatActivity implements View.OnClickListener  {
    private static IndexOfSign head = null;
    private static IndexOfSign end = null;
    private static Minus minusHead = null;
    private static Minus MinusEnd = null;
    private static StringBuffer input = null;
    private static String result = null;
    private static double total = Double.MIN_VALUE;
    private EditText text;
    //数字键
    private Button bEquals;
    private Button button0;
    private Button button1;
    private Button button2;
    private Button button3;
    private Button button4;
    private Button button5;
    private Button button6;
    private Button button7;
    private Button button8;
    private Button button9;
    private Button buttonPointer;

    //清除键
    private Button buttonBackspace;
    private Button buttonC;
    //运算符键
    private Button buttonAdd;
    private Button buttonMinus;
    private Button buttonMultiple;
    private Button buttonDivide;

    private Button buttonExit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_standard);
        //根据ID找出控件对象
        text = (EditText) findViewById(R.id.editText);//作为屏幕输出打印
        input = new StringBuffer(text.getText().toString());//接受用户信息
        bEquals = (Button) findViewById(R.id.buttonEquals);

        button0 = (Button) findViewById(R.id.button0);
        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);
        button4 = (Button) findViewById(R.id.button4);
        button5 = (Button) findViewById(R.id.button5);
        button6 = (Button) findViewById(R.id.button6);
        button7 = (Button) findViewById(R.id.button7);
        button8 = (Button) findViewById(R.id.button8);
        button9 = (Button) findViewById(R.id.button9);
        buttonPointer = (Button) findViewById(R.id.buttonPointer);

        buttonBackspace = (Button) findViewById(R.id.buttonBackspace);
        buttonC = (Button) findViewById(R.id.buttonC);

        buttonAdd = (Button) findViewById(R.id.buttonAdd);
        buttonMinus = (Button) findViewById(R.id.buttonMinus);
        buttonMultiple = (Button) findViewById(R.id.buttonMultiple);
        buttonDivide = (Button) findViewById(R.id.buttonDivide);

        buttonExit = (Button)findViewById(R.id.buttonPercent);

        //控件设置监听器
        bEquals.setOnClickListener(this);
        button0.setOnClickListener(this);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        button5.setOnClickListener(this);
        button6.setOnClickListener(this);
        button7.setOnClickListener(this);
        button8.setOnClickListener(this);
        button9.setOnClickListener(this);
        buttonPointer.setOnClickListener(this);

        buttonBackspace.setOnClickListener(this);
        buttonC.setOnClickListener(this);

        buttonAdd.setOnClickListener(this);
        buttonMinus.setOnClickListener(this);
        buttonMultiple.setOnClickListener(this);
        buttonDivide.setOnClickListener(this);

        buttonExit.setOnClickListener(this);


    }


    @Override
    //各个按钮监听器点击方法
    public void onClick(View v) {
        Character ch = null;//获得input里的字符进行判断
        switch (v.getId()) {
            case R.id.buttonEquals:

                try {
                    //Toast.makeText(Standard.this,count() + "",Toast.LENGTH_SHORT).show();
                    input = new StringBuffer(text.getText().toString());
                    //1
                    if (input.toString().equals("") || input.toString().equals("-")) {//如果input内容为空时
                        total = 0;//直接把total赋值为0
                        input.append(0);
                        text.setText((input + ("\n= " + total)).toCharArray(), 0, (input + ("\n=" + total)).toCharArray().length);
                        return;
                    }

                    //遍历input判断是否有'='(等于号)
                    for (int i = 0; i < input.length(); i++) {
                        if (i == 0) {
                            if (input.charAt(i) == '=') {
                                input = new StringBuffer();
                                input.append(0);
                                total = 0;
                                text.setText((input + ("\n= " + total)).toCharArray(), 0, (input + ("\n=" + total)).toCharArray().length);
                                return;
                            }

                        }
                        if (input.charAt(i) == '=')
                            input.delete(i - 1, input.length());//如果有把它后面内容删除进行后续的计算
                    }

                /*//分别判断input第一个和最后一个字符是否为运算符,进行处理
                ch = input.charAt(0);
                if (ch.compareTo('*') == 0 || ch.compareTo('/') == 0 || ch.compareTo('+') == 0) {
                    input.delete(0, 1);

                }
                ch = input.charAt(input.length() - 1);
                if (ch.compareTo('-') == 0 || ch.compareTo('*') == 0 || ch.compareTo('/') == 0 || ch.compareTo('+') == 0) {

                    input.delete(input.length() - 1, input.length());

                }*/
                    for (int i = 0; i < input.length(); i++) {
                        if (input.charAt(i) == '-' || input.charAt(i) == '0' || input.charAt(i) == '1' || input.charAt(i) == '2' || input.charAt(i) == '3' || input.charAt(i) == '4' ||
                                input.charAt(i) == '5' || input.charAt(i) == '6' || input.charAt(i) == '7' || input.charAt(i) == '8' || input.charAt(i) == '9') {
                            if (i == 0) break;
                            input.delete(0, i);
                            text.setText(input.toString().toCharArray(), 0, input.toString().toCharArray().length);
                            break;
                        }
                    }
                    for (int i = input.length() - 1; i >= 0; i--) {
                        if (input.charAt(i) == '0' || input.charAt(i) == '1' || input.charAt(i) == '2' || input.charAt(i) == '3' || input.charAt(i) == '4' ||
                                input.charAt(i) == '5' || input.charAt(i) == '6' || input.charAt(i) == '7' || input.charAt(i) == '8' || input.charAt(i) == '9') {
                            if (i == input.length() - 1) break;
                            input.delete(i + 1, input.length());
                            text.setText(input.toString().toCharArray(), 0, input.toString().toCharArray().length);
                            break;
                        }
                    }


                    //把处理好的input赋给result进行计算
                    result = input.toString();
                    //判别result是否有非法字符影响计算
                    StringBuffer error = new StringBuffer();
                    for (int i = 0; i < result.length(); i++) {

                        if (result.charAt(i) != '0' && result.charAt(i) != '1' && result.charAt(i) != '2' && result.charAt(i) != '3'
                                && result.charAt(i) != '4' && result.charAt(i) != '5' && result.charAt(i) != '6' && result.charAt(i) != '7'
                                && result.charAt(i) != '8' && result.charAt(i) != '9' && result.charAt(i) != '+' && result.charAt(i) != '-'
                                && result.charAt(i) != '*' && result.charAt(i) != '/' && result.charAt(i) != '.' && result.charAt(i) != 'E'
                                ) {
                            error.append(result.charAt(i));

                        }
                    }
                    if (!error.toString().equals("")) {
                        Toast.makeText(Standard.this, "您输入的数据\n( " + error.toString() + ")为非法字符串\n请自行调整数据", Toast.LENGTH_SHORT).show();
                        error.setLength(0);
                        return;
                    }


                    //判定符号连续
                    IndexOfSign.findSign(result);
                    if (head != end) {
                        IndexOfSign temp = head;

                        while (temp != null) {
                            if (temp == head) {
                                temp = temp.next;
                                continue;
                            }


                            if (temp.index == temp.previous.index + 1) {
                                Toast.makeText(Standard.this, "检测有符号连续\n请自行调整", Toast.LENGTH_SHORT).show();
                                total = Double.MIN_VALUE;
                                head = null;
                                end = null;
                                return;
                            }
                            temp = temp.next;

                        }
                    }


                    //调用方法进行计算
                    count();

                    //把算出结果打印到edittext显示
                    text.setText((input + "\n=" + total).toCharArray(), 0, (input + "\n=" + total).toCharArray().length);
                    text.setSelection(text.length());
                }catch(Exception e){
                    e.printStackTrace();
                    text.setText((input + "\n=" + total).toCharArray(), 0, (input + "\n=" + total).toCharArray().length);
                }

                break;

            case R.id.button0:
//                input = new StringBuffer(text.getText().toString());
//                if (text.getText().toString().indexOf('=') != -1) {
//                    Toast.makeText(Standard.this, "请不要非法按数字键", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                input.append(0);
//                text.setText(input.toString().toCharArray(), 0, input.toString().toCharArray().length);
//                break;
            case R.id.button1:
//                input = new StringBuffer(text.getText().toString());
//                if (text.getText().toString().indexOf('=') != -1) {
//                    Toast.makeText(Standard.this, "请不要非法按数字键", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                input.append(1);
//                text.setText(input.toString().toCharArray(), 0, input.toString().toCharArray().length);
//                break;
            case R.id.button2:
//                input = new StringBuffer(text.getText().toString());
//                if (text.getText().toString().indexOf('=') != -1) {
//                    Toast.makeText(Standard.this, "请不要非法按数字键", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                input.append(2);
//                text.setText(input.toString().toCharArray(), 0, input.toString().toCharArray().length);
//                break;
            case R.id.button3:
//                input = new StringBuffer(text.getText().toString());
//                if (text.getText().toString().indexOf('=') != -1) {
//                    Toast.makeText(Standard.this, "请不要非法按数字键", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                input.append(3);
//                text.setText(input.toString().toCharArray(), 0, input.toString().toCharArray().length);
//                break;
            case R.id.button4:
//                input = new StringBuffer(text.getText().toString());
//                if (text.getText().toString().indexOf('=') != -1) {
//                    Toast.makeText(Standard.this, "请不要非法按数字键", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                input.append(4);
//                text.setText(input.toString().toCharArray(), 0, input.toString().toCharArray().length);
//                break;
            case R.id.button5:
//                input = new StringBuffer(text.getText().toString());
//                if (text.getText().toString().indexOf('=') != -1) {
//                    Toast.makeText(Standard.this, "请不要非法按数字键", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                input.append(5);
//                text.setText(input.toString().toCharArray(), 0, input.toString().toCharArray().length);
//                break;
            case R.id.button6:
//                input = new StringBuffer(text.getText().toString());
//                if (text.getText().toString().indexOf('=') != -1) {
//                    Toast.makeText(Standard.this, "请不要非法按数字键", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                input.append(6);
//                text.setText(input.toString().toCharArray(), 0, input.toString().toCharArray().length);
//                break;
            case R.id.button7:
//                input = new StringBuffer(text.getText().toString());
//                if (text.getText().toString().indexOf('=') != -1) {
//                    Toast.makeText(Standard.this, "请不要非法按数字键", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                input.append(7);
//                text.setText(input.toString().toCharArray(), 0, input.toString().toCharArray().length);
//                break;
            case R.id.button8:
//                input = new StringBuffer(text.getText().toString());
//                if (text.getText().toString().indexOf('=') != -1) {
//                    Toast.makeText(Standard.this, "请不要非法按数字键", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                input.append(8);
//                text.setText(input.toString().toCharArray(), 0, input.toString().toCharArray().length);
//                break;
            case R.id.button9:
                input = new StringBuffer(text.getText().toString());
                if (text.getText().toString().indexOf('=') != -1) {
                    Toast.makeText(Standard.this, "请不要非法按数字键", Toast.LENGTH_SHORT).show();
                    return;
                }
                input.append(((Button)v).getText());
                text.setText(input.toString().toCharArray(), 0, input.toString().toCharArray().length);
//                try{
//                    //调用方法进行计算
//                    count();
//                    //把算出结果打印到edittext显示
//                    text.setText((input + "\n=" + total).toCharArray(), 0, (input + "\n=" + total).toCharArray().length);
//                }catch (Exception e){
//                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
//                }


                text.setSelection(text.length());
                break;

            case R.id.buttonPointer:
                input = new StringBuffer(text.getText().toString());
                if (input.toString().equals("")) input.append(0);//如果input为空添加0字符
                if (input.charAt(input.length() - 1) == '.') return;//如果input前一个字符含有点就直接返回
                for (int i = 0; i < input.length(); i++) {
                    if (input.charAt(i) == '=') {//如果input里有等号直接返回

                        return;
                    }
                }

                input.append('.');
                text.setText(input.toString().toCharArray(), 0, input.toString().toCharArray().length);
                break;

            case R.id.buttonAdd:
                input = new StringBuffer(text.getText().toString());
                if (input.toString().equals("")) input.append(0);


                for (int i = 0; i < input.length(); i++) {
                    if (input.charAt(i) == '=') {
                        text.setText((total + "+").toCharArray(), 0, (total + "+").toCharArray().length);
                        input = new StringBuffer(text.getText().toString());

                        return;
                    }
                }
                /*if(input.toString().indexOf('=') != -1){
                    text.setText((total + "+").toCharArray(),0,(total + "+").toCharArray().length);
                    return;
                }*/


                ch = input.charAt(input.length() - 1);
                if (ch.compareTo('-') == 0 || ch.compareTo('*') == 0 || ch.compareTo('/') == 0 || ch.compareTo('+') == 0) {
                    input.delete(input.length() - 1, input.length());
                }
                input.append('+');
                text.setText(input.toString().toCharArray(), 0, input.toString().toCharArray().length);
                break;
            case R.id.buttonMinus:
                input = new StringBuffer(text.getText().toString());
                if (input.toString().equals(""))
                {
                    input.append('-');
                    text.setText(input.toString().toCharArray(), 0, input.toString().toCharArray().length);
                    return;
                }


                for (int i = 0; i < input.length(); i++) {
                    if (input.charAt(i) == '=') {
                        text.setText((total + "-").toCharArray(), 0, (total + "-").toCharArray().length);
                        input = new StringBuffer(text.getText().toString());

                        return;
                    }
                }


                ch = input.charAt(input.length() - 1);
                if (ch.compareTo('-') == 0 || ch.compareTo('*') == 0 || ch.compareTo('/') == 0 || ch.compareTo('+') == 0) {
                    input.delete(input.length() - 1, input.length());

                }
                input.append('-');
                text.setText(input.toString().toCharArray(), 0, input.toString().toCharArray().length);
                break;
            case R.id.buttonMultiple:
                input = new StringBuffer(text.getText().toString());
                if (input.toString().equals("")) input.append(0);


                for (int i = 0; i < input.length(); i++) {
                    if (input.charAt(i) == '=') {
                        text.setText((total + "*").toCharArray(), 0, (total + "*").toCharArray().length);
                        input = new StringBuffer(text.getText().toString());

                        return;
                    }
                }

                ch = input.charAt(input.length() - 1);
                if (ch.compareTo('-') == 0 || ch.compareTo('*') == 0 || ch.compareTo('/') == 0 || ch.compareTo('+') == 0) {
                    input.delete(input.length() - 1, input.length());

                }
                input.append('*');
                text.setText(input.toString().toCharArray(), 0, input.toString().toCharArray().length);
                break;
            case R.id.buttonDivide:
                input = new StringBuffer(text.getText().toString());
                if (input.toString().equals("")) input.append(0);


                for (int i = 0; i < input.length(); i++) {
                    if (input.charAt(i) == '=') {
                        if (total == 0) return;
                        text.setText((total + "/").toCharArray(), 0, (total + "/").toCharArray().length);
                        input = new StringBuffer(text.getText().toString());

                        return;
                    }
                }

                ch = input.charAt(input.length() - 1);
                if (ch.compareTo('-') == 0 || ch.compareTo('*') == 0 || ch.compareTo('/') == 0 || ch.compareTo('+') == 0) {
                    input.delete(input.length() - 1, input.length());

                }
                input.append('/');
                text.setText(input.toString().toCharArray(), 0, input.toString().toCharArray().length);
                break;
            case R.id.buttonC:
                input = null;
                input = new StringBuffer();
                text.setText(input.toString().toCharArray(), 0, input.toString().toCharArray().length);
                //Toast.makeText(Standard.this, "主人:屏幕焕然一新", Toast.LENGTH_SHORT).show();
                Toast.makeText(Standard.this, "代表月亮消灭你", Toast.LENGTH_SHORT).show();
                break;
            case R.id.buttonBackspace:
                input = new StringBuffer(text.getText().toString());
                if (input.toString().equals("")) {
                    Toast.makeText(Standard.this, "主人:屏幕已经清空了\n请别按我", Toast.LENGTH_SHORT).show();
                    return;
                }


                input.delete(input.length() - 1, input.length());
                text.setText(input.toString().toCharArray(), 0, input.toString().toCharArray().length);
                break;

            case R.id.buttonPercent:
                AlertDialog.Builder dialog = new AlertDialog.Builder(Standard.this);
                dialog.setTitle("warning");
                dialog.setMessage("Are you sure you want to exit now?");
                dialog.setCancelable(true);
                dialog.setPositiveButton("yes",new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {

//                                input = new StringBuffer("Welcome to use this calculator");
//                                text.setText(input.toString().toCharArray(),0,input.toString().toCharArray().length);
//                                Toast.makeText(Standard.this,"Bye~~~",Toast.LENGTH_LONG).show();
//                                dialog.dismiss();
//                                try{
//                                    Thread.sleep(3000);
//                                }catch (Exception e){
//                                    Toast.makeText(Standard.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
//                                }
                                finish();

                            }
                        });
                dialog.setNegativeButton("no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        return;
                    }
                });
                dialog.show();

                /*try
                {
                    Thread.currentThread().sleep(5000);//毫秒
                }
                catch(Exception e){}*/
                /*Timer timer=new Timer();//实例化Timer类
                timer.schedule(new TimerTask(){
                    public void run(){

                        this.cancel();}},10000);//五百毫秒*/


                break;

        }

    }

    //传入字符串寻找符号和下标并动态制造对象链表(头对象为head)
    public static class IndexOfSign {

        public char sign;
        public int index;
        public IndexOfSign next;
        public IndexOfSign previous;

        public static void findSign(String result) {
            head = null;
            end = null;
            // char[] charArray = result.toCharArray();
            for (int i = 0; i < result.length(); i++) {
                if (result.charAt(i) == '+') {
                    if (head == null) {
                        head = new IndexOfSign();
                        end = head;
                        end.index = i;
                        end.sign = '+';


                    } else {

                        end.next = new IndexOfSign();
                        end.next.previous = end;
                        end.next.index = i;
                        end.next.sign = '+';
                        end = end.next;


                    }


                }

                if (result.charAt(i) == '-') {
                    if (head == null) {
                        head = new IndexOfSign();
                        end = head;
                        end.index = i;
                        end.sign = '-';

                    } else {

                        end.next = new IndexOfSign();
                        end.next.previous = end;
                        end.next.index = i;
                        end.next.sign = '-';
                        end = end.next;

                    }


                }

                if (result.charAt(i) == '*') {
                    if (head == null) {
                        head = new IndexOfSign();
                        end = head;
                        end.index = i;
                        end.sign = '*';

                    } else {

                        end.next = new IndexOfSign();
                        end.next.previous = end;
                        end.next.index = i;
                        end.next.sign = '*';
                        end = end.next;

                    }


                }

                if (result.charAt(i) == '/') {
                    if (head == null) {
                        head = new IndexOfSign();
                        end = head;
                        end.index = i;
                        end.sign = '/';

                    } else {

                        end.next = new IndexOfSign();
                        end.next.previous = end;
                        end.next.index = i;
                        end.next.sign = '/';
                        end = end.next;

                    }


                }
            }
        }
    }
    private class SleepThread extends Thread{
        @Override
        public void run(){
            try {
                sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }



    public static class Minus {
        public static Minus minusHead = null;
        public static Minus MinusEnd = null;
        public char sign;
        public double number;
        public Minus next;

        public static void findMinusNumber(StringBuffer calc) {

            minusHead = null;
            MinusEnd = null;

            IndexOfSign.findSign(calc.toString());
            if (Standard.head == null) return;

            else {

                //String textNumber = null;
                IndexOfSign temp = Standard.head;

                while (temp != null) {
                    if (minusHead == null) {
                        minusHead = new Minus();
                        MinusEnd = minusHead;
                        if (temp.index == 0) {//如果下标0的字符是运算符
                            minusHead.sign = temp.sign;

                            //textNumber = calc.substring(1,temp.index);
                            if (temp.next == null) {
                                minusHead.number = Double.parseDouble(calc.substring(1, calc.length()));
                                break;
                            } else
                                minusHead.number = Double.parseDouble(calc.substring(1, temp.next.index));
                        /*temp = temp.next;
                        continue;*/
                        } else {
                            minusHead.sign = '+';
                            //textNumber = calc.substring(0,temp.index);
                            minusHead.number = Double.parseDouble(calc.substring(0, temp.index));


                            if (temp.next == null) {
                                MinusEnd.next = new Minus();
                                MinusEnd = MinusEnd.next;
                                MinusEnd.sign = temp.sign;
                                //textNumber = calc.substring(temp.index + 1,calc.length());
                                MinusEnd.number = Double.parseDouble(calc.substring(temp.index + 1, calc.length()));
                                break;
                            } else {
                                MinusEnd.next = new Minus();
                                MinusEnd = MinusEnd.next;
                                MinusEnd.sign = temp.sign;
                                //textNumber = calc.substring(temp.index + 1,temp.next.index);
                                MinusEnd.number = Double.parseDouble(calc.substring(temp.index + 1, temp.next.index));
                            }

                        }


                    } else {


                        MinusEnd.next = new Minus();
                        MinusEnd = MinusEnd.next;
                        MinusEnd.sign = temp.sign;
                        if (temp.next == null) {

                            //textNumber = calc.substring(temp.index,calc.length());
                            MinusEnd.number = Double.parseDouble(calc.substring(temp.index + 1, calc.length()));
                            break;
                        } else {
                            //textNumber = calc.substring(temp.previous.index,temp.index);
                            MinusEnd.number = Double.parseDouble(calc.substring(temp.index + 1, temp.next.index));
                        }


                    }
                    temp = temp.next;
                }


            }
        }
    }




        public  void count() {

        IndexOfSign indexOfSign = new IndexOfSign();
        indexOfSign.findSign(result);
        StringBuffer calc = new StringBuffer(result);



        String number1 = null, number2 = null, textNumber = null;
        head = null;
        end = null;
        indexOfSign.findSign(calc.toString());
        IndexOfSign temp = head;

        while (temp != null) {
            if (temp.sign == '*') {
                if (temp.previous == null) {
                    number1 = calc.substring(0, temp.index);
                } else {
                    number1 = calc.substring(temp.previous.index + 1, temp.index);
                }


                if (temp.next == null) {
                    number2 = calc.substring(temp.index + 1, calc.length());
                } else {
                    number2 = calc.substring(temp.index + 1, temp.next.index);
                }

                textNumber = Double.parseDouble(number1) * Double.parseDouble(number2) + "";

                if (temp.previous != null && temp.next != null) {
                    calc.replace(temp.previous.index + 1, temp.next.index, textNumber);
                } else if (temp.previous == null && temp.next != null) {
                    calc.replace(0, temp.next.index, textNumber);
                } else if (temp.previous != null && temp.next == null) {
                    calc.replace(temp.previous.index + 1, calc.length(), textNumber);
                } else if (temp.previous == null && temp.next == null) {
                    calc.replace(0, calc.length(), textNumber);
                }
                head = null;
                end = null;
                indexOfSign.findSign(calc.toString());

                temp = head;
                continue;
            }

            if (temp.sign == '/') {
                if (temp.previous == null) {
                    number1 = calc.substring(0, temp.index);
                } else {
                    number1 = calc.substring(temp.previous.index + 1, temp.index);
                }


                if (temp.next == null) {
                    number2 = calc.substring(temp.index + 1, calc.length());
                } else {
                    number2 = calc.substring(temp.index + 1, temp.next.index);
                }

                textNumber = Double.parseDouble(number1) / Double.parseDouble(number2) + "";

                if (temp.previous != null && temp.next != null) {
                    calc.replace(temp.previous.index + 1, temp.next.index, textNumber);
                } else if (temp.previous == null && temp.next != null) {
                    calc.replace(0, temp.next.index, textNumber);
                } else if (temp.previous != null && temp.next == null) {
                    calc.replace(temp.previous.index + 1, calc.length(), textNumber);
                } else if (temp.previous == null && temp.next == null) {
                    calc.replace(0, calc.length(), textNumber);
                }
                head = null;
                end = null;
                indexOfSign.findSign(calc.toString());

                temp = head;
                continue;
            }


            temp = temp.next;
        }


        //错误:在负数计算
        /*head = null;
        end = null;
        indexOfSign.findSign(calc.toString());
        temp = head;

        while(temp != null)
        {
            if(temp.sign == '+')
            {
                if(temp.previous == null) {
                    number1 = calc.substring(0,temp.index);
                }else {
                    number1 = calc.substring(temp.previous.index + 1, temp.index);
                }


                if(temp.next == null) {
                    number2 = calc.substring(temp.index + 1, calc.length());
                }else {
                    number2 = calc.substring(temp.index + 1, temp.next.index);
                }

                textNumber = Double.parseDouble(number1) + Double.parseDouble(number2) + "";

                if(temp.previous != null && temp.next != null) {
                    calc.replace(temp.previous.index + 1, temp.next.index, textNumber);
                }else if(temp.previous == null && temp.next != null) {
                    calc.replace(0, temp.next.index, textNumber);
                }else if(temp.previous != null && temp.next == null) {
                    calc.replace(temp.previous.index + 1, calc.length(), textNumber);
                }else if(temp.previous == null && temp.next == null) {
                    calc.replace(0, calc.length(), textNumber);
                }
                head = null;
                end = null;
                indexOfSign.findSign(calc.toString());

                temp = head;
                continue;
            }

            if(temp.sign == '-')
            {
                if(temp.previous == null) {
                    number1 = calc.substring(0,temp.index);
                }else {
                    number1 = calc.substring(temp.previous.index + 1, temp.index);
                }


                if(temp.next == null) {
                    number2 = calc.substring(temp.index + 1, calc.length());
                }else {
                    number2 = calc.substring(temp.index + 1, temp.next.index);
                }

                textNumber = Double.parseDouble(number1) - Double.parseDouble(number2) + "";

                if(temp.previous != null && temp.next != null) {
                    calc.replace(temp.previous.index + 1, temp.next.index, textNumber);
                }else if(temp.previous == null && temp.next != null) {
                    calc.replace(0, temp.next.index, textNumber);
                }else if(temp.previous != null && temp.next == null) {
                    calc.replace(temp.previous.index + 1, calc.length(), textNumber);
                }else if(temp.previous == null && temp.next == null) {
                    calc.replace(0, calc.length(), textNumber);
                }
                head = null;
                end = null;
                indexOfSign.findSign(calc.toString());

                temp = head;
                continue;
            }




            temp = temp.next;
        }
        Minus.findMinusNumber(calc);

        if(minusHead != null){
            total = 0;
            Minus minus = minusHead;
            while(minus != null){
                if(minus.sign == '+')total += minus.number;
                else if (minus.sign == '-')total -= minus.number;
                minus = mius.next;
            }
        }*/

        Minus minus = new Minus();
            Minus.findMinusNumber(calc);

            if(Minus.minusHead != null){
                total = 0;
                minus = Minus.minusHead;
                while(minus != null){
                    if(minus.sign == '+')total += minus.number;
                    else if (minus.sign == '-')total -= minus.number;

                    System.out.println("数值" + minus.number + "\t符号:" + minus.sign);
                    minus = minus.next;
                }
            }else total = Double.parseDouble(calc.toString());


    }

}
