package es.ucm.pad.teamjvr.mylittlebusiness.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import es.ucm.pad.teamjvr.mylittlebusiness.R;

/**
 * Actividad que representa un splash screen para nuestra aplicación
 *
 */
public class SplashActivity extends Activity implements Runnable, OnClickListener {
	AnimationDrawable loadingAnimation;
	ImageView loadingBar;
	Handler handler;

	@Override
	public void onClick(View v) {
		handler.removeCallbacks(this);
		run();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);

		loadingBar = (ImageView) findViewById(R.id.imgLoading);
		loadingBar.setBackgroundResource(R.xml.loading);
		loadingAnimation = (AnimationDrawable) loadingBar.getBackground();
		findViewById(R.id.linear_splash).setOnClickListener(this);

		// Genera una llamada al método run() de esta clase en 2500 ms
		this.handler = new Handler();
		this.handler.postDelayed(this, 2500);
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		loadingAnimation.start();
		super.onWindowFocusChanged(hasFocus);
	}

	/**
	 * Para la animacióin y entra en la pantalla principal
	 * 
	 */
	@Override
	public void run() {
		loadingAnimation.stop();
		startActivity(new Intent(getApplication(), ProductListActivity.class));
		this.finish();
	}
}