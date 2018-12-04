package com.example.administrator.bluetoothtest.mysocket.util;

import java.util.LinkedList;
import java.util.List;


public class SysConvert {
	public static int[] parseHex(String string) {
		// 如果传入string为空或是"",返回空的整形数组
		if (string == null || "".equals(string)) {
			return new int[0];
		}
		// 请字符串两边空格去掉
		string = string.trim();
		
		//定义数字边界
		int start = 0,end = -1;
		
		try {
			// 如果字符串中间不含空格,直接转换为一个数字,返回长度为1的整形数组
			if ((end = string.indexOf(" ")) == -1) {
				return new int[] {
						Integer.parseInt(string,16)
				};
			}
			
			// 创建list装载转换后整形数据
			List<Integer> nums = new LinkedList<>();
			
			while((end = string.indexOf(" ",start)) != -1){
				if (start == end) {
					start = end + 1;
					continue;
				}
				nums.add(Integer.parseInt(string.substring(start, end),16));
				start = end + 1;
				
			}
			//把最后一个子字符串转换为一个数字并装载进去list
			nums.add(Integer.parseInt(string.substring(start, string.length()),16));
			int[]values = new int [nums.size()];
			for (int i = 0; i < values.length; i++) {
				values[i] = nums.get(i);
			}
			return values;
		}catch(NumberFormatException e) {
			e.printStackTrace();
		}
		return new int[0];
		
		
	}
	
	public static void main(String[] args) {
		
		int[]values = SysConvert.parseHex("10 11  12     13  14 15");
		for (int i = 0; i < values.length; i++) {
			System.out.println(values[i]);
		}
	}
}
