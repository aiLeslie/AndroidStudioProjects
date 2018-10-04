package com.example.administrator.asynctasktest;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private boolean isActivityRun = true;
    private ProgressBar bar;
    private TextView dispaly;
    private EditText editText;
    private String fileName;
    private String[] fileNameArray;
    private ListView listView;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == -1) {
                stopAnimation();
            }
            if (msg.what == 0 && listView != null ){
                System.out.println("listView Update");
                if (fileNameArray.length != 0)listView.setAdapter(new ArrayAdapter<String>(MainActivity.this, R.layout.support_simple_spinner_dropdown_item, fileNameArray));
                else{
                    listView.setAdapter(new ArrayAdapter<String>(MainActivity.this, R.layout.support_simple_spinner_dropdown_item, new String[]{"You didn't download the file", "So nothing to show ! "}));
                }
            }

        }
    };
    private ImageView animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        isActivityRun = true;
        openingAnimation();//加载动画
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);//申请权限
        InitView();//初始化控件
    }

    private void InitView() {
        Button button = findViewById(R.id.buttonDownload);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editText.getText().toString().equals("")/* && editText.getText().toString().lastIndexOf("/") != -1*/) {//如果edittext不为空且含有'/'
                    if (editText.getText().toString().indexOf('/') != -1)
                        fileName = editText.getText().toString().substring(editText.getText().toString().lastIndexOf("/") + 1);//得到'/'后面文件名
                    else {
                        fileName = editText.getText().toString();
                    }
                    try {
                        new MyAsyncTask(MainActivity.this, bar,dispaly).execute(editText.getText().toString());//执行异步任务
                    } catch (Exception e) {

                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        editText.setText(e.getMessage());
                    }
                } else {
                    Toast.makeText(MainActivity.this, "please enter download url!", Toast.LENGTH_SHORT).show();
                }


            }
        });
        button = findViewById(R.id.buttonClean);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText("");
            }
        });
        button = findViewById(R.id.buttonOpenPath);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Button) v).setText(getDownloadPath());
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivity(intent);

            }
        });

        listView = findViewById(R.id.listView);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                if (fileNameArray.length == 0){
                    Toast.makeText(MainActivity.this, "You didn't download the file, So nothing to show ! ",Toast.LENGTH_SHORT).show();
                    return true;
                }
                AlertDialog dialog;
                final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Wranning");
                builder.setMessage("Are you sure to delete " + fileNameArray[position] + " ?");
                builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        File file = new File(getDownloadPath() + "/" + fileNameArray[position]);
                        String fileName = fileNameArray[position];
                        file.delete();
                        handler.sendEmptyMessage(0);
                        if (!new File(getDownloadPath() + "/" + fileName).exists()){
                            Toast.makeText(MainActivity.this,"delete " +  fileName + " succeed", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(MainActivity.this,"delete " +  fileName + " failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.show();
                return true;
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (fileNameArray.length == 0){
                    Toast.makeText(MainActivity.this, "You didn't download the file, So nothing to show ! ",Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    String fileName = fileNameArray[position];

                    String type = findType(fileName.substring(fileName.lastIndexOf(".")));
                    if (type == null) return;

                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.fromFile(new File(getDownloadPath() + "/" + fileName)), type);
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    editText.setText(e.getMessage());
                }


            }

        });

        bar = (ProgressBar) findViewById(R.id.progressBar);
        bar.setVisibility(View.GONE);
        dispaly = (TextView) findViewById(R.id.textViewProgress);
        dispaly.setVisibility(View.GONE);

        editText = (EditText) findViewById(R.id.editText);

    }

    public String getDownloadPath() {
        return this.getExternalFilesDir("download file").getAbsolutePath();
    }

    private void openingAnimation() {
        getSupportActionBar().hide();
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        animation = new ImageView(this);
//        getPictureResource(MainActivity.this.getExternalFilesDir("picture").getAbsolutePath());
//        Bitmap bitmap = BitmapFactory.decodeFile(MainActivity.this.getExternalFilesDir("picture").getAbsolutePath() + "/mipmap.png");
//        itmap bitmap = BitmapFactory.decodeFile(getDownloadPath() + "/mipmap.jpg");
//        if (bitmap != null) animation.setImageBitmap(bitmap);
        animation.setImageResource(R.drawable.java);
        //animation.setImageResource(R.drawable.leslie);
        animation.setScaleType(ImageView.ScaleType.CENTER_CROP);
        linearLayout.addView(animation, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    sleep(1000 * 1);
                    Message message = handler.obtainMessage(-1);
                    handler.sendMessage(message);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }

    private void stopAnimation() {

        LinearLayout linearLayout = (android.widget.LinearLayout) findViewById(R.id.linearLayout);
        linearLayout.removeView(animation);
        getSupportActionBar().show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        new Thread() {
            @Override
            public void run()
            {

                fileNameArray = new File(getDownloadPath()).list();
                handler.sendEmptyMessage(0);

                System.out.println("正在运行子线程");
                while (isActivityRun)
                {
                    String[] currentFileNameArray = new File(getDownloadPath()).list();
                    if (currentFileNameArray.length != fileNameArray.length) {//如果当前文件数组长度不一样,就发送消息
                        fileNameArray = currentFileNameArray;
                        handler.sendEmptyMessage(0);
                        continue;
                    }
                    for (int i = 0; i < currentFileNameArray.length; i++) {
                        if (!currentFileNameArray[i].equals(fileNameArray[i])) {
                            fileNameArray = currentFileNameArray;
                            handler.sendEmptyMessage(0);
                            break;
                        }
                    }


                    try {
                        Thread.sleep(1000 / 60);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                //System.out.println("终止子线程");

            }

        }.start();


    }


    @Override
    protected void onStop() {
        super.onStop();
        isActivityRun = false;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[grantResults.length - 1] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "授权成功,欢迎使用", Toast.LENGTH_SHORT).show();
        } else {
            finish();
        }
    }


    public final String[][] MIME_MapTable = {
            //{后缀名，MIME类型}
            {".3gp", "video/3gpp"},
            {".apk", "application/vnd.android.package-archive"},
            {".asf", "video/x-ms-asf"},
            {".avi", "video/x-msvideo"},
            {".bin", "application/octet-stream"},
            {".bmp", "image/bmp"},
            {".c", "text/plain"},
            {".class", "application/octet-stream"},
            {".conf", "text/plain"},
            {".cpp", "text/plain"},
            {".doc", "application/msword"},
            {".docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"},
            {".xls", "application/vnd.ms-excel"},
            {".xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"},
            {".exe", "application/octet-stream"},
            {".gif", "image/gif"},
            {".gtar", "application/x-gtar"},
            {".gz", "application/x-gzip"},
            {".h", "text/plain"},
            {".htm", "text/html"},
            {".html", "text/html"},
            {".jar", "application/java-archive"},
            {".java", "text/plain"},
            {".jpeg", "image/jpeg"},
            {".jpg", "image/jpeg"},
            {".js", "application/x-javascript"},
            {".log", "text/plain"},
            {".m3u", "audio/x-mpegurl"},
            {".m4a", "audio/mp4a-latm"},
            {".m4b", "audio/mp4a-latm"},
            {".m4p", "audio/mp4a-latm"},
            {".m4u", "video/vnd.mpegurl"},
            {".m4v", "video/x-m4v"},
            {".mov", "video/quicktime"},
            {".mp2", "audio/x-mpeg"},
            {".mp3", "audio/x-mpeg"},
            {".mp4", "video/mp4"},
            {".mpc", "application/vnd.mpohun.certificate"},
            {".mpe", "video/mpeg"},
            {".mpeg", "video/mpeg"},
            {".mpg", "video/mpeg"},
            {".mpg4", "video/mp4"},
            {".mpga", "audio/mpeg"},
            {".msg", "application/vnd.ms-outlook"},
            {".ogg", "audio/ogg"},
            {".pdf", "application/pdf"},
            {".png", "image/png"},
            {".pps", "application/vnd.ms-powerpoint"},
            {".ppt", "application/vnd.ms-powerpoint"},
            {".pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation"},
            {".prop", "text/plain"},
            {".rc", "text/plain"},
            {".rmvb", "audio/x-pn-realaudio"},
            {".rtf", "application/rtf"},
            {".sh", "text/plain"},
            {".tar", "application/x-tar"},
            {".tgz", "application/x-compressed"},
            {".txt", "text/plain"},
            {".wav", "audio/x-wav"},
            {".wma", "audio/x-ms-wma"},
            {".wmv", "audio/x-ms-wmv"},
            {".wps", "application/vnd.ms-works"},
            {".xml", "text/plain"},
            {".z", "application/x-compress"},
            {".zip", "application/x-zip-compressed"},

    };

    public String findType(String fileName) {
        for (String[] is : MIME_MapTable) {
            if (is[0].substring(1).indexOf(fileName) != -1 || is[0].indexOf(fileName) != -1) {
                return is[1];
            }
        }
        return "text/html";

    }

    public static void getPictureResource(String filePath) {
        URL url = null;
        byte[] content;
        //将InputStream转换成Reader的套路:使用装饰器类InputSreamrReader
        BufferedInputStream in = null;
        InputStream inputStream = null;

        try {
            url = new URL("http://guolin.tech/api/bing_pic");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            inputStream = connection.getInputStream();
            content = new byte[in.available()];
            inputStream.read(content);


            downloadPictrue(new String(content), filePath);


        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (inputStream != null) try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void downloadPictrue(String strUrl, String filePath) {
        final int MAX_BUFFER_SIZE = 1024;

        //1.打开http连接,获得下载内容的长度
        //2.创建RandomAccessFile对象
        //3.将下载内容缓存到字节数组
        //4.将缓存字节数组通过RandomAccessFile对象写入到文件(涉及到文件指针的操作)
        HttpURLConnection connection = null;

        BufferedInputStream in = null;
        try {
            URL url = new URL(strUrl);
            connection = (HttpURLConnection) url.openConnection();

            /**
             * 设置连接属性 - Range指从服务器下载文件的字节数范围, 0- 表示不终止字节数
             */
            connection.setRequestProperty("Range", "bytes=0-");

            //判断连接是否成功
            /**
             * 1xx:指示消息,表示请求已接收,继续操作
             * 2xx:成功,表示请求已被接受,操作
             * 3xx:重定向,要求完成请求必须进行进一步操作
             * 4xx:客户端错误,请求有语法错误或请求无法实现
             * 5xx:服务器错误,服务器未能实现合法请求
             */
            switch (connection.getResponseCode() / 100) {
                case 1:
                    System.out.println("指示消息,表示请求已接收,继续操作");
                    break;
                case 2:
                    System.out.println("成功,表示请求已被接受,操作");
                    break;
                case 3:
                    System.out.println("重定向,要求完成请求必须进行进一步操作");
                    break;
                case 4:
                    System.out.println("客户端错误,请求有语法错误或请求无法实现");
                    break;
                case 5:
                    System.out.println("服务器错误,服务器未能实现合法请求");
                    break;
            }
            if (connection.getResponseCode() / 100 != 2) {
                System.err.println("连接响应不在200范围内连接错误,请重试!");
                return;
            }

            int fileSize = connection.getContentLength();//获得下载长度(单位字节)
            in = new BufferedInputStream(connection.getInputStream(), MAX_BUFFER_SIZE);
            int downloaded = 0;//已下载字节数 - 用来计算下载百分比
            //String fileName = url.getFile().substring(url.getFile().lastIndexOf("/") + 1);
            RandomAccessFile file = new RandomAccessFile(filePath + "/" + "mipmap.png", "rw");
            file.setLength(0);
            file.seek(0);
            byte[] buffer = null;
            while (downloaded < fileSize) {

                //Object obj = System.console();


                //判断未下载的大小是否超过最大缓存数
                if (fileSize - downloaded >= MAX_BUFFER_SIZE) {
                    buffer = new byte[MAX_BUFFER_SIZE];
                } else {
                    buffer = new byte[fileSize - downloaded];
                }
                long startTime = System.currentTimeMillis();
                int read = in.read(buffer);
                if (read == -1) break;
                file.seek(downloaded);
                //file.write(buffer,0,read);
                file.write(buffer);
                long endTime = System.currentTimeMillis() - startTime;
                downloaded += read;
                System.out.println();
                System.out.println("--------------");
                System.out.println("|当前下载进度:|");
                System.out.printf("|%.2f%%\n", ((float) downloaded / fileSize * 100));
                System.out.println("|当前下载速度:|");
                //System.out.printf("|%.2fkB/S\n",( (new BigDecimal(read + "").divide(new BigDecimal(Math.pow(1024, 2) + "")) ).divide(new BigDecimal(endTime + "").divide(new BigDecimal(1000 + "")))).floatValue());
                System.out.printf("|%.2fkB/MS\n", read / (float) Math.pow(1024, 1) / ((float) endTime));
                System.out.println("--------------");

            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null)
                    in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            connection.disconnect();
        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        isActivityRun = true;
    }

    public void start(View v){
        fileNameArray = new File(getDownloadPath()).list();
        handler.sendEmptyMessage(0);
    }
}
