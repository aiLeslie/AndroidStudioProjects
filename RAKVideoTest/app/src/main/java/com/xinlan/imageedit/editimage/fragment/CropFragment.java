package com.xinlan.imageedit.editimage.fragment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
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
import android.widget.TextView;
import com.xinlan.imageedit.editimage.BaseActivity;
import com.xinlan.imageedit.editimage.EditImageActivity;
import com.xinlan.imageedit.editimage.matrix.Matrix3;
import com.xinlan.imageedit.editimage.model.RatioItem;
import com.xinlan.imageedit.editimage.ui.imagezoom.ImageViewTouchBase.DisplayType;
import com.xinlan.imageedit.editimage.view.CropImageView;
import com.example.jean.rakvideotest.R;

/**
 * 裁剪
 */
@SuppressLint({ "InflateParams", "NewApi" })
public class CropFragment extends Fragment
{
	public static final String TAG = CropFragment.class.getName();
	private View mainView;
	private EditImageActivity activity;
	private View backToMenu;
	public CropImageView mCropPanel;
	private LinearLayout ratioList;
	private static List<RatioItem> dataList = new ArrayList<RatioItem>();
	private List<TextView> textViewList = new ArrayList<TextView>();

	public static int SELECTED_COLOR = Color.YELLOW;
	public static int UNSELECTED_COLOR = Color.WHITE;
	private CropRationClick mCropRationClick = new CropRationClick();
	public TextView selctedTextView;

	public static CropFragment newInstance(EditImageActivity activity)
	{
		CropFragment fragment = new CropFragment();
		fragment.activity = activity;
		fragment.mCropPanel = activity.mCropPanel;
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
		dataList.clear();
		dataList.add(new RatioItem(getString(R.string.edit_crop_renyi), -1f));
		dataList.add(new RatioItem("2:1", 2f));
		dataList.add(new RatioItem("16:9", 16 / 9f));
		dataList.add(new RatioItem("5:3", 5 / 3f));
		dataList.add(new RatioItem("3:2", 3 / 2f));
		dataList.add(new RatioItem("4:3", 4 / 3f));
		dataList.add(new RatioItem("5:4", 5 / 4f));
		dataList.add(new RatioItem("1:1", 1f));
		mainView = inflater.inflate(R.layout.fragment_edit_image_crop, null);
		backToMenu = mainView.findViewById(R.id.back_to_main);
		ratioList = (LinearLayout) mainView.findViewById(R.id.ratio_list_group);
		setUpRatioList();
		return mainView;
	}

	private void setUpRatioList()
	{
		// init UI
		ratioList.removeAllViews();
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		params.gravity = Gravity.CENTER_VERTICAL;
		params.leftMargin = 20;
		params.rightMargin = 20;
		for (int i = 0; i < dataList.size(); i++)
		{
			TextView text = new TextView(activity);
			text.setTextColor(UNSELECTED_COLOR);
			text.setTextSize(20);
			text.setText(dataList.get(i).getText());
			textViewList.add(text);
			ratioList.addView(text, params);
			text.setTag(i);
			if (i == 0)
			{
				selctedTextView = text;
				mCropPanel.setRatioCropRect(activity.mainImage.getBitmapRect(), -1f);
			}
			dataList.get(i).setIndex(i);
			text.setTag(dataList.get(i));
			text.setOnClickListener(mCropRationClick);
		}// end for i
		selctedTextView.setTextColor(SELECTED_COLOR);
	}

	/**
	 * 裁剪
	 */
	private final class CropRationClick implements OnClickListener
	{
		@Override
		public void onClick(View v)
		{
			TextView curTextView = (TextView) v;
			selctedTextView.setTextColor(UNSELECTED_COLOR);
			RatioItem dataItem = (RatioItem) v.getTag();
			selctedTextView = curTextView;
			selctedTextView.setTextColor(SELECTED_COLOR);

			mCropPanel.setRatioCropRect(activity.mainImage.getBitmapRect(), dataItem.getRatio());
		}
	}// end inner class

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		backToMenu.setOnClickListener(new BackToMenuClick());
	}

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
		mCropPanel.setVisibility(View.GONE);
		activity.mainImage.setScaleEnabled(true);
		activity.bottomGallery.setCurrentItem(0);
		if (selctedTextView != null)
		{
			selctedTextView.setTextColor(UNSELECTED_COLOR);
		}
		mCropPanel.setRatioCropRect(activity.mainImage.getBitmapRect(), -1f);
		activity.bannerFlipper.showPrevious();
	}

	/**
	 * 保存
	 */
	public void saveCropImage()
	{
		CropImageTask task = new CropImageTask();
		task.execute(activity.mainBitmap);
	}

	/**
	 * 裁剪线程
	 */
	private final class CropImageTask extends AsyncTask<Bitmap, Void, Bitmap>
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
			RectF cropRect = mCropPanel.getCropRect();
			Matrix touchMatrix = activity.mainImage.getImageViewMatrix();
			// Canvas canvas = new Canvas(resultBit);
			float[] data = new float[9];
			touchMatrix.getValues(data);
			Matrix3 cal = new Matrix3(data);
			Matrix3 inverseMatrix = cal.inverseMatrix();
			Matrix m = new Matrix();
			m.setValues(inverseMatrix.getValues());
			m.mapRect(cropRect);
			Bitmap resultBit = Bitmap.createBitmap(params[0], (int) cropRect.left, (int) cropRect.top, (int) cropRect.width(), (int) cropRect.height());

			saveBitmap(resultBit, activity.saveFilePath);
			return resultBit;
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
			activity.mCropPanel.setCropRect(activity.mainImage.getBitmapRect());
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
