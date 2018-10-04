package com.example.jean.video;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

import com.example.jean.rakvideotest.R;

import java.util.List;

public class MyFloatView implements OnClickListener
{
	private float mTouchStartX;
	private float mTouchStartY;
	private float x;
	private float y;
	private ViewGroup mlayoutView;
	private Context context;
	private Display currentDisplay;
	private int _viewWidth = 300, _viewHeight = 300;

	private int WIDTH = 1024, HEIGHT = 552;
	private int left, top, right, bottom;
	private WindowManager wm = null;
	private LayoutParams wmParams = null;
	private float mLastTouchY = 0;
	int rangeOut_H = -1, rangeOut_V = -1;
	long firstTime=-1;
	long secondTime=-1;

	public MyFloatView(ViewGroup layoutView)
	{
		// TODO Auto-generated constructor stub
		mlayoutView = layoutView;
		context = mlayoutView.getContext();
		mlayoutView.setOnTouchListener(new OnTouchListener()
		{
			@Override
			public boolean onTouch(View paramView, MotionEvent paramMotionEvent)
			{
				// TODO Auto-generated method stub
				onTouchEvent(paramMotionEvent);
				return false;
			}
		});
		mlayoutView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.e("==>","onClick");
				if(firstTime==-1){
					firstTime=SystemClock.uptimeMillis();
				}
				else{
					if(secondTime==-1){
						secondTime=SystemClock.uptimeMillis();
					}
				}
				if ((firstTime!=-1)&&(secondTime!=-1)) {
					if (secondTime-firstTime < 500) {
						firstTime=-1;
						secondTime=-1;
						// 设置启动的程序，如果存在则找出，否则新的启动
						Intent intent = new Intent(Intent.ACTION_MAIN);
						intent.addCategory(Intent.CATEGORY_LAUNCHER);
						intent.setComponent(new ComponentName(VideoPlay._self, VideoPlay.class));//用ComponentName得到class对象
						intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
								| Intent.FLAG_ACTIVITY_NEW_TASK
								| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);// 关键的一步，设置启动模式，两种情况
						VideoPlay._self.startActivity(intent);
					} else {
						firstTime=secondTime;
						secondTime=-1;
					}
				}

				if(VideoPlay._floatVideoExit!=null){
					if(VideoPlay._floatVideoExit.getVisibility()==View.VISIBLE){
						VideoPlay._floatVideoExit.setVisibility(View.INVISIBLE);
					}
					else{
						VideoPlay._floatVideoExit.setVisibility(View.VISIBLE);
					}
				}
			}
		});
		initWindow();
	}

	public View getLayoutView()
	{
		return mlayoutView;
	}

	public void initWindow()
	{
		// 获取WindowManager
		wm = (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
		// 设置LayoutParams(全局变量）相关参数
		wmParams = new LayoutParams();
		/**
		 * 以下都是WindowManager.LayoutParams的相关属性 具体用途可参考SDK文档
		 */
		wmParams.type = /*LayoutParams.TYPE_SYSTEM_ALERT | */LayoutParams.TYPE_TOAST; // 设置window type
		// 设置图片格式，效果为背景透明
		//wmParams.format = PixelFormat.TRANSPARENT;
		// 设置Window flag
		wmParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL
				| LayoutParams.FLAG_NOT_FOCUSABLE
				| LayoutParams.FLAG_LAYOUT_NO_LIMITS;
		/*
		 * 下面的flags属性的效果形同“锁定”。 悬浮窗不可触摸，不接受任何事件,同时不影响后面的事件响应。
		 * wmParams.flags=LayoutParams.FLAG_NOT_TOUCH_MODAL |
		 * LayoutParams.FLAG_NOT_FOCUSABLE | LayoutParams.FLAG_NOT_TOUCHABLE;
		 */
		wmParams.gravity = Gravity.LEFT | Gravity.TOP; // 调整悬浮窗口至左上角
		// 以屏幕左上角为原点，设置x、y初始值
		currentDisplay = wm.getDefaultDisplay();
		WIDTH = currentDisplay.getWidth();
		HEIGHT = currentDisplay.getHeight();
		_viewWidth=WIDTH*2/5;


		if(VideoPlay._videoPipe.getText().toString().equals(VideoPlay._self.getString(R.string.video_BD))){
			adjustImage(320,240);
		}
		else if(VideoPlay._videoPipe.getText().toString().equals(VideoPlay._self.getString(R.string.video_HD))){
			adjustImage(1280,720);
		}
		else if(VideoPlay._videoPipe.getText().toString().equals(VideoPlay._self.getString(R.string.video_VHD))){
			adjustImage(1920,1280);
		}
		else{
			if(VideoPlay._deviceIp.equals("127.0.0.1")){
				adjustImage(320,240);
			}
			else{
				adjustImage(1280,720);
			}
		}

		wmParams.x = (WIDTH - _viewWidth);
		wmParams.y = (HEIGHT- _viewHeight);
		// 设置悬浮窗口长宽数据
		wmParams.width = _viewWidth;
		wmParams.height = _viewHeight;
		left =0;
		top = 0;
		right = WIDTH- _viewWidth / 2;
		bottom = HEIGHT - _viewWidth / 2;
	}

	public void bindViewListener()
	{
		initialUI();
	}

	private void initialUI()
	{
		if(VideoPlay._floatView!=null)
				mlayoutView.addView(VideoPlay._floatView);
	}

	public void onExit()
	{
		try
		{
			mlayoutView.removeView(VideoPlay._floatView);
			wm.removeView(mlayoutView);
		} catch (Exception e)
		{
			// TODO: handle exception
		}
	}

	public void showLayoutView()
	{
		wm.addView(mlayoutView, wmParams);
	}



	public boolean onTouchEvent(MotionEvent event)
	{
		// 获取相对屏幕的坐标，即以屏幕左上角为原点
		x = event.getRawX();
		y = event.getRawY(); // 25是系统状态栏的高度
		switch (event.getAction())
		{
		case MotionEvent.ACTION_DOWN:
			// 获取相对View的坐标，即以此View左上角为原点
			mTouchStartX = event.getX();
			mTouchStartY = event.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			if (mLastTouchY > 110)
			{
				mTouchStartX = event.getX();
				mTouchStartY = event.getY();
				mLastTouchY = 0;
			}
			int positionX = (int) (x - mTouchStartX);
			int positionY = (int) (y - mTouchStartY);

			if (positionX < left || positionX > right)
			{// check borad just
				// reset
				rangeOut_H = positionX < left ? left : right;
			}
			if (positionY < top || positionY > bottom)
			{// check borad just
				// reset
				rangeOut_V = positionY < top ? top : bottom;
			}

			updateViewPosition();
			break;

		case MotionEvent.ACTION_UP:

//			if (rangeOut_H != -1)
//			{
//				x = rangeOut_H + mTouchStartX;
//			}
//			if (rangeOut_V != -1)
//			{
//				y = rangeOut_V + mTouchStartY;
//			}
//
//			updateViewPosition();
//			mTouchStartX = mTouchStartY = 0;
//			rangeOut_H = rangeOut_V = -1;
			break;
		default:
			mTouchStartX = mTouchStartY = 0;
			rangeOut_H = rangeOut_V = -1;
			break;
		}
		return true;
	}

	private void updateViewPosition()
	{
		// 更新浮动窗口位置参数
		wmParams.x = (int) (x - mTouchStartX);
		wmParams.y = (int) (y - mTouchStartY);
		wm.updateViewLayout(mlayoutView, wmParams);
	}

	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub
	}

	/**
	 * Adjust Image
	 */
	void adjustImage(int imageWidth, int imageHeight) {
//		if (imageWidth == 0 || imageHeight == 0|| _viewWidth == 0|| _viewHeight == 0) {
//			return;
//		}
//
//		float viewRatio = (float)_viewHeight / _viewWidth;
//		float imageRatio = (float)imageHeight / imageWidth;
//		if ((viewRatio == imageRatio)) {
//		} else if (viewRatio < imageRatio) {
//			_viewWidth=_viewWidth-_viewHeight*imageWidth/imageHeight;
//		} else {
//			_viewHeight=_viewHeight-_viewWidth*imageHeight/imageWidth;
//		}
		_viewHeight=_viewWidth*imageHeight/imageWidth;
	}


}