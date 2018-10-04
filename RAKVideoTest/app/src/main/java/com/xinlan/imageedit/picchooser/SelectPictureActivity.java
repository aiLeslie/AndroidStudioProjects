/*
 * Copyright 2013 Thomas Hoffmann
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.xinlan.imageedit.picchooser;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

public class SelectPictureActivity extends FragmentActivity
{

	@SuppressLint("InlinedApi")
	@Override
	protected void onCreate(final Bundle b)
	{
		super.onCreate(b);

		setResult(RESULT_CANCELED);
		// Create new fragment and transaction
		Fragment newFragment = new BucketsFragment();
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

		// Replace whatever is in the fragment_container view with this
		// fragment,
		// and add the transaction to the back stack
		transaction.replace(android.R.id.content, newFragment);

		// Commit the transaction
		transaction.commit();
	}

	void showBucket(final int bucketId)
	{
		Bundle b = new Bundle();
		b.putInt("bucket", bucketId);
		Fragment f = new ImagesFragment();
		f.setArguments(b);
		getSupportFragmentManager().beginTransaction().replace(android.R.id.content, f).addToBackStack(null).commit();
	}

	void imageSelected(final String imgPath, final String imgTaken, final long imageSize)
	{
		Intent result = new Intent();
		result.putExtra("imgPath", imgPath);
		result.putExtra("dateTaken", imgTaken);
		result.putExtra("imageSize", imageSize);
		setResult(RESULT_OK, result);
		finish();
	}

}
