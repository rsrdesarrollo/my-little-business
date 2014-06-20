package es.ucm.pad.teamjvr.mylittlebusiness.model;

import java.io.ByteArrayOutputStream;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import es.ucm.pad.teamjvr.mylittlebusiness.R;
import es.ucm.pad.teamjvr.mylittlebusiness.model.exceptions.ProductAttrException;

/**
 * Implementa la lógica de la aplicación, guarda los detalles de un producto
 *
 */
public class Product {
	private String name;		// Nombre descriptivo del producto
	private int stock;			// Cantidad de unidades en almacen
	private double cost;		// Coste de fabricación/adquisición de cada unidad
	private double price;		// Precio de venta de cada unidad
	private int boughtUnits;	// Unidades fabricadas/adquiridas
	private Bitmap photo;		// Foto del producto

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
		this(name, stock, cost, price, photo, stock);
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
		this.boughtUnits = boughtUnits;
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

	public int getSoldUnits() {
		return boughtUnits - stock;
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

	public double getBenefits() {
		Double benefitPerUnit = (this.price - this.cost);
		return Math.round(100*(getSoldUnits() * benefitPerUnit - this.stock*this.cost)) / 100.0;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o == null)
			return false;
		if(!(o instanceof Product))
			return false;
		if(o == this)
			return true;
		
		Product other = (Product) o;
		
		if(other.boughtUnits != this.boughtUnits)
			return false;
		if(other.cost != this.cost)
			return false;
		if(!other.name.equals(this.name))
			return false;
		if(!other.photo.equals(this.photo))
			return false;
		if(other.price != this.price)
			return false;
		if(other.stock != this.stock)
			return false;
		
		return true;
	}
}