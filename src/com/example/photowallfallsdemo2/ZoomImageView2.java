package com.example.photowallfallsdemo2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.AttributeSet;
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
	
	
	
}
