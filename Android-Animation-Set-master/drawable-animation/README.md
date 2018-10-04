# Ⅱ. Drawable Animation （Drawable动画）使用详解

## 1. Drawable 动画概述

Drawable 动画其实就是 Frame 动画（帧动画），它允许你实现像播放幻灯片一样的效果，这种动画的实质其实是 Drawable，
所以这种动画的 XML 定义方式文件一般放在 `res/drawable/` 目录下。具体关于帧动画的 xml 使用方式翻墙点击我查看，
java方式翻墙点击我查看。

如下图就是帧动画的源码文件：

![AnimationDrawable class](https://raw.githubusercontent.com/OCNYang/Android-Animation-Set/master/README_Res/animation_drawable.jpg?token=AQ83MlWYnnPDJoSwlBJmUrqALx9-eZQEks5awY-CwA%3D%3D)  

可以看见实际的真实父类就是 Drawable。

## 2. Drawable 动画详细说明

我们依旧可以使用 xml 或者 java 方式实现帧动画。但是依旧推荐使用 xml，具体如下：

`<animation-list>` 必须是根节点，包含一个或者多个`<item>`元素，属性有：

* `android:oneshot` true 代表只执行一次，false 循环执行。
* `<item>` 类似一帧的动画资源。

`<item>` animation-list 的子项，包含属性如下：

* android:drawable 一个 frame 的 Drawable 资源。
* android:duration 一个 frame 显示多长时间。

## 3. Drawable 动画实例演示

关于帧动画相对来说比较简单，这里给出一个常规使用框架，如下：

    <!-- 注意：rocket.xml文件位于res/drawable/目录下 -->
    <?xml version="1.0" encoding="utf-8"?>
    <animation-list xmlns:android="http://schemas.android.com/apk/res/android"
        android:oneshot=["true" | "false"] >
        <item
            android:drawable="@[package:]drawable/drawable_resource_name"
            android:duration="integer" />
    </animation-list>

使用：

    ImageView rocketImage = (ImageView) findViewById(R.id.rocket_image);
    rocketImage.setBackgroundResource(R.drawable.rocket_thrust);
    
    rocketAnimation = (AnimationDrawable) rocketImage.getBackground();
    rocketAnimation.start();

特别注意，AnimationDrawable 的 start() 方法不能在 Activity 的 onCreate 方法中调运，因为 AnimationDrawable 还未完全附着到 window 上，
所以最好的调运时机是 onWindowFocusChanged() 方法中。

至此帧动画也就说明完成了。让我们接下来进入 Android 更牛叉的动画类型。



附录：  
本文摘录自：[工匠若水 - Android应用开发之所有动画使用详解](https://blog.csdn.net/yanbober/article/details/46481171)  