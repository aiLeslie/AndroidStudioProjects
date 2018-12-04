package com.example.administrator.bluetoothtest.handle;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.example.administrator.bluetoothtest.activities.checkup.view.CoordinateActivity;
import com.example.administrator.bluetoothtest.mysocket.Task.Task;
import com.example.administrator.bluetoothtest.mysocket.Task.TaskHandler;


import java.lang.ref.SoftReference;

public class CheckUpHandler extends AbstractHandler {

    private AlertDialog dialog;
    private boolean isAlern = true;
    private String aMsg;

    public CheckUpHandler(CoordinateActivity activity) {
        super(activity);
    }

    public void setActivitySR(CoordinateActivity activity) {
       super.setActivitySR(activity);
    }

    @Override
    public void handleTask(Task task) {
        switch (task.getId()) {
            case 1:
                // 环境温度数据处理
                activitySR.get().tempCoordView.update(task.getImportanceData());
                activitySR.get().showTemp.setText(task.getImportanceData()[0] + "");
                break;

            case 2:
                // 身体温度数据处理
                activitySR.get().bodyTCoordView.update(task.getImportanceData());
                activitySR.get().showBodyT.setText(task.getImportanceData()[0] + "");
                aMsg = checkBodyT(task.getImportanceData());
                alert(aMsg);
                break;

            case 3:
                // 血氧数据处理
                activitySR.get().boCoordView.update(task.getImportanceData());
                activitySR.get().showBO.setText(task.getImportanceData()[0] + "");
                aMsg = checkBO(task.getImportanceData());
                alert(aMsg);
                break;

            case 4:
                // 心率数据处理
                activitySR.get().heartCoordView.update(task.getImportanceData());
                activitySR.get().showHeart.setText(task.getImportanceData()[0] + "");
                aMsg = checkHeart(task.getImportanceData());
                alert(aMsg);
                break;
            default:
                final StringBuffer buffer = new StringBuffer();
                for (int i : task.getImportanceData()) {
                    buffer.append(i + "");
                }
                activitySR.get().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(activitySR.get(), buffer.toString(), Toast.LENGTH_SHORT).show();

                    }
                });

                break;
        }
    }


    public synchronized CheckUpHandler alert(final String msg) {
        // 关闭警告
        if (!isAlern || msg == null) {
            return this;
        }

        activitySR.get().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialog = new AlertDialog.Builder(activitySR.get()).setTitle("warning").setMessage(msg).setNeutralButton("不再提示", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        isAlern = false;
                        dialog.dismiss();
                    }
                }).setPositiveButton("我知道", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();


            }
        });
        return this;
    }

    private String checkBodyT(int[] data) {
        String conten = null;
        int i = data[0];
//        for (int i : data) {

            if (i <= 35) { // 体温过低
                conten = "经过检查,您的体温" + i + "°C,体温是明显的偏低,与自身的体质差、保暖措施不当有关，需要积极的调理改善。首先需要多穿衣物、其次是需要多喝温开水，多行运动增强自身的体质!";
//                break;

            }  else if (i >= 38){ // 体温过高
                conten = "经过检查,您的体温" + i + "°C,属于体温过高,是发烧症状,吃退烧片,多喝开水,如果症状仍没消减,请前往医院看症!";
//                break;
            }
//        }

        return conten;
    }


    private String checkBO(int[] data) {
        String conten = null;
        int i = data[0];
//        for (int i : data) {

            if (i <= 90) { // 体温过低
                conten = "经过检查,您的血氧浓度" + i + ",血氧偏低!";
//                break;

            } else if (i >= 100) { // 体温稍高
                conten = "经过检查,您的血氧浓度" + i + ",血氧偏高!";
//                break;
            }
//        }

        return conten;
    }

    private String checkHeart(int[] data) {
        String conten = null;
        int i = data[0];
//        for (int i : data) {

            if (i <= 55) { // 体温过低
                conten = "经过检查,您的心率" + i + ",心率偏低!";
//                break;

            } else if (i >= 100) { // 体温稍高
                conten = "经过检查,您的心率" + i + ",心率偏高!";
//                break;
            }
//        }

        return conten;
    }


//    private class ChangeAlearnSwitch extends Thread {
//        @Override
//        public void run() {
//            super.run();
//            isAlern = false;
//            try {
//                sleep(5000L);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            isAlern = true;
//        }
//    }

}
