package es.ucm.pad.teamjvr.mylittlebusiness;

import java.util.List;

import android.app.ActionBar.LayoutParams;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import es.ucm.pad.teamjvr.mylittlebusiness.model.Product;

public class ProductListActivity extends ListActivity {
	
	private OnItemLongClickListener onListItemLongClick = new OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View v, int pos, long id) {
        	final ListView l = (ListView)parent;
        	final int position = pos;
        	
            new AlertDialog.Builder(ProductListActivity.this)
            	.setIcon(android.R.drawable.ic_dialog_alert)
            	.setTitle(R.string.delete_item)
            	.setMessage(R.string.delete_item_confrm_msg)
            	.setNegativeButton(android.R.string.cancel, null)
            	.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						MLBApplication app = (MLBApplication) getApplication();
						app.deleteProduct((Product)l.getItemAtPosition(position));
						
						regenerateProductsList();
						
					}
				})
				.show();
            
            return true;
        }
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
		ListView lv = getListView();
		
		lv.setEmptyView(textMsg);
		lv.setOnItemLongClickListener(onListItemLongClick);

		// Must add the progress bar to the root of the layout
		ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
		root.addView(textMsg);
		
		regenerateProductsList();
		
	}
	

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Intent intent = new Intent(this, ProductDetailsActivity.class);
		((MLBApplication) getApplication()).setCurrentProd((Product) l.getItemAtPosition(position));
		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.product_list, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) {
			case R.id.action_addItem: {
				Intent intent = new Intent(this, AddProductActivity.class);
				startActivity(intent);
			}
			
			case R.id.action_settings:{
				//TODO: Añadir actividad de opciones
			}
			
			case R.id.action_about: {
				//TODO: Añadir actividad de about
			}
			
			case R.id.action_stats: {
				//TODO: Añadir actividad de estadísticas
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
	
	private void regenerateProductsList() {
		List<Product> products = ((MLBApplication) getApplication()).productList();
		getListView().setAdapter(new ProductAdapter(this, android.R.layout.simple_list_item_1, products));
	};
	
	private class ProductAdapter extends ArrayAdapter<Product> {
		private List<Product> items;
		private LayoutInflater inflater;

		public ProductAdapter(Context context, int resource, List<Product> products) {
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
}