package com.leslie.viewpage;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private ImageView imageView;
    private List<View> lists = new ArrayList<View>();
    private ViewPagerAdapter adapter;
    private Bitmap cursor;
    private int offSet;
    private int currentItem;
    private Matrix matrix = new Matrix();
    private int bmWidth;
    private Animation animation;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 随页面滑动图片
        imageView = (ImageView) findViewById(R.id.viewpaget_img);
        // 热门商圈和热门分类 页面添加到viewPager集合
        lists.add(getLayoutInflater().inflate(R.layout.search_hot_shangqu, null));
        lists.add(getLayoutInflater().inflate(R.layout.search_hot_fenlei, null));
        // 初始化滑动图片位置
        initeCursor();
        adapter = new ViewPagerAdapter(lists);
        viewPager = (ViewPager) findViewById(R.id.search_viewpager);
        viewPager.setAdapter(adapter);
        // ViewPager滑动监听器
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                // TODO Auto-generated method stub
                // 当滑动时，顶部的imageView是通过animation缓慢的滑动
                switch (arg0) {
                    case 0:
                        if (currentItem == 1) {
                            animation = new TranslateAnimation(offSet * 2 + bmWidth, 0, 0, 0);
                        } else if (currentItem == 2) {
                            animation = new TranslateAnimation(offSet * 4 + 2 * bmWidth, 0, 0, 0);
                        }

                        break;
                    case 1:
                        if (currentItem == 0) {
                            animation = new TranslateAnimation(0, offSet * 2 + bmWidth, 0, 0);
                        } else if (currentItem == 2) {
                            animation = new TranslateAnimation(4 * offSet + 2 * bmWidth, offSet * 2 + bmWidth, 0, 0);
                        }


                        break;
                }
                currentItem = arg0;
                animation.setDuration(500);
                animation.setFillAfter(true);
                imageView.startAnimation(animation);

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub

            }
        });

    }


    /**
     * 计算滑动的图片的位置
     */
    private void initeCursor() {
        cursor = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_background);
        bmWidth = cursor.getWidth();
        DisplayMetrics dm;
        dm = getResources().getDisplayMetrics();
        offSet = (dm.widthPixels - 2 * bmWidth) / 4;
        matrix.setTranslate(offSet, 0);
        imageView.setImageMatrix(matrix); // 需要iamgeView的scaleType为matrix
        currentItem = 0;
    }
}


