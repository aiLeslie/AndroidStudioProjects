package com.leslie.clocktest;

import android.graphics.Color;

import java.util.Random;

public class RandomColorUtils {
    private static Random random = new Random();

    public static int getColor() {
        return random.nextInt(Color.WHITE - Color.BLACK) + Color.BLACK;

    }

}
