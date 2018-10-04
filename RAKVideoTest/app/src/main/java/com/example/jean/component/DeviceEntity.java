package com.example.jean.component;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import java.util.ArrayList;

public class DeviceEntity{
	public String name;
	public String id;
	public String ip;

	public DeviceEntity(String name) {
		this.name = name;
	}

	public DeviceEntity(String id, String ip) {
		this.id = id;
		this.ip = ip;
	}

	public DeviceEntity(String id, String ip, String name) {
		this.id = id;
		this.ip = ip;
		this.name = name;
	}


	public static String _deviceIdKey="DEVICE_ID_KEY";
	public static String _deviceNameKey="DEVICE_NAME_KEY";
	public static String _deviceIpKey="DEVICE_IP_KEY";
	public static String _devicePasswordKey="DEVICE_PASSWORD_KEY";
	/**
	 * 功能说明：获取保存的设备id
	 * @param ctx
	 * @param key
	 * @return
	 */
	public static ArrayList<String> getArraySharedPreference(Context ctx,String key)
	{
		String regularEx = "#";
		ArrayList<String> array = new ArrayList<String>();
		SharedPreferences sp = ctx.getSharedPreferences("data", Context.MODE_PRIVATE);
		String values;
		values = sp.getString(key, "");
		String[] str = values.split(regularEx);
		if(str.length>0)
		{
			for(int i=0;i<str.length;i++)
			{
				if(str[i].equals("")!=true)
					array.add(str[i]);
			}
		}
		return array;
	}
	/**
	 * 功能说明：功能说明：保存设备id
	 * @param ctx
	 * @param key
	 * @return
	 */
	public static void setArraySharedPreference(Context ctx,String key, ArrayList<String> values)
	{
		String regularEx = "#";
		String str = "";
		SharedPreferences sp = ctx.getSharedPreferences("data", Context.MODE_PRIVATE);
		if (values != null && values.size() > 0)
		{
			for (int i=0;i<values.size();i++)
			{
				str += values.get(i).toString();
				str += regularEx;
			}
			SharedPreferences.Editor et = sp.edit();
			et.putString(key, str);
			et.commit();
		}
		else{
			SharedPreferences.Editor et = sp.edit();
			et.putString(key, "");
			et.commit();
		}
	}

	/**
	 * 功能说明：保存设备信息
	 */
	public static void saveDevicesById(Context ctx,String id,String name,String ip) {
		ArrayList<String> _deviceIds = new ArrayList<String>();
		_deviceIds=getArraySharedPreference(ctx,_deviceIdKey);
		boolean same=false;
		for(int i=0;i<_deviceIds.size();i++){
			if(_deviceIds.get(i).toString().equals(id)){
				same=true;
				break;
			}
		}
		if(!same)//不相同则添加
		{
			_deviceIds.add(id);
			setArraySharedPreference(ctx, _deviceIdKey, _deviceIds);//保存id
		}
		//更新设备信息
		//根据id保存name
		modifyDeviceNameById(ctx, id, name);
		//根据id保存ip
		modifyDeviceIpById(ctx, id, ip);
	}

	/**
	 * 功能说明：获取保存的id
	 */
	public static ArrayList<String> getDevicesId(Context ctx) {
		ArrayList<String> _deviceIds = new ArrayList<String>();
		_deviceIds=getArraySharedPreference(ctx,_deviceIdKey);
		return _deviceIds;
	}

	/**
	 * 功能说明：根据id获取name
	 */
	public static String getDeviceNameFromId(Context ctx,String id) {
		String nameInId;
		int index=id.indexOf(".");
		if(index!=-1){
			int index1=id.indexOf(".",index+1);
			if(index1!=-1){
				nameInId=id.substring(index+1,index1);
			}
			else{
				nameInId=id;
			}
		}
		else{
			nameInId=id;
		}
		SharedPreferences p = ctx.getSharedPreferences(id, ctx.MODE_PRIVATE);
		String name=p.getString(_deviceNameKey, nameInId);
		return name;
	}

	/**
	 * 功能说明：根据id获取ip
	 */
	public static String getDeviceIpFromId(Context ctx,String id) {
		SharedPreferences p = ctx.getSharedPreferences(id, ctx.MODE_PRIVATE);
		String ip=p.getString(_deviceIpKey, "127.0.0.1");
		return ip;
	}

	/**
	 * 功能说明：根据id获取password
	 */
	public static String getDevicePasswordFromId(Context ctx,String id) {
		SharedPreferences p = ctx.getSharedPreferences(id, ctx.MODE_PRIVATE);
		String ip=p.getString(_devicePasswordKey, "");
		return ip;
	}

	/**
	 * 功能说明：修改设备名称
	 */
	public static void modifyDeviceNameById(Context ctx,String id,String name) {
		SharedPreferences.Editor editor = ctx.getSharedPreferences(id, ctx.MODE_PRIVATE).edit();
		editor.putString(_deviceNameKey, name);
		editor.commit();
	}

	/**
	 * 功能说明：修改设备IP
	 */
	public static void modifyDeviceIpById(Context ctx,String id,String ip) {
		SharedPreferences.Editor editor = ctx.getSharedPreferences(id, ctx.MODE_PRIVATE).edit();
		editor.putString(_deviceIpKey, ip);
		editor.commit();
		//根据id保存password

	}

	/**
	 * 功能说明：修改设备密码
	 */
	public static void modifyDevicePasswordById(Context ctx,String id,String password) {
		SharedPreferences.Editor editor = ctx.getSharedPreferences(id, ctx.MODE_PRIVATE).edit();
		editor.putString(_devicePasswordKey, password);
		editor.commit();
	}

	/**
	 * 功能说明：删除设备信息
	 */
	public static void deleteDeviceById(Context ctx,String id) {
		SharedPreferences.Editor editor = ctx.getSharedPreferences(id, ctx.MODE_PRIVATE).edit();
		editor.clear();
		editor.commit();
		ArrayList<String> _deviceIds = new ArrayList<String>();
		_deviceIds = getArraySharedPreference(ctx, _deviceIdKey);
		for (int i = 0; i < _deviceIds.size(); i++) {
			if (_deviceIds.get(i).toString().equals(id)) {
				_deviceIds.remove(i);
				break;
			}
		}
		setArraySharedPreference(ctx, _deviceIdKey, _deviceIds);//保存id
	}

	/**
	 * 功能说明：检测路由名称
	 */
	public static boolean checkRouteName(String name) {
		if (TextUtils.isEmpty(name)) {
			return true;
		}
		return !(name.contains("&") || name.contains("\\")
				|| name.contains("`") || name.contains("\"") || name.contains("=#"));
	}

	/**
	 * 功能说明：检测路由密码
	 */
	public static boolean checkRoutePassword(String password) {
		return checkRouteName(password);
	}


	/**
	 *  Find String
	 */
	public static String _passwordKey="\"password\": \"";
	public static String _endKey="\"";
	public static String Find_Str(String srcStr,String keyStr)
	{
		String res="";
		int index=srcStr.indexOf(keyStr);
		if(index!=-1)
		{
			int index1=srcStr.indexOf(_endKey,keyStr.length()+index);
			if(index1!=-1)
			{
				res=srcStr.substring(keyStr.length()+index,index1);
			}
		}
		return res;
	}
}
