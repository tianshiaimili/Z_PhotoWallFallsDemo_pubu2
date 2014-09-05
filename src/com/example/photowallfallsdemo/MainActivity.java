package com.example.photowallfallsdemo;

import android.os.Bundle;
import android.view.Window;
import android.app.Activity;

/**
 * bubulalal撒大大缩短
 * @author zero
 *
 */
public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
	}

}
