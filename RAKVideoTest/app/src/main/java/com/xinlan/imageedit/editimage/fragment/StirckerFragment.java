package com.xinlan.imageedit.editimage.fragment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ViewFlipper;

import com.xinlan.imageedit.editimage.BaseActivity;
import com.xinlan.imageedit.editimage.EditImageActivity;
import com.xinlan.imageedit.editimage.StickerAdapter;
import com.xinlan.imageedit.editimage.StickerTypeAdapter;
import com.xinlan.imageedit.editimage.matrix.Matrix3;
import com.xinlan.imageedit.editimage.view.StickerItem;
import com.xinlan.imageedit.editimage.view.StickerView;
import com.example.jean.rakvideotest.R;

/**
 * 贴图
 */
public class StirckerFragment extends Fragment
{
	public static final String TAG = StirckerFragment.class.getName();
	private View mainView;
	private EditImageActivity activity;
	private ViewFlipper flipper;
	private View backToMenu;
	private RecyclerView typeList;
	private RecyclerView stickerList;
	private View backToType;
	private StickerView mStickerView;
	private StickerAdapter mStickerAdapter;

	public static StirckerFragment newInstance(EditImageActivity activity)
	{
		StirckerFragment fragment = new StirckerFragment();
		fragment.activity = activity;
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
		mainView = inflater.inflate(R.layout.fragment_edit_image_sticker_type, null);
		this.mStickerView = activity.mStickerView;
		flipper = (ViewFlipper) mainView.findViewById(R.id.flipper);
		flipper.setInAnimation(activity, R.anim.in_bottom_to_top);
		flipper.setOutAnimation(activity, R.anim.out_bottom_to_top);

		//
		backToMenu = mainView.findViewById(R.id.back_to_main);
		typeList = (RecyclerView) mainView.findViewById(R.id.stickers_type_list);
		typeList.setHasFixedSize(true);
		LinearLayoutManager mLayoutManager = new LinearLayoutManager(activity);
		mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
		typeList.setLayoutManager(mLayoutManager);
		typeList.setAdapter(new StickerTypeAdapter(this));
		backToType = mainView.findViewById(R.id.back_to_type);

		stickerList = (RecyclerView) mainView.findViewById(R.id.stickers_list);
		// stickerList.setHasFixedSize(true);
		LinearLayoutManager stickerListLayoutManager = new LinearLayoutManager(activity);
		stickerListLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
		stickerList.setLayoutManager(stickerListLayoutManager);
		mStickerAdapter = new StickerAdapter(this);
		stickerList.setAdapter(mStickerAdapter);

		return mainView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);

		backToMenu.setOnClickListener(new BackToMenuClick());
		backToType.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				flipper.showPrevious();
			}
		});

	}

	public void swipToStickerDetails(String path)
	{
		mStickerAdapter.addStickerImages(path);
		flipper.showNext();
	}

	private Bitmap getImageFromAssetsFile(String fileName)
	{
		Bitmap image = null;
		AssetManager am = getResources().getAssets();
		try
		{
			InputStream is = am.open(fileName);
			image = BitmapFactory.decodeStream(is);
			is.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return image;
	}

	public void selectedStickerItem(String path)
	{
		mStickerView.addBitImage(getImageFromAssetsFile(path));
	}

	public StickerView getmStickerView()
	{
		return mStickerView;
	}

	public void setmStickerView(StickerView mStickerView)
	{
		this.mStickerView = mStickerView;
	}

	private final class BackToMenuClick implements OnClickListener
	{
		@Override
		public void onClick(View v)
		{
			backToMain();
		}
	}// end inner class

	public void backToMain()
	{
		activity.mode = EditImageActivity.MODE_NONE;
		activity.bottomGallery.setCurrentItem(0);
		mStickerView.setVisibility(View.GONE);
		activity.bannerFlipper.showPrevious();
	}

	@SuppressLint("NewApi")
	private final class SaveStickersTask extends AsyncTask<Bitmap, Void, Bitmap>
	{
		private Dialog dialog;

		@Override
		protected Bitmap doInBackground(Bitmap... params)
		{
			Matrix touchMatrix = activity.mainImage.getImageViewMatrix();

			Bitmap resultBit = Bitmap.createBitmap(params[0]).copy(Bitmap.Config.RGB_565, true);
			Canvas canvas = new Canvas(resultBit);

			float[] data = new float[9];
			touchMatrix.getValues(data);
			Matrix3 cal = new Matrix3(data);
			Matrix3 inverseMatrix = cal.inverseMatrix();
			Matrix m = new Matrix();
			m.setValues(inverseMatrix.getValues());

			LinkedHashMap<Integer, StickerItem> addItems = mStickerView.getBank();
			for (Integer id : addItems.keySet())
			{
				StickerItem item = addItems.get(id);
				item.matrix.postConcat(m);
				canvas.drawBitmap(item.bitmap, item.matrix, null);
			}// end for
			saveBitmap(resultBit, activity.saveFilePath);
			return resultBit;
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
			mStickerView.clear();
			activity.changeMainBitmap(result);
			dialog.dismiss();
		}

		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();
			dialog = BaseActivity.getLoadingDialog(getActivity(), getString(R.string.edit_photo_processing), false);
			dialog.show();
		}
	}// end inner class

	/**
	 * 保存
	 */
	public void saveStickers()
	{
		SaveStickersTask task = new SaveStickersTask();
		task.execute(activity.mainBitmap);
	}

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
