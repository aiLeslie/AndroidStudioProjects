package com.xinlan.imageedit.editimage;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.jean.rakvideotest.R;

@SuppressLint("InflateParams")
public abstract class BaseActivity extends FragmentActivity
{
	protected LayoutInflater mLayoutInflater;

	@Override
	protected void onCreate(Bundle arg0)
	{
		super.onCreate(arg0);
		mLayoutInflater = LayoutInflater.from(this);
	}

	public static Dialog getLoadingDialog(Context context, String title, boolean canCancel)
	{
		View dialogView = LayoutInflater.from(context).inflate(R.layout.view_loading_dialog, null);// 加载view
		ImageView image = (ImageView) dialogView.findViewById(R.id.img); //
		TextView loadingDialogText = (TextView) dialogView.findViewById(R.id.tipTextView);// 提示文字
		if (!TextUtils.isEmpty(title))
		{
			loadingDialogText.setText(title);
		}
		Dialog loadingDialog = new Dialog(context, R.style.loading_dialog);
		loadingDialog.setCancelable(canCancel);
		AnimationDrawable animationDrawable = (AnimationDrawable) image.getDrawable();
		animationDrawable.start();
		loadingDialog.setContentView(dialogView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局
		return loadingDialog;
	}
}// end class
