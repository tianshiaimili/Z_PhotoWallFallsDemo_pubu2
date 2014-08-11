package com.example.photowallfallsdemo;

import com.example.photowallfallsdemo.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity2 extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		  //不显示系统的标题栏          
//        getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                              WindowManager.LayoutParams.FLAG_FULLSCREEN );
        setContentView(R.layout.activity_main2);
	}
	
}
