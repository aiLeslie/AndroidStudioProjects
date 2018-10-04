package com.xinlan.imageedit.editimage;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.example.jean.video.VideoPlay;
import com.xinlan.imageedit.editimage.fragment.ColorFragment;
import com.xinlan.imageedit.editimage.fragment.CropFragment;
import com.xinlan.imageedit.editimage.fragment.FliterListFragment;
import com.xinlan.imageedit.editimage.fragment.MainMenuFragment;
import com.xinlan.imageedit.editimage.fragment.RotateFragment;
import com.xinlan.imageedit.editimage.fragment.StirckerFragment;
import com.xinlan.imageedit.editimage.ui.CustomViewPager;
import com.xinlan.imageedit.editimage.ui.imagezoom.ImageViewTouch;
import com.xinlan.imageedit.editimage.ui.imagezoom.ImageViewTouchBase.DisplayType;
import com.xinlan.imageedit.editimage.utils.BitmapUtils;
import com.xinlan.imageedit.editimage.view.CropImageView;
import com.xinlan.imageedit.editimage.view.RotateImageView;
import com.xinlan.imageedit.editimage.view.StickerView;
import com.example.jean.rakvideotest.R;

/**
 * 功能： 1.贴图 2.滤镜 3.裁剪 4.旋转 5.调色
 */
public class EditImageActivity extends BaseActivity
{
	public static final int MODE_NONE = 0;
	public static final int MODE_STICKERS = 1;
	public static final int MODE_FILTER = 2;
	public static final int MODE_CROP = 3;
	public static final int MODE_TEXT = 4;
	public static final int MODE_ROTATE = 5;
	public static final int MODE_COLOR=6;

	public String filePath;
	public String saveFilePath;
	private int imageWidth, imageHeight;
	private LoadImageTask mLoadImageTask;

	public int mode = MODE_NONE;
	private EditImageActivity mContext;
	public Bitmap mainBitmap;
	public ImageViewTouch mainImage;
	private View backBtn;

	public ViewFlipper bannerFlipper;
	private TextView title;
	private View applyBtn;
	private View saveBtn;

	public StickerView mStickerView;
	public CropImageView mCropPanel;
	public RotateImageView mRotatePanel;

	public CustomViewPager bottomGallery;
	private BottomGalleryAdapter mBottomGalleryAdapter;
	private MainMenuFragment mMainMenuFragment;// Menu
	public StirckerFragment mStirckerFragment;
	public FliterListFragment mFliterListFragment;
	public CropFragment mCropFragment;
	public RotateFragment mRotateFragment;
	public ColorFragment mColorFragment;
	private Dialog dialog;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_edit);
		initView();
		getData();
	}

	private void getData()
	{
		Intent intent = getIntent();
		filePath=VideoPlay.photofile_path+"/"+intent.getStringExtra("photodata")+".jpg";
		saveFilePath=filePath;
		title.setText(intent.getStringExtra("photodata"));
		loadImage(filePath);
	}

	private void initView()
	{
		mContext = this;
		DisplayMetrics metrics = getResources().getDisplayMetrics();
		imageWidth = metrics.widthPixels; // /1.5
		imageHeight = metrics.heightPixels;

		bannerFlipper = (ViewFlipper) findViewById(R.id.banner_flipper);
		bannerFlipper.setInAnimation(this, R.anim.in_bottom_to_top);
		bannerFlipper.setOutAnimation(this, R.anim.out_bottom_to_top);
		title = (TextView) findViewById(R.id.title);
		applyBtn = findViewById(R.id.apply);
		applyBtn.setOnClickListener(new ApplyBtnClick());
		saveBtn = findViewById(R.id.save_btn);
		saveBtn.setOnClickListener(new SaveBtnClick());
		saveBtn.setVisibility(View.GONE);

		mainImage = (ImageViewTouch) findViewById(R.id.main_image);
		backBtn = findViewById(R.id.back_btn);
		backBtn.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				forceReturnBack();
			}
		});

		mStickerView = (StickerView) findViewById(R.id.sticker_panel);
		mCropPanel = (CropImageView) findViewById(R.id.crop_panel);
		mRotatePanel = (RotateImageView) findViewById(R.id.rotate_panel);

		bottomGallery = (CustomViewPager) findViewById(R.id.bottom_gallery);
		mMainMenuFragment = MainMenuFragment.newInstance(this);
		mBottomGalleryAdapter = new BottomGalleryAdapter(this.getSupportFragmentManager());
		mStirckerFragment = StirckerFragment.newInstance(this);
		mFliterListFragment = FliterListFragment.newInstance(this);
		mCropFragment = CropFragment.newInstance(this);
		mRotateFragment = RotateFragment.newInstance(this);
		mColorFragment= ColorFragment.newInstance(this);

		bottomGallery.setAdapter(mBottomGalleryAdapter);
	}

	private final class BottomGalleryAdapter extends FragmentPagerAdapter
	{
		public BottomGalleryAdapter(FragmentManager fm)
		{
			super(fm);
		}

		@Override
		public Fragment getItem(int index)
		{
			// System.out.println("createFragment-->"+index);
			if (index == 0)
				return mMainMenuFragment;
			if (index == 1)
				return mStirckerFragment;
			if (index == 2)
				return mFliterListFragment;
			if (index == 3)
				return mCropFragment;
			if (index == 4)
				return mRotateFragment;
			if (index == 5)
				return mColorFragment;
			return MainMenuFragment.newInstance(mContext);
		}

		@Override
		public int getCount()
		{
			return 6;
		}
	}// end inner class

	/**
	 * 加载原始图片
	 */
	public void loadImage(String filepath)
	{
		if (mLoadImageTask != null)
			mLoadImageTask.cancel(true);

		mLoadImageTask = new LoadImageTask();
		mLoadImageTask.execute(filepath);
	}

	private final class LoadImageTask extends AsyncTask<String, Void, Bitmap>
	{
		@Override
		protected Bitmap doInBackground(String... params)
		{
			return BitmapUtils.loadImageByPath(params[0], imageWidth, imageHeight);
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
			if (mainBitmap != null)
			{
				mainBitmap.recycle();
				mainBitmap = null;
				System.gc();
			}
			mainBitmap = result;
			mainImage.setImageBitmap(result);
			mainImage.setDisplayType(DisplayType.FIT_TO_SCREEN);
			dialog.dismiss();
		}
		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();
			dialog = BaseActivity.getLoadingDialog(EditImageActivity.this, getString(R.string.edit_photo_loading), false);
			dialog.show();
		}
	}

	/**
	 * 返回
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			switch (mode)
			{
				case MODE_STICKERS:
					mStirckerFragment.backToMain();
					return true;
				case MODE_FILTER:
					mFliterListFragment.backToMain();
					return true;
				case MODE_CROP:
					mCropFragment.backToMain();
					return true;
				case MODE_ROTATE:
					mRotateFragment.backToMain();
					return true;
				case MODE_COLOR:
					mColorFragment.backToMain();
					return true;

			}// end switch

			forceReturnBack();
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 强制返回
	 */
	private void forceReturnBack()
	{
		setResult(RESULT_CANCELED);
		this.finish();
	}

	/**
	 * 应用
	 */
	private final class ApplyBtnClick implements OnClickListener
	{
		@Override
		public void onClick(View v)
		{
			switch (mode)
			{
				case MODE_STICKERS:
					mStirckerFragment.saveStickers();
					break;
				case MODE_FILTER:
					mFliterListFragment.saveFilterImage();
					break;
				case MODE_CROP:
					mCropFragment.saveCropImage();
					break;
				case MODE_ROTATE:
					mRotateFragment.saveRotateImage();
					break;
				case MODE_COLOR:
					mColorFragment.saveColorImage();
					break;
				default:
					break;
			}

		}
	}

	/**
	 * 保存
	 */
	private final class SaveBtnClick implements OnClickListener
	{
		@Override
		public void onClick(View v)
		{
			Intent returnIntent = new Intent();
			returnIntent.putExtra("save_file_path", saveFilePath);
			mContext.setResult(RESULT_OK, returnIntent);
			mContext.finish();
		}
	}

	public void changeMainBitmap(Bitmap newBit)
	{
		if (mainBitmap != null)
		{
			if (!mainBitmap.isRecycled())
			{
				mainBitmap.recycle();
			}
			mainBitmap = newBit;
		} else
		{
			mainBitmap = newBit;
		}// end if
		mainImage.setImageBitmap(mainBitmap);
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		if (mLoadImageTask != null)
		{
			mLoadImageTask.cancel(true);
		}
	}

}// end class
