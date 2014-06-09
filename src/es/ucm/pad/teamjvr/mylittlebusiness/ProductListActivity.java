package es.ucm.pad.teamjvr.mylittlebusiness;

import java.util.Currency;
import java.util.List;
import java.util.Locale;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.SearchView;
import android.widget.TextView;
import es.ucm.pad.teamjvr.mylittlebusiness.model.Product;
import es.ucm.pad.teamjvr.mylittlebusiness.model.db_adapter.ProductsDBAdapter;

public class ProductListActivity extends ListActivity {
	
	private ProductsDBAdapter db;
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
						db.deleteProduct((Product)l.getItemAtPosition(position));
						
						regenerateProductsList(null);
					}
				})
				.show();
            
            return true;
        }
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.db = new ProductsDBAdapter(this);
		this.db.open();
		
		LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		
		View emptyList = inflater.inflate(R.layout.empty_list, null);
		ListView lv = getListView();
		
		lv.setEmptyView(emptyList);
		lv.setOnItemLongClickListener(onListItemLongClick);
		
		
		

		// Must add the progress bar to the root of the layout
		ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
		root.addView(emptyList);		
	}
	
	@Override
	protected void onResume() {
		regenerateProductsList(null);
		super.onResume();
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
		inflater.inflate(R.menu.product_list_menu, menu);
		
		SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();	
		
		searchView.setOnCloseListener(new SearchView.OnCloseListener() {
			@Override
			public boolean onClose() {
				regenerateProductsList(null);
				return false;
			}
		});
		
		searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
			@Override
			public boolean onQueryTextChange(String newText) {
				return false;
			}
			@Override
			public boolean onQueryTextSubmit(String query) {
				regenerateProductsList(query);
				return true;
			}
		});
		
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
				break;
			}
			
			case R.id.action_settings:{
				//TODO: Añadir actividad de opciones
				break;
			}
			
			case R.id.action_about: {
				Intent intent = new Intent(this, AboutMLBActivity.class);
				startActivity(intent);
				break;
			}
			
			case R.id.action_stats: {
				//TODO: Añadir actividad de estadísticas
				break;
			}
			
		}
		
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onDestroy() {
		this.db.close();
		super.onStop();
	}
	
	private void regenerateProductsList(String filter) {
		List<Product> products = db.getProductsList(filter);
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
			TextView benefit = (TextView) vi.findViewById(R.id.benefitItemText);
			TextView price = (TextView) vi.findViewById(R.id.priceItemText);
			ImageView photo = (ImageView) vi.findViewById(R.id.photoItemImage);

			String sCurrency = Currency.getInstance(Locale.getDefault()).getSymbol();
			String sPrice = getResources().getString(R.string.price_hint);
			String sStock = getResources().getString(R.string.stock_hint);
			String sBenefit = getResources().getString(R.string.benefits_hint);			
			
			Product item = items.get(position);
			int cBenefit;
			
			if(item.getBenefits() <= 0)
				cBenefit = getResources().getColor(R.color.negative_benefits);
			else
				cBenefit = getResources().getColor(R.color.positive_benefits);

			if (item != null) {
				name.setText(item.getName());
				stock.setText(sStock+" "+item.getStock());
				price.setText(sPrice+" "+item.getPrice()+sCurrency);
				benefit.setText(sBenefit+" "+item.getBenefits()+sCurrency);
				benefit.setTextColor(cBenefit);
				
				if (item.getPhoto() != null)
					photo.setImageBitmap(item.getPhoto());
			}

			return vi;
		}
	}
}