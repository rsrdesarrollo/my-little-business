package es.ucm.pad.teamjvr.mylittlebusiness.model;

import java.io.ByteArrayOutputStream;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import es.ucm.pad.teamjvr.mylittlebusiness.R;
import es.ucm.pad.teamjvr.mylittlebusiness.model.exceptions.ProductAttrException;

public class Product {
	private String name;
	private int stock;
	private double cost;
	private double price;
	private int boughtUnits;
	private Bitmap photo;

	public Product() {
		this("");
	}

	
	public Product(Product other) {
		this.name = other.name;
		this.stock = other.stock;
		this.cost = other.cost;
		this.price = other.price;
		this.photo = other.photo;
		this.boughtUnits = other.boughtUnits;
	}

	public Product(String name) {
		this.name = name;
		this.stock = 0;
		this.cost = 0;
		this.price = 0;
		this.photo = null;
		this.boughtUnits = 0;
	}
	
	public Product(String name, int stock, double cost, double price, Bitmap photo) throws ProductAttrException {
		this(name, stock, cost, price, photo, 0);
	}

	public Product(String name, int stock, double cost, double price, Bitmap photo, int boughtUnits) throws ProductAttrException {
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

	public byte[] getPhotoAsByteArray() {
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

	@Override
	public String toString() {
		return this.getName();
	}
}