package com.xinlan.imageedit.editimage.fragment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Color;
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
import android.widget.TextView;

import com.xinlan.imageedit.editimage.BaseActivity;
import com.xinlan.imageedit.editimage.EditImageActivity;
import com.xinlan.imageedit.editimage.filter.PhotoProcessing;
import com.example.jean.rakvideotest.R;

/**
 * 滤镜
 */
@SuppressLint({ "NewApi", "InflateParams" })
public class FliterListFragment extends Fragment
{
	public static final String TAG = FliterListFragment.class.getName();
	private View mainView;
	private View backBtn;

	private Bitmap fliterBit;
	private EditImageActivity activity;

	private LinearLayout mFilterGroup;
	private String[] fliters = PhotoProcessing.FILTERS;
	private Bitmap currentBitmap;

	public static FliterListFragment newInstance(EditImageActivity activity)
	{
		FliterListFragment fragment = new FliterListFragment();
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
		fliters[0]=getString(R.string.edit_fliter_yuanse);
		fliters[1]=getString(R.string.edit_fliter_qingrou);
		fliters[2]=getString(R.string.edit_fliter_heibai);
		fliters[3]=getString(R.string.edit_fliter_jingdian);
		fliters[4]=getString(R.string.edit_fliter_xuanli);
		fliters[5]=getString(R.string.edit_fliter_fugu);
		fliters[6]=getString(R.string.edit_fliter_youya);
		fliters[7]=getString(R.string.edit_fliter_jiaopian);
		fliters[8]=getString(R.string.edit_fliter_huiyi);
		fliters[9]=getString(R.string.edit_fliter_youge);
		fliters[10]=getString(R.string.edit_fliter_liunian);
		fliters[11]=getString(R.string.edit_fliter_guangxuan);

		mainView = inflater.inflate(R.layout.fragment_edit_image_fliter, null);
		backBtn = mainView.findViewById(R.id.back_to_main);
		mFilterGroup = (LinearLayout) mainView.findViewById(R.id.fliter_group);
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
		setUpFliters();
	}

	/**
	 * 返回
	 */
	public void backToMain()
	{
		currentBitmap = activity.mainBitmap;
		fliterBit = null;
		activity.mainImage.setImageBitmap(activity.mainBitmap);
		activity.mode = EditImageActivity.MODE_NONE;
		activity.bottomGallery.setCurrentItem(0);
		activity.mainImage.setScaleEnabled(true);
		activity.bannerFlipper.showPrevious();
	}

	/**
	 * 保存
	 */
	public void saveFilterImage()
	{
		if (currentBitmap == activity.mainBitmap)
		{
			backToMain();
			return;
		} else
		{
			SaveImageTask saveTask = new SaveImageTask();
			saveTask.execute(fliterBit);
		}// end if
	}

	/**
	 * 保存线程
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
				activity.mainBitmap = fliterBit;
				backToMain();
			}// end if
		}

		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();
			dialog = EditImageActivity.getLoadingDialog(getActivity(), getString(R.string.edit_photo_processing), false);
			dialog.show();
			Log.e("==>","222222222222");
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

	/**
	 * 设置滤镜
	 */
	private void setUpFliters()
	{
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		params.gravity = Gravity.CENTER_VERTICAL;
		params.leftMargin = 20;
		params.rightMargin = 20;
		mFilterGroup.removeAllViews();
		for (int i = 0, len = fliters.length; i < len; i++)
		{
			TextView text = new TextView(activity);
			text.setTextColor(Color.WHITE);
			text.setTextSize(20);
			text.setText(fliters[i]);
			mFilterGroup.addView(text, params);
			text.setTag(i);
			text.setOnClickListener(new FliterClick());
		}// end for i
	}

	@Override
	public void onDestroy()
	{
		if (fliterBit != null && (!fliterBit.isRecycled()))
		{
			fliterBit.recycle();
		}
		super.onDestroy();
	}

	/**
	 * 滤镜功能
	 */
	private final class FliterClick implements OnClickListener
	{
		@Override
		public void onClick(View v)
		{
			int position = ((Integer) v.getTag()).intValue();
			if (position == 0)
			{
				activity.mainImage.setImageBitmap(activity.mainBitmap);
				currentBitmap = activity.mainBitmap;
				return;
			}

			ProcessingImage task = new ProcessingImage();
			task.execute(position);
		}
	}// end inner class

	/**
	 * 使用滤镜
	 */
	private final class ProcessingImage extends AsyncTask<Integer, Void, Bitmap>
	{
		private Dialog dialog;
		private Bitmap srcBitmap;

		@Override
		protected Bitmap doInBackground(Integer... params)
		{
			int type = params[0];
			if (srcBitmap != null && !srcBitmap.isRecycled())
			{
				srcBitmap.recycle();
			}

			srcBitmap = Bitmap.createBitmap(activity.mainBitmap.copy(Bitmap.Config.RGB_565, true));
			return PhotoProcessing.filterPhoto(srcBitmap, type);
		}

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
		protected void onPostExecute(Bitmap result)
		{
			super.onPostExecute(result);
			dialog.dismiss();
			if (result == null)
				return;
			if (fliterBit != null && (!fliterBit.isRecycled()))
			{
				fliterBit.recycle();
			}
			fliterBit = result;
			activity.mainImage.setImageBitmap(fliterBit);
			currentBitmap = fliterBit;
		}

		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();
			dialog = BaseActivity.getLoadingDialog(getActivity(), getString(R.string.edit_photo_processing), false);
			dialog.show();
		}

	}// end inner class

	public Bitmap getCurrentBitmap()
	{
		return currentBitmap;
	}

	public void setCurrentBitmap(Bitmap currentBitmap)
	{
		this.currentBitmap = currentBitmap;
	}
}// end class
