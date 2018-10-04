package com.example.administrator.asynctasktest;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class PictureResource {


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


}
