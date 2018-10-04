package com.example.jean.video;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.jean.rakvideotest.R;

import java.util.Locale;

public class SplashScreen extends Activity {
	/**
	 * The thread to process splash screen events
	 */
	private Thread mSplashThread;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.splash);
		// Start animating the image
		final ImageView splashImageView = (ImageView) findViewById(R.id.SplashImageView);
		splashImageView.setBackgroundResource(R.drawable.splash);
		final AnimationDrawable frameAnimation = (AnimationDrawable)splashImageView.getBackground();
		splashImageView.post(new Runnable() {
			@Override
			public void run() {
				frameAnimation.start();
			}
		});

		String sta=getResources().getConfiguration().locale.getCountry().toLowerCase();
		Log.e("sta=>",sta);
		if(sta.equals("cn"))
			sta="cn";
		else
			sta="en";
		Locale myLocale = new Locale(sta);
		Resources res = getResources();
		DisplayMetrics dm = res.getDisplayMetrics();
		Configuration conf = res.getConfiguration();
		conf.locale = myLocale;
		res.updateConfiguration(conf, dm);

		mSplashThread =  new Thread(){
			@Override
			public void run(){
				try {
					synchronized(this){
						// Wait given period of time or exit on touch
						wait(3000);
					}
				}
				catch(InterruptedException ex){
				}

				finish();
				{
					// Run next activity
					Intent intent = new Intent();
					intent.setClass(SplashScreen.this, MainActivity.class);
					startActivity(intent);
				}
			}
		};

		mSplashThread.start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		super.onCreateOptionsMenu(menu);
		return false;
	}

	/**
	 * Processes splash screen touch events
	 */
	@Override
	public boolean onTouchEvent(MotionEvent evt)
	{
		if(evt.getAction() == MotionEvent.ACTION_DOWN)
		{
			synchronized(mSplashThread){
				mSplashThread.notifyAll();
			}
		}
		return true;
	}


}
