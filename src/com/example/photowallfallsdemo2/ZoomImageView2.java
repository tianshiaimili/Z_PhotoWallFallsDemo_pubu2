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
	
	
	
}
