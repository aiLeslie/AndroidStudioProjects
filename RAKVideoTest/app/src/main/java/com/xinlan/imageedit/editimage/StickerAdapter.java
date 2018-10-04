package com.xinlan.imageedit.editimage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import android.annotation.SuppressLint;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.xinlan.imageedit.editimage.fragment.StirckerFragment;
import com.example.jean.rakvideotest.R;

/**
 * 贴图分类列表Adapter
 */
public class StickerAdapter extends RecyclerView.Adapter<ViewHolder>
{
	private StirckerFragment mStirckerFragment;
	private ImageClick mImageClick = new ImageClick();
	private List<String> pathList = new ArrayList<String>();// 图片路径列表

	public StickerAdapter(StirckerFragment fragment)
	{
		super();
		this.mStirckerFragment = fragment;
	}

	public class ImageHolder extends ViewHolder
	{
		public ImageView image;

		public ImageHolder(View itemView)
		{
			super(itemView);
			this.image = (ImageView) itemView.findViewById(R.id.img);
		}
	}

	@Override
	public int getItemCount()
	{
		return pathList.size();
	}

	@Override
	public int getItemViewType(int position)
	{
		return 1;
	}

	@SuppressLint("InflateParams")
	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewtype)
	{
		View v = null;
		v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_sticker_item, null);
		ImageHolder holer = new ImageHolder(v);
		return holer;
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position)
	{
		ImageHolder imageHoler = (ImageHolder) holder;
		String path = pathList.get(position);
		// System.out.println(path);

		AssetManager assetManager = mStirckerFragment.getActivity().getAssets();
		try
		{
			InputStream in = assetManager.open(path);
			Bitmap bmp = BitmapFactory.decodeStream(in);
			imageHoler.image.setImageBitmap(bmp);
		} catch (Exception e)
		{
		}

		imageHoler.image.setTag(path);
		imageHoler.image.setOnClickListener(mImageClick);
	}

	public void addStickerImages(String folderPath)
	{
		pathList.clear();
		try
		{
			String[] files = mStirckerFragment.getActivity().getAssets().list(folderPath);
			for (String name : files)
			{
				pathList.add(folderPath + File.separator + name);
				// System.out.println(name);
			}
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		this.notifyDataSetChanged();
	}

	/**
	 * 选择贴图
	 */
	private final class ImageClick implements OnClickListener
	{
		@Override
		public void onClick(View v)
		{
			String data = (String) v.getTag();
			// System.out.println("data---->" + data);
			mStirckerFragment.selectedStickerItem(data);
		}
	}// end inner class

}// end class
