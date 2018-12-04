package com.example.administrator.bluetoothtest.picture;


import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Random;

public class PictureResource {
    private AppCompatActivity activity;
    private int layoutId;

    public PictureResource(AppCompatActivity activity, int layoutId) {
        this.activity = activity;
        this.layoutId = layoutId;
    }

    /**
     * 异步写日期到文件中
     */
    public void writeDateAsync() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                // 实例化写日期的文件
                File file = new File(activity.getExternalCacheDir() + "/date.txt");
                // 获得今天号数
                Calendar calendar = Calendar.getInstance();
                int date = calendar.get(Calendar.DATE);
                // 实例化随机访问流
                RandomAccessFile raf = null;
                try {


                    // 如果文件不存在就新建文件把今天日期写进去
                    if (!file.exists()) {
                        if (!file.createNewFile()) {
                            Toast.makeText(activity, "创建文件失败", Toast.LENGTH_SHORT).show();
                        }
                        raf = new RandomAccessFile(file, "rw");
                        raf.seek(0);
                        raf.setLength(0);
                        raf.writeInt(date);
                        writePictureResourceAsync(activity.getExternalFilesDir(null).getAbsolutePath());

                    } else {
                        raf = new RandomAccessFile(file, "rw");
                        raf.seek(0);
                        int previous = raf.readInt();
                        if (previous != date) {
                            raf.seek(0);
                            raf.setLength(0);
                            raf.write(date);
                            writePictureResourceAsync(activity.getExternalFilesDir(null).getAbsolutePath());
                        }
                    }
                } catch (EOFException e) {
                    e.printStackTrace();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (raf != null) raf.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();


    }

    /**
     * 获取目录里随机文件的绝对路径
     * @param parentPath
     * @return
     */
    public String getRandomPath(final String parentPath) {
        File file = new File(parentPath);
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files.length == 0) {
                return null;
            }
            Random random = new Random();
            int index = random.nextInt(files.length);
            if (files[index].isDirectory()) {
                return null;
            } else {
                return files[index].getAbsolutePath();
            }

        } else {
            throw new IllegalArgumentException("File Not Found !");
        }
    }


    // 开始动画
    public void openingAnimation() {
        openingAnimation(activity, layoutId);
    }

    /**
     * 开始动画
     *
     * @param activity
     * @param layoutId
     */
    public void openingAnimation(final AppCompatActivity activity, final int layoutId) {

        // 获取随机路径
        String path = getRandomPath(activity.getExternalFilesDir(null).getAbsolutePath());
        if (path == null) {
            writePictureResourceAsync(activity.getExternalFilesDir(null).getAbsolutePath());
            return;
        }
        // 写日期到文件中
        writeDateAsync();

        // 隐藏actionBar
        activity.getSupportActionBar().hide();
        // 获取布局对象
        LinearLayout layout = (LinearLayout) activity.findViewById(layoutId);
        // 实例化ImageView
        final ImageView animation = new ImageView(activity);


        animation.setImageBitmap(BitmapFactory.decodeFile(path));
//        animation.setImageResource(R.drawable.crystal);
        animation.setScaleType(ImageView.ScaleType.CENTER_CROP);
        // 布局中加入ImageView控件
        layout.addView(animation, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    sleep(1000 * 3);
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            /**
                             * 布局中删除ImageView控件
                             */
                            LinearLayout layout = (LinearLayout) activity.findViewById(layoutId);
                            layout.removeView(animation);
                            activity.getSupportActionBar().show();
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }

    // 开启下载图片资源子线程
    public static void writePictureResourceAsync(final String filePath) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                writePictureResource(filePath);
            }
        }.start();
    }

    /**
     * 获得图片资源url
     * 下载图片资源写进目标路径
     *
     * @param filePath
     */
    private static void writePictureResource(final String filePath) {
        URL url = null;
        try {
            url = new URL("http://guolin.tech/api/bing_pic");
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        }
        Reader reader = null;
        InputStreamReader isr = null;

        StringBuffer content = null;
        // 将InputStream转换成Reader的套路:使用装饰器类InputSreamrReader
        try {
            isr = new InputStreamReader(url.openStream());
            reader = new BufferedReader(isr);
            content = new StringBuffer();
            String line = null;

            while ((line = ((BufferedReader) reader).readLine()) != null) {
                content.append(line);
                System.out.println(line);
                content.append(System.getProperty("line.separator"));
            }
            downloadPictrue(content.toString(), filePath);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (isr != null) {
                try {
                    isr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    /**
     * 下载图片资源写进目标路径
     *
     * @param strUrl
     * @param filePath
     */
    private static void downloadPictrue(String strUrl, String filePath) {
        final int MAX_BUFFER_SIZE = 1024 * 8;

        // 1.打开http连接,获得下载内容的长度
        // 2.创建RandomAccessFile对象
        // 3.将下载内容缓存到字节数组
        // 4.将缓存字节数组通过RandomAccessFile对象写入到文件(涉及到文件指针的操作)
        HttpURLConnection connection = null;

        BufferedInputStream in = null;
        try {
            URL url = new URL(strUrl);
            connection = (HttpURLConnection) url.openConnection();

            /**
             * 设置连接属性 - Range指从服务器下载文件的字节数范围, 0- 表示不终止字节数
             */
            connection.setRequestProperty("Range", "bytes=0-");

            // 判断连接是否成功
            /**
             * 1xx:指示消息,表示请求已接收,继续操作 2xx:成功,表示请求已被接受,操作 3xx:重定向,要求完成请求必须进行进一步操作
             * 4xx:客户端错误,请求有语法错误或请求无法实现 5xx:服务器错误,服务器未能实现合法请求
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

            int fileSize = connection.getContentLength();// 获得下载长度(单位字节)
            in = new BufferedInputStream(connection.getInputStream(), MAX_BUFFER_SIZE);
            int downloaded = 0;// 已下载字节数 - 用来计算下载百分比
            String fileName = url.getFile().substring(url.getFile().lastIndexOf("/") + 1);
            @SuppressWarnings("resource")
            RandomAccessFile file = new RandomAccessFile(filePath + "/" + fileName, "rw");
            file.setLength(0);
            file.seek(0);
            byte[] buffer = null;
            while (downloaded < fileSize) {

                // 判断未下载的大小是否超过最大缓存数
                if (fileSize - downloaded >= MAX_BUFFER_SIZE) {
                    buffer = new byte[MAX_BUFFER_SIZE];
                } else {
                    buffer = new byte[fileSize - downloaded];
                }
                long startTime = System.currentTimeMillis();
                int read = in.read(buffer);
                if (read == -1)
                    break;
                file.seek(downloaded);
                // file.write(buffer,0,read);
                file.write(buffer);
                long endTime = System.currentTimeMillis() - startTime;
                downloaded += read;
                System.out.println();
                System.out.println("--------------");
                System.out.println("|当前下载进度:|");
                System.out.printf("|%.2f%%\n", ((float) downloaded / fileSize * 100));

                System.out.println("|当前下载速度:|");
                System.out.printf("|%.2fKB/S\n", (float) read * 1000 / Math.pow(1024, 1) / endTime);
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
