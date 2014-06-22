package es.ucm.pad.teamjvr.mylittlebusiness.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import es.ucm.pad.teamjvr.mylittlebusiness.R;

/**
 * Actividad que representa la pantalla de opciones de la aplicación
 *
 */
public class SettingsActivity extends Activity {
	public static class SettingsFragment extends PreferenceFragment {
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);

	        // Load the preferences from an XML resource
	        addPreferencesFromResource(R.xml.preferences);
	    }
	}

	public static String PREF_SORT_BY = "pref_sort_by";
	public static String PREF_TOP_LENGTH = "pref_top_length";
	private SettingsFragment settingsFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setupActionBar();
		this.settingsFragment = new SettingsFragment();
		getFragmentManager().beginTransaction()
				.replace(android.R.id.content, this.settingsFragment).commit();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		
		if (id == android.R.id.home) {
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		
		return super.onOptionsItemSelected(item);
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			// Show the Up button in the action bar.
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}
}