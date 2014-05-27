package es.ucm.pad.teamjvr.mylittlebusiness.model;

import java.io.ByteArrayOutputStream;

import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import es.ucm.pad.teamjvr.mylittlebusiness.R;
import es.ucm.pad.teamjvr.mylittlebusiness.model.db_adapter.ProductsDBAdapter;
import es.ucm.pad.teamjvr.mylittlebusiness.model.exceptions.ProductAttrException;

public class Product {
	private String name;
	private int stock;
	private double cost;
	private double price;
	private int boughtUnits;
	private Bitmap photo;

	public Product(Cursor cursor) {
		this.name = cursor.getString(ProductsDBAdapter.PROD_NAME_COL);
		this.stock = cursor.getInt(ProductsDBAdapter.PROD_STOCK_COL);
		this.cost = cursor.getDouble(ProductsDBAdapter.PROD_COST_COL);
		this.price = cursor.getDouble(ProductsDBAdapter.PROD_PRICE_COL);
		this.boughtUnits = cursor.getInt(ProductsDBAdapter.PROD_BOUGHT_COL);

		byte[] imageBlob = cursor.getBlob(ProductsDBAdapter.PROD_PHOTO_COL);
		this.photo = BitmapFactory.decodeByteArray(imageBlob, 0,
				imageBlob.length);
	}

	public Product(Product other) {
		this.name = other.name;
		this.stock = other.stock;
		this.cost = other.cost;
		this.price = other.price;
		this.photo = other.photo;
		this.boughtUnits = other.boughtUnits;
	}
	
	public Product(String name, int stock, double cost, double price,
			Bitmap photo) throws ProductAttrException {
		if (stock < 0)
			throw new ProductAttrException(R.string.error_invalid_stock_or_too_large);

		if (cost < 0)
			throw new ProductAttrException(R.string.error_invalid_cost);

		if (price < 0)
			throw new ProductAttrException(R.string.error_invalid_price);
		
		if (name == null || name.equals(""))
			throw new ProductAttrException(R.string.error_name_empty);
		
		this.name = name;
		this.stock = stock;
		this.boughtUnits = stock;
		this.cost = cost;
		this.price = price;
		this.photo = photo;
	}

	public void addStock(int units) throws ProductAttrException {
		if (((stock+units) < 0) || ((boughtUnits + units) < 0))
			throw new ProductAttrException(R.string.error_invalid_stock_or_too_large);
		
		this.stock += units;
		this.boughtUnits += units;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Product other = (Product) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	public String getBoughtUnits() {
		return Integer.toString(boughtUnits);
	}

	public String getCost() {
		return Double.toString(cost);
	}

	public String getName() {
		return name;
	}

	/**
	 * 
	 * @return photo of this Product, or null if there's no photo.
	 */
	public Bitmap getPhoto() {
		return photo;
	}

	private byte[] getPhotoAsByteArray() {
		if (photo != null) {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			photo.compress(CompressFormat.PNG, 0, outputStream); // PNG: lossless compression
			return outputStream.toByteArray();
		}

		return new byte[0];
	}

	public String getPrice() {
		return Double.toString(price);
	}

	public String getSoldUnits() {
		return Integer.toString(boughtUnits - stock);
	}

	public String getStock() {
		return Integer.toString(stock);
	}

	public void sellUnits(int units) throws ProductAttrException {
		if ((stock-units) < 0)
			throw new ProductAttrException(R.string.error_invalid_stock_or_too_large);
		
		this.stock -= units;
	}

	public void setCost(double cost) throws ProductAttrException {
		if (cost < 0)
			throw new ProductAttrException(R.string.error_invalid_cost);
		
		this.cost = cost;
	}

	public void setName(String name) throws ProductAttrException {
		if (name == null || name.equals(""))
			throw new ProductAttrException(R.string.error_name_empty);
		
		this.name = name;
	}

	public void setPhoto(Bitmap photo) {
		this.photo = photo;
	}

	public void setPrice(double price) throws ProductAttrException {
		if (price < 0)
			throw new ProductAttrException(R.string.error_invalid_price);
		
		this.price = price;
	}

	public void setStock(int stock) throws ProductAttrException {
		if (stock < 0)
			throw new ProductAttrException(R.string.error_invalid_stock_or_too_large);
		
		this.stock = stock;
	}

	public ContentValues toContentValues() {
		ContentValues content = new ContentValues();

		content.put(ProductsDBAdapter.KEY_PROD_NAME, this.name);
		content.put(ProductsDBAdapter.KEY_PROD_STOCK, this.stock);
		content.put(ProductsDBAdapter.KEY_PROD_COST, this.cost);
		content.put(ProductsDBAdapter.KEY_PROD_PRICE, this.price);
		content.put(ProductsDBAdapter.KEY_PROD_BOUGHT, this.boughtUnits);
		content.put(ProductsDBAdapter.KEY_PROD_PHOTO,
				this.getPhotoAsByteArray());

		return content;
	}

	@Override
	public String toString() {
		return this.getName();
	}
}