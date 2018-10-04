package com.example.administrator.fileexplorer;

import android.Manifest;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {
    private TextView textView;//显示路径
    private File[] fileArray;//当前目录中的文件数组
    private File currFile = null;//当前文件
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (textView != null) textView.setText((String) msg.obj);
        }
    };
    private byte[] copyDate = null;
    private File copyFile;//复制文件

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED && ContextCompat.checkSelfPermission(this, Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS}, 0);
            return;
        }
        try {
            textView = (TextView) findViewById(R.id.textViewPath);
            initFileListView();


        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void getCurrentStoragePath() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                Message message = handler.obtainMessage();
                message.obj = currFile.getAbsolutePath();
                handler.sendMessage(message);
            }
        }.start();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[grantResults.length - 1] == PackageManager.PERMISSION_GRANTED) {
            initFileListView();
        } else {
            finish();
        }
    }

    private void initFileListView() {
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
        currFile = file;
        fileArray = file.listFiles();
        final ListView listView = (ListView) findViewById(R.id.fileListView);
        listView.setAdapter(new FileAdapter(this, R.layout.file_item, fileArray));
        listView.setOnHoverListener(new View.OnHoverListener() {
            @Override
            public boolean onHover(View v, MotionEvent event) {
                Toast.makeText(MainActivity.this, "onHover", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final File file = fileArray[position];
                final View contentView = LayoutInflater.from(MainActivity.this).inflate(R.layout.function_item, null);
                final PopupWindow popupWindow = new PopupWindow(contentView, ActionBar.LayoutParams.MATCH_PARENT, 1700, true);
                //popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.white_background));
                popupWindow.showAtLocation(LayoutInflater.from(MainActivity.this).inflate(R.layout.activity_main, null), Gravity.BOTTOM, 0, 0);
                TextView textView = contentView.findViewById(R.id.textViewSelectFile);
                textView.setText("Select File: " + file.getName());
                Button button = contentView.findViewById(R.id.buttonDismiss);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });
                button = contentView.findViewById(R.id.buttonRename);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                        final View contentView = LayoutInflater.from(MainActivity.this).inflate(R.layout.newfile_window, null);
                        final PopupWindow popupWindow = new PopupWindow(contentView, ActionBar.LayoutParams.MATCH_PARENT, 700, true);

                        TextView textView = (TextView) contentView.findViewById(R.id.textView);
                        textView.setText("Rename");
                        EditText editText = (EditText) contentView.findViewById(R.id.editTextNewFileName);
                        editText.setText(file.getName());
                        Button button = contentView.findViewById(R.id.buttonGranted);
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                EditText editText = (EditText) contentView.findViewById(R.id.editTextNewFileName);
                                editText.setText(file.getName());
                                editText.setSelection(editText.getText().toString().length());
                                file.setExecutable(true);
                                file.setWritable(true);
                                if ((!editText.equals("")) && file.renameTo(new File(currFile, editText.getText().toString())) && new File(currFile, editText.getText().toString()).exists()) {
                                    Toast.makeText(MainActivity.this, "Reaname succeed", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(MainActivity.this, "Reaname failed", Toast.LENGTH_SHORT).show();
                                }
                                listViewUpdate();
                                popupWindow.dismiss();
                            }

                        });

                        button = contentView.findViewById(R.id.buttonDismiss);
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                popupWindow.dismiss();
                            }
                        });
                        popupWindow.showAtLocation(LayoutInflater.from(MainActivity.this).inflate(R.layout.activity_main, null), Gravity.BOTTOM, 0, 0);

                    }
                });
                button = contentView.findViewById(R.id.buttonDelete);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog dialog;
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("Wranning");
                        builder.setMessage("Are you sure to delete " + file.getName() + " ?");
                        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                popupWindow.dismiss();
                            }
                        });
                        builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                file.setExecutable(true, false);
                                file.setReadable(true, false);
                                file.setWritable(true, false);
                                if (file.delete())
                                    Toast.makeText(MainActivity.this, "delete file succeed", Toast.LENGTH_SHORT).show();
                                else {
                                    Toast.makeText(MainActivity.this, "delete file failed", Toast.LENGTH_SHORT).show();
                                }
                                fileArray = currFile.listFiles();
                                listViewUpdate();
                                popupWindow.dismiss();

                            }
                        });
                        dialog = builder.show();

                    }
                });
                button = contentView.findViewById(R.id.buttonCopy);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog dialog;
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("Wranning");
                        builder.setMessage("Are you sure to copy " + file.getName() + " ?");
                        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                popupWindow.dismiss();
                            }
                        });
                        builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    copyFile(file);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                                fileArray = currFile.listFiles();
                                listViewUpdate();
                                popupWindow.dismiss();

                            }
                        });
                        dialog = builder.show();

                    }
                });
                button = contentView.findViewById(R.id.buttonInfo);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("File Info");
//                        int pointIndex = -1;
//                        ((file.length() / Math.pow(1024, 2) + "").indexOf(".") != -1 ? (file.length() / Math.pow(1024, 2) + "").substring(0, pointIndex + 2) +
//                                "MB" : (file.length() / Math.pow(1024, 2) + "") + "MB")
                        builder.setMessage("file Name: " + file.getName() + "\nfile Size: " + String.format("%.2f", file.length() / (float) Math.pow(1024, 2)) + " MB\ncan write: " + file.canWrite() + "\ncan read: " + file.canRead());
                        builder.show();
                        popupWindow.dismiss();

                    }
                });
                button = contentView.findViewById(R.id.buttonCopyPath);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ClipboardManager clipboardManager = (ClipboardManager) MainActivity.this.getSystemService(Context.CLIPBOARD_SERVICE);
                        clipboardManager.setText(file.getAbsolutePath());
                        Toast.makeText(MainActivity.this, "copy path succeed", Toast.LENGTH_SHORT).show();
                        popupWindow.dismiss();

                    }
                });
                return true;
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                currFile = fileArray[position];
                if (currFile.isFile()) {
                    String type = findType(currFile.getAbsolutePath().substring(currFile.getAbsolutePath().lastIndexOf(".")));
                    if (type == null) return;

                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.fromFile(currFile), type);
                    startActivity(intent);
                    return;
                }

                if (currFile.listFiles() != null) {
                    fileArray = currFile.listFiles();
                    ListView listView = (ListView) findViewById(R.id.fileListView);
                    listView.setAdapter(new FileAdapter(MainActivity.this, R.layout.file_item, fileArray));
                    getCurrentStoragePath();
                } else {
                    ListView listView = (ListView) findViewById(R.id.fileListView);
                    listView.setAdapter(new FileAdapter(MainActivity.this, R.layout.file_item));
                }


            }
        });
        getCurrentStoragePath();
    }

    @Override
    public void onBackPressed() {

        if (Environment.getExternalStorageDirectory().getName().equals(currFile.getName())) {
            finish();
            return;
        }
//        Toast.makeText(this, fileArray[0].getParent(), Toast.LENGTH_LONG).show();
        ListView listView = (ListView) findViewById(R.id.fileListView);
        currFile = new File(currFile.getParent());
        listViewUpdate();
        getCurrentStoragePath();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.itemNew) {
            final View contentView = LayoutInflater.from(this).inflate(R.layout.newfile_window, null);
            final PopupWindow popupWindow = new PopupWindow(contentView, ActionBar.LayoutParams.MATCH_PARENT, 700, true);
            //popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.white_background));
            Button button = contentView.findViewById(R.id.buttonGranted);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditText editText = (EditText) contentView.findViewById(R.id.editTextNewFileName);
                    if (!editText.equals("")) {
                        File file = new File(currFile.getAbsolutePath() + "/" + editText.getText().toString());

                        if (file.mkdir())
                            Toast.makeText(MainActivity.this, "create " + editText.getText() + " directory succeed", Toast.LENGTH_SHORT).show();
                        popupWindow.dismiss();
                        fileArray = Arrays.copyOf(fileArray, fileArray.length + 1);
                        fileArray[fileArray.length - 1] = file;

                    }
                }

            });

            button = contentView.findViewById(R.id.buttonDismiss);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                }
            });
            popupWindow.showAtLocation(LayoutInflater.from(this).inflate(R.layout.activity_main, null), Gravity.BOTTOM, 0, 0);


        } else if (item.getItemId() == R.id.itemSortByName) {
            Arrays.sort(fileArray, (new Comparator<File>() {
                @Override
                public int compare(File o1, File o2) {
                    return o1.getName().compareTo(o2.getName());
                }
//                    if (o1.getName().charAt(0) == '.'|| o2.getName().charAt(0) == '.') return 1;
//                    if (o1.getName().charAt(0) > o2.getName().charAt(0)) return 1;
//                    else if (o1.getName().charAt(0) == o2.getName().charAt(0)){
//                        return 0;
//                    }
//                    else return -1;
//                }
            }));
        } else if (item.getItemId() == R.id.itemSortByTime) {
            Arrays.sort(fileArray, (new Comparator<File>() {
                @Override
                public int compare(File o1, File o2) {
                    if (o1.lastModified() > o2.lastModified()) return 1;
                    else if (o1.lastModified() == o2.lastModified()) return 0;
                    else return -1;
                }
            }));

        } else if (item.getItemId() == R.id.itemPaste) {
            try {
                pasteFile();
                fileArray = currFile.listFiles();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        listViewUpdate();
        return true;
    }

    private void listViewUpdate() {
        fileArray = currFile.listFiles();
        ListView listView = (ListView) findViewById(R.id.fileListView);
        listView.setAdapter(new FileAdapter(MainActivity.this, R.layout.file_item, fileArray));
        getCurrentStoragePath();
    }


    public static final String[][] MIME_MapTable = {
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

    public static String findType(String suffixName) {
        for (String[] is : MIME_MapTable) {
            if (is[0].substring(1).equals(suffixName) || is[0].equals(suffixName)) {
                return is[1];
            }
        }
        return null;

    }

    public void copyFile(File sourceFile) throws Exception {

        FileInputStream in = new FileInputStream(sourceFile);
        copyDate = new byte[in.available()];
        copyFile = sourceFile;
        copyFile.setReadable(true);

        in.read(copyDate, 0, copyDate.length);

        Toast.makeText(this, "copy File succeed", Toast.LENGTH_SHORT).show();


    }

    public void pasteFile() throws Exception {
        if (copyFile == null || copyDate == null) {
            Toast.makeText(this, "not found the copy file", Toast.LENGTH_SHORT).show();
            return;
        }
        FileOutputStream out = new FileOutputStream(currFile + "/" + copyFile.getName());
        currFile.setWritable(true);
        out.write(copyDate, 0, copyDate.length);
        Toast.makeText(this, "paste file succeed", Toast.LENGTH_SHORT).show();

    }


}
