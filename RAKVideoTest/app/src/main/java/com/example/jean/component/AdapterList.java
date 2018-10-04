package com.example.jean.component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.jean.rakvideotest.R;

public class AdapterList extends BaseAdapter implements Filterable
{

	private int[] mTo;
	private String[] mFrom;
	private ViewBinder mViewBinder;

	private List<? extends Map<String, ?>> mData;

	private int mResource;
	private int mDropDownResource;
	private LayoutInflater mInflater;

	private SimpleFilter mFilter;
	private ArrayList<Map<String, ?>> mUnfilteredData;
	private Context _ctx;

	/**
	 * Constructor
	 * 
	 * @param context
	 *            The context where the View associated with this SimpleAdapter
	 *            is running
	 * @param data
	 *            A List of Maps. Each entry in the List corresponds to one row
	 *            in the list. The Maps contain the data for each row, and
	 *            should include all the entries specified in "from"
	 * @param resource
	 *            Resource identifier of a view layout that defines the views
	 *            for this list item. The layout file should include at least
	 *            those named views defined in "to"
	 * @param from
	 *            A list of column names that will be added to the Map
	 *            associated with each item.
	 * @param to
	 *            The views that should display column in the "from" parameter.
	 *            These should all be TextViews. The first N views in this list
	 *            are given the values of the first N columns in the from
	 *            parameter.
	 */
	public AdapterList(Context context, List<? extends Map<String, ?>> data,
					   int resource, String[] from, int[] to)
	{
		_ctx=context;
		mData = data;
		mResource = mDropDownResource = resource;
		mFrom = from;
		mTo = to;
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	/**
	 * @see android.widget.Adapter#getCount()
	 */
	public int getCount()
	{
		return mData.size();
	}

	/**
	 * @see android.widget.Adapter#getItem(int)
	 */
	public Object getItem(int position)
	{
		return mData.get(position);
	}

	/**
	 * @see android.widget.Adapter#getItemId(int)
	 */
	public long getItemId(int position)
	{
		return position;
	}

	/**
	 * @see android.widget.Adapter#getView(int, View, ViewGroup)
	 */
	public View getView(int position, View convertView, ViewGroup parent)
	{
		return createViewFromResource(position, convertView, parent, mResource);
	}

	private View createViewFromResource(final int position, View convertView,
										ViewGroup parent, int resource)
	{
		View v;
		if (convertView == null)
		{
			v = mInflater.inflate(resource, parent, false);
		} else
		{
			v = convertView;
		}

		bindView(position, v);
		return v;
	}

	/**
	 * Listen Download Click
	 */
	private OnDownloadClickListener _onDownloadClickListener;
	public void setOnDownloadClickListener(OnDownloadClickListener listener) {
		_onDownloadClickListener = listener;
	}
	public interface OnDownloadClickListener {
		void onDownloadClicked(int _position);
	}

	/**
	 * Update Downloading Progress
	 */
	private HashMap<String, String> _changeProgress = new HashMap<String, String>();
	public void changeProgress(int position, int value) {
		_changeProgress.put(""+position, ""+value);
		notifyDataSetChanged();
	}

	public void clearProgress() {
		_changeProgress.clear();
	}

	/**
	 * Set Edit Image
	 * 0:Show and Not choose
	 * 1:Show and choose
	 */
	private HashMap<String, String> _editImg = new HashMap<String, String>();
	private boolean _isVisible=false;

	public void setVisible(boolean visible) {
		_isVisible=visible;
		notifyDataSetChanged();
	}

	public void setEditImage(int pos) {
		if(getEditImage(pos)){//若已选中，则清0
			_editImg.put(""+pos, ""+0);
		}
		else{
			_editImg.put(""+pos, ""+1);
		}
		notifyDataSetChanged();
	}

	public boolean getEditImage(int pos) {
		if(_editImg.get(pos+"")=="1"){
			return true;
		}
		return false;
	}

	public void clearEditImage() {
		_editImg.clear();
	}

	/**
	 * <p>
	 * Sets the layout resource to create the drop down views.
	 * </p>
	 * 
	 * @param resource
	 *            the layout resource defining the drop down views
	 * @see #getDropDownView(int, View, ViewGroup)
	 */
	public void setDropDownViewResource(int resource)
	{
		this.mDropDownResource = resource;
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent)
	{
		return createViewFromResource(position, convertView, parent,
				mDropDownResource);
		//return null;
	}
	private int selectedPosition = -1;
	public void setSelectedPosition(int position)
	{
		selectedPosition = position;
	}
	private void bindView(int position, View view)
	{
		final Map dataSet = mData.get(position);
		if (dataSet == null)
		{
			return;
		}

		final ViewBinder binder = mViewBinder;
		final String[] from = mFrom;
		final int[] to = mTo;
		final int count = to.length;

		for (int i = 0; i < count; i++)
		{
			final View v = view.findViewById(to[i]);
			if (v != null)
			{
				final Object data = dataSet.get(from[i]);
				String text = data == null ? "" : data.toString();
				if (text == null)
				{
					text = "";
				}

				boolean bound = false;
				if (binder != null)
				{
					bound = binder.setViewValue(v, data, text);
				}

				if (!bound)
				{
					if (v instanceof TextView)
					{
						((TextView) v).setText(text);
					} else if (v instanceof ImageView)
					{
						if (data instanceof Integer)
						{
							setViewImage((ImageView) v, (Integer) data);
						} else
						{
							setViewImage((ImageView) v, text);
						}
						//设置已缓存视频伤处按钮
						if(_isVisible){
							if(i==3) {
								v.setVisibility(View.VISIBLE);
								if(_editImg.get(""+position)==null)
									return;
								if(_editImg.get(""+position).toString().equals("0"))
									((ImageView) v).setImageResource(R.drawable.nochoose);
								else
									((ImageView) v).setImageResource(R.drawable.choose);
							}
						}
						else{
							if(i==3) {
								v.setVisibility(View.GONE);
								((ImageView) v).setImageResource(R.drawable.nochoose);
							}
						}

//						if(i==4) {
//							String value=_changeProgress.get(""+position);
//							if((value==null)||(value=="")){
//								((ImageView) v).setImageResource(R.drawable.fullplayer_icon_download2);
//								((ImageView) v).setEnabled(false);
//							}
//							else{
//								if(Integer.parseInt(value)==100){
//									((ImageView) v).setImageResource(R.drawable.fullplayer_icon_download3);
//									((ImageView) v).setEnabled(true);
//								}
//							}
//						}

					}else if (v instanceof ProgressBar)
					{
						//更新缓存进度
						String value=_changeProgress.get(""+position);
						if((value==null)||(value=="")){
							((ProgressBar) v).setProgress(0);
						}
						else{
							((ProgressBar) v).setProgress(Integer.parseInt(value));
						}
					}
					else
					{
						throw new IllegalStateException(
								v.getClass().getName()
										+ " is not a "
										+ " view that can be bounds by this SimpleAdapter");
					}
				}
			}
		}		
	}
	/**
	 * Returns the {@link ViewBinder} used to bind data to views.
	 * 
	 * @return a ViewBinder or null if the binder does not exist
	 * 
	 * @see #setViewBinder(android.widget.SimpleAdapter.ViewBinder)
	 */
	public ViewBinder getViewBinder()
	{
		return mViewBinder;
	}

	/**
	 * Sets the binder used to bind data to views.
	 * 
	 * @param viewBinder
	 *            the binder used to bind data to views, can be null to remove
	 *            the existing binder
	 * 
	 * @see #getViewBinder()
	 */
	public void setViewBinder(ViewBinder viewBinder)
	{
		mViewBinder = viewBinder;
	}

	/**
	 * Called by bindView() to set the image for an ImageView but only if there
	 * is no existing ViewBinder or if the existing ViewBinder cannot handle
	 * binding to an ImageView.
	 * 
	 * This method is called instead of {@link #setViewImage(ImageView, String)}
	 * if the supplied data is an int or Integer.
	 * 
	 * @param v
	 *            ImageView to receive an image
	 * @param value
	 *            the value retrieved from the data set
	 * 
	 * @see #setViewImage(ImageView, String)
	 */
	public void setViewImage(ImageView v, int value)
	{
		v.setImageResource(value);
	}

	/**
	 * Called by bindView() to set the image for an ImageView but only if there
	 * is no existing ViewBinder or if the existing ViewBinder cannot handle
	 * binding to an ImageView.
	 * 
	 * By default, the value will be treated as an image resource. If the value
	 * cannot be used as an image resource, the value is used as an image Uri.
	 * 
	 * This method is called instead of {@link #setViewImage(ImageView, int)} if
	 * the supplied data is not an int or Integer.
	 * 
	 * @param v
	 *            ImageView to receive an image
	 * @param value
	 *            the value retrieved from the data set
	 * 
	 * @see #setViewImage(ImageView, int)
	 */
	public void setViewImage(ImageView v, String value)
	{
		try
		{
			v.setImageResource(Integer.parseInt(value));
		} catch (NumberFormatException nfe)
		{
			v.setImageURI(Uri.parse(value));
		}
	}

	/**
	 * Called by bindView() to set the text for a TextView but only if there is
	 * no existing ViewBinder or if the existing ViewBinder cannot handle
	 * binding to an TextView.
	 * 
	 * @param v
	 *            TextView to receive text
	 * @param text
	 *            the text to be set for the TextView
	 */
	public void setViewText(TextView v, String text)
	{
		v.setText(text);
	}

	public Filter getFilter()
	{
		if (mFilter == null)
		{
			mFilter = new SimpleFilter();
		}
		return mFilter;
	}

	/**
	 * This class can be used by external clients of SimpleAdapter to bind
	 * values to views.
	 * 
	 * You should use this class to bind values to views that are not directly
	 * supported by SimpleAdapter or to change the way binding occurs for views
	 * supported by SimpleAdapter.
	 * 
	 * @see SimpleAdapter#setViewImage(ImageView, int)
	 * @see SimpleAdapter#setViewImage(ImageView, String)
	 * @see SimpleAdapter#setViewText(TextView, String)
	 */
	public static interface ViewBinder
	{
		/**
		 * Binds the specified data to the specified view.
		 * 
		 * When binding is handled by this ViewBinder, this method must return
		 * true. If this method returns false, SimpleAdapter will attempts to
		 * handle the binding on its own.
		 * 
		 * @param view
		 *            the view to bind the data to
		 * @param data
		 *            the data to bind to the view
		 * @param textRepresentation
		 *            a safe String representation of the supplied data: it is
		 *            either the result of data.toString() or an empty String
		 *            but it is never null
		 * 
		 * @return true if the data was bound to the view, false otherwise
		 */
		boolean setViewValue(View view, Object data, String textRepresentation);
	}

	/**
	 * <p>
	 * An array filters constrains the content of the array adapter with a
	 * prefix. Each item that does not start with the supplied prefix is removed
	 * from the list.
	 * </p>
	 */
	private class SimpleFilter extends Filter
	{

		@Override
		protected FilterResults performFiltering(CharSequence prefix)
		{
			FilterResults results = new FilterResults();

			if (mUnfilteredData == null)
			{
				mUnfilteredData = new ArrayList<Map<String, ?>>(mData);
			}

			if (prefix == null || prefix.length() == 0)
			{
				ArrayList<Map<String, ?>> list = mUnfilteredData;
				results.values = list;
				results.count = list.size();
			} else
			{
				String prefixString = prefix.toString().toLowerCase();

				ArrayList<Map<String, ?>> unfilteredValues = mUnfilteredData;
				int count = unfilteredValues.size();

				ArrayList<Map<String, ?>> newValues = new ArrayList<Map<String, ?>>(
						count);

				for (int i = 0; i < count; i++)
				{
					Map<String, ?> h = unfilteredValues.get(i);
					if (h != null)
					{

						int len = mTo.length;

						for (int j = 0; j < len; j++)
						{
							String str = (String) h.get(mFrom[j]);

							String[] words = str.split(" ");
							int wordCount = words.length;

							for (int k = 0; k < wordCount; k++)
							{
								String word = words[k];

								if (word.toLowerCase().startsWith(prefixString))
								{
									newValues.add(h);
									break;
								}
							}
						}
					}
				}

				results.values = newValues;
				results.count = newValues.size();
			}

			return results;
		}

		@Override
		protected void publishResults(CharSequence constraint,
				FilterResults results)
		{
			// noinspection unchecked
			mData = (List<Map<String, ?>>) results.values;
			if (results.count > 0)
			{
				notifyDataSetChanged();
			} else
			{
				notifyDataSetInvalidated();
			}
		}
	}
}
