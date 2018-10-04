package com.example.administrator.asynctasktest;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Administrator on 2018/1/23.
 */

public class MyAsyncTask extends AsyncTask<String, Integer, Boolean> {
    private URL url;
    private int fileSize;
    private String fileName;
    private String type;
    private ProgressBar bar;
    private TextView dispaly;
    private Context context;
    private static final int MAX_BUFFER_SIZE = 1024 * 10;//一个千字节capacity 1024byte


    public MyAsyncTask(Context context, ProgressBar bar) {
        this.bar = bar;
        this.context = context;
    }

    public MyAsyncTask(Context context, ProgressBar bar, TextView dispaly) {
        this.bar = bar;
        this.context = context;
        this.dispaly = dispaly;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Toast.makeText(context, "New Download Task", Toast.LENGTH_SHORT).show();
        bar.setVisibility(View.VISIBLE);
        dispaly.setVisibility(View.VISIBLE);

    }


    @Override
    protected Boolean doInBackground(String... strings) {


        HttpURLConnection connection = null;

        BufferedInputStream in = null;
        try {
            url = new URL(strings[0]);
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
                return false;
            }


            fileSize = connection.getContentLength();//获得下载长度(单位字节)
            in = new BufferedInputStream(connection.getInputStream(), MAX_BUFFER_SIZE);
            int downloaded = 0;//已下载字节数 - 用来计算下载百分比

            if (url.getFile().indexOf('/') != -1)
                fileName = url.getFile().substring(url.getFile().lastIndexOf("/") + 1);
            else {
                fileName = url.getFile();
            }
            if (fileName.length() > 50)
                fileName = fileName.substring(fileName.length() - 50, fileName.length());
            RandomAccessFile file = new RandomAccessFile(getdownloadPath() + "/" + fileName, "rw");
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
                System.out.printf("|%.2fkB/S\n",read * 1.0 / 1024  /((float)endTime / 1000));
                //System.out.println(read * 1.0 / 1024 /(endTime / 1000));
                System.out.println("--------------");
                publishProgress((int) ((double) downloaded / fileSize * 100));

            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (in != null) in.close();
                if (connection != null) connection.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }


        }
        type = connection.getFileNameMap().getContentTypeFor(getdownloadPath() + "/" + fileName);

        return true;


    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        bar.setProgress(values[0]);
        dispaly.setText("Download Progress: " + String.format("%.2fMB / %.2fMB", (float) fileSize / 1024 / 1024 * values[0] * 0.01, (float) fileSize / 1024 / 1024));
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        try {
            bar.setVisibility(View.GONE);
            dispaly.setVisibility(View.GONE);
            if (aBoolean) {
                Toast.makeText(context, "download succeed", Toast.LENGTH_SHORT).show();
                if (fileName.contains(".") && type == null) {
                    type = findType(fileName.substring(fileName.lastIndexOf('.')));
                }
                if (type == null) type = findType(url.getFile());

                String suffixName = findSuffixName(type);
                if (!fileName.contains(suffixName)) {
                    try {
                        File file = new File(getdownloadPath() + "/" + fileName);
                        fileName = fileName.substring(0, fileName.length() - suffixName.length()) + suffixName;
                        file.renameTo(new File(getdownloadPath() + "/" + fileName));


                    } catch (ArrayIndexOutOfBoundsException e) {
                        e.printStackTrace();
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                    Intent intent = new Intent(Intent.ACTION_VIEW);

                    intent.setDataAndType(Uri.fromFile(new File(getdownloadPath() + "/" + fileName)), type);
                    context.startActivity(intent);



            } else {
                Toast.makeText(context, "download failed", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    public String getdownloadPath() {
        return context.getExternalFilesDir("download file").getAbsolutePath();
    }

    public final String[][] MIME_MapTable = {
            //{后缀名，MIME类型}
            {".3gp", "video/3gpp"},
            {".apk", "application/vnd.android.package-archive"},
            {".asf", "video/x-ms-asf"},
            {".avi", "video/x-msvideo"},
            {".bin", "application/octet-stream"},
            {".bmp", "image/bmp"},
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
            {".mp4", "video/mp4"},
            {".mp3", "audio/x-mpeg"},
            {".mp2", "audio/x-mpeg"},
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
            {".rmvb", "audio/x-pn-realaudio"},
            {".rtf", "application/rtf"},
            {".tar", "application/x-tar"},
            {".tgz", "application/x-compressed"},
            {".txt", "text/plain"},
            {".wav", "audio/x-wav"},
            {".wma", "audio/x-ms-wma"},
            {".wmv", "audio/x-ms-wmv"},
            {".wps", "application/vnd.ms-works"},
            {".xml", "text/plain"},
            {".zip", "application/x-zip-compressed"},
            {".rc", "text/plain"},
            {".sh", "text/plain"},
            {".gz", "application/x-gzip"},
            {".c", "text/plain"},
            {".h", "text/plain"},
            {".z", "application/x-compress"},

    };

    public String findType(String fileName) {
        for (String[] is : MIME_MapTable) {
            if (fileName.indexOf(is[0].substring(1)) != -1) {
                return is[1];
            }
        }
        return null;

    }

    public String findSuffixName(String type) {
        for (String[] is : MIME_MapTable) {
            if (is[1].equals(type)) {
                return is[0];
            }
        }
        return null;

    }

}
