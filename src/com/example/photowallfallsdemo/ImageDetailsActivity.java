package com.example.photowallfallsdemo;

import java.io.File;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

/**
 * �鿴��ͼ��Activity���档
 * 
 * @author guolin
 */
public class ImageDetailsActivity extends Activity implements
		OnPageChangeListener {

	/**
	 * ���ڹ���ͼƬ�Ļ���
	 */
	private ViewPager viewPager;

	/**
	 * ��ʾ��ǰͼƬ��ҳ��
	 */
	private TextView pageText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.image_details);
		int imagePosition = getIntent().getIntExtra("image_position", 0);
		pageText = (TextView) findViewById(R.id.page_text);
		viewPager = (ViewPager) findViewById(R.id.view_pager);
		ViewPagerAdapter adapter = new ViewPagerAdapter();
		viewPager.setAdapter(adapter);
		viewPager.setCurrentItem(imagePosition);
		viewPager.setOnPageChangeListener(this);
		viewPager.setEnabled(false);
		// �趨��ǰ��ҳ������ҳ��
		pageText.setText((imagePosition + 1) + "/" + Images.imageUrls.length);
	}

	/**
	 * ViewPager��������
	 * 
	 * @author guolin
	 */
	class ViewPagerAdapter extends PagerAdapter {

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			String imagePath = getImagePath(Images.imageUrls[position]);
			Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
			if (bitmap == null) {
				bitmap = BitmapFactory.decodeResource(getResources(),
						R.drawable.empty_photo);
			}
			View view = LayoutInflater.from(ImageDetailsActivity.this).inflate(
					R.layout.zoom_image_layout, null);
			ZoomImageView zoomImageView = (ZoomImageView) view
					.findViewById(R.id.zoom_image_view);
			zoomImageView.setImageBitmap(bitmap);
			container.addView(view);
			return view;
		}

		@Override
		public int getCount() {
			return Images.imageUrls.length;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			View view = (View) object;
			container.removeView(view);
		}

	}

	/**
	 * ��ȡͼƬ�ı��ش洢·����
	 * 
	 * @param imageUrl
	 *            ͼƬ��URL��ַ��
	 * @return ͼƬ�ı��ش洢·����
	 */
	private String getImagePath(String imageUrl) {
		int lastSlashIndex = imageUrl.lastIndexOf("/");
		String imageName = imageUrl.substring(lastSlashIndex + 1);
		String imageDir = Environment.getExternalStorageDirectory().getPath()
				+ "/PhotoWallFalls/";
		File file = new File(imageDir);
		if (!file.exists()) {
			file.mkdirs();
		}
		String imagePath = imageDir + imageName;
		return imagePath;
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int currentPage) {
		// ÿ��ҳ�������ı�ʱ�����趨һ�鵱ǰ��ҳ������ҳ��
		pageText.setText((currentPage + 1) + "/" + Images.imageUrls.length);
	}

}