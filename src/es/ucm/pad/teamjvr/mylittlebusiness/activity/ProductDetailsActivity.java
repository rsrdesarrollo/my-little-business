package es.ucm.pad.teamjvr.mylittlebusiness.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import android.widget.Toast;
import es.ucm.pad.teamjvr.mylittlebusiness.MLBApplication;
import es.ucm.pad.teamjvr.mylittlebusiness.R;
import es.ucm.pad.teamjvr.mylittlebusiness.model.Product;
import es.ucm.pad.teamjvr.mylittlebusiness.model.db_adapter.ProductsDBAdapter;
import es.ucm.pad.teamjvr.mylittlebusiness.model.exceptions.ProductAttrException;

/**
 * Actividad que representa la pantalla de detalles de un producto en la aplicación
 *
 */
public class ProductDetailsActivity extends Activity {
	/*
	 * Guardamos dos referencias: el producto que se quiere editar (sin cambios)
	 * y un nuevo Product que irá guardando los cambios progresivamente hasta que
	 * el usuario decida guardar esos cambios, entonces se graban los cambios de productEdited
	 * en el producto que se quiere editar
	 * 
	 */
	private Product product;
	private Product productEdited;

	private Button bttSave;
	
	private TextView txtStockNum;

	private EditText txtName;
	private EditText txtCost;
	private EditText txtPrice;

	private EditText txtAdd;
	private EditText txtSell;
	private Button bttAdd;
	private Button bttSell;

	private ImageView prodImage;
	private ImageView prodExtImage;
	
	private ProductsDBAdapter db;

	/**
	 * Implementa la acción que ocurre al pulsar el botón de 'Add'
	 * 
	 */
	private OnClickListener onbttAddClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			try {
				productEdited.addStock(Integer.valueOf(txtAdd.getText().toString()));
				txtStockNum.setText(productEdited.getStock());
			} catch (Exception e) {
				Toast.makeText(getApplicationContext(),
						R.string.error_stock_out_of_bounds, Toast.LENGTH_SHORT)
						.show();
			}
		}
	};

	/**
	 * Implementa la acción que ocurre al pulsar el botón de 'Sell'
	 * 
	 */
	private OnClickListener onBttSellClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			try {
				productEdited.sellUnits(Integer.valueOf(txtSell.getText().toString()));
				txtStockNum.setText(productEdited.getStock());
			} catch (Exception e) {
				Toast.makeText(getApplicationContext(),
						R.string.error_there_are_not_enough, Toast.LENGTH_SHORT)
						.show();
			}
		}
	};
	
	/**
	 * Implementa la acción que ocurre al pulsar el botón de 'Save'
	 * 
	 */
	private OnClickListener onBttSaveClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			
			try {
				productEdited = getChangedProduct();
			} catch (ProductAttrException e) {
				Toast.makeText(getApplicationContext(), e.getDetailMessageId(), Toast.LENGTH_SHORT)
					 .show();
				return;
			}
	
			/*
			 * Se intentan guardar los cambios en el producto original, controlando
			 * los posibles errores que pudieran suceder
			 * 
			 */
			if (productEdited.getName().equals(product.getName())) {
				if (!db.updateProduct(productEdited)) {
					Toast.makeText(getApplicationContext(), R.string.error_product_data, Toast.LENGTH_SHORT)
						 .show();
					productEdited = new Product(product);
				} else {
					finish();
					Log.i("SavedProduct", "Description: '" + productEdited.getName() + "'");
				}
			} else {
				if (!db.addProduct(productEdited)) {
					Toast.makeText(getApplicationContext(), R.string.error_product_exist, Toast.LENGTH_SHORT)
						 .show();
					productEdited = new Product(product);
				} else {
					Log.i("SavedAndRenamedProduct", "Description: '" + product
							+ "'" + " ->'" + productEdited.getName() + "'");
					db.deleteProduct(product);
					finish();
				}
			}
		}
	};
	
	
	/**
	 * Muestra un Dialog de confirmación de cierre sin guardar cambios al usuario
	 * 
	 */
	private void close() {
		if (!isChanged()) {
			navigateUpFromSameTask();
		} else
			new AlertDialog.Builder(ProductDetailsActivity.this)
					.setIcon(android.R.drawable.ic_dialog_alert)
					.setTitle(R.string.close)
					.setMessage(R.string.close_without_save)
					.setNegativeButton(android.R.string.cancel, null)
					.setPositiveButton(android.R.string.yes,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									if (which == DialogInterface.BUTTON_POSITIVE)
										navigateUpFromSameTask();
								}
							}).show();
	}
	
	private Product getChangedProduct() throws ProductAttrException {
		Product productEdited = new Product(this.productEdited);
		String name = txtName.getText().toString();
		double cost, price;

		/* 
		 * Vamos comprobando que todos los valores son adecuados para editar el Product,
		 * si alguno no es correcto sacamos un toast informativo para el usuario
		 * 
		 */
		try {
			cost = Double.valueOf(txtCost.getText().toString()).doubleValue();
		} catch (NumberFormatException e) {
			throw new ProductAttrException(R.string.error_invalid_cost);
		}

		try {
			price = Double.valueOf(txtPrice.getText().toString()).doubleValue();
		} catch (NumberFormatException e) {
			throw new ProductAttrException(R.string.error_invalid_price);
		}

		// Se actualizan los valores para productEdited

		productEdited.setCost(cost);
		productEdited.setPrice(price);
		productEdited.setName(name);
		

		return productEdited;
	}
	
	private boolean isChanged() {
		Product changed;

		try {
			changed = getChangedProduct();
			return !changed.equals(product);
		} catch (ProductAttrException e) {
			return false;
		}
	}

	private void navigateUpFromSameTask() {
		NavUtils.navigateUpFromSameTask(this);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product_details);
		// Show the Up button in the action bar.
		setupActionBar();

		this.bttSave = (Button) findViewById(R.id.bttSaveItem);
		this.bttSave.setOnClickListener(onBttSaveClick);
		
		this.txtStockNum = (TextView) findViewById(R.id.textStockNum);
		
		this.txtName = (EditText) findViewById(R.id.edit_prod_name);
		this.txtCost = (EditText) findViewById(R.id.edit_prod_cost);
		this.txtPrice = (EditText) findViewById(R.id.edit_prod_price);

		this.txtAdd = (EditText) findViewById(R.id.units_to_add);
		this.txtSell = (EditText) findViewById(R.id.units_to_sell);
		
		this.bttAdd = (Button) findViewById(R.id.btt_add_stock);
		this.bttAdd.setOnClickListener(onbttAddClick);
		
		this.bttSell = (Button) findViewById(R.id.btt_sell_units);
		this.bttSell.setOnClickListener(onBttSellClick);

		this.prodImage = (ImageView) findViewById(R.id.prod_det_image);
		this.prodImage.setOnClickListener(zoomImageFromThumb());
		this.prodExtImage = (ImageView) findViewById(R.id.prod_det_exp_image);
		this.prodExtImage.setOnClickListener(zoomOutImage());
		this.db = new ProductsDBAdapter(this);
		this.db.open();
		this.product = ((MLBApplication) getApplication()).getCurrentProd();
		this.productEdited = new Product(this.product);
		setUI();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}
	
	@Override
	protected void onDestroy() {
		this.db.close();
		super.onStop();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
			case KeyEvent.KEYCODE_BACK:
				close();
				return true;
		}
		
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				close();
				return true;
		}
		
		return super.onOptionsItemSelected(item);
	}

	/**
	 * Actualiza los valores de la vista del producto
	 * 
	 */
	private void setUI() {
		txtName.setText(productEdited.getName());
		txtCost.setText(productEdited.getCost());
		txtPrice.setText(productEdited.getPrice());
		txtStockNum.setText(productEdited.getStock());
		
		if (productEdited.getPhoto() != null) {
			prodImage.setImageBitmap(productEdited.getPhoto());
			prodExtImage.setImageBitmap(productEdited.getPhoto());
		}
		
		prodExtImage.setScaleType(ScaleType.FIT_XY);
		prodExtImage.setAdjustViewBounds(false);
		prodExtImage.setAdjustViewBounds(true);
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}
	
	/**
	 * Oculta la vista actual y muestra la imagen del producto a pantalla completa
	 * 
	 */
	private OnClickListener zoomImageFromThumb() {
		return new OnClickListener() {
			@Override
			public void onClick(View v) {
				findViewById(R.id.linear_prod_info).setVisibility(View.GONE);
				prodExtImage.setVisibility(View.VISIBLE);
			}
		};
	}

	/**
	 * Recupera la vista de edición del producto
	 * 
	 */
	private OnClickListener zoomOutImage() {
		return new OnClickListener() {
			@Override
			public void onClick(View v) {
				prodExtImage.setVisibility(View.GONE);
				findViewById(R.id.linear_prod_info).setVisibility(View.VISIBLE);
			}
		};
	}
}