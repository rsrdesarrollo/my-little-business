package es.ucm.pad.teamjvr.mylittlebusiness;

import java.util.List;

import android.app.ActionBar.LayoutParams;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import es.ucm.pad.teamjvr.mylittlebusiness.Model.Product;

public class ProductListActivity extends ListActivity {
	private class ProductAdapter extends ArrayAdapter<Product> {
		private List<Product> items;
		private LayoutInflater inflater;

		public ProductAdapter(Context context, int resource,
				List<Product> products) {
			super(context, resource, products);
			this.items = products;
			this.inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View vi = convertView;
			if (convertView == null)
				vi = inflater.inflate(R.layout.list_item_product, null);

			TextView name = (TextView) vi.findViewById(R.id.nameItemText);
			TextView stock = (TextView) vi.findViewById(R.id.stockItemText);
			ImageView photo = (ImageView) vi.findViewById(R.id.photoItemImage);

			Product item = items.get(position);

			if (item != null) {
				name.setText(item.getName());
				stock.setText(item.getStock());
				
				if (item.getPhoto() != null)
					photo.setImageBitmap(item.getPhoto());
			}

			return vi;
		}
	}
	
	private void regenerateProductsList() {
		List<Product> products = ((MLBApplication) getApplication()).productList();
		getListView().setAdapter(
				new ProductAdapter(this, android.R.layout.simple_list_item_1,
						products));
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Create a progress bar to display while the list loads
		String emptyMsg = getResources().getString(R.string.empty_list);
		TextView textMsg = new TextView(this);
		textMsg.setText(emptyMsg);
		textMsg.setLayoutParams(new LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				Gravity.CENTER));
		getListView().setEmptyView(textMsg);

		// Must add the progress bar to the root of the layout
		ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
		root.addView(textMsg);
		
		regenerateProductsList();
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
		switch (item.getItemId()) {
			case R.id.addItem: {
				Intent intent = new Intent(this, AddProductActivity.class);
				startActivity(intent);
			}
				
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		regenerateProductsList();
	}
}