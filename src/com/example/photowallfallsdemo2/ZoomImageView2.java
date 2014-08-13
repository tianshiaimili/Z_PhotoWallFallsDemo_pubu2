package com.example.photowallfallsdemo2;

import com.example.util.LogUtils2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * 
 * @author Hua
 *
 */
public class ZoomImageView2 extends View{

	/**
	 * ��ʼ��״̬����
	 */
	public static final int STATUS_INIT = 1;

	/**
	 * ͼƬ�Ŵ�״̬����
	 */
	public static final int STATUS_ZOOM_OUT = 2;

	/**
	 * ͼƬ��С״̬����
	 */
	public static final int STATUS_ZOOM_IN = 3;

	/**
	 * ͼƬ�϶�״̬����
	 */
	public static final int STATUS_MOVE = 4;

	/**
	 * ���ڶ�ͼƬ�����ƶ������ű任�ľ���
	 */
	private Matrix matrix = new Matrix();

	/**
	 * ��չʾ��Bitmap����
	 */
	private Bitmap sourceBitmap;

	/**
	 * ��¼��ǰ������״̬����ѡֵΪSTATUS_INIT��STATUS_ZOOM_OUT��STATUS_ZOOM_IN��STATUS_MOVE
	 */
	private int currentStatus;

	/**
	 * ZoomImageView�ؼ��Ŀ��
	 */
	private int width;

	/**
	 * ZoomImageView�ؼ��ĸ߶�
	 */
	private int height;

	/**
	 * ��¼��ָͬʱ������Ļ��ʱ�����ĵ�ĺ�����ֵ
	 */
	private float centerPointX;

	/**
	 * ��¼��ָͬʱ������Ļ��ʱ�����ĵ��������ֵ
	 */
	private float centerPointY;

	/**
	 * ��¼��ǰͼƬ�Ŀ�ȣ�ͼƬ������ʱ�����ֵ��һ��䶯
	 */
	private float currentBitmapWidth;

	/**
	 * ��¼��ǰͼƬ�ĸ߶ȣ�ͼƬ������ʱ�����ֵ��һ��䶯
	 */
	private float currentBitmapHeight;

	/**
	 * ��¼�ϴ���ָ�ƶ�ʱ�ĺ�����
	 */
	private float lastXMove = -1;

	/**
	 * ��¼�ϴ���ָ�ƶ�ʱ��������
	 */
	private float lastYMove = -1;

	/**
	 * ��¼��ָ�ں����귽���ϵ��ƶ�����
	 */
	private float movedDistanceX;

	/**
	 * ��¼��ָ�������귽���ϵ��ƶ�����
	 */
	private float movedDistanceY;

	/**
	 * ��¼ͼƬ�ھ����ϵĺ���ƫ��ֵ
	 */
	private float totalTranslateX;

	/**
	 * ��¼ͼƬ�ھ����ϵ�����ƫ��ֵ
	 */
	private float totalTranslateY;

	/**
	 * ��¼ͼƬ�ھ����ϵ������ű���
	 */
	private float totalRatio;

	/**
	 * ��¼��ָ�ƶ��ľ�������ɵ����ű���
	 */
	private float scaledRatio;

	/**
	 * ��¼ͼƬ��ʼ��ʱ�����ű���
	 */
	private float initRatio;

	/**
	 * ��¼�ϴ���ָ֮��ľ���
	 */
	private double lastFingerDis;

	
	public ZoomImageView2(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public ZoomImageView2(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		currentStatus = STATUS_INIT;
		
	}
	
	/**
	 * ����չʾ��ͼƬ���ý�����
	 * 
	 * @param bitmap
	 *            ��չʾ��Bitmap����
	 */
	public void setImageBitmap(Bitmap bitmap){
		sourceBitmap = bitmap;
		invalidate();
	}
	
	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		// TODO Auto-generated method stub
		super.onLayout(changed, left, top, right, bottom);
		if(changed){
			
			width = getWidth();
			height = getHeight();
			
		}
	}
	
	/**
	 * 
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		if(initRatio == totalRatio){
			getParent().requestDisallowInterceptTouchEvent(false);
		}else {
			getParent().requestDisallowInterceptTouchEvent(true);
		}
		
		int code = event.getActionMasked();
		
		switch (code) {
		case MotionEvent.ACTION_POINTER_DOWN:
		int pointerCount = event.getPointerCount();
		//˫ָ
		if(pointerCount == 2){
			
			lastFingerDis = distanceBetweenFingers(event);
			
		}else if(pointerCount == 1){
			
		}
			break;
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_MOVE:
			/**
			 * ��ָ��ʾ�϶�״̬��
			 */
			if(event.getPointerCount() == 1){
				float xMove = event.getX();
				float yMove = event.getY();
				if(lastXMove == -1 && lastYMove == -1){
					lastXMove = xMove;
					lastYMove = yMove;
				}
				
				currentStatus = STATUS_MOVE;
				movedDistanceX = xMove - lastXMove;
				movedDistanceY = yMove - lastYMove;
				LogUtils2.i("movedDistanceX=="+movedDistanceX);
				LogUtils2.i("movedDistanceY=="+movedDistanceY);
				LogUtils2.i("xMove=="+xMove);
				LogUtils2.i("yMove=="+yMove);
				LogUtils2.i("lastXMove=="+lastXMove);
				LogUtils2.i("lastXMove=="+lastXMove);
				LogUtils2.i("totalTranslateX=="+totalTranslateX);
				LogUtils2.i("totalTranslateY=="+totalTranslateY);
				
				// ���б߽��飬������ͼƬ�ϳ��߽�
				if(totalTranslateX + movedDistanceX > 0){
					movedDistanceX = 0;
				}else if(width - (totalTranslateX + movedDistanceX) > currentBitmapWidth){
					movedDistanceX = 0;
				}
				
				if (totalTranslateY + movedDistanceY > 0) {
					movedDistanceY = 0;
				} else if (height - (totalTranslateY + movedDistanceY) > currentBitmapHeight) {
					movedDistanceY = 0;
				}
				
				// ����onDraw()��������ͼƬ
				invalidate();
				lastXMove = xMove;
				lastYMove = yMove;
				
				
			}else if(event.getPointerCount() == 2) {
				
				// ��������ָ������Ļ���ƶ�ʱ��Ϊ����״̬
				centerPointBetweenFingers(event);
				//type name = new type(arguments);
				double fingerDis = distanceBetweenFingers(event);
				if(fingerDis > lastFingerDis){
					currentStatus = STATUS_ZOOM_OUT;
				}else {
					currentStatus = STATUS_ZOOM_IN;
				}
				
				// �������ű�����飬���ֻ����ͼƬ�Ŵ�4������С������С����ʼ������
				if ((currentStatus == STATUS_ZOOM_OUT && totalRatio < 4 * initRatio)
						|| (currentStatus == STATUS_ZOOM_IN && totalRatio > initRatio)) {
					
					scaledRatio = (float)(fingerDis / lastFingerDis);
					
					totalRatio = totalRatio * scaledRatio;
					
					if (totalRatio > 4 * initRatio) {
						totalRatio = 4 * initRatio;
					} else if (totalRatio < initRatio) {
						totalRatio = initRatio;
					}
					
					// ����onDraw()��������ͼƬ
					invalidate();
					lastFingerDis = fingerDis;
					
				}
				
			}
			
			break;
			
		case MotionEvent.ACTION_POINTER_UP:
			if (event.getPointerCount() == 2) {
				// ��ָ�뿪��Ļʱ����ʱֵ��ԭ
				lastXMove = -1;
				lastYMove = -1;
			}
			break;
		case MotionEvent.ACTION_UP:
			// ��ָ�뿪��Ļʱ����ʱֵ��ԭ
			lastXMove = -1;
			lastYMove = -1;
			break;
			
		default:
			break;
		}
		return true;//super.onTouchEvent(event);
	}

	/**
	 * ����currentStatus��ֵ��������ͼƬ����ʲô���Ļ��Ʋ�����
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		LogUtils2.i("onDraw............");
		
		switch (currentStatus) {
		case STATUS_ZOOM_OUT:
		case STATUS_ZOOM_IN:
			zoom(canvas);
			break;
		case STATUS_MOVE:
			move(canvas);
			break;
		case STATUS_INIT:
			initBitmap(canvas);
		default:
			if (sourceBitmap != null) {
				canvas.drawBitmap(sourceBitmap, matrix, null);
			}
			break;
		}
	}
	
	private void initBitmap(Canvas canvas) {
		// TODO Auto-generated method stub
		
	}

	private void move(Canvas canvas) {
		// TODO Auto-generated method stub
		
	}

	private void zoom(Canvas canvas) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * ����������ָ֮�����ĵ�����ꡣ
	 * 
	 * @param event
	 */
	private void centerPointBetweenFingers(MotionEvent event) {
		// TODO Auto-generated method stub
		
		float xPoint0 = event.getX(0);
		float yPoint0 = event.getY(0);
		float xPoint1 = event.getX(1);
		float yPoint1 = event.getY(1);
		centerPointX = (xPoint0 + xPoint1) / 2;
		centerPointY = (yPoint0 + yPoint1) / 2;
		
	}

	/**
	 * ����������ָ֮��ľ��롣
	 * 
	 * @param event
	 * @return ������ָ֮��ľ���
	 */
	private double distanceBetweenFingers(MotionEvent event) {
		// TODO Auto-generated method stub
		int pointer0X = (int) event.getX(0);
		int pointer0Y = (int) event.getY(0);
		int pointer1X = (int) event.getX(1);
		int pointer1Y = (int) event.getY(1);
		
		float disX = Math.abs(pointer0X - pointer1X);
		float disY = Math.abs(pointer0Y - pointer1Y);
		
		double dis = Math.sqrt(disX* disX + disY * disY);
		return dis;
	}
	
	
}
