package com.xinlan.imageedit.editimage.fragment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.xinlan.imageedit.editimage.BaseActivity;
import com.xinlan.imageedit.editimage.EditImageActivity;
import com.xinlan.imageedit.editimage.ui.imagezoom.ImageViewTouchBase.DisplayType;
import com.xinlan.imageedit.editimage.view.RotateImageView;
import com.example.jean.rakvideotest.R;

/**
 * 旋转
 */
@SuppressLint("NewApi")
public class RotateFragment extends Fragment
{
	public static final String TAG = RotateFragment.class.getName();
	private View mainView;
	private EditImageActivity activity;
	private View backToMenu;
	public SeekBar mSeekBar;
	private RotateImageView mRotatePanel;

	public static RotateFragment newInstance(EditImageActivity activity)
	{
		RotateFragment fragment = new RotateFragment();
		fragment.activity = activity;
		fragment.mRotatePanel = activity.mRotatePanel;
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		mainView = inflater.inflate(R.layout.fragment_edit_image_rotate, null);
		backToMenu = mainView.findViewById(R.id.back_to_main);
		mSeekBar = (SeekBar) mainView.findViewById(R.id.rotate_bar);
		mSeekBar.setProgress(0);
		return mainView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		backToMenu.setOnClickListener(new BackToMenuClick());

		mSeekBar.setOnSeekBarChangeListener(new RotateAngleChange());
	}

	/**
	 * 旋转
	 */
	private final class RotateAngleChange implements OnSeekBarChangeListener
	{
		@Override
		public void onProgressChanged(SeekBar seekBar, int angle, boolean fromUser)
		{
			mRotatePanel.rotateImage(angle);
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar)
		{

		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar)
		{

		}
	}// end inner class

	/**
	 * 返回
	 */
	private final class BackToMenuClick implements OnClickListener
	{
		@Override
		public void onClick(View v)
		{
			backToMain();
		}
	}// end class

	/**
	 * 返回
	 */
	public void backToMain()
	{
		activity.mode = EditImageActivity.MODE_NONE;
		activity.bottomGallery.setCurrentItem(0);
		activity.mainImage.setVisibility(View.VISIBLE);
		this.mRotatePanel.setVisibility(View.GONE);
		activity.bannerFlipper.showPrevious();
	}

	/**
	 * 保存
	 */
	public void saveRotateImage()
	{
		if (mSeekBar.getProgress() == 0 || mSeekBar.getProgress() == 360)
		{
			backToMain();
			return;
		} else
		{
			SaveRotateImageTask task = new SaveRotateImageTask();
			task.execute(activity.mainBitmap);
		}// end if
	}

	/**
	 * 保存线程
	 */
	private final class SaveRotateImageTask extends AsyncTask<Bitmap, Void, Bitmap>
	{
		private Dialog dialog;

		@Override
		protected void onCancelled()
		{
			super.onCancelled();
			dialog.dismiss();
		}

		@Override
		protected void onCancelled(Bitmap result)
		{
			super.onCancelled(result);
			dialog.dismiss();
		}

		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();
			dialog = BaseActivity.getLoadingDialog(getActivity(), getString(R.string.edit_photo_processing), false);
			dialog.show();
		}

		@Override
		protected Bitmap doInBackground(Bitmap... params)
		{
			RectF imageRect = mRotatePanel.getImageNewRect();
			Bitmap originBit = params[0];
			Bitmap result = Bitmap.createBitmap((int) imageRect.width(), (int) imageRect.height(), Bitmap.Config.ARGB_4444);
			Canvas canvas = new Canvas(result);
			int w = originBit.getWidth() >> 1;
			int h = originBit.getHeight() >> 1;
			float centerX = imageRect.width() / 2;
			float centerY = imageRect.height() / 2;

			float left = centerX - w;
			float top = centerY - h;

			RectF dst = new RectF(left, top, left + originBit.getWidth(), top + originBit.getHeight());
			canvas.save();
			canvas.scale(mRotatePanel.getScale(), mRotatePanel.getScale(), imageRect.width() / 2, imageRect.height() / 2);
			canvas.rotate(mRotatePanel.getRotateAngle(), imageRect.width() / 2, imageRect.height() / 2);

			canvas.drawBitmap(originBit, new Rect(0, 0, originBit.getWidth(), originBit.getHeight()), dst, null);
			canvas.restore();

			saveBitmap(result, activity.saveFilePath);
			return result;
		}

		@Override
		protected void onPostExecute(Bitmap result)
		{
			super.onPostExecute(result);
			dialog.dismiss();
			if (result == null)
				return;
			if (activity.mainBitmap != null && !activity.mainBitmap.isRecycled())
			{
				activity.mainBitmap.recycle();
			}
			activity.mainBitmap = result;
			activity.mainImage.setImageBitmap(activity.mainBitmap);
			activity.mainImage.setDisplayType(DisplayType.FIT_TO_SCREEN);
			backToMain();
		}
	}// end inner class

	/**
	 * 保存Bitmap
	 */
	public static void saveBitmap(Bitmap bm, String filePath)
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
		} catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}// end class
