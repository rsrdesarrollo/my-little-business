package es.ucm.pad.teamjvr.mylittlebusiness.activity;

import java.util.Locale;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.webkit.WebView;
import es.ucm.pad.teamjvr.mylittlebusiness.R;

/**
 * Actividad que representa la pantalla about de la aplicación
 *
 */
public class AboutMLBActivity extends Activity {
	/**
	 * Tiene un WebView que carga un html
	 * 
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_about_mlb);
		WebView myWebView = (WebView) findViewById(R.id.webview);
		
		if (Locale.getDefault().getLanguage().equalsIgnoreCase("es"))
			myWebView.loadUrl("file:///android_asset/www_es/about.html");
		else
			myWebView.loadUrl("file:///android_asset/www/about.html");

		setupActionBar();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				NavUtils.navigateUpFromSameTask(this);
				return true;
		}
		
		return super.onOptionsItemSelected(item);
	}

	private void setupActionBar() {
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}
}