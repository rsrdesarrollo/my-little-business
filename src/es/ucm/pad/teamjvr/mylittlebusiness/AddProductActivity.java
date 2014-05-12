package es.ucm.pad.teamjvr.mylittlebusiness;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import es.ucm.pad.teamjvr.mylittlebusiness.Model.Product;

public class AddProductActivity extends Activity implements
		OnClickListener {
	private Button bttAdd;
	private EditText txtName;
	private EditText txtStock;
	private EditText txtCost;
	private EditText txtPrice;
	private ImageView prodImage;
	private Bitmap photo;

	private static final int TAKE_PROD_PIC = 1;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == TAKE_PROD_PIC)
			if (data != null)
				if (data.hasExtra("data")) {
					this.photo = data.getParcelableExtra("data");
					prodImage.setImageBitmap(this.photo);
				}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onClick(View v) {
		String descript = txtName.getText().toString();
		int stock;
		double cost, price;

		try {
			stock = Integer.valueOf(txtStock.getText().toString());

			if (stock < 0)
				throw new NumberFormatException();
		} catch (NumberFormatException e) {
			Toast.makeText(getApplicationContext(),
					R.string.error_invalid_stock_or_too_large,
					Toast.LENGTH_SHORT).show();
			return;
		}

		try {
			cost = Double.valueOf(txtCost.getText().toString()).doubleValue();

			if (cost < 0)
				throw new NumberFormatException();
		} catch (NumberFormatException e) {
			Toast.makeText(getApplicationContext(),
					R.string.error_invalid_cost, Toast.LENGTH_SHORT).show();
			return;
		}

		try {
			price = Double.valueOf(txtPrice.getText().toString()).doubleValue();

			if (price < 0)
				throw new NumberFormatException();
		} catch (NumberFormatException e) {
			Toast.makeText(getApplicationContext(),
					R.string.error_invalid_price, Toast.LENGTH_SHORT).show();
			return;
		}

		Log.i("AddProduct", "Description: '" + descript + "'");
		
		MLBApplication appl = (MLBApplication) getApplication();
		
		if (descript.equals(""))
			Toast.makeText(getApplicationContext(), R.string.error_name_empty,
					Toast.LENGTH_SHORT).show();
		else if (!appl.addProduct(new Product(descript, stock, cost, price,
				this.photo)))
			Toast.makeText(getApplicationContext(),
					R.string.error_product_exist, Toast.LENGTH_SHORT).show();
		else
			this.finish();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_add_product);

		this.bttAdd = (Button) findViewById(R.id.bttAddItem);
		this.txtName = (EditText) findViewById(R.id.nameNewText);
		this.txtStock = (EditText) findViewById(R.id.stockNewText);
		this.txtCost = (EditText) findViewById(R.id.costNewText);
		this.txtPrice = (EditText) findViewById(R.id.priceNewText);
		this.prodImage = (ImageView) findViewById(R.id.prodImage);

		this.prodImage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent takePic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(takePic, TAKE_PROD_PIC);
			}
		});

		bttAdd.setOnClickListener(this);
	}
}