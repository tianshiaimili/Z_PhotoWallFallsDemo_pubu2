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
	 * 初始化状态常量
	 */
	public static final int STATUS_INIT = 1;

	/**
	 * 图片放大状态常量
	 */
	public static final int STATUS_ZOOM_OUT = 2;

	/**
	 * 图片缩小状态常量
	 */
	public static final int STATUS_ZOOM_IN = 3;

	/**
	 * 图片拖动状态常量
	 */
	public static final int STATUS_MOVE = 4;

	/**
	 * 用于对图片进行移动和缩放变换的矩阵
	 */
	private Matrix matrix = new Matrix();

	/**
	 * 待展示的Bitmap对象
	 */
	private Bitmap sourceBitmap;

	/**
	 * 记录当前操作的状态，可选值为STATUS_INIT、STATUS_ZOOM_OUT、STATUS_ZOOM_IN和STATUS_MOVE
	 */
	private int currentStatus;

	/**
	 * ZoomImageView控件的宽度
	 */
	private int width;

	/**
	 * ZoomImageView控件的高度
	 */
	private int height;

	/**
	 * 记录两指同时放在屏幕上时，中心点的横坐标值
	 */
	private float centerPointX;

	/**
	 * 记录两指同时放在屏幕上时，中心点的纵坐标值
	 */
	private float centerPointY;

	/**
	 * 记录当前图片的宽度，图片被缩放时，这个值会一起变动
	 */
	private float currentBitmapWidth;

	/**
	 * 记录当前图片的高度，图片被缩放时，这个值会一起变动
	 */
	private float currentBitmapHeight;

	/**
	 * 记录上次手指移动时的横坐标
	 */
	private float lastXMove = -1;

	/**
	 * 记录上次手指移动时的纵坐标
	 */
	private float lastYMove = -1;

	/**
	 * 记录手指在横坐标方向上的移动距离
	 */
	private float movedDistanceX;

	/**
	 * 记录手指在纵坐标方向上的移动距离
	 */
	private float movedDistanceY;

	/**
	 * 记录图片在矩阵上的横向偏移值
	 */
	private float totalTranslateX;

	/**
	 * 记录图片在矩阵上的纵向偏移值
	 */
	private float totalTranslateY;

	/**
	 * 记录图片在矩阵上的总缩放比例
	 */
	private float totalRatio;

	/**
	 * 记录手指移动的距离所造成的缩放比例
	 */
	private float scaledRatio;

	/**
	 * 记录图片初始化时的缩放比例
	 */
	private float initRatio;

	/**
	 * 记录上次两指之间的距离
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
	 * 将待展示的图片设置进来。
	 * 
	 * @param bitmap
	 *            待展示的Bitmap对象
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
		//双指
		if(pointerCount == 2){
			
			lastFingerDis = distanceBetweenFingers(event);
			
		}else if(pointerCount == 1){
			
		}
			break;
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_MOVE:
			/**
			 * 单指表示拖动状态了
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
				
				// 进行边界检查，不允许将图片拖出边界
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
				
				// 调用onDraw()方法绘制图片
				invalidate();
				lastXMove = xMove;
				lastYMove = yMove;
				
				
			}else if(event.getPointerCount() == 2) {
				
				// 有两个手指按在屏幕上移动时，为缩放状态
				centerPointBetweenFingers(event);
				//type name = new type(arguments);
				double fingerDis = distanceBetweenFingers(event);
				if(fingerDis > lastFingerDis){
					currentStatus = STATUS_ZOOM_OUT;
				}else {
					currentStatus = STATUS_ZOOM_IN;
				}
				
				// 进行缩放倍数检查，最大只允许将图片放大4倍，最小可以缩小到初始化比例
				if ((currentStatus == STATUS_ZOOM_OUT && totalRatio < 4 * initRatio)
						|| (currentStatus == STATUS_ZOOM_IN && totalRatio > initRatio)) {
					
					scaledRatio = (float)(fingerDis / lastFingerDis);
					
					totalRatio = totalRatio * scaledRatio;
					
					if (totalRatio > 4 * initRatio) {
						totalRatio = 4 * initRatio;
					} else if (totalRatio < initRatio) {
						totalRatio = initRatio;
					}
					
					// 调用onDraw()方法绘制图片
					invalidate();
					lastFingerDis = fingerDis;
					
				}
				
			}
			
			break;
			
		case MotionEvent.ACTION_POINTER_UP:
			if (event.getPointerCount() == 2) {
				// 手指离开屏幕时将临时值还原
				lastXMove = -1;
				lastYMove = -1;
			}
			break;
		case MotionEvent.ACTION_UP:
			// 手指离开屏幕时将临时值还原
			lastXMove = -1;
			lastYMove = -1;
			break;
			
		default:
			break;
		}
		return true;//super.onTouchEvent(event);
	}

	/**
	 * 根据currentStatus的值来决定对图片进行什么样的绘制操作。
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
	 * 计算两个手指之间中心点的坐标。
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
	 * 计算两个手指之间的距离。
	 * 
	 * @param event
	 * @return 两个手指之间的距离
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
