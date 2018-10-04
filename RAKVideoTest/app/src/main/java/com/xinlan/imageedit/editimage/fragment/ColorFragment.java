package com.xinlan.imageedit.editimage.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.example.jean.rakvideotest.R;
import com.xinlan.imageedit.editimage.BaseActivity;
import com.xinlan.imageedit.editimage.EditImageActivity;
import com.xinlan.imageedit.editimage.filter.PhotoProcessing;
import com.xinlan.imageedit.editimage.ui.imagezoom.ImageViewTouchBase.DisplayType;
import com.xinlan.imageedit.editimage.view.RotateImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 调色
 */
@SuppressLint("NewApi")
public class ColorFragment extends Fragment
{

	/**
	 * 饱和度
	 */
	private TextView mSaturation;
	private SeekBar mSaturationBar;

	/**
	 * 色调
	 */
	private TextView mHue;
	private SeekBar mHueBar;

	/**
	 * 亮度
	 */
	private TextView mLum;
	private SeekBar mLumBar;

	private float mDensity;
	private static final int TEXT_WIDTH = 50;

	private LinearLayout mParent;

	private ColorMatrix mLightnessMatrix;
	private ColorMatrix mSaturationMatrix;
	private ColorMatrix mHueMatrix;
	private ColorMatrix mAllMatrix;

	/**
	 * 亮度
	 */
	private float mLightnessValue = 1F;

	/**
	 * 饱和度
	 */
	private float mSaturationValue = 0F;

	/**
	 * 色相
	 */
	private float mHueValue = 0F;
	private final int MIDDLE_VALUE = 127;

	public static final String TAG = ColorFragment.class.getName();
	private Bitmap srcBitmap;
	public SeekBar baoheduSeekBar;
	public SeekBar sexiangSeekBar;
	public SeekBar liangduSeekBar;
	private View mainView;
	private View backBtn;
	private EditImageActivity activity;
	private Bitmap currentBitmap;

	public static ColorFragment newInstance(EditImageActivity activity)
	{
		ColorFragment fragment = new ColorFragment();
		fragment.activity = activity;
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		srcBitmap = Bitmap.createBitmap(activity.mainBitmap.copy(Bitmap.Config.ARGB_8888, true));
		mainView = inflater.inflate(R.layout.fragment_edit_image_color, null);
		backBtn = mainView.findViewById(R.id.back_to_main);
		baoheduSeekBar=(SeekBar)mainView.findViewById(R.id.baohe_bar);
		baoheduSeekBar.setOnSeekBarChangeListener(baoheduSeekBar_Change);
		sexiangSeekBar=(SeekBar)mainView.findViewById(R.id.sexiang_bar);
		sexiangSeekBar.setOnSeekBarChangeListener(sexiangSeekBar_Change);
		liangduSeekBar=(SeekBar)mainView.findViewById(R.id.liangdu_bar);
		liangduSeekBar.setOnSeekBarChangeListener(liangduSeekBar_Change);
		baoheduSeekBar.setProgress(MIDDLE_VALUE);
		liangduSeekBar.setProgress(MIDDLE_VALUE);
		sexiangSeekBar.setProgress(MIDDLE_VALUE);

		handleImage(srcBitmap,0);
		handleImage(srcBitmap,1);
		handleImage(srcBitmap,2);

		return mainView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		backBtn.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				backToMain();
			}
		});
	}

	/**
	 * 返回
	 */
	public void backToMain()
	{
		currentBitmap = activity.mainBitmap;
		srcBitmap = null;
		activity.mainImage.setImageBitmap(activity.mainBitmap);
		activity.mode = EditImageActivity.MODE_NONE;
		activity.bottomGallery.setCurrentItem(0);
		activity.mainImage.setScaleEnabled(true);
		activity.bannerFlipper.showPrevious();
	}

	/**
	 * 调节饱和度
	 */
	OnSeekBarChangeListener baoheduSeekBar_Change=new OnSeekBarChangeListener() {
		@Override
		public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
			setSaturation(progress);
			handleImage(srcBitmap, 1);
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {

		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {

		}
	};

	/**
	 * 调节色相
	 */
	OnSeekBarChangeListener sexiangSeekBar_Change=new OnSeekBarChangeListener() {
		@Override
		public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
			setHue(progress);
			handleImage(srcBitmap, 0);
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {

		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {

		}
	};

	/**
	 * 调节亮度
	 */
	OnSeekBarChangeListener liangduSeekBar_Change=new OnSeekBarChangeListener() {
		@Override
		public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
			setLum(progress);
			handleImage(srcBitmap, 2);
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {

		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {

		}
	};

	/**
	 * 保存
	 */
	public void saveColorImage()
	{
		if (currentBitmap == activity.mainBitmap)
		{
			backToMain();
			return;
		} else
		{
			SaveImageTask saveTask = new SaveImageTask();
			saveTask.execute(currentBitmap);
		}// end if
	}

	/**
	 * 保存图片
	 */
	private final class SaveImageTask extends AsyncTask<Bitmap, Void, Boolean>
	{
		private Dialog dialog;

		@Override
		protected Boolean doInBackground(Bitmap... params)
		{
			return saveBitmap(params[0], activity.saveFilePath);
		}

		@Override
		protected void onCancelled()
		{
			super.onCancelled();
			dialog.dismiss();
		}

		@Override
		protected void onCancelled(Boolean result)
		{
			super.onCancelled(result);
			dialog.dismiss();
		}

		@Override
		protected void onPostExecute(Boolean result)
		{
			super.onPostExecute(result);
			dialog.dismiss();
			if (result)
			{
				if (activity.mainBitmap != null && !activity.mainBitmap.isRecycled())
				{
					activity.mainBitmap.recycle();
				}
				activity.mainBitmap = currentBitmap;
				backToMain();
			}// end if
		}

		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();
			dialog = EditImageActivity.getLoadingDialog(getActivity(), getString(R.string.edit_photo_processing), false);
			dialog.show();
		}
	}// end inner class

	/**
	 * 保存Bitmap
	 */
	public static boolean saveBitmap(Bitmap bm, String filePath)
	{
		File f = new File(filePath);
		if (f.exists())
		{
			f.delete();
		}
		try
		{
			FileOutputStream out = new FileOutputStream(f);
			bm.compress(Bitmap.CompressFormat.PNG, 90, out);
			out.flush();
			out.close();
			return true;
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
			return false;
		} catch (IOException e)
		{
			e.printStackTrace();
			return false;
		}
	}


	@Override
	public void onDestroy()
	{
		if (srcBitmap != null && (!srcBitmap.isRecycled()))
		{
			srcBitmap.recycle();
		}
		super.onDestroy();
	}


	public void setSaturation(int saturation)
	{
		mSaturationValue = (float) (saturation * 1.0D / MIDDLE_VALUE);
	}

	public void setHue(int hue)
	{
		mHueValue = (float) (hue * 1.0D / MIDDLE_VALUE);
	}

	public void setLum(int lum)
	{
		mLightnessValue = (float) ((lum - MIDDLE_VALUE) * 1.0D / MIDDLE_VALUE * 180);
	}

	/**
	 *	比特位0 表示是否改变色相，比位1表示是否改变饱和度,比特位2表示是否改变明亮度
	 */
	public Bitmap handleImage(Bitmap bm, int flag)
	{
		Bitmap bmp = Bitmap.createBitmap(bm.getWidth(), bm.getHeight(), Bitmap.Config.ARGB_8888);
		// 创建一个相同尺寸的可变的位图区,用于绘制调色后的图片
		Canvas canvas = new Canvas(bmp); // 得到画笔对象
		Paint paint = new Paint(); // 新建paint
		paint.setAntiAlias(true); // 设置抗锯齿,也即是边缘做平滑处理
		if (null == mAllMatrix)
		{
			mAllMatrix = new ColorMatrix();
		}

		if (null == mLightnessMatrix)
		{
			mLightnessMatrix = new ColorMatrix(); // 用于颜色变换的矩阵，android位图颜色变化处理主要是靠该对象完成
		}

		if (null == mSaturationMatrix)
		{
			mSaturationMatrix = new ColorMatrix();
		}

		if (null == mHueMatrix)
		{
			mHueMatrix = new ColorMatrix();
		}

		switch (flag)
		{
			case 0: // 需要改变色相
				// f 表示亮度比例，取值小于1，表示亮度减弱，否则亮度增强
				mHueMatrix.reset();
				mHueMatrix.setScale(mHueValue, mHueValue, mHueValue, 1); // 红、绿、蓝三分量按相同的比例,最后一个参数1表示透明度不做变化，此函数详细说明参考
				// // android
				// doc
				Log.d("may", "改变色相");
				break;
			case 1: // 需要改变饱和度
				// saturation 饱和度值，最小可设为0，此时对应的是灰度图(也就是俗话的“黑白图”)，
				// 为1表示饱和度不变，设置大于1，就显示过饱和
				mSaturationMatrix.reset();
				mSaturationMatrix.setSaturation(mSaturationValue);
				Log.d("may", "改变饱和度");
				break;
			case 2: // 亮度
				// hueColor就是色轮旋转的角度,正值表示顺时针旋转，负值表示逆时针旋转
				mLightnessMatrix.reset(); // 设为默认值
				mLightnessMatrix.setRotate(0, mLightnessValue); // 控制让红色区在色轮上旋转hueColor葛角度
				mLightnessMatrix.setRotate(1, mLightnessValue); // 控制让绿红色区在色轮上旋转hueColor葛角度
				mLightnessMatrix.setRotate(2, mLightnessValue); // 控制让蓝色区在色轮上旋转hueColor葛角度
				// 这里相当于改变的是全图的色相
				Log.d("may", "改变亮度");
				break;
		}
		mAllMatrix.reset();
		mAllMatrix.postConcat(mHueMatrix);
		mAllMatrix.postConcat(mSaturationMatrix); // 效果叠加
		mAllMatrix.postConcat(mLightnessMatrix); // 效果叠加

		paint.setColorFilter(new ColorMatrixColorFilter(mAllMatrix));// 设置颜色变换效果
		canvas.drawBitmap(bm, 0, 0, paint); // 将颜色变化后的图片输出到新创建的位图区
		// 返回新的位图，也即调色处理后的图片
		activity.mainImage.setImageBitmap(bmp);
		currentBitmap = bmp;
		return bmp;
	}

	public Bitmap getCurrentBitmap()
	{
		return currentBitmap;
	}

	public void setCurrentBitmap(Bitmap currentBitmap)
	{
		this.currentBitmap = currentBitmap;
	}
}// end class
