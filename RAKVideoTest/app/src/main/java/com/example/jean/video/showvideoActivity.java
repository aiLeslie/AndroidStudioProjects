package com.example.jean.video;


import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.jean.rakvideotest.R;

public class showvideoActivity extends Activity
{   
	VideoView videoView;
	com.example.jean.component.MainMenuButton video_back;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.showvideo);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);//自动旋转
		videoView=(VideoView)findViewById(R.id.showvideo);
		//videoView.setOnClickListener(new Show_Video_Click());
		video_back=(com.example.jean.component.MainMenuButton)findViewById(R.id.video_back);
		video_back.setOnClickListener(new video_back_Click());
		Intent intent = getIntent();
		
		MediaController mc = new MediaController(this);		
		videoView.setMediaController(mc);
		videoView.setVideoPath(VideoPlay.videofile_path+"/"+intent.getStringExtra("videodata")+".mp4");
		videoView.requestFocus();
	    videoView.start();			
	}

	/*********************************************************************************************************
	** 功能说明：返回上一个界面
	** 传入参数：无
	** 得到参数：无      
	*********************************************************************************************************/		
	 class video_back_Click implements OnClickListener
	 {

		@Override
		public void onClick(View arg0)
		{
			showvideoActivity.this.finish();
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
