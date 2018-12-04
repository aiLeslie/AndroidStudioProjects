package com.example.administrator.bluetoothtest.util.gear;

import java.util.Map;
import java.util.TreeMap;

public class GearProgress {
	private GearProgress() {
		
	}
	public static Map<Gear,Integer> gearMap ;
	
	public static Map<Gear,Integer> newInstance(){
		if (gearMap != null)return gearMap;
		gearMap = new TreeMap<Gear, Integer>();
		gearMap.put(Gear.PARK_GEAR, 0);
		gearMap.put(Gear.REVERSE_GEAR, 1);
		gearMap.put(Gear.NONE_GEAR, 2);
		gearMap.put(Gear.FIRST_GEAR, 3);
		gearMap.put(Gear.SECOND_GEAR, 4);
		gearMap.put(Gear.Third_GEAR, 5);
		return gearMap;
		
	}
	
	public static void main(String[] args) {
		System.out.println(GearProgress.newInstance());
	}

}
