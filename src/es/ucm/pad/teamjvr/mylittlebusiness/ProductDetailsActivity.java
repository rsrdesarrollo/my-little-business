package es.ucm.pad.teamjvr.mylittlebusiness;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import es.ucm.pad.teamjvr.mylittlebusiness.model.Product;
import es.ucm.pad.teamjvr.mylittlebusiness.model.exceptions.ProductAttrException;

public class ProductDetailsActivity extends Activity implements OnClickListener {
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
	private Animator prodImageAnim;

	/**
	 * bttAdd action.
	 */
	private OnClickListener onbttAddClick() {
		return new OnClickListener() {
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
	}
	
	/**
	 * bttSell action.
	 */
	private OnClickListener onBttSellClick() {
		return new OnClickListener() {
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
	}
	
	private OnClickListener zoomImageFromThumb() {
		return new OnClickListener() {
			@Override
			public void onClick(View v) {
				findViewById(R.id.linear_prod_info).setVisibility(View.GONE);
				if (prodImageAnim != null)
					prodImageAnim.cancel();

				final Rect startBounds = new Rect();
				final Rect finalBounds = new Rect();
				final Point globalOffset = new Point();

				prodImage.getGlobalVisibleRect(startBounds);
				findViewById(R.id.prodImageExtCont).getGlobalVisibleRect(
						finalBounds, globalOffset);
				startBounds.offset(-globalOffset.x, -globalOffset.y);
				finalBounds.offset(-globalOffset.x, -globalOffset.y);

				float startScale;
				if ((float) finalBounds.width() / finalBounds.height() > (float) startBounds
						.width() / startBounds.height()) {
					// Extend start bounds horizontally
					startScale = (float) startBounds.height()
							/ finalBounds.height();
					float startWidth = startScale * finalBounds.width();
					float deltaWidth = (startWidth - startBounds.width()) / 2;
					startBounds.left -= deltaWidth;
					startBounds.right += deltaWidth;
				} else {
					// Extend start bounds vertically
					startScale = (float) startBounds.width()
							/ finalBounds.width();
					float startHeight = startScale * finalBounds.height();
					float deltaHeight = (startHeight - startBounds.height()) / 2;
					startBounds.top -= deltaHeight;
					startBounds.bottom += deltaHeight;
				}

				prodImage.setAlpha(0f);
				prodExtImage.setVisibility(View.VISIBLE);

				// Set the pivot point for SCALE_X and SCALE_Y transformations
				// to the top-left corner of the zoomed-in view (the default
				// is the center of the view).
				prodExtImage.setPivotX(0f);
				prodExtImage.setPivotY(0f);

				// Construct and run the parallel animation of the four
				// translation and
				// scale properties (X, Y, SCALE_X, and SCALE_Y).
				AnimatorSet set = new AnimatorSet();
				set.play(
						ObjectAnimator.ofFloat(prodExtImage, View.X,
								startBounds.left, finalBounds.left))
						.with(ObjectAnimator.ofFloat(prodExtImage, View.Y,
								startBounds.top, finalBounds.top))
						.with(ObjectAnimator.ofFloat(prodExtImage,
								View.SCALE_X, startScale, 1f))
						.with(ObjectAnimator.ofFloat(prodExtImage,
								View.SCALE_Y, startScale, 1f));
				set.setDuration(getResources().getInteger(
						android.R.integer.config_shortAnimTime));
				set.setInterpolator(new DecelerateInterpolator());
				set.addListener(new AnimatorListenerAdapter() {
					@Override
					public void onAnimationEnd(Animator animation) {
						prodImageAnim = null;
					}

					@Override
					public void onAnimationCancel(Animator animation) {
						prodImageAnim = null;
					}
				});
				set.start();
				prodImageAnim = set;

				// Upon clicking the zoomed-in image, it should zoom back down
				// to the original bounds and show the thumbnail instead of
				// the expanded image.
				final float startScaleFinal = startScale;
				prodExtImage.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						if (prodImageAnim != null) {
							prodImageAnim.cancel();
						}

						// Animate the four positioning/sizing properties in
						// parallel,
						// back to their original values.
						AnimatorSet set = new AnimatorSet();
						set.play(
								ObjectAnimator.ofFloat(prodExtImage, View.X,
										startBounds.left))
								.with(ObjectAnimator.ofFloat(prodExtImage,
										View.Y, startBounds.top))
								.with(ObjectAnimator.ofFloat(prodExtImage,
										View.SCALE_X, startScaleFinal))
								.with(ObjectAnimator.ofFloat(prodExtImage,
										View.SCALE_Y, startScaleFinal));
						set.setDuration(getResources().getInteger(
								android.R.integer.config_shortAnimTime));
						set.setInterpolator(new DecelerateInterpolator());
						set.addListener(new AnimatorListenerAdapter() {
							@Override
							public void onAnimationEnd(Animator animation) {
								prodImage.setAlpha(1f);
								prodExtImage.setVisibility(View.GONE);
								prodImageAnim = null;
								findViewById(R.id.linear_prod_info).setVisibility(View.VISIBLE);
							}

							@Override
							public void onAnimationCancel(Animator animation) {
								prodImage.setAlpha(1f);
								prodExtImage.setVisibility(View.GONE);
								prodImageAnim = null;
							}
						});
						set.start();
						prodImageAnim = set;
					}
				});
			}
		};

	}
	
	/**
	 * bttSave action.
	 */
	@Override
	public void onClick(View v) {
		String descript = txtName.getText().toString();
		double cost, price;

		try {
			cost = Double.valueOf(txtCost.getText().toString()).doubleValue();
		} catch (NumberFormatException e) {
			Toast.makeText(getApplicationContext(),
					R.string.error_invalid_cost, Toast.LENGTH_SHORT).show();
			return;
		}

		try {
			price = Double.valueOf(txtPrice.getText().toString()).doubleValue();
		} catch (NumberFormatException e) {
			Toast.makeText(getApplicationContext(),
					R.string.error_invalid_price, Toast.LENGTH_SHORT).show();
			return;
		}

		try {
			productEdited.setCost(cost);
			productEdited.setPrice(price);
			productEdited.setName(descript);
		} catch (ProductAttrException e) {
			Toast.makeText(getApplicationContext(), e.getDetailMessageId(),
					Toast.LENGTH_SHORT).show();
			productEdited = new Product(product);
			return;
		}

		MLBApplication appl = (MLBApplication) getApplication();

		if (productEdited.equals(product)) {
			if (!appl.updateProduct(productEdited)) {
				Toast.makeText(getApplicationContext(),
						R.string.error_product_data, Toast.LENGTH_SHORT).show();
				productEdited = new Product(product);
			} else {
				regenerateProductAttr();
				this.finish();
				Log.i("SavedProduct", "Description: '" + descript + "'");
			}
		} else {
			if (!appl.addProduct(productEdited)) {
				Toast.makeText(getApplicationContext(),
						R.string.error_product_exist, Toast.LENGTH_SHORT)
						.show();
				productEdited = new Product(product);
			} else {
				Log.i("SavedAndRenamedProduct", "Description: '" + product
						+ "'" + " ->'" + descript + "'");
				appl.deleteProduct(product);
				appl.setCurrentProd(productEdited);
				regenerateProductAttr();
				this.finish();
			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product_details);
		// Show the Up button in the action bar.
		setupActionBar();

		this.bttSave = (Button) findViewById(R.id.bttSaveItem);
		this.bttSave.setOnClickListener(this);
		
		this.txtStockNum = (TextView) findViewById(R.id.textStockNum);
		
		this.txtName = (EditText) findViewById(R.id.edit_prod_name);
		this.txtCost = (EditText) findViewById(R.id.edit_prod_cost);
		this.txtPrice = (EditText) findViewById(R.id.edit_prod_price);

		this.txtAdd = (EditText) findViewById(R.id.units_to_add);
		this.txtSell = (EditText) findViewById(R.id.units_to_sell);
		
		this.bttAdd = (Button) findViewById(R.id.btt_add_stock);
		this.bttAdd.setOnClickListener(onbttAddClick());
		
		this.bttSell = (Button) findViewById(R.id.btt_sell_units);
		this.bttSell.setOnClickListener(onBttSellClick());

		this.prodImage = (ImageView) findViewById(R.id.prod_det_image);
		this.prodImage.setOnClickListener(zoomImageFromThumb());
		this.prodExtImage = (ImageView) findViewById(R.id.prod_det_exp_image);

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
			product = new Product(prod_aux);
			productEdited = new Product(prod_aux);
			setUI();
		} else if (product == null || productEdited == null) {
			product = new Product();
			productEdited = new Product(getResources().getText(R.string.name_label).toString());
			setUI();
		}
	}
	
	private void setUI() {
		txtName.setText(productEdited.getName());
		txtCost.setText(productEdited.getCost());
		txtPrice.setText(productEdited.getPrice());
		txtStockNum.setText(productEdited.getStock());
		
		if (productEdited.getPhoto() != null) {
			prodImage.setImageBitmap(productEdited.getPhoto());
			prodExtImage.setImageBitmap(productEdited.getPhoto());
		}
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}
}