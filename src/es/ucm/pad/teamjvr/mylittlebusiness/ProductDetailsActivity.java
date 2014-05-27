package es.ucm.pad.teamjvr.mylittlebusiness;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import es.ucm.pad.teamjvr.mylittlebusiness.model.Product;

public class ProductDetailsActivity extends Activity implements
OnClickListener {
	private Product product;

	private Button bttSave;

	private EditText txtName;
	private EditText txtCost;
	private EditText txtPrice;

	private EditText txtAdd;
	private EditText txtSell;
	private Button bttAdd;
	private Button bttSell;

	private ImageView prodImage;
	private Bitmap photo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product_details);
		// Show the Up button in the action bar.
		setupActionBar();

		this.bttSave = (Button) findViewById(R.id.bttSaveItem);

		this.txtName = (EditText) findViewById(R.id.edit_prod_name);
		this.txtCost = (EditText) findViewById(R.id.edit_prod_cost);
		this.txtPrice = (EditText) findViewById(R.id.edit_prod_price);

		this.txtAdd = (EditText) findViewById(R.id.units_to_add);
		this.txtSell = (EditText) findViewById(R.id.units_to_sell);
		this.bttAdd = (Button) findViewById(R.id.btt_add_stock);
		this.bttSell = (Button) findViewById(R.id.btt_sell_units);

		// TODO Mostrar imagen y stock actual

		regenerateProductAttr();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.product_details, menu);
		return true;
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

	@Override
	protected void onResume() {
		super.onResume();
		regenerateProductAttr();
	}

	private void regenerateProductAttr() {
		Product prod_aux = ((MLBApplication) getApplication()).getCurrentProd();

		if ((prod_aux != null) && (!prod_aux.equals(product))) {
			product = prod_aux;
			txtName.setText(product.getName());
			txtCost.setText(product.getCost());
			txtPrice.setText(product.getPrice());
		}
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
}