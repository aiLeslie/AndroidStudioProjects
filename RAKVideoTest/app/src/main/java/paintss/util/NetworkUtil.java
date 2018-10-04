package paintss.util;

import android.app.Activity;
import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.format.Formatter;

/**
 * Created by lewis on 2015/1/25.
 */
public class NetworkUtil {
	public static String getSsid(Activity activity) {
		WifiManager wifiManager =
				(WifiManager)activity.getSystemService(Context.WIFI_SERVICE);
		String ssid = null;
		if (wifiManager.isWifiEnabled()) {
			WifiInfo wifiInfo = wifiManager.getConnectionInfo();
			ssid = wifiInfo.getSSID();
			int lm = ssid.indexOf('"');
			if (lm >= 0)
				ssid = ssid.substring((lm + 1), (ssid.length() - 1));
		}

		return ssid;
	}

	public static String getIpAddress(Context context) {
		WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		return Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
	}

	public static String getGatewayAddress(Context context) {
		WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		DhcpInfo dhcpInfo = wm.getDhcpInfo();
		return Formatter.formatIpAddress(dhcpInfo.gateway);
	}
}
