package es.ucm.pad.teamjvr.mylittlebusiness.Modelo;

import android.content.ContentValues;
import android.database.Cursor;
import es.ucm.pad.teamjvr.mylittlebusiness.Modelo.DBAdapter.*;



public class Product {

	private String name;
	private int stock;
	private double cost;
	private double price;
	private int boughtUnits;
	
	public Product(Cursor cursor){
		this.name = cursor.getString(ProductsDBAdapter.PROD_NAME_COL);
		this.stock = cursor.getInt(ProductsDBAdapter.PROD_STOCK_COL);
		this.cost = cursor.getDouble(ProductsDBAdapter.PROD_COST_COL);
		this.price = cursor.getDouble(ProductsDBAdapter.PROD_PRICE_COL);
		this.boughtUnits = cursor.getInt(ProductsDBAdapter.PROD_BOUGHT_COL);
	}
	
	public Product(Product prod){
		this.name = prod.name;
		this.stock = prod.stock;
		this.boughtUnits = prod.boughtUnits;
		this.cost = prod.cost;
		this.price = prod.price;
	}
	
	public Product(String name){
		this.name = name;
		this.stock = 0;
		this.boughtUnits = 0;
		this.cost = 0;
		this.price = 0;
	}
	
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
		
		return content;
	}

	public String toString() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public void addStock(int units){
		this.stock += units;
		this.boughtUnits += units;
	}
	
	public void sellUnits(int units){
		this.stock -= units;
	}
}
