package com.example.jean.video;


import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.jean.rakvideotest.R;

public class showphotoActivity extends Activity
{   
	ImageView show;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.showphoto);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);//自动旋转
		show=(ImageView)findViewById(R.id.showphoto);
		show.setOnClickListener(new Show_Photo_Click());
		Intent intent = getIntent();
		Bitmap bitmap = BitmapFactory.decodeFile(VideoPlay.photofile_path+"/"+intent.getStringExtra("photodata")+".jpg");
		show.setImageBitmap(bitmap);
	}

	/*********************************************************************************************************
	** 功能说明：返回上一个界面
	** 传入参数：无
	** 得到参数：无      
	*********************************************************************************************************/		
	 class Show_Photo_Click implements OnClickListener
	 {
		@Override
		public void onClick(View arg0)
		{
			showphotoActivity.this.finish();
		}		 
	 }	
		 
	/*********************************************************************************************************
	 ** 功能说明：UI界面消息显示
	 ********************************************************************************************************/
	public void DisplayToast(String str)
	{
		Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
	}			 
	 
}
