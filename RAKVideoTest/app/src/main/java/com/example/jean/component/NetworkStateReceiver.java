package com.example.jean.component;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class NetworkStateReceiver extends BroadcastReceiver {
	private static OnNetworkChangedListener _onNetworkChangedListener;

	@Override
	public void onReceive(Context context, Intent intent) {
		int state = 0;
		if (intent.getExtras() != null) {
			NetworkInfo ni = (NetworkInfo)intent.getExtras().get(ConnectivityManager.EXTRA_NETWORK_INFO);
			if (ni != null && ni.getState() == NetworkInfo.State.CONNECTED) {
				state = 1;
			} else if (intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, Boolean.FALSE)) {
				state = -1;
			}
		}

		if (_onNetworkChangedListener != null)
			_onNetworkChangedListener.onChanged(state);
	}

	//region event
	public static void setOnNetworkChangedListener(OnNetworkChangedListener listener) {
		_onNetworkChangedListener = listener;
	}

	public static interface OnNetworkChangedListener {
		public void onChanged(int state);
	}
	//endregion
};