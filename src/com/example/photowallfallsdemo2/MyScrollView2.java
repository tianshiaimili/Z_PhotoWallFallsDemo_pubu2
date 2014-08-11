package com.example.photowallfallsdemo2;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.photowallfallsdemo.ImageDetailsActivity;
import com.example.photowallfallsdemo.ImageLoader;
import com.example.photowallfallsdemo.Images;
import com.example.photowallfallsdemo.R;
import com.example.util.LogUtils2;

/**
 * 啦啦啦德玛西亚
 * @author Hua
 *
 */
public class MyScrollView2 extends ScrollView implements OnTouchListener{

	/**
	 * 每页要加载的图片数量
	 */
	public static final int PAGE_SIZE = 20;
	/**
	 * 记录当前已加载到第几页
	 */
	private int currentPage;
	/**
	 * 每一列的宽度
	 */
	private int columnWidth;

	/**
	 * 当前第一列的高度
	 */
	private int firstColumnHeight;
	/**
	 * 当前第二列的高度
	 */
	private int secondColumnHeight;

	/**
	 * 当前第三列的高度
	 */
	private int thirdColumnHeight;
	/**
	 * 当前第四列的高度
	 */
	private  int fourColumnHeight;
	/**
	 * 是否已加载过一次layout，这里onLayout中的初始化只需加载一次
	 */
	private boolean loadOnce;
	/**
	 * 第一列的布局
	 */
	private LinearLayout firstColumnLayout;

	/**
	 * 第二列的布局
	 */
	private LinearLayout secondColumnLayout;

	/**
	 * 第三列的布局
	 */
	private LinearLayout thirdColumnLayout;
	
	/**
	 * 第四列的布局
	 */
	private LinearLayout fourColumnLayout; 
	
	/**
	 * MyScrollView下的直接子布局。
	 */
	private static View subScrollLayout;

	/**
	 * MyScrollView布局的高度。
	 */
	private static int scrollViewHeight;

	/**
	 * 记录上垂直方向的滚动距离。
	 */
	private static int lastScrollY = -1;

	/**
	 * 记录所有界面上的图片，用以可以随时控制对图片的释放。
	 */
	private List<ImageView> imageViewList = new ArrayList<ImageView>();
	
	/**
	 * 对图片进行管理的工具类
	 */
	private ImageLoader imageLoader;

	/**
	 * 记录所有正在下载或等待下载的任务。
	 */
	private static Set<LoadImageTask2> taskCollection;

	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			MyScrollView2 scrollView2 = (MyScrollView2) msg.obj;
			int scrollY = scrollView2.getScrollY();
			/**
			 * 当滑动到当前subScrollLayout底部（注意subScrollLayout 的高度是动态改变的）
			 * 且 图片都加载完成也就是taskCollection种的线程为0了
			 * 那就加载下一页
			 */
			if(lastScrollY == scrollY){
				if((scrollY + scrollViewHeight ) >= subScrollLayout.getHeight()
						&& taskCollection.isEmpty()){
					scrollView2.loadMoreImages();
					
				}
				scrollView2.checkVisibility();
			}else {
				
				lastScrollY = scrollY;
				Message message = handler.obtainMessage(0, scrollView2);
				handler.sendMessageDelayed(message, 5);
				
			}
			
			
			
		};
	};
	
	public MyScrollView2(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	protected void checkVisibility() {
		// TODO Auto-generated method stub
		for(int i = 0; i < imageViewList.size(); i++){
			
			ImageView imageView = imageViewList.get(i);
			int borderTop  = (Integer) imageView.getTag(R.string.border_top);
			int borderBottom  = (Integer) imageView.getTag(R.string.border_bottom);
			if(borderBottom > getScrollY() && borderTop < getScrollY() + scrollViewHeight){
				String imageUrl = (String) imageView.getTag(R.string.image_url);
				Bitmap bitmap = imageLoader.getBitmapFromMemoryCache(imageUrl);
				if(bitmap != null){
					imageView.setImageBitmap(bitmap);
				}else {
					LoadImageTask2 task2 = new LoadImageTask2();
					task2.execute(i);
				}
				
			}else {
				imageView.setImageResource(R.drawable.empty_photo);
			}
			
		}
		
	}

	public MyScrollView2(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		imageLoader = ImageLoader.getInstance();
		taskCollection = new HashSet<MyScrollView2.LoadImageTask2>();
		setOnTouchListener(this);
		
	}
	
	/**
	 * * 进行一些关键性的初始化操作，获取MyScrollView的高度，以及得到第一列的宽度值。并在这里开始加载第一页的图片。
	 */
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		super.onLayout(changed, l, t, r, b);
		
		//这里是给子view赋值了，和初始化一系列东西
		if(changed && !loadOnce){
			//获取当前ScrollView的高度
			scrollViewHeight = getHeight();
			/**
			 * 这里是获取ScrollView下的第一个子View，在activity_main2.xml文件
			 * 中第一个子View应该是一个LinearLayout
			 */
			subScrollLayout = getChildAt(0);
			LogUtils2.i("subScrollLayout = getChildAt==="+subScrollLayout);
			///
			firstColumnLayout = (LinearLayout) findViewById(R.id.first_column2);
			secondColumnLayout = (LinearLayout) findViewById(R.id.second_column2);
			thirdColumnLayout = (LinearLayout) findViewById(R.id.third_column2);
			fourColumnLayout = (LinearLayout) findViewById(R.id.four_column2);
			columnWidth = firstColumnLayout.getWidth();
			loadOnce = true;
			/**
			 * 加载图片
			 */
			loadMoreImages();
			
		}
		
	}
	
	/**
	 * 开始加载下一页的图片，每张图片都会开启一个异步线程去下载。
	 */
	private void loadMoreImages() {
		// TODO Auto-generated method stub
		if(haveSDCard()){
			
			int startIndex = currentPage * PAGE_SIZE;
			int endIndex = currentPage * PAGE_SIZE + PAGE_SIZE;
			int countImagesLenght = Images.imageUrls.length;
			if(startIndex < countImagesLenght){
				
				Toast.makeText(getContext(), "正在加载...", Toast.LENGTH_SHORT).show();
				if(endIndex > countImagesLenght){
					endIndex = countImagesLenght;
				}
				////
				for(int i=startIndex ;i < endIndex ;i++){
					
					LoadImageTask2 loadImageTask2 = new LoadImageTask2();
					taskCollection.add(loadImageTask2);
					loadImageTask2.execute(i);
					LogUtils2.i("开始加载第 "+i+" 个图片....");
				}
				currentPage ++;
				
			}else {
				Toast.makeText(getContext(), "已没有更多图片", Toast.LENGTH_SHORT).show();
			}
		} else {
			Toast.makeText(getContext(), "未发现SD卡", Toast.LENGTH_SHORT).show();
		}
	}

	private boolean haveSDCard() {
		// TODO Auto-generated method stub
		boolean isCard = false;
		if(Environment.MEDIA_MOUNTED .equals(Environment.getExternalStorageState())){
			isCard = true;
		}
		return isCard;
	}

	/**
	 * 异步加载图片
	 * 这里的第一个参数 是对应AsyncTask.execute(i)方法中的参数,
	 * 也是对应方法doInBackground(Integer... params)中的参数
	 * 
	 * 第三个参数 是doInBackground(Integer... params)方法返回的值，是
	 * 对应onPostExecute(Bitmap result)方法中的参数
	 * 
	 */
	class LoadImageTask2 extends AsyncTask<Integer, String, Bitmap>{

		/**
		 * 记录每个图片对应的位置
		 */
		private int mItemPosition;

		/**
		 * 图片的URL地址
		 */
		private String mImageUrl;

		/**
		 * 可重复使用的ImageView
		 */
		private ImageView mImageView;

		public LoadImageTask2() {
		}
		
		public LoadImageTask2(ImageView imageView){
			mImageView = imageView;
		}
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			LogUtils2.i("onPreExecute.......");
			super.onPreExecute();
		}
		
		/**
		 * 这里返回的值是对应AsyncTask（...）参数中的第三个参数的
		 * 而且，返回的值 是onPostExecute（...）方法中的参数
		 */
		@Override
		protected Bitmap doInBackground(Integer... params) {
			// TODO Auto-generated method stub
			LogUtils2.i("doInBackground.......");
			mItemPosition = params[0];
			mImageUrl = Images.imageUrls[mItemPosition];
			//从缓存中获取图片
			Bitmap bitmap = imageLoader.getBitmapFromMemoryCache(mImageUrl);
			if(bitmap == null){
				//缓存中没有图片的话 从之前保存的内存卡中获取，如果也 没有则从网络加载
				LogUtils2.i("loadImage.........");
				bitmap = loadImage(mImageUrl);
				
			}
			return bitmap;
		}
		
		@Override
		protected void onPostExecute(Bitmap bitmap) {
			// TODO Auto-generated method stub
			LogUtils2.i("onPostExecute.......");
			super.onPostExecute(bitmap);
			if(bitmap != null){
				
				double ratio = bitmap.getWidth() / (1*columnWidth);
				int scaledHeight = (int) (bitmap.getHeight() / ratio);
				/**
				 * 因为下载下来的图片可能很大，
				 * 所以加到布局先，先设置一下大小
				 */
				addImage(bitmap , columnWidth,scaledHeight);
				
			}
			
			taskCollection.remove(this);
			
		}
	
		private void addImage(Bitmap bitmap, int columnWidth, int scaledHeight) {
			// TODO Auto-generated method stub
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(columnWidth, scaledHeight);
			if(mImageView != null){
				mImageView.setImageBitmap(bitmap);
			}else {
				
				ImageView imageView = new ImageView(getContext());
				imageView.setPadding(5, 5, 5, 5);
				imageView.setLayoutParams(params);
				imageView.setScaleType(ScaleType.FIT_XY);
				imageView.setTag(R.string.image_url, mImageUrl);
				imageView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
//						Intent intent = new Intent(getContext(), ImageDetailsActivity.class);
//						intent.putExtra("image_position", mItemPosition);
//						getContext().startActivity(intent);
					}
				});
				
				findColumnToAdd(imageView, scaledHeight).addView(imageView);
				imageViewList.add(imageView);
			}
		}

		/**
		 * 找到此时应该添加图片的一列。原则就是对四列的高度进行判断，当前高度最小的一列就是应该添加的一列。
		 * 
		 * @param imageView
		 * @param imageHeight
		 * @return 应该添加图片的一列
		 */
		private LinearLayout findColumnToAdd(ImageView imageView, int imageHeight){
			
			LogUtils2.i("firstColumnHeight=="+firstColumnHeight);
			LogUtils2.i("secondColumnHeight=="+secondColumnHeight);
			LogUtils2.i("thirdColumnHeight=="+thirdColumnHeight);
			LogUtils2.i("fourColumnHeight=="+fourColumnHeight);
			
			if (firstColumnHeight <= secondColumnHeight) {
				if (firstColumnHeight <= thirdColumnHeight) {
					if(firstColumnHeight <= fourColumnHeight){
						imageView.setTag(R.string.border_top, firstColumnHeight);
						firstColumnHeight += imageHeight;
						imageView.setTag(R.string.border_bottom, firstColumnHeight);
						return firstColumnLayout;
					}
					
					imageView.setTag(R.string.border_top, fourColumnHeight);
					fourColumnHeight += imageHeight;
					imageView.setTag(R.string.border_bottom, fourColumnHeight);
					return fourColumnLayout;
					
				}
				imageView.setTag(R.string.border_top, thirdColumnHeight);
				thirdColumnHeight += imageHeight;
				imageView.setTag(R.string.border_bottom, thirdColumnHeight);
				return thirdColumnLayout;
			} else {
				if (secondColumnHeight <= thirdColumnHeight) {
					if(secondColumnHeight <= fourColumnHeight){
						imageView.setTag(R.string.border_top, secondColumnHeight);
						secondColumnHeight += imageHeight;
						imageView.setTag(R.string.border_bottom, secondColumnHeight);
						return secondColumnLayout;
					}
					
					
					imageView.setTag(R.string.border_top, fourColumnHeight);
					fourColumnHeight += imageHeight;
					imageView.setTag(R.string.border_bottom, fourColumnHeight);
					return fourColumnLayout;
					
				}
				imageView.setTag(R.string.border_top, thirdColumnHeight);
				thirdColumnHeight += imageHeight;
				imageView.setTag(R.string.border_bottom, thirdColumnHeight);
				return thirdColumnLayout;
			}
			
		}
		
		/**
		 * 根据传入的URL，对图片进行加载。如果这张图片已经存在于SD卡中，则直接从SD卡里读取，否则就从网络上下载。
		 * 
		 * @param imageUrl
		 *            图片的URL地址
		 * @return 加载到内存的图片。
		 */
		public Bitmap loadImage(String mImageUrl){
			
			String filePath = getImagePath(mImageUrl);
			File file = new File(filePath);
			if(!file.exists()){
				///如果内存中也不存在就从网络获取
				LogUtils2.i("downloadImage.........");
				downloadImage(mImageUrl);
				
			}
			
			if(mImageUrl != null ){
				Bitmap bitmap = imageLoader.decodeSampledBitmapFromResource(file.getPath(), columnWidth);
				if(bitmap != null){
					imageLoader.addBitmapToMemoryCache(mImageUrl, bitmap);
					return bitmap;
				}
			}
			
			return null;
			
		}

		/**
		 * 将图片下载到SD卡缓存起来。
		 * 
		 * @param imageUrl
		 *            图片的URL地址。
		 */
		private void downloadImage(String mImageUrl) {
			// TODO Auto-generated method stub
			HttpURLConnection con =null ;
			FileOutputStream fos = null;
			BufferedOutputStream bos = null;
			BufferedInputStream bis = null;
			File imageFile = null;
			try {
				URL url = new URL(mImageUrl);
				con = (HttpURLConnection) url.openConnection();
				con.setConnectTimeout(5 * 1000);
				con.setReadTimeout(15 * 1000);
				con.setDoInput(true);
				con.setDoOutput(true);
				bis = new BufferedInputStream(con.getInputStream());
				imageFile = new File(getImagePath(mImageUrl));
				fos = new FileOutputStream(imageFile);
				bos = new BufferedOutputStream(fos);
				byte[] b = new byte[1024];
				int length;
				while ((length = bis.read(b)) != -1) {
					bos.write(b, 0, length);
					bos.flush();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally {
				try {
					if (bis != null) {
						bis.close();
					}
					if (bos != null) {
						bos.close();
					}
					if (con != null) {
						con.disconnect();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (imageFile != null) {
				Bitmap bitmap = ImageLoader.decodeSampledBitmapFromResource(imageFile.getPath(),
						columnWidth);
				if (bitmap != null) {
					imageLoader.addBitmapToMemoryCache(mImageUrl, bitmap);
				}
			}
			
			
		}

		/**
		 * 获取图片的本地存储路径。
		 * 
		 * @param imageUrl
		 *            图片的URL地址。
		 * @return 图片的本地存储路径。
		 * http://img.my.csdn.net/uploads/201309/01/1378037235_3453.jpg
		 */
		private String getImagePath(String mImageUrl2) {
			// TODO Auto-generated method stub
			 int lastSlashIndex = mImageUrl2.lastIndexOf("/");
			 String imageName = mImageUrl2.substring(lastSlashIndex+1);
			 String ImageFileDir = 
					 Environment.getExternalStorageDirectory().
					 getAbsolutePath()+"/PhotoWallFalls2/";
			 File imageFile = new File(ImageFileDir);
			 if(!imageFile.exists()){
				 imageFile.mkdir();
			 }
			 String imagePath = ImageFileDir + imageName;
			return imagePath;
		}
		
	}
	
	/**
	 * 
	 */
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		if (event.getAction() == MotionEvent.ACTION_UP) {
			Message message = new Message();
			message.obj = this;
			handler.sendMessageDelayed(message, 5);
		}
		
		
		return false;
	}
}
