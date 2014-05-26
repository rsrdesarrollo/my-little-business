package es.ucm.pad.teamjvr.mylittlebusiness;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;


public class SplashActivity extends Activity implements Runnable {
	
	AnimationDrawable loadingAnimation;
	ImageView loadingBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		
		loadingBar = (ImageView) findViewById(R.id.imgLoading);
		loadingBar.setBackgroundResource(R.drawable.loading);
		loadingAnimation = (AnimationDrawable) loadingBar.getBackground();
		
		Handler handler = new Handler();
		handler.postDelayed(this, 2500);
	}
	
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		loadingAnimation.start();
		super.onWindowFocusChanged(hasFocus);
	}


	@Override
	public void run() {
		loadingAnimation.stop();
		startActivity(new Intent(getApplication(), ProductListActivity.class));
		this.finish();
	}
	
}