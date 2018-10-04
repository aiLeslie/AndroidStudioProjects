package com.example.jean.video;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class FloatViewBroadCastReceiver extends BroadcastReceiver {

	public static final String ACTION_MOVIE_START = "com.example.jean.video.START";
	public static final String ACTION_MOVIE_STOP = "com.example.jean.video.STOP";

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if (action.equals(ACTION_MOVIE_START)) {
			Intent mIntent = new Intent("createUI");
			mIntent.setClass(context, FloatViewService.class);
			context.startService(mIntent);
		}
		else if (action.equals(ACTION_MOVIE_STOP)) {
			Intent mIntent = new Intent("removeUI");
			mIntent.setClass(context, FloatViewService.class);
			context.startService(mIntent);
		}
	}

}
