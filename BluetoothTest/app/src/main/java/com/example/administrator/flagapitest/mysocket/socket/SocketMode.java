package com.example.administrator.flagapitest.mysocket.socket;

public class SocketMode {
	// 服务端
	public static final String SERVER_MODE = "SERVER";
	// 客户端
	public static final String CLIENT_MODE = "CLIENT";
	
	public static final String NULL_MODE = "NULL";
	
	public String mode;
	
	public SocketMode() {mode = NULL_MODE;}
	
	public SocketMode(String mode) {
		if (SERVER_MODE.equals(mode) || CLIENT_MODE.equals(mode) || NULL_MODE.equals(mode))
			this.mode = mode;
			else new IllegalArgumentException();
	}
	
	public String getmode() {
		return mode;
	}

	public void setmode(String mode) {
		if (SERVER_MODE.equals(mode) || CLIENT_MODE.equals(mode) || NULL_MODE.equals(mode))
		this.mode = mode;
		else new IllegalArgumentException();
	}
	public boolean isServer() {
		if (SERVER_MODE.equals(mode)) {
			return true;
		}else {
			return false;
		}
	}
	public boolean isClient() {
		if (CLIENT_MODE.equals(mode)) {
			return true;
		}else {
			return false;
		}
	}
	
	public boolean isNull() {
		if (NULL_MODE.equals(mode)) {
			return true;
		}else {
			return false;
		}
	}
	
	

	
}
