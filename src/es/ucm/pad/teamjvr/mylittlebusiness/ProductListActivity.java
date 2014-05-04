package es.ucm.pad.teamjvr.mylittlebusiness;

import android.app.ActionBar.LayoutParams;
import android.app.ListActivity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

public class ProductListActivity extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product_list);
		
		// Create a progress bar to display while the list loads
		ProgressBar progressBar = new ProgressBar(this);
		progressBar.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT, Gravity.CENTER));
		progressBar.setIndeterminate(true);
		getListView().setEmptyView(progressBar);
		
		// Must add the progress bar to the root of the layout
		ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
		root.addView(progressBar);
		
		ListView listview = getListView();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.product_list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_product_list,
					container, false);
			return rootView;
		}
	}

}
