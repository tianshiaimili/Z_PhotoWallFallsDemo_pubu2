package com.example.photowallfallsdemo2;

import java.io.File;

import com.example.photowallfallsdemo.Images;
import com.example.photowallfallsdemo.R;
import com.example.photowallfallsdemo.ZoomImageView;

import android.R.integer;
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
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 查看大图的Activity界面。
 * 
 * @author guolin
 */
public class ImageDetailsActivity2 extends Activity implements
		OnPageChangeListener {

	/**
	 * 用于管理图片的滑动
	 */
	private ViewPager viewPager2;

	/**
	 * 显示当前图片的页数
	 */
	private TextView pageText2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.image_details2);
		viewPager2 = (ViewPager)findViewById(R.id.view_pager2);
		pageText2 = (TextView) findViewById(R.id.page_text2);
		int imagePosition = getIntent().getIntExtra("image_position", 0);
		MyViewPagerAdapter adapter = new MyViewPagerAdapter();
		viewPager2.setAdapter(adapter);
		viewPager2.setCurrentItem(imagePosition);
		viewPager2.setEnabled(false);
		viewPager2.setOnPageChangeListener(this);
		pageText2.setText((imagePosition+1)+"/"+Images.imageUrls.length);
	
	}
	
	/**
	 * 
	 */
	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageSelected(int arg0) {
		// TODO Auto-generated method stub
		// 每当页数发生改变时重新设定一遍当前的页数和总页数
		pageText2.setText((arg0 + 1) + "/" + Images.imageUrls.length);
	}

	
	class MyViewPagerAdapter extends PagerAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return Images.imageUrls.length;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == arg1;
		}
		
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			// TODO Auto-generated method stub
			String imagePath = getImagePath(Images.imageUrls[position]);
			Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
			if(bitmap == null){
				//没有图片 就用空的代替
				bitmap = BitmapFactory.decodeResource(getResources(),
						R.drawable.empty_photo);
			}
			ViewGroup.LayoutParams params = 
					new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			ImageView imageView = new ImageView(getBaseContext());
			imageView.setImageBitmap(bitmap);
			container.addView(imageView);
			return imageView;
		}
		
		/**
		 * 
		 */
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// TODO Auto-generated method stub
//			super.destroyItem(container, position, object);
			View view = (View)object;
			container.removeView(view);
		}
	}


	public String getImagePath(String imageUrl) {
		// TODO Auto-generated method stub
		int lastSlashIndex = imageUrl.lastIndexOf("/");
		String imageName = imageUrl.substring(lastSlashIndex+1);
		String imageDir = Environment.getExternalStorageDirectory().getPath()+
				"/PhotoWallFalls/";
		File file = new File(imageDir);
		if(!file.exists()){
			file.mkdir();
		}
		String imagePath = imageDir +imageName;
		return imagePath;
	}
	
	
}