package es.ucm.pad.teamjvr.mylittlebusiness.activity;

import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
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
import android.widget.ListView;
import android.widget.SearchView;
import es.ucm.pad.teamjvr.mylittlebusiness.MLBApplication;
import es.ucm.pad.teamjvr.mylittlebusiness.R;
import es.ucm.pad.teamjvr.mylittlebusiness.model.Product;
import es.ucm.pad.teamjvr.mylittlebusiness.model.db_adapter.ProductsDBAdapter;
import es.ucm.pad.teamjvr.mylittlebusiness.view.ProductAdapter;

/**
 * Actividad que representa la pantalla principal de lista de productos en la aplicación
 *
 */
public class ProductListActivity extends ListActivity {
	private ProductsDBAdapter db;
	private ProductAdapter productAdapter;
	
	/*
	 * Implementa la función al pulsar un elemento de la lista durante
	 * largo tiempo (borrar el producto con confirmación)
	 * 
	 */
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
	

	/**
	 * Infla las opciones del menú así como implementa las funciones especiales para
	 * gestionar la lista en la barra superior (búsqueda de productos)
	 * 
	 */
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
	protected void onDestroy() {
		this.db.close();
		super.onStop();
	}

	/**
	 * Implementa la función al pulsar sobre un elemento
	 * de la lista (acceder a la pantalla de edición / detalles del producto)
	 * 
	 */
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Intent intent = new Intent(this, ProductDetailsActivity.class);
		((MLBApplication) getApplication()).setCurrentProd((Product) l.getItemAtPosition(position));
		startActivity(intent);
	}

	/**
	 * Implementa las opciones para el menú
	 * 
	 */
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
				Intent intent = new Intent(this, SettingsActivity.class);
				startActivity(intent);
				break;
			}
			
			case R.id.action_about: {
				Intent intent = new Intent(this, AboutMLBActivity.class);
				startActivity(intent);
				break;
			}
			
			case R.id.action_stats: {
				Intent intent = new Intent(this, TopStatsActivity.class);
				startActivity(intent);
				break;
			}
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onResume() {
		regenerateProductsList(null);
		super.onResume();
	};
	
	/**
	 * Recarga la lista de productos
	 * 
	 */
	private void regenerateProductsList(String filter) {
		List<Product> products = db.getProductsList(filter);
		this.productAdapter = new ProductAdapter(this, android.R.layout.simple_list_item_1, products);
		getListView().setAdapter(this.productAdapter);
	}
}