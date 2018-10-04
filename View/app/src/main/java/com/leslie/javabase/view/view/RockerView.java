package com.leslie.javabase.view.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;

public class RockerView extends MySurfaceView implements MySurfaceView.Drawable {
    public float cx = Integer.MIN_VALUE; // 底盘圆心X坐标
    public float cy = Integer.MIN_VALUE; // 底盘圆心Y坐标
    public float rx = cx; // 摇杆圆心Y坐标
    public float ry = cy; // 摇杆圆心Y坐标
    public float Sradius = 85; //摇杆半径
    public float Mradius = 270; // 摇杆活动范围
    public float Bradius = Mradius + 30; // 摇杆底盘半径
    private boolean isInside = false;
    private Quadrant quadrant; // 象限
    private OnEventListener listener;
    private double radian = 0;

    public static enum Quadrant {
        First, Second, Third, Fourth, Xlow, XHigh, Ylow, YHigh, Midpoint
    }

    public RockerView(Context context, OnEventListener listener) {
        super(context);
        this.listener = listener;
    }

    public RockerView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public RockerView setListener(OnEventListener listener) {
        this.listener = listener;
        return this;
    }

    {
        setDrawable(this);
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:

                // 是否操控摇杆
                if (getDistance(cx, cy, motionEvent.getX(), motionEvent.getY()) <= Bradius) {
                    isInside = true;
                }
                return true;
            case MotionEvent.ACTION_MOVE:
                if (isInside) {
                    setPosition(motionEvent.getX(), motionEvent.getY());

                }


                return true;
            case MotionEvent.ACTION_UP:
                isInside = false;
                rx = cx;
                ry = cy;
                return true;
            default:
                return true;
        }
    }

    boolean getSize = false;

    @Override
    public void ondraw() {
        if (!getSize) {
            cx = getWidth() / 2;
            cy = getHeight() / 2;
            rx = cx;
            ry = cy;
            getSize = true;
        }

        /**
         * 绘画最外层摇杆底盘
         */
        paint.setColor(Color.parseColor("#808080"));
        canvas.drawCircle(cx, cy, Bradius, paint);
        /**
         * 绘画最内层摇杆底盘
         */

        if (isInside) {
            paint.setColor(Color.parseColor("#FFCCCCCC"));
        } else {
            paint.setColor(Color.parseColor("#FFAAAAAA"));
        }
        canvas.drawCircle(cx, cy, Mradius, paint);


//        /**
//         * 绘画摇杆柱头和柱尾
//         */
//        paint.setColor(Color.parseColor("#FF3D3D3D"));
//        canvas.drawCircle(cx, cy, 40, paint);
//        canvas.drawCircle(rx, ry, 30, paint);
//        /**
//         * 绘画摇杆柱身
//         */
//        float strokeWidth = paint.getStrokeWidth();
//        paint.setStrokeWidth(strokeWidth + 40);
//        canvas.drawLine(rx, ry, cx, cy, paint);
//        paint.setStrokeWidth(strokeWidth);
        /**
         * 绘画摇杆头部
         */
        paint.setColor(Color.parseColor("#ff8d8d"));
        canvas.drawCircle(rx, ry, Sradius, paint);



    }

    private void setPosition(float x, float y) {
        setQuadrant(x, y);
        // 如果在摇杆圆盘里面
        if (getDistance(cx, cy, x, y) < Mradius) {

            rx = x;
            ry = y;


            if (getDistance(cx, cy, x, y) >= Mradius / 3) {
                setRadian(x, y);
                listener.onEvent(quadrant, radian, getDistance(cx, cy, rx, ry) / Mradius);
            }


        } else if (getDistance(cx, cy, x, y) >= Mradius) {

            radian = 0;
            if (quadrant == Quadrant.XHigh) {
                rx = cx + Mradius - Sradius;
                ry = cy;

            } else if (quadrant == Quadrant.Xlow) {
                rx = cx - Mradius + Sradius;
                ry = cy;
            } else if (quadrant == Quadrant.YHigh) {
                rx = cx;
                ry = cy + Mradius - Sradius;
            } else if (quadrant == Quadrant.Ylow) {
                rx = cx;
                ry = cy - Mradius + Sradius;
            } else {

                setRadian(x, y);
                if (quadrant == Quadrant.First) {
                    rx = cx + (Mradius - Sradius) * (float) Math.cos(radian);
                    ry = cy + (Mradius - Sradius) * (float) Math.sin(radian);
                } else if (quadrant == Quadrant.Second) {
                    rx = cx - (Mradius - Sradius) * (float) Math.cos(radian);
                    ry = cy - (Mradius - Sradius) * (float) Math.sin(radian);
                } else if (quadrant == Quadrant.Third) {
                    rx = cx - (Mradius - Sradius) * (float) Math.cos(radian);
                    ry = cy - (Mradius - Sradius) * (float) Math.sin(radian);
                } else if (quadrant == Quadrant.Fourth) {
                    rx = cx + (Mradius - Sradius) * (float) Math.cos(radian);
                    ry = cy + (Mradius - Sradius) * (float) Math.sin(radian);
                }
            }
            listener.onEvent(quadrant, radian, getDistance(cx, cy, rx, ry) / Mradius);
        }


    }

    private void setRadian(float x, float y) {
        radian = Math.atan((y - cy) / (x - cx));
    }

    /**
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return 获得两点之间的距离
     */
    public static float getDistance(float x1, float y1, float x2, float y2) {
        return (float) Math.sqrt(Math.pow(Math.abs(x1 - x2), 2) + Math.pow(Math.abs(y1 - y2), 2));
    }

    public Quadrant setQuadrant(float x, float y) {

        float changeX = x - cx;
        float changeY = y - cy;
        if (changeX == 0 && changeY > 0) {
            quadrant = Quadrant.YHigh;
        } else if (changeX == 0 && changeY < 0) {
            quadrant = Quadrant.Ylow;
        } else if (changeY == 0 && changeX > 0) {
            quadrant = Quadrant.XHigh;
        } else if (changeY == 0 && changeX < 0) {
            quadrant = Quadrant.Xlow;
        } else if (changeX > 0 && changeY > 0) {
            quadrant = Quadrant.First;
        } else if (changeX > 0 && changeY < 0) {
            quadrant = Quadrant.Fourth;
        } else if (changeX < 0 && changeY > 0) {
            quadrant = Quadrant.Second;
        } else if (changeX < 0 && changeY < 0) {
            quadrant = Quadrant.Third;
        } else {
            quadrant = Quadrant.Midpoint;
        }
        return quadrant;
    }


    public interface OnEventListener {
        void onEvent(Quadrant quadrant, double radian, double progress);
    }


    public static class EventParese {
        public static final int LEFT = 0;
        public static final int TOP = 1;
        public static final int RIGHT = 2;
        public static final int BOTTOM = 3;

        public static int parseOrientation(Quadrant quadrant, double radian) {
            if (quadrant == RockerView.Quadrant.First) {

                if (Math.abs(radian) > Math.PI / 4) {
                    return BOTTOM;
                } else {
                    return RIGHT;
                }
            } else if (quadrant == RockerView.Quadrant.Second) {
                if (Math.abs(radian) > Math.PI / 4) {
                    return BOTTOM;
                } else {
                    return LEFT;
                }
            } else if (quadrant == RockerView.Quadrant.Third) {
                if (Math.abs(radian) > Math.PI / 4) {
                    return TOP;
                } else {
                    return LEFT;
                }
            } else if (quadrant == RockerView.Quadrant.Fourth) {
                if (Math.abs(radian) > Math.PI / 4) {
                    return TOP;
                } else {
                    return RIGHT;
                }
            } else {
                if (quadrant == Quadrant.XHigh) {
                    return LEFT;
                } else if (quadrant == Quadrant.Xlow) {
                    return RIGHT;

                } else if (quadrant == Quadrant.YHigh) {
                    return BOTTOM;

                } else if (quadrant == Quadrant.Ylow) {
                    return TOP;
                } else {
                    return -1;
                }
            }

        }
    }
}
