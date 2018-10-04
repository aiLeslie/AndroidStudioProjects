package com.example.administrator.webviewtest;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

/**
 * Created by Administrator on 2018/1/19.
 */

public class DownloadTask extends AsyncTask<Void,Integer,Boolean> {

    private AlertDialog.Builder builder;
    private AlertDialog dialog  ;
    private Context context;

    public DownloadTask(Context context) {
        this.context = context;
        this.builder = new ProgressDialog.Builder(context);
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        long i = 0;
        for(i = 0; i < 100000 ;i++){
            publishProgress((int)i);
        };
        if(i == 100000)return true;
        else return false;
    }

    @Override
    protected void onPreExecute() {
        builder.setTitle("download progress");
        builder.setMessage("Download" + 0 + "%");
        dialog = builder.show();

    }





    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        dialog.setMessage("Download" + values[values.length - 1] / 100000 * 100 + "%");
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        dialog.dismiss();
        if(aBoolean){
            Toast.makeText(context,"download succeeded",Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(context,"download failed",Toast.LENGTH_LONG).show();
        }

    }
}
