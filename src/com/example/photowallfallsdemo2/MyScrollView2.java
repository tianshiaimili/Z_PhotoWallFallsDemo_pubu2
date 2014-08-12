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
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.photowallfallsdemo.ImageLoader;
import com.example.photowallfallsdemo.Images;
import com.example.photowallfallsdemo.R;
import com.example.util.LogUtils2;

/**��������
 * ��������������
 * @author Hua
 *
 */
public class MyScrollView2 extends ScrollView implements OnTouchListener{

	/**
	 * ÿҳҪ���ص�ͼƬ����
	 */
	public static final int PAGE_SIZE = 20;
	/**
	 * ��¼��ǰ�Ѽ��ص��ڼ�ҳ
	 */
	private int currentPage;
	/**
	 * ÿһ�еĿ��
	 */
	private int columnWidth;

	/**
	 * ��ǰ��һ�еĸ߶�
	 */
	private int firstColumnHeight;
	/**
	 * ��ǰ�ڶ��еĸ߶�
	 */
	private int secondColumnHeight;

	/**
	 * ��ǰ�����еĸ߶�
	 */
	private int thirdColumnHeight;
	/**
	 * ��ǰ�����еĸ߶�
	 */
	private  int fourColumnHeight;
	/**
	 * �Ƿ��Ѽ��ع�һ��layout������onLayout�еĳ�ʼ��ֻ�����һ��
	 */
	private boolean loadOnce;
	/**
	 * ��һ�еĲ���
	 */
	private LinearLayout firstColumnLayout;

	/**
	 * �ڶ��еĲ���
	 */
	private LinearLayout secondColumnLayout;

	/**
	 * �����еĲ���
	 */
	private LinearLayout thirdColumnLayout;
	
	/**
	 * �����еĲ���
	 */
	private LinearLayout fourColumnLayout; 
	
	/**
	 * MyScrollView�µ�ֱ���Ӳ��֡�
	 */
	private static View subScrollLayout;

	/**
	 * MyScrollView���ֵĸ߶ȡ�
	 */
	private static int scrollViewHeight;

	/**
	 * ��¼�ϴ�ֱ����Ĺ������롣
	 */
	private static int lastScrollY = -1;

	/**
	 * ��¼���н����ϵ�ͼƬ�����Կ�����ʱ���ƶ�ͼƬ���ͷš�
	 */
	private List<ImageView> imageViewList = new ArrayList<ImageView>();
	
	/**
	 * ��ͼƬ���й���Ĺ�����
	 */
	private ImageLoader imageLoader;

	/**
	 * ��¼�����������ػ�ȴ����ص�����
	 */
	private static Set<LoadImageTask2> taskCollection;

	public MyScrollView2(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public MyScrollView2(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		imageLoader = ImageLoader.getInstance();
		taskCollection = new HashSet<MyScrollView2.LoadImageTask2>();
		setOnTouchListener(this);
		
	}
	
	/**
	 * * ����һЩ�ؼ��Եĳ�ʼ����������ȡMyScrollView�ĸ߶ȣ��Լ��õ���һ�еĿ��ֵ���������￪ʼ���ص�һҳ��ͼƬ��
	 */
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		super.onLayout(changed, l, t, r, b);
		
		//�����Ǹ���view��ֵ�ˣ��ͳ�ʼ��һϵ�ж���
		if(changed && !loadOnce){
			//��ȡ��ǰScrollView�ĸ߶�
			scrollViewHeight = getHeight();
			/**
			 * �����ǻ�ȡScrollView�µĵ�һ����View����activity_main2.xml�ļ�
			 * �е�һ����ViewӦ����һ��LinearLayout
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
			 * ����ͼƬ
			 */
			loadMoreImages();
			
		}
		
	}
	
	/**
	 * ��ʼ������һҳ��ͼƬ��ÿ��ͼƬ���Ὺ��һ���첽�߳�ȥ���ء�
	 */
	private void loadMoreImages() {
		// TODO Auto-generated method stub
		if(haveSDCard()){
			
			int startIndex = currentPage * PAGE_SIZE;
			int endIndex = currentPage * PAGE_SIZE + PAGE_SIZE;
			int countImagesLenght = Images.imageUrls.length;
			if(startIndex < countImagesLenght){
				
				Toast.makeText(getContext(), "���ڼ���...", Toast.LENGTH_SHORT).show();
				if(endIndex > countImagesLenght){
					endIndex = countImagesLenght;
				}
				////
				for(int i=startIndex ;i < endIndex ;i++){
					
					LoadImageTask2 loadImageTask2 = new LoadImageTask2();
					taskCollection.add(loadImageTask2);
					loadImageTask2.execute(i);
					LogUtils2.i("��ʼ���ص� "+i+" ��ͼƬ....");
				}
				currentPage ++;
				
			}else {
				Toast.makeText(getContext(), "��û�и���ͼƬ", Toast.LENGTH_SHORT).show();
			}
		} else {
			Toast.makeText(getContext(), "δ����SD��", Toast.LENGTH_SHORT).show();
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
	 * �첽����ͼƬ
	 * ����ĵ�һ������ �Ƕ�ӦAsyncTask.execute(i)�����еĲ���,
	 * Ҳ�Ƕ�Ӧ����doInBackground(Integer... params)�еĲ���
	 * 
	 * ���������� ��doInBackground(Integer... params)�������ص�ֵ����
	 * ��ӦonPostExecute(Bitmap result)�����еĲ���
	 * 
	 */
	class LoadImageTask2 extends AsyncTask<Integer, String, Bitmap>{

		/**
		 * ��¼ÿ��ͼƬ��Ӧ��λ��
		 */
		private int mItemPosition;

		/**
		 * ͼƬ��URL��ַ
		 */
		private String mImageUrl;

		/**
		 * ���ظ�ʹ�õ�ImageView
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
		 * ���ﷵ�ص�ֵ�Ƕ�ӦAsyncTask��...�������еĵ�����������
		 * ���ң����ص�ֵ ��onPostExecute��...�������еĲ���
		 */
		@Override
		protected Bitmap doInBackground(Integer... params) {
			// TODO Auto-generated method stub
			LogUtils2.i("doInBackground.......");
			mItemPosition = params[0];
			mImageUrl = Images.imageUrls[mItemPosition];
			//�ӻ����л�ȡͼƬ
			Bitmap bitmap = imageLoader.getBitmapFromMemoryCache(mImageUrl);
			if(bitmap == null){
				//������û��ͼƬ�Ļ� ��֮ǰ������ڴ濨�л�ȡ�����Ҳ û������������
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
				 * ��Ϊ����������ͼƬ���ܴܺ�
				 * ���Լӵ������ȣ�������һ�´�С
				 */
				addImage(bitmap , columnWidth,scaledHeight);
				
			}
		}
	
		private void addImage(Bitmap bitmap, int columnWidth, int scaledHeight) {
			// TODO Auto-generated method stub
			
		}

		/**
		 * ���ݴ����URL����ͼƬ���м��ء��������ͼƬ�Ѿ�������SD���У���ֱ�Ӵ�SD�����ȡ������ʹ����������ء�
		 * 
		 * @param imageUrl
		 *            ͼƬ��URL��ַ
		 * @return ���ص��ڴ��ͼƬ��
		 */
		public Bitmap loadImage(String mImageUrl){
			
			String filePath = getImagePath(mImageUrl);
			File file = new File(filePath);
			if(!file.exists()){
				///����ڴ���Ҳ�����ھʹ������ȡ
				downloadImage(mImageUrl);
				
			}
			
			if(mImageUrl != null){
				Bitmap bitmap = imageLoader.decodeSampledBitmapFromResource(mImageUrl, columnWidth);
				if(bitmap != null){
					imageLoader.addBitmapToMemoryCache(mImageUrl, bitmap);
					return bitmap;
				}
			}
			
			return null;
			
		}

		/**
		 * ��ͼƬ���ص�SD������������
		 * 
		 * @param imageUrl
		 *            ͼƬ��URL��ַ��
		 */
		private void downloadImage(String mImageUrl) {
			// TODO Auto-generated method stub
			HttpURLConnection connection =null ;
			URL url =null;
			FileOutputStream fos = null;
			BufferedOutputStream bos = null;
			BufferedInputStream bis = null;
			File imageFile = null;
			try {
				url = new URL(mImageUrl);
				connection = (HttpURLConnection) url.openConnection();
				connection.setConnectTimeout(5000);
				connection.setReadTimeout(5000);
				connection.setDoInput(true);
				connection.setDoOutput(true);
				///
				int responseCode = connection.getResponseCode();
				if(responseCode == HttpURLConnection.HTTP_OK){
					
					bis = new BufferedInputStream(connection.getInputStream());
					imageFile = new File(getImagePath(mImageUrl));
					fos = new FileOutputStream(imageFile);
					bos = new BufferedOutputStream(fos);
					byte[] buffer = new byte[1024];
					int length;
					while((length = bis.read()) != -1){
						
						bos.write(buffer, 0, length);
						bos.flush();
						
					}
					
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
					if (connection != null) {
						connection.disconnect();
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
		 * ��ȡͼƬ�ı��ش洢·����
		 * 
		 * @param imageUrl
		 *            ͼƬ��URL��ַ��
		 * @return ͼƬ�ı��ش洢·����
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
		return false;
	}
}
