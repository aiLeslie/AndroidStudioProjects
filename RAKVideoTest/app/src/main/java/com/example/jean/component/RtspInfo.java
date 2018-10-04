package com.example.jean.component;

public class RtspInfo {
	public String username;
	public String password;
	public String ip;
	public int port;
	public String pipe;
	public boolean audio;
	public int type; // 0: udp; 1: tcp

	public static final int UDP = 0;
	public static final int TCP = 1;
}
