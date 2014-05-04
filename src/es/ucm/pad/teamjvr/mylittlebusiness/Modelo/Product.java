package es.ucm.pad.teamjvr.mylittlebusiness.Modelo;

import java.io.ByteArrayOutputStream;

import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import es.ucm.pad.teamjvr.mylittlebusiness.Modelo.DBAdapter.ProductsDBAdapter;



public class Product {

	private String name;
	private int stock;
	private double cost;
	private double price;
	private int boughtUnits;
	private Bitmap photo;

	public Product(Cursor cursor){
		this.name = cursor.getString(ProductsDBAdapter.PROD_NAME_COL);
		this.stock = cursor.getInt(ProductsDBAdapter.PROD_STOCK_COL);
		this.cost = cursor.getDouble(ProductsDBAdapter.PROD_COST_COL);
		this.price = cursor.getDouble(ProductsDBAdapter.PROD_PRICE_COL);
		this.boughtUnits = cursor.getInt(ProductsDBAdapter.PROD_BOUGHT_COL);
		
		byte[] imageBlob = cursor.getBlob(ProductsDBAdapter.PROD_PHOTO_COL);		
		this.photo = BitmapFactory.decodeByteArray(imageBlob, 0, imageBlob.length);
	}
	
	public Product(Product prod){
		this.name = prod.name;
		this.stock = prod.stock;
		this.boughtUnits = prod.boughtUnits;
		this.cost = prod.cost;
		this.price = prod.price;
		this.photo = prod.photo;
	}
	
	//TODO: Pensar que hacer con la inicialización del bitmap
//	public Product(String name){
//		this.name = name;
//		this.stock = 0;
//		this.boughtUnits = 0;
//		this.cost = 0;
//		this.price = 0;
//		this.photo = BitmapFactory.decodeResource(R, android.R.drawable.ic_menu_gallery);
//	}
	
	public Product(String name,int stock, double cost, double price){
		this.name = name;
		this.stock = stock;
		this.boughtUnits = stock;
		this.cost = cost;
		this.price = price;
	}
	
	public Product(String name, double cost, double price){
		this.name = name;
		this.stock = 0;
		this.boughtUnits = 0;
		this.cost = cost;
		this.price = price;
	}
	
	public ContentValues toContentValues(){
		ContentValues content = new ContentValues();
		
		content.put(ProductsDBAdapter.KEY_PROD_NAME, this.name);
		content.put(ProductsDBAdapter.KEY_PROD_STOCK, this.stock);
		content.put(ProductsDBAdapter.KEY_PROD_COST, this.cost);
		content.put(ProductsDBAdapter.KEY_PROD_PRICE, this.price);
		content.put(ProductsDBAdapter.KEY_PROD_BOUGHT, this.boughtUnits);
		content.put(ProductsDBAdapter.KEY_PROD_PHOTO, this.getBitmapAsVyteArray());
		
		return content;
	}
	
	private byte[] getBitmapAsVyteArray(){
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	    photo.compress(CompressFormat.PNG, 0, outputStream); // PNG es compresión sin pérdida, ignora la calidad. 
	    return outputStream.toByteArray();
	}

	public String toString() {
		return this.getName();
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getName(){
		return name;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}
	
	public final double getCost() {
		return cost;
	}

	public final void setCost(double cost) {
		this.cost = cost;
	}

	public final double getPrice() {
		return price;
	}

	public final void setPrice(double price) {
		this.price = price;
	}
	
	public final int getSalesUnits(){
		return boughtUnits - stock;
	}

	public final int getBoughtUnits() {
		return boughtUnits;
	}
	
	public Bitmap getPhoto() {
		return photo;
	}

	public void setPhoto(Bitmap photo) {
		this.photo = photo;
	}

	public void addStock(int units){
		this.stock += units;
		this.boughtUnits += units;
	}
	
	public void sellUnits(int units){
		this.stock -= units;
	}
}
