package com.example.administrator.flagapitest.mysocket.socket;

public class DataFormat {
	// 字符串格式
	public static final String STRING_FORMAT = "STRING";
	// 十六进制格式
	public static final String HEX_FORMAT = "HEX";
	
	public String format = HEX_FORMAT;
	
	public DataFormat() {}
	
	public DataFormat(String format) {
		if (STRING_FORMAT.equals(format) || HEX_FORMAT.equals(format))
			this.format = format;
			else new IllegalArgumentException();
	}
	
	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		if (STRING_FORMAT.equals(format) || HEX_FORMAT.equals(format))
		this.format = format;
		else new IllegalArgumentException();
	}
	public boolean isHexFormat() {
		if (HEX_FORMAT.equals(format)) {
			return true;
		}else {
			return false;
		}
	}
	public boolean isStringFormat() {
		if (STRING_FORMAT.equals(format)) {
			return true;
		}else {
			return false;
		}
	}

	
}
