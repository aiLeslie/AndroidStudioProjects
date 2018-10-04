package com.example.jean.video;

import java.util.List;  

import android.content.Context;  
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;  
import android.net.wifi.WifiConfiguration;  
import android.net.wifi.WifiInfo;  
import android.net.wifi.WifiManager;  
import android.net.wifi.WifiManager.WifiLock;  

public class WLANAPI
{
    WifiManager mWifiManager;
    private WifiInfo mWifiInfo;
    DhcpInfo mDhcpInfo;
    private List<ScanResult> mWifiList;
    private List<WifiConfiguration> mWifiConfigurations;
    WifiLock mWifiLock;
    public WLANAPI(Context context)
    {
        mWifiManager=(WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        mWifiInfo=mWifiManager.getConnectionInfo();
        mDhcpInfo=mWifiManager.getDhcpInfo();
    }

    public boolean isNetworkConnected(Context context)
    {
    	if (context != null)
    	{
	    	ConnectivityManager mConnectivityManager = (ConnectivityManager) context
	    	.getSystemService(Context.CONNECTIVITY_SERVICE);
	    	NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
	    	if (mNetworkInfo != null)
	    	{
	    		return mNetworkInfo.isAvailable();
	    	}
    	}
    	return false;
    }


    public static boolean isConnectionAvailable(Context cotext)
    {
        boolean isConnectionFail = true;
        ConnectivityManager connectivityManager = (ConnectivityManager)cotext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null)
        {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if (activeNetworkInfo == null || !activeNetworkInfo.isConnected())
            {
                isConnectionFail = true;
            }
            else
            {
                isConnectionFail = false;
            }
        }
        return isConnectionFail;
    }

    public boolean IsopenWifi()
    {
        if(!mWifiManager.isWifiEnabled())
        {
            return false;
        }
        return true;
    }

    public void openWifi()
    {
        if(!mWifiManager.isWifiEnabled())
        {
            mWifiManager.setWifiEnabled(true);

        }
    }

    public void closeWifi()
    {
        if(!mWifiManager.isWifiEnabled())
        {
            mWifiManager.setWifiEnabled(false);
        }
    }

    public int checkState()
    {
        return mWifiManager.getWifiState();
    }

    public void acquireWifiLock()
    {
        mWifiLock.acquire();
    }

    public void releaseWifiLock()
    {
        if(mWifiLock.isHeld()){
            mWifiLock.acquire();
        }
    }

    public void createWifiLock()
    {
        mWifiLock=mWifiManager.createWifiLock("test");
    }

    public List<WifiConfiguration> getConfiguration()
    {
        return mWifiConfigurations;
    }

    public void connetionConfiguration(int index)
    {
        if(index>mWifiConfigurations.size())
        {
            return ;
        }
        int wcgID = mWifiConfigurations.get(index).networkId;
        mWifiManager.enableNetwork(wcgID, true);
    }
    public void startScan()
    {
        mWifiManager.startScan();
        mWifiList=mWifiManager.getScanResults();
        mWifiConfigurations=mWifiManager.getConfiguredNetworks();
    }

    public List<ScanResult> getWifiList()
    {
        return mWifiList;
    }


    public StringBuffer lookUpScan()
    {
        StringBuffer sb=new StringBuffer();
        for(int i=0;i<mWifiList.size();i++){
            sb.append("Index_" + new Integer(i + 1).toString() + ":");
            sb.append((mWifiList.get(i)).toString()).append("\n");
        }
        return sb;
    }
    public String getMacAddress()
    {
        return (mWifiInfo==null)?"NULL":mWifiInfo.getMacAddress();
    }
    public String getSSID()
    {
        return (mWifiInfo==null)?"NULL":mWifiInfo.getSSID();
    }
    public String getBSSID()
    {
        return (mWifiInfo==null)?"NULL":mWifiInfo.getBSSID();
    }
    public int getIpAddress()
    {
        return (mWifiInfo==null)?0:mWifiInfo.getIpAddress();
    }
    public int getGateway()
    {
        return (mDhcpInfo==null)?0:mDhcpInfo.gateway;
    }
    public int getMask()
    {
        return (mDhcpInfo==null)?0:mDhcpInfo.netmask;
    }

    public int getNetWorkId()
    {
        return (mWifiInfo==null)?0:mWifiInfo.getNetworkId();
    }

    public String getWifiInfo()
    {
        return (mWifiInfo==null)?"NULL":mWifiInfo.toString();
    }

    public void addNetWork(WifiConfiguration configuration)
    {
        int wcgId=mWifiManager.addNetwork(configuration);
        mWifiManager.enableNetwork(wcgId, true);
    }

    public void disConnectionWifi(int netId)
    {
        mWifiManager.disableNetwork(netId);
        mWifiManager.disconnect();
    }
    private static boolean isHex(String key)
    {
    	for (int i = key.length() - 1; i >= 0; i--)
    	{
	    	final char c = key.charAt(i);
	    	if (!(c >= '0' && c <= '9' || c >= 'A' && c <= 'F' || c >= 'a' && c <= 'f'))
	    	{
	    		return false;
	    	}
    	}
    	return true;
    }

    private static boolean isHexWepKey(String wepKey)
    {
    	final int len = wepKey.length();
    	// WEP-40, WEP-104, and some vendors using 256-bit WEP (WEP-232?)
    	if (len != 10 && len != 26 && len != 58)
    	{
    		return false;
    	}
    		return isHex(wepKey);
    }

    public WifiConfiguration CreateWifiInfo(String SSID, String Password, int Type)
    {
          WifiConfiguration config = new WifiConfiguration();
           config.allowedAuthAlgorithms.clear();
           config.allowedGroupCiphers.clear();
           config.allowedKeyManagement.clear();
           config.allowedPairwiseCiphers.clear();
           config.allowedProtocols.clear();
          config.SSID = "\"" + SSID + "\"";

        WifiConfiguration tempConfig = this.IsExsits(SSID);
          if(tempConfig != null)
          {
              mWifiManager.removeNetwork(tempConfig.networkId);
          }

          if(Type == 1) //WIFICIPHER_NOPASS
          {
               config.wepKeys[0] = "\"" + "\"";
              config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
              config.wepTxKeyIndex = 0;
          }
          if(Type == 2) //WIFICIPHER_WEP
          {
              config.hiddenSSID = true;
              if (isHexWepKey(Password))
              {
            	  config.wepKeys[0] = Password;

              }
              else
              {
            	  config.wepKeys[0] = "\"" + Password + "\"";
              }
              config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
              config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
              config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
              config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
              config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
              config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
              config.wepTxKeyIndex = 0;
          }
          if(Type == 3) //WIFICIPHER_WPA
          {
	          config.preSharedKey = "\""+Password+"\"";
	          config.hiddenSSID = true;
	          config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
	          config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
	          config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
	          config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
	          //config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
	          config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
	          config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
	          config.status = WifiConfiguration.Status.ENABLED;
          }
           return config;
    }

    private WifiConfiguration IsExsits(String SSID)   
    {   
        List<WifiConfiguration> existingConfigs = mWifiManager.getConfiguredNetworks();   
           for (WifiConfiguration existingConfig : existingConfigs)    
           {   
             if (existingConfig.SSID.equals("\""+SSID+"\""))   
             {   
                 return existingConfig;   
             }   
           }   
        return null;    
    } 

}

