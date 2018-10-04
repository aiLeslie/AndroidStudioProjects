package com.example.jean.rockviewtest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.SurfaceHolder.Callback;

public class MySurfaceView extends SurfaceView implements Callback, Runnable {

	private Thread th;
	private SurfaceHolder sfh;
	private Canvas canvas;
	private Paint paint;
	private boolean flag;
	//�̶�ҡ�˱���Բ�ε�X,Y�����Լ��뾶
	private int RockerCircleX = 100;
	private int RockerCircleY = 100;
	private int RockerCircleR = 50;
	//ҡ�˵�X,Y�����Լ�ҡ�˵İ뾶
	private float SmallRockerCircleX = 100;
	private float SmallRockerCircleY = 100;
	private float SmallRockerCircleR = 20;

	public MySurfaceView(Context context) {
		super(context);
		Log.v("Himi", "MySurfaceView");
		this.setKeepScreenOn(true);
		sfh = this.getHolder();
		sfh.addCallback(this);
		paint = new Paint();
		paint.setAntiAlias(true);
		setFocusable(true);
		setFocusableInTouchMode(true);
	}

	public void surfaceCreated(SurfaceHolder holder) {
		th = new Thread(this);
		flag = true;
		th.start();
	}

	/***
	 * �õ�����֮��Ļ���
	 */
	public double getRad(float px1, float py1, float px2, float py2) {
		//�õ�����X�ľ���
		float x = px2 - px1;
		//�õ�����Y�ľ���
		float y = py1 - py2;
		//���б�߳�
		float xie = (float) Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
		//�õ�����Ƕȵ�����ֵ��ͨ�����Ǻ����еĶ��� ���ڱ�/б��=�Ƕ�����ֵ��
		float cosAngle = x / xie;
		//ͨ�������Ҷ����ȡ����ǶȵĻ���
		float rad = (float) Math.acos(cosAngle);
		//ע�⣺��������λ��Y����<ҡ�˵�Y��������Ҫȡ��ֵ-0~-180
		if (py2 < py1) {
			rad = -rad;
		}
		return rad;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
			// �����������ڻ��Χ��
			if (Math.sqrt(Math.pow((RockerCircleX - (int) event.getX()), 2) + Math.pow((RockerCircleY - (int) event.getY()), 2)) >= RockerCircleR) {
				//�õ�ҡ���봥�������γɵĽǶ�
				double tempRad = getRad(RockerCircleX, RockerCircleY, event.getX(), event.getY());
				//��֤�ڲ�СԲ�˶��ĳ�������
				getXY(RockerCircleX, RockerCircleY, RockerCircleR, tempRad);
			} else {//���С�����ĵ�С�ڻ�����������û��������ƶ�����
				SmallRockerCircleX = (int) event.getX();
				SmallRockerCircleY = (int) event.getY();
			}
		} else if (event.getAction() == MotionEvent.ACTION_UP) {
			//���ͷŰ���ʱҡ��Ҫ�ָ�ҡ�˵�λ��Ϊ��ʼλ��
			SmallRockerCircleX = 100;
			SmallRockerCircleY = 100;
		}
		return true;
	}

	/**
	 * 
	 * @param R
	 *            Բ���˶�����ת��
	 * @param centerX
	 *            ��ת��X
	 * @param centerY
	 *            ��ת��Y
	 * @param rad
	 *            ��ת�Ļ���
	 */
	public void getXY(float centerX, float centerY, float R, double rad) {
		//��ȡԲ���˶���X���� 
		SmallRockerCircleX = (float) (R * Math.cos(rad)) + centerX;
		//��ȡԲ���˶���Y����
		SmallRockerCircleY = (float) (R * Math.sin(rad)) + centerY;
	}

	public void draw() {
		try {
			canvas = sfh.lockCanvas();
			canvas.drawColor(Color.WHITE);
			//����͸����
			paint.setColor(0x70000000);
			//����ҡ�˱���
			canvas.drawCircle(RockerCircleX, RockerCircleY, RockerCircleR, paint);
			paint.setColor(0x70ff0000);
			//����ҡ��
			canvas.drawCircle(SmallRockerCircleX, SmallRockerCircleY, SmallRockerCircleR, paint);
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			try {
				if (canvas != null)
					sfh.unlockCanvasAndPost(canvas);
			} catch (Exception e2) {

			}
		}
	}

	public void run() {
		// TODO Auto-generated method stub
		while (flag) {
			draw();
			try {
				Thread.sleep(50);
			} catch (Exception ex) {
			}
		}
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		Log.v("Himi", "surfaceChanged");
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		flag = false;
		Log.v("Himi", "surfaceDestroyed");
	}
}