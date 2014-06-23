package es.ucm.pad.teamjvr.mylittlebusiness.activity;

import java.util.List;
import java.util.Locale;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.ListFragment;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import es.ucm.pad.teamjvr.mylittlebusiness.R;
import es.ucm.pad.teamjvr.mylittlebusiness.model.Product;
import es.ucm.pad.teamjvr.mylittlebusiness.model.db_adapter.ProductsDBAdapter;
import es.ucm.pad.teamjvr.mylittlebusiness.view.ProductAdapter;

public class TopStatsActivity extends FragmentActivity implements ActionBar.TabListener {
	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {
		private TopListFragment topListFragment;

		public SectionsPagerAdapter(FragmentManager fragmentManager) {
			super(fragmentManager);
		}

		@Override
		public int getCount() {
			// Show 2 total pages.
			return 2;
		}

		@Override
		public Fragment getItem(int position) {
			switch (position) {
			case 0:
				this.topListFragment = new TopBenefitsListFragment();
				break;
			case 1:
				this.topListFragment = new TopSalesListFragment();
				break;
			}
			
			return this.topListFragment;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_top_benefits_section).toUpperCase(l);
			case 1:
				return getString(R.string.title_top_sales_section).toUpperCase(l);
			}
			return null;
		}
	}
	
	/**
	 * A placeholder fragment containing a simple view.
	 * 
	 */
	public static class TopBenefitsListFragment extends TopListFragment {
		public TopBenefitsListFragment(){}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
			int length = Integer.valueOf(sharedPreferences.getString(SettingsActivity.PREF_TOP_LENGTH, "10"));
			setListData(db.getTopNBenefits(length));
			return super.onCreateView(inflater, container, savedInstanceState);
		}
	}

	public static class TopListFragment extends ListFragment {
		protected ProductsDBAdapter db;
		protected ProductAdapter productAdapter;
		
		public TopListFragment(){}
		
		@Override
		public void onAttach(Activity activity) {
			this.db = new ProductsDBAdapter(activity);
			this.db.open();
			super.onAttach(activity);
		}
		@Override
		public void onDestroy() {
			this.db.close();
			super.onDestroy();
		}
		
		/**
		 * Implementa la función al pulsar sobre un elemento
		 * de la lista (acceder a la pantalla de edición / detalles del producto)
		 * 
		 */
		@Override
		public void onListItemClick(ListView l, View v, int position, long id) {
//			Intent intent = new Intent(this.getActivity(), ProductDetailsActivity.class);
//			((MLBApplication) this.getActivity().getApplication()).setCurrentProd((Product) l.getItemAtPosition(position));
//			startActivity(intent);
			
			// TODO Auto-generated method stub
			super.onListItemClick(l, v, position, id);
		}
		
		
		public void setListData(List<Product> products) {
			this.productAdapter = new ProductAdapter(getActivity(), android.R.layout.simple_list_item_1, products);
			setListAdapter(this.productAdapter);
		}
	}

	public static class TopSalesListFragment extends TopListFragment {
		public TopSalesListFragment(){}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
			int length = Integer.valueOf(sharedPreferences.getString(SettingsActivity.PREF_TOP_LENGTH, "10"));
			setListData(db.getTopNSales(length));
			return super.onCreateView(inflater, container, savedInstanceState);
		}
	}

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a {@link FragmentPagerAdapter}
	 * derivative, which will keep every loaded fragment in memory. If this
	 * becomes too memory intensive, it may be best to switch to a
	 * {@link android.support.v13.app.FragmentStatePagerAdapter}.
	 */
	private SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	private ViewPager mViewPager;
	
	private void navigateUpFromSameTask() {
		NavUtils.navigateUpFromSameTask(this);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_top_stats);

		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayHomeAsUpEnabled(true);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the activity.
		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		if (id == android.R.id.home) {
			navigateUpFromSameTask();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {}

	@Override
	public void onTabSelected(ActionBar.Tab tab,FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,	FragmentTransaction fragmentTransaction) {}
}